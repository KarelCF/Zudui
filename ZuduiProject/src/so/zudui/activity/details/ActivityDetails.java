package so.zudui.activity.details;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import so.zudui.application.ZuduiApplication;
import so.zudui.condition.DetailConditions;
import so.zudui.entity.Activities.Activity;
import so.zudui.friends.MyFriendsPage;
import so.zudui.home.HomePage;
import so.zudui.home.HomePageActivitiesAdatper;
import so.zudui.launch.activity.R;
import so.zudui.space.FriendSpaceActivity;
import so.zudui.util.DistanceUtil;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ImageLoaderUtil;
import so.zudui.util.TimeUtil;
import so.zudui.webservice.WebServiceUtil;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivityDetails extends SherlockActivity {
	
	private ImageView categoryImageView = null;
	private TextView startTimeTextView = null;
	private TextView shopNameTextView = null;
	private TextView introTextView = null;
	private TextView addressTextView = null;
	private TextView distanceTextView = null;
	private ProgressBar progressBar = null;
	
	
	private MapView mapView = null;
	private GridView participantsGridView = null;
	private DetailsParticipantsAdapter detailsParticipantsAdapter = null;
	
	private Activity activityContainsDetails = null;
	
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	// 地图相关
	private MapController mapController = null;
	private BMapManager mapManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapManager = ZuduiApplication.getBMapManager();
		setContentView(R.layout.layout_activity_details);
		getInfo();
		initImageLoader();
		initActivityDetailsView();
		new GetParticipantsAvatarTask().execute();
	}
	
	private void getInfo() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		int position = (Integer) bundle.get("position");
		int condition = (Integer) bundle.get("condition");
		if (condition == DetailConditions.MY_SPACE) {
			activityContainsDetails = EntityTableUtil.getMyActivitiesList().get(position);
		} else if (condition == DetailConditions.HOME_PAGE) {
			activityContainsDetails = EntityTableUtil.getHomeActivitiesList().get(position);
		}
	}
	
	private void initImageLoader() {
		ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(this);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	private void initActivityDetailsView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		categoryImageView = (ImageView) findViewById(R.id.activity_details_imageview);
		startTimeTextView = (TextView) findViewById(R.id.activity_detail_textview_start_time);
		shopNameTextView = (TextView) findViewById(R.id.activity_detail_textview_shop);
		introTextView = (TextView) findViewById(R.id.activity_detail_textview_intro);
		addressTextView = (TextView) findViewById(R.id.activity_detail_textview_address);
		distanceTextView = (TextView) findViewById(R.id.activity_details_textview_distance);
		progressBar = (ProgressBar) findViewById(R.id.activity_details_progressbar_loading);
		
		// 点击参与者头像进入空间
		participantsGridView = (GridView) findViewById(R.id.activity_details_gridview_participants);
		participantsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (position < EntityTableUtil.getFriendsListSize()) {
					Intent intent = new Intent(ActivityDetails.this, FriendSpaceActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);
					intent.putExtra("bundle", bundle);
					ActivityDetails.this.startActivity(intent);
				}
			}
		});
		
		
		String title = activityContainsDetails.getName();
		String categoryImageUrl = activityContainsDetails.getIconUrl();
		String startTime = TimeUtil.getFormatStartTime(activityContainsDetails.getStarttime());
		String shopName = activityContainsDetails.getShopname();
		String intro = activityContainsDetails.getIntroduce();
		String address = activityContainsDetails.getAddress();
		String distacne = getDistance();
		
		// 地点在小地图上呈现
		showAddressOnMap();
		
		this.setTitle(title);
		imageLoader.displayImage(categoryImageUrl, categoryImageView, options);
		startTimeTextView.setText(startTime);
		shopNameTextView.setText(shopName);
		introTextView.setText(intro);
		addressTextView.setText(address);
		distanceTextView.setText(distacne);
		
	}
	
	private String getDistance() {
		String distance = "";
		DistanceUtil distanceUtil = new DistanceUtil();
		distance = distanceUtil.getDistance(activityContainsDetails.getLatitude(), activityContainsDetails.getLongitude());
		return distance;
	}
	
	private void showAddressOnMap() {
		// 找到目的地点
		GeoPoint point = new GeoPoint((int) (activityContainsDetails.getLatitude() * 1E6), (int) (activityContainsDetails.getLongitude() * 1E6));  
		// 初始化mapView
		mapView = (MapView) findViewById(R.id.activity_details_mapview);
		mapController = mapView.getController();
		mapController.setZoom(16);
		mapController.setCenter(point);
		
		// 显示地点
		Drawable mark = getResources().getDrawable(R.drawable.icon_gcoding_23);
		ItemizedOverlay itemizedOverlay = new ItemizedOverlay<OverlayItem>(mark, mapView);
		OverlayItem item = new OverlayItem(point, "shop", "");
		item.setMarker(mark);
		
		itemizedOverlay.addItem(item);
		mapView.getOverlays().clear();  
		mapView.getOverlays().add(itemizedOverlay);  
		mapView.refresh();
		
	}
	
	private List<String> allParticipantsUid = new ArrayList<String>();
	
	private class GetParticipantsAvatarTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			// 所有耗时操作
			getParticipantsUid();
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			activityContainsDetails.clearParticipantsAvatar();		
			// TODO 同时添加FriendsList
			EntityTableUtil.clearFriendsList();
			for (int i = 0; i < allParticipantsUid.size(); i++) {
				result = webServiceUtil.queryParticipantUser(allParticipantsUid.get(i), activityContainsDetails);
				if (result == WebServiceUtil.FAILED)
					break;
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (result == WebServiceUtil.SUCCESS) {
				List<String> participantsAvatarUrls = activityContainsDetails.getParticipantsAvatar();
				detailsParticipantsAdapter = new DetailsParticipantsAdapter(ActivityDetails.this, participantsAvatarUrls, activityContainsDetails.getPreNumber());
				participantsGridView.setAdapter(detailsParticipantsAdapter);
			} else {
				Toast.makeText(ActivityDetails.this, "参与者加载失败", Toast.LENGTH_SHORT).show();
			}
			progressBar.setVisibility(View.GONE);
			participantsGridView.setVisibility(View.VISIBLE);
		}
	}
	
	private void getParticipantsUid() {
		String sponsorUid = activityContainsDetails.getCreateUserUid();
		String[] participantsUid = activityContainsDetails.getJoinUserId().split(",");
		allParticipantsUid.clear();
		allParticipantsUid.add(sponsorUid);
		for (String temp : participantsUid)
			allParticipantsUid.add(temp);
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
	
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	mapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	mapView.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mapView.onRestoreInstanceState(savedInstanceState);
    }
}
