package so.zudui.space;

import java.util.ArrayList;
import java.util.List;

import so.zudui.activity.details.ActivityDetails;
import so.zudui.condition.DetailConditions;
import so.zudui.condition.HandlerConditions;
import so.zudui.entity.Activities;
import so.zudui.entity.Activities.Activity;
import so.zudui.entity.Friends.Friend;
import so.zudui.launch.activity.R;
import so.zudui.space.Constants.Extra;
import so.zudui.util.ActivityInfoUtil;
import so.zudui.util.DistanceUtil;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ImageLoaderUtil;
import so.zudui.util.ListAndArrayConversionUtil;
import so.zudui.webservice.WebServiceUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendSpaceActivity extends SherlockActivity {

	private Friend friend = null;
	
	// 个人信息功能区
	private ImageView friendAvatarImageView = null; 
	private ImageView friendGenderImageView = null; 
	private TextView friendDistanceTextView = null;
	private TextView friendFaithTextView = null;
	
	// 相册功能区
	private GridView photosGridView = null;
//	private RelativeLayout photoAreaTextLayout = null;
//	private RelativeLayout photoAreaGridLayout = null;
	private FriendPhotosAdatper friendPhotosAdapter = null;
	private String[] photoUrls;
	
	// 活动功能区
	private ProgressBar loadingProgressBar = null;
	private GridView activitiesGridView = null;
//	private RelativeLayout activitiyAreaTextLayout = null;
	private RelativeLayout activitiyAreaGridLayout = null;
	private FriendActivitiesAdatper friendActivitiesAdatper = null;
	
	// 填充头像准备
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	private List<Activity> friendActivitiesList = new ArrayList<Activity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_friend_space);
		getInfo();
		initFriendSpaceView();
		new SetFriendActivitiesTask().execute();
	}
	
	private void getInfo() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		int position = bundle.getInt("position");
		friend = EntityTableUtil.getFriendsList().get(position);
		new GetFriendPhotosTask().execute();
	}
	
	
	private class GetFriendPhotosTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			webServiceUtil.queryFriendUser(friend.getUid(), friend);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			getphtotUrlsArray();
			friendPhotosAdapter = new FriendPhotosAdatper(FriendSpaceActivity.this, photoUrls);
			photosGridView.setAdapter(friendPhotosAdapter);
		}
		
	}
	
	private void getphtotUrlsArray() {
		String urls = friend.getShowimages();
		if(!urls.equals(",") && urls != null ) {
			photoUrls = friend.getShowimages().split(",");
			List<String> photoUrlsList = new ArrayList<String>();
			photoUrlsList.addAll(ListAndArrayConversionUtil.arrayToList(photoUrls));
			photoUrlsList.remove(0);
			photoUrls = ListAndArrayConversionUtil.listToArray(photoUrlsList);
		}
	}
	
	private void initFriendSpaceView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		activitiyAreaGridLayout = (RelativeLayout) findViewById(R.id.activity_area_grid_layout);
		friendAvatarImageView = (ImageView) findViewById(R.id.friend_space_imageview_avatar);
		friendGenderImageView = (ImageView) findViewById(R.id.friend_space_imageview_gender);
		friendDistanceTextView = (TextView) findViewById(R.id.friend_space_textview_distance);
		friendFaithTextView = (TextView) findViewById(R.id.friend_space_textview_faith);
		
		photosGridView = (GridView) findViewById(R.id.friend_space_gridview_photos);
		photosGridView.setEmptyView(findViewById(R.id.friend_space_no_photo_area));
		
		photosGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startPhotosPagerActivity(position);
			}
		});
		
		loadingProgressBar = (ProgressBar) findViewById(R.id.friend_space_progressbar_loading);
		activitiesGridView = (GridView) findViewById(R.id.friend_space_gridview_activities);
		activitiesGridView.setEmptyView(findViewById(R.id.friend_space_no_activity_area));
		activitiesGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				EntityTableUtil.setMyActivitiesList(friendActivitiesList);
				Intent intent = new Intent(FriendSpaceActivity.this, ActivityDetails.class);
				Bundle bundle = new Bundle();
				bundle.putInt("condition", DetailConditions.MY_SPACE);
				bundle.putInt("position", position);
				intent.putExtra("bundle", bundle);
				FriendSpaceActivity.this.startActivity(intent);
			}
		});
		
		String name = friend.getUname();
		String faith = String.valueOf(friend.getScore());
		String distance = getDistance();
		setTitle(name);
		setFriendAvatar(); 
		friendDistanceTextView.setText(distance);
		friendFaithTextView.setText(faith);
		
	}
	
	private void setFriendAvatar() {
		imageLoaderUtil = new ImageLoaderUtil(this);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
		imageLoader.displayImage(friend.getUpicurl(), friendAvatarImageView, options);
		photosGridView.setBackgroundResource(R.drawable.image_border);
		setFriendGender();
	}
	
	private void setFriendGender() {
		if (friend.getSex() == 1) {
			friendGenderImageView.setImageResource(R.drawable.pic_male);
		} else {
			friendGenderImageView.setImageResource(R.drawable.pic_female);
		}
	}
	
	private String getDistance() {
		DistanceUtil distanceUtil = new DistanceUtil();
		String distance = distanceUtil.getDistance(friend.getLatitude(), friend.getLongitude());
		return distance;
	}
	
	
	private void startPhotosPagerActivity(int position) {
		// 点击看大图
		Intent intent = new Intent(this, FriendImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, photoUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		intent.putExtra("FRIEND_NAME", friend.getUname());
		startActivity(intent);
	}
	
	private class SetFriendActivitiesTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			String uid = friend.getUid();
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			
			result = webServiceUtil.queryActivitiesOnGoing(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myUpComingActivities = EntityTableUtil.getMyActivities();
				List<Activity> myUpComingActivitiesList = myUpComingActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myUpComingActivitiesList, HandlerConditions.GET_MY_ONGOING_ACTIVITIES);
				friendActivitiesList.addAll(myUpComingActivitiesList);
				result = 0;
			}
			
			result = webServiceUtil.queryActivitiesUpComing(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myOnGoingActivities = EntityTableUtil.getMyActivities();
				List<Activity> myOnGoingActivitiesList = myOnGoingActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myOnGoingActivitiesList, HandlerConditions.GET_MY_UPCOMING_ACTIVITIES);
				friendActivitiesList.addAll(myOnGoingActivitiesList);
				result = 0;
			}
			
			result = webServiceUtil.queryActivitiesFinished(uid);
			if (result != WebServiceUtil.EMPTY && result != WebServiceUtil.FAILED) {
				Activities myFinishedActivities = EntityTableUtil.getMyActivities();
				List<Activity> myFinishedActivitiesList = myFinishedActivities.getActivities();
				ActivityInfoUtil.getActivityIconAndStatus(myFinishedActivitiesList, HandlerConditions.GET_MY_FINISHED_ACTIVITIES);
				friendActivitiesList.addAll(myFinishedActivitiesList);
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == WebServiceUtil.EMPTY) {
				Toast.makeText(FriendSpaceActivity.this, "TA还没有参加过活动哦", Toast.LENGTH_SHORT).show();
			} else if (result == WebServiceUtil.FAILED){
				Toast.makeText(FriendSpaceActivity.this, "查找活动失败", Toast.LENGTH_SHORT).show();
			} 
			showMyActivities();
		}
		
	}
	
	private void showMyActivities() {
		friendActivitiesAdatper = new FriendActivitiesAdatper(this, friendActivitiesList);
		activitiesGridView.setAdapter(friendActivitiesAdatper);
		loadingProgressBar.setVisibility(View.GONE);
		activitiyAreaGridLayout.setVisibility(View.VISIBLE);
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
