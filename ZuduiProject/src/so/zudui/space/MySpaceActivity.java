package so.zudui.space;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import so.zudui.activity.details.ActivityDetails;
import so.zudui.condition.DetailConditions;
import so.zudui.condition.HandlerConditions;
import so.zudui.database.QueryScoreUtil;
import so.zudui.database.UpdatePhotosUtil;
import so.zudui.entity.Activities;
import so.zudui.entity.Activities.Activity;
import so.zudui.entity.User;
import so.zudui.launch.activity.R;
import so.zudui.space.Constants.Extra;
import so.zudui.util.ActivityInfoUtil;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ImageLoaderUtil;
import so.zudui.webservice.WebServiceUtil;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MySpaceActivity extends SherlockActivity {
	
	private static final int IMAGE_ALBUM = 1000;
	private static final int IMAGE_CAMERA = 1001;
	
	// 个人信息功能区
	private ImageView myAvatarImageView = null; 
	private ImageView myGenderImageView = null; 
	private TextView myFaithTextView = null;
	
	// 相册功能区
	private GridView photosGridView = null;
	private RelativeLayout photoAreaTextLayout = null;
	private RelativeLayout photoAreaGridLayout = null;
	private MyPhotosAdatper myPhotosAdapter = null;
	
	// 活动功能区
	private ProgressBar loadingProgressBar = null;
	private GridView activitiesGridView = null;
	private RelativeLayout activitiyAreaTextLayout = null;
	private RelativeLayout activitiyAreaGridLayout = null;
	private MyActivitiesAdatper myActivitiesAdatper = null;
	
	private User user = EntityTableUtil.getMainUser();
	
	// 填充头像准备
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	private List<Activity> myActivitiesList = new ArrayList<Activity>();
	
	private ProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_my_space);
		setMyTitle();
		initMySpaceActivityView();
		setMyAvatar();
		new SetMyScoreTask().execute();
		new SetMyActivitiesTask().execute();
	}
	
	private void setMyTitle() {
		setTitle(user.getUname());
	}
	
	private void initMySpaceActivityView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		myAvatarImageView = (ImageView) findViewById(R.id.myspace_activity_imageview_avatar);
		myGenderImageView = (ImageView) findViewById(R.id.myspace_activity_imageview_gender);
		myFaithTextView = (TextView) findViewById(R.id.myspace_activity_textview_faith);
		
		loadingProgressBar = (ProgressBar) findViewById(R.id.myspace_progressbar_loading);
		activitiyAreaTextLayout = (RelativeLayout) findViewById(R.id.activity_area_text_layout);
		activitiyAreaGridLayout = (RelativeLayout) findViewById(R.id.activity_area_grid_layout);
		activitiesGridView = (GridView) findViewById(R.id.myspace_gridview_activities);
		activitiesGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				EntityTableUtil.setMyActivitiesList(myActivitiesList);
				Intent intent = new Intent(MySpaceActivity.this, ActivityDetails.class);
				Bundle bundle = new Bundle();
				bundle.putInt("condition", DetailConditions.MY_SPACE);
				bundle.putInt("position", position);
				intent.putExtra("bundle", bundle);
				MySpaceActivity.this.startActivity(intent);
			}
		});

		photosGridView = (GridView) findViewById(R.id.myspace_gridview_photos);
		photoAreaGridLayout = (RelativeLayout) findViewById(R.id.photo_area_grid_layout);
		photoAreaTextLayout = (RelativeLayout) findViewById(R.id.photo_area_text_layout);
		photoAreaTextLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPhotosDialog();
				photoAreaGridLayout.setVisibility(View.VISIBLE);
				photoAreaTextLayout.setVisibility(View.GONE);
			}
		});
		myPhotosAdapter = new MyPhotosAdatper(this);
				
		if(myPhotosAdapter.getCount() > 1) {
			photoAreaGridLayout.setVisibility(View.VISIBLE);
			photoAreaTextLayout.setVisibility(View.GONE);
		}
		photosGridView.setAdapter(myPhotosAdapter);
		photosGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (position == myPhotosAdapter.getCount() - 1 ) {
					// 调用本机相册与相机拍照
					getPhotosDialog();
				} else {
					startPhotosPagerActivity(position);
				}
			}
		});
	}
	
	private void getPhotosDialog() {            
		final Dialog dialog = new Dialog(MySpaceActivity.this, R.style.get_photos_dialog);            
		dialog.setContentView(R.layout.dialog_choose_photos);            
		dialog.setCanceledOnTouchOutside(true);            
		dialog.setCancelable(true);    
		
		Button selectFromAlbumBtn = (Button)dialog.getWindow().findViewById(R.id.selectFromAlbumBtn);            
		Button startCameraBtn = (Button)dialog.getWindow().findViewById(R.id.startCameraBtn);            
		selectFromAlbumBtn.setOnClickListener(new OnClickListener() {                               
			@Override                
			public void onClick(View v) {                    
				if(dialog != null && dialog.isShowing()) {                        
					dialog.dismiss();                    
					getPhotosFromAlbum();
					}                
				}            
			});     
		startCameraBtn.setOnClickListener(new OnClickListener() {                               
			@Override                
			public void onClick(View v) {                    
				if(dialog != null && dialog.isShowing()) {                        
					dialog.dismiss();   
					getPhotosFromCamera();
				}                
			}            
		});    
		if(dialog!=null && !dialog.isShowing())            
			dialog.show();   
		
	}
	
	private void getPhotosFromAlbum() {
		Intent getAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbumIntent.setType("image/*");
		startActivityForResult(getAlbumIntent, IMAGE_ALBUM);
	}
	
	private static File file = null;
	
	private void getPhotosFromCamera() {
		Intent getCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下方代码使拍得照片直接存入sdcard,避免转为Bitmap后图像失真严重的问题
		setFile();
		getCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(getCameraIntent, IMAGE_CAMERA);
	}	
	
	private WebServiceUtil webServiceUtil = null;
	private Uri photoUri = null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {    
		    Toast.makeText(this, "未读取照片", Toast.LENGTH_SHORT).show();
		    return;
	    }
		
		if (requestCode == IMAGE_ALBUM) {
            photoUri = data.getData(); // 获得图片的uri 
            String photoUriString = photoUri.toString();
			myPhotosAdapter.addPicToList(photoUriString);
			myPhotosAdapter.notifyDataSetChanged();
			new SetMyPhotosTask().execute(requestCode);
		}
		
		if (requestCode == IMAGE_CAMERA) {
			String photoUriStringfromSD = "file://" + file.toString();
			myPhotosAdapter.addPicToList(photoUriStringfromSD);
			myPhotosAdapter.notifyDataSetChanged();
			new SetMyPhotosTask().execute(requestCode);
		}
	}
	
	private class SetMyPhotosTask extends AsyncTask<Integer, Void, String> {
		
		private SetMyPhotosTask() {
			progressDialog = ProgressDialog.show(MySpaceActivity.this, null, "更新相册中...", true);
		}

		@Override
		protected String doInBackground(Integer... params) {
			int photoFrom = params[0];
			SerializePicturesUtil uploadPicturesUtil = new SerializePicturesUtil();
			byte[] bitmapBuffer = null;
			if (photoFrom == IMAGE_ALBUM) {
				ContentResolver resolver = MySpaceActivity.this.getContentResolver();
				bitmapBuffer = uploadPicturesUtil.getByteArrayByUri(photoUri, resolver);
			} else if(photoFrom == IMAGE_CAMERA) {
				bitmapBuffer = uploadPicturesUtil.getByteArrayByFile(file);
			}
			webServiceUtil = new WebServiceUtil(); 
			String photoUrl = webServiceUtil.uploadUserShowImages(bitmapBuffer);
			if (photoUrl == null)
				return photoUrl;
			
			StringBuffer photosUrlBuffer = new StringBuffer(user.getShowimages());
			photosUrlBuffer.append(photoUrl + ",");
			user.setShowimages(photosUrlBuffer.toString());
			UpdatePhotosUtil updatePhotosUtil = new UpdatePhotosUtil(MySpaceActivity.this);
			updatePhotosUtil.updatePhotos();
			return photoUrl;
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result != null) {
				Toast.makeText( MySpaceActivity.this, "相册已更新", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText( MySpaceActivity.this, "相册更新失败", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	private boolean sdcardExists = false;
	
	private void setFile() {
		// 准备好File路径
	    file = null;
	    sdcardExists = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	    if (sdcardExists) {
	        file = new File(Environment.getExternalStorageDirectory().toString() 
	            + File.separator + "image" + File.separator 
	            + "Zudui" + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
	        if (!file.getParentFile().exists())
	            file.getParentFile().mkdirs();
	    } else {
	        Toast.makeText(this, "请插入sd卡", Toast.LENGTH_SHORT).show();
	    }
	}
	
	private void startPhotosPagerActivity(int position) {
		List<String> photoUrlList = myPhotosAdapter.getPhotoUrlList();
		// 为了使GridView中最后一张按钮图不在ViewPager中显示出来而做的一些工作
		List<String> photoUrlListWithoutBtn = new ArrayList<String>();
		photoUrlListWithoutBtn.addAll(photoUrlList);
		photoUrlListWithoutBtn.remove(photoUrlListWithoutBtn.size() - 1);
		String[] photoUrls = (String[]) photoUrlListWithoutBtn.toArray(new String[photoUrlListWithoutBtn.size()]); 
		
		// 点击看大图
		Intent intent = new Intent(this, MyImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, photoUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}
	
	
	private void setMyAvatar() {
		imageLoaderUtil = new ImageLoaderUtil(this);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
		imageLoader.displayImage(user.getUpicurl(), myAvatarImageView, options);
		myAvatarImageView.setBackgroundResource(R.drawable.image_border_light);
		setMyGender();
	}
	
	private void setMyGender() {
		if (user.getSex() == 1) {
			myGenderImageView.setImageResource(R.drawable.pic_male);
		} else {
			myGenderImageView.setImageResource(R.drawable.pic_female);
		}
	}
	
	private class SetMyScoreTask extends AsyncTask<Void, Void, Void> {
		QueryScoreUtil queryScoreUtil = null;
		@Override
		protected Void doInBackground(Void... params) {
			queryScoreUtil = new QueryScoreUtil(MySpaceActivity.this);
			queryScoreUtil.queryMyScore();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			String score = queryScoreUtil.getScore();
			myFaithTextView.setText(score);
			super.onPostExecute(result);
		}
		
		
		
	}
	
	private class SetMyActivitiesTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			String uid = user.getUid();
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			
			result = webServiceUtil.queryActivitiesOnGoing(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myUpComingActivities = EntityTableUtil.getMyActivities();
				List<Activity> myUpComingActivitiesList = myUpComingActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myUpComingActivitiesList, HandlerConditions.GET_MY_ONGOING_ACTIVITIES);
				myActivitiesList.addAll(myUpComingActivitiesList);
				result = 0;
			}
			
			result = webServiceUtil.queryActivitiesUpComing(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myOnGoingActivities = EntityTableUtil.getMyActivities();
				List<Activity> myOnGoingActivitiesList = myOnGoingActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myOnGoingActivitiesList, HandlerConditions.GET_MY_UPCOMING_ACTIVITIES);
				myActivitiesList.addAll(myOnGoingActivitiesList);
				result = 0;
			}
			
			result = webServiceUtil.queryActivitiesFinished(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myFinishedActivities = EntityTableUtil.getMyActivities();
				List<Activity> myFinishedActivitiesList = myFinishedActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myFinishedActivitiesList, HandlerConditions.GET_MY_FINISHED_ACTIVITIES);
				myActivitiesList.addAll(myFinishedActivitiesList);
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == WebServiceUtil.EMPTY) {
				Toast.makeText(MySpaceActivity.this, "您还没有参加过活动哦", Toast.LENGTH_SHORT).show();
			} else if (result == WebServiceUtil.FAILED){
				Toast.makeText(MySpaceActivity.this, "查找活动失败", Toast.LENGTH_SHORT).show();
			} 
			showMyActivities();
		}
		
	}
	
	private void showMyActivities() {
		if (myActivitiesList.size() == 0) {
			loadingProgressBar.setVisibility(View.GONE);
			activitiyAreaTextLayout.setVisibility(View.VISIBLE);
			return;
		}
		myActivitiesAdatper = new MyActivitiesAdatper(this, myActivitiesList);
		activitiesGridView.setAdapter(myActivitiesAdatper);
		loadingProgressBar.setVisibility(View.GONE);
		activitiyAreaGridLayout.setVisibility(View.VISIBLE);
	}
	
	
	@Override
	protected void onResume() {
		checkPhotoListSize();
		super.onResume();
	}

	private void checkPhotoListSize() {
		if (myPhotosAdapter.getCount() == 1) {
			photoAreaGridLayout.setVisibility(View.GONE);
			photoAreaTextLayout.setVisibility(View.VISIBLE);
		}
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
