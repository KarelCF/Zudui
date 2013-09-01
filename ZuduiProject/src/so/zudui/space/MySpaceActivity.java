package so.zudui.space;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import so.zudui.database.QueryScoreUtil;
import so.zudui.database.UpdatePhotosUtil;
import so.zudui.entity.User;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.launch.activity.R;
import so.zudui.space.Constants.Extra;
import so.zudui.util.EntityTableUtil;
import so.zudui.webservice.WebServiceUtil;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
	
	private GetMyScoreHandler getMyScoreHandler = new GetMyScoreHandler(this);
	private UpdateMyPhotosHandler updateMyPhotosHandler = new UpdateMyPhotosHandler(this);
	
	private User user = EntityTableUtil.getUser();
	
	// 填充头像准备
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	private ProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_myspace_activity);
		setMyTitle();
		initMySpaceActivityView();
		setMyAvatar();
		setMyScore();
		
	}
	
	private void setMyTitle() {
		setTitle(user.getUname());
	}
	
	private void initMySpaceActivityView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// TODO 周日改动
		myPhotosAdapter = new MyPhotosAdatper(this);
		if(myPhotosAdapter.getCount() > 1) {
			photoAreaGridLayout.setVisibility(View.VISIBLE);
			photoAreaTextLayout.setVisibility(View.GONE);
		}
		
		myAvatarImageView = (ImageView) findViewById(R.id.myspace_activity_imageview_avatar);
		myGenderImageView = (ImageView) findViewById(R.id.myspace_activity_imageview_gender);
		myFaithTextView = (TextView) findViewById(R.id.myspace_activity_textview_faith);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {    
		    Toast.makeText(this, "未读取照片", Toast.LENGTH_SHORT).show();
		    return;
	    }
		
		if (requestCode == IMAGE_ALBUM) {
            Uri photoUri = data.getData(); // 获得图片的uri 
            String photoUriString = photoUri.toString();
			myPhotosAdapter.addPicToList(photoUriString);
			myPhotosAdapter.notifyDataSetChanged();
			
			// 获取序列化数据传到服务器
			ContentResolver resolver = this.getContentResolver();
			SerializePicturesUtil uploadPicturesUtil = new SerializePicturesUtil();
			final byte[] bitmapBuffer = uploadPicturesUtil.getByteArrayByUri(photoUri, resolver);
			webServiceUtil = new WebServiceUtil(updateMyPhotosHandler); 
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					webServiceUtil.uploadUserShowImages(bitmapBuffer);
				}
			}).start();
		}
		
		if (requestCode == IMAGE_CAMERA) {
			String photoUriStringfromSD = "file://" + file.toString();
			myPhotosAdapter.addPicToList(photoUriStringfromSD);
			myPhotosAdapter.notifyDataSetChanged();
			
			// 获取序列化数据传到服务器
			SerializePicturesUtil uploadPicturesUtil = new SerializePicturesUtil();
			final byte[] bitmapBuffer = uploadPicturesUtil.getByteArrayByFile(file);
			webServiceUtil = new WebServiceUtil(updateMyPhotosHandler); 
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					webServiceUtil.uploadUserShowImages(bitmapBuffer);
				}
			}).start();
			
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
		// 为了使ridView中最后一张按钮图不在ViewPager中显示出来而做的一些工作
		List<String> photoUrlListWithoutBtn = new ArrayList<String>();
		photoUrlListWithoutBtn.addAll(photoUrlList);
		photoUrlListWithoutBtn.remove(photoUrlListWithoutBtn.size() - 1);
		String[] photoUrls = (String[]) photoUrlListWithoutBtn.toArray(new String[photoUrlListWithoutBtn.size()]); 
		
		// 点击看大图
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, photoUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}
	
	
	private void setMyAvatar() {
		imageLoader.init(ImageLoaderConfiguration.createDefault(MySpaceActivity.this));
		initLoaderOptions();
		imageLoader.displayImage(user.getUpicurl(), myAvatarImageView, options);
		myAvatarImageView.setBackgroundResource(R.drawable.image_border);
		setMyGender();
	}
	
	private void setMyGender() {
		if (user.getSex() == 1) {
			myGenderImageView.setImageResource(R.drawable.pic_male);
		} else {
			myGenderImageView.setImageResource(R.drawable.pic_female);
		}
	}
	
	private void initLoaderOptions() {
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	private void setMyScore() {
		QueryScoreUtil queryScoreUtil = new QueryScoreUtil(MySpaceActivity.this, getMyScoreHandler);
		queryScoreUtil.queryMyScore();
	}
	
	
	
	private static class GetMyScoreHandler extends Handler {
		WeakReference<MySpaceActivity> weakReference;
		MySpaceActivity mySpaceActivity;
		GetMyScoreHandler(MySpaceActivity activity) {
			weakReference = new WeakReference<MySpaceActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			mySpaceActivity = weakReference.get();
			switch(msg.what) {
			case HandlerConditions.GET_MY_SCORE:
				String score = (String) msg.obj;
				mySpaceActivity.myFaithTextView.setText(score);
			}
		}
		
	}
	
	private static class UpdateMyPhotosHandler extends Handler {
		WeakReference<MySpaceActivity> weakReference;
		MySpaceActivity mySpaceActivity;
		UpdateMyPhotosHandler(MySpaceActivity activity) {
			weakReference = new WeakReference<MySpaceActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			mySpaceActivity = weakReference.get();
			switch(msg.what) {
			case HandlerConditions.UPLOAD_MY_PHOTOS:
				StringBuffer photosUrlBuffer = new StringBuffer(mySpaceActivity.user.getShowimages());
				photosUrlBuffer.append((String) msg.obj + ",");
				mySpaceActivity.user.setShowimages(photosUrlBuffer.toString());
System.out.println(photosUrlBuffer.toString());
				UpdatePhotosUtil updatePhotosUtil = new UpdatePhotosUtil(mySpaceActivity);
				updatePhotosUtil.updatePhotos();
				Toast.makeText(mySpaceActivity, "相册已更新", Toast.LENGTH_SHORT).show();
				break;
				// TODO 周日修改
			case HandlerConditions.DELETE_MY_PHOTOS_BEGIN:
				mySpaceActivity.progressDialog = ProgressDialog.show(mySpaceActivity, null, "删除照片中...", true);
				break;
			case HandlerConditions.DELETE_MY_PHOTOS_FINISH:
				mySpaceActivity.progressDialog.dismiss();
				break;
				
			}
		}
		
	}
	
	// 禁止GridView滑动，返回true消费此次事件，使得事件不在继续分发，即可在触发滑动之前中止滑动
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE)
			return true;
		return super.dispatchTouchEvent(ev);
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
