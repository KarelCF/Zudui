package so.zudui.home;

import java.lang.ref.WeakReference;

import so.zudui.baidumap.MyLocationListener;
import so.zudui.database.UpdateMyLocationUtil;
import so.zudui.entity.User;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.launch.activity.R;
import so.zudui.util.EntityTableUtil;
import so.zudui.webservice.WebServiceUtil;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class HomeActivity extends SherlockActivity implements OnNavigationListener {
	
	private TextView textView = null;
	private User user = EntityTableUtil.getUser();
	private String[] sortingMethods;
	private GetMyLocationHandler getMyLocationHandler = new GetMyLocationHandler(this);
	private UpdateMyLocationHandler updateMyLocationHandler = new UpdateMyLocationHandler(this);
	private GetActivitiesHandler getActivitiesHandler = new GetActivitiesHandler(this);
	private WebServiceUtil webServiceUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home_activity);
		this.setTitle("              ");
		initListNavigation();
		initLocationService();
		initHomeActivityView();
	}
	
	private void initListNavigation() {
		// ActionBar上的下拉选项与左右按钮
		Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> listNavigationAdapter = ArrayAdapter.createFromResource(context, R.array.sorting_method, R.layout.sherlock_spinner_item);
        listNavigationAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(listNavigationAdapter, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        sortingMethods = getResources().getStringArray(R.array.sorting_method);
        
	}
	
	// 定位相关
	public static GeoPoint myGeoPoint = null;
	public MyLocationListener myLocationListener = new MyLocationListener(getMyLocationHandler);
	private LocationClient locationClient = null;
	private LocationData mylocationData;
	private void initLocationService() {
		// 定位初始化
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);
        locationClient.setLocOption(option);
        locationClient.start();
	}
	
	private void initHomeActivityView() {
		textView = (TextView) findViewById(R.id.textView);
	}
	
	private static class GetMyLocationHandler extends Handler {
    	WeakReference<HomeActivity> weakReference;
    	HomeActivity homeActivity;
    	GetMyLocationHandler(HomeActivity activity) {
    		weakReference = new WeakReference<HomeActivity>(activity);
        }
		@Override
		public void handleMessage(Message msg) {
			homeActivity = weakReference.get();
			switch(msg.what) {
			case HandlerConditions.GET_MY_LOCATION:
				homeActivity.mylocationData = (LocationData) msg.obj;
				User user = EntityTableUtil.getUser();
				user.setLatitude(homeActivity.mylocationData.latitude);
				user.setLongitude(homeActivity.mylocationData.longitude);
				new Thread(new Runnable() {
					@Override
					public void run() {
						homeActivity.webServiceUtil = new WebServiceUtil(homeActivity.updateMyLocationHandler);
						homeActivity.webServiceUtil.updateUserLocation(homeActivity.mylocationData.longitude, homeActivity.mylocationData.latitude);
					}
				}).start();
				break;
			}
		}
    	
	}
	
	private static class UpdateMyLocationHandler extends Handler {
    	WeakReference<HomeActivity> weakReference;
    	HomeActivity homeActivity;
    	UpdateMyLocationHandler(HomeActivity activity) {
    		weakReference = new WeakReference<HomeActivity>(activity);
        }
		@Override
		public void handleMessage(Message msg) {
			homeActivity = weakReference.get();
			switch(msg.what) {
			case HandlerConditions.UPDATE_MY_LOCATION_SERVER_SUCCESS:
				Toast.makeText(homeActivity, "位置更新成功", Toast.LENGTH_SHORT).show();
				homeActivity.locationClient.stop();
				// 将我的位置插入表中
				UpdateMyLocationUtil updateMyLocationUtil = new UpdateMyLocationUtil(homeActivity, homeActivity.getActivitiesHandler);
				updateMyLocationUtil.updateMyLocation();
				break;
			case HandlerConditions.UPDATE_MY_LOCATION_SERVER_FAILED:
				Toast.makeText(homeActivity, "位置更新失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
    	
	}
	
	
	
	private static class GetActivitiesHandler extends Handler {
		WeakReference<HomeActivity> weakReference;
		HomeActivity homeActivity;
		GetActivitiesHandler(HomeActivity activity) {
			weakReference = new WeakReference<HomeActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			homeActivity = weakReference.get();
			switch(msg.what) {
			case HandlerConditions.UPDATE_MY_LOCATION_SQLITE_SUCCESS:
				Toast.makeText(homeActivity, "查找活动中..", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						homeActivity.webServiceUtil.queryActivityOrderByRest(homeActivity.mylocationData.longitude, homeActivity.mylocationData.latitude);
					}
				}).start();
				break;
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_home_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if((user = EntityTableUtil.getUser()) != null) {
			textView.setText(user.getUname() + ": " + sortingMethods[itemPosition]);
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "跳到设置界面", Toast.LENGTH_SHORT).show();
			break;
		case R.id.message_box:
			Toast.makeText(this, "跳到消息盒子", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
