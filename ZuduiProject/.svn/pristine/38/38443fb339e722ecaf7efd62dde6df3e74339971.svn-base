package so.zudui.home;

import java.util.ArrayList;
import java.util.List;

import so.zudui.entity.Activities.Activity;
import so.zudui.launch.activity.R;
import so.zudui.util.DistanceUtil;
import so.zudui.util.ImageLoaderUtil;
import so.zudui.util.TimeUtil;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageActivitiesAdatper extends BaseAdapter {
	
	private Context context;
	private List<Activity> homePageActivities = new ArrayList<Activity>();
	
	private ParticipantsAdatper participantsAdatper = null;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	
	public HomePageActivitiesAdatper(Context context, List<Activity> homePageActivities) {
		this.context = context;
		this.homePageActivities = homePageActivities;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	
	@Override
	public int getCount() {
		return homePageActivities.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		NormalViewHolder normalHolder;
		
		if (convertView == null) {
			normalHolder = new NormalViewHolder();
			convertView = inflater.inflate(R.layout.item_home_activities, null);
			normalHolder.activityIconImageView = (ImageView) convertView.findViewById(R.id.home_page_item_imageview_category);
			normalHolder.activityTitleTextView = (TextView) convertView.findViewById(R.id.home_page_item_textview_title);
			normalHolder.activityDistanceTextView = (TextView) convertView.findViewById(R.id.home_page_item_textview_distance);
			normalHolder.activityStartTimeTextView = (TextView) convertView.findViewById(R.id.home_page_item_textview_start_time);
			normalHolder.activityAddressTextView = (TextView) convertView.findViewById(R.id.home_page_item_textview_address);
			normalHolder.activityParticipantGridView = (GridView) convertView.findViewById(R.id.home_page_item_gridview_participants);
			convertView.setTag(normalHolder);
		} else {
			normalHolder = (NormalViewHolder) convertView.getTag();
		}
		
		Activity activity = homePageActivities.get(position);
		String iconUrl = activity.getIconUrl();
		String title = activity.getName();
		String distance = getDistance(activity.getLatitude(), activity.getLongitude());
		String address = activity.getAddress();
		String startTime = TimeUtil.getFormatStartTime(activity.getStarttime());
				
		imageLoader.displayImage(iconUrl, normalHolder.activityIconImageView, options);
		normalHolder.activityTitleTextView.setText(title);
		normalHolder.activityDistanceTextView.setText(distance);
		normalHolder.activityStartTimeTextView.setText(startTime);
		normalHolder.activityAddressTextView.setText(address);
		// 添加参与者adapter
		
		participantsAdatper = new ParticipantsAdatper(context, activity.getParticipantsAvatar(), activity.getPreNumber());
		normalHolder.activityParticipantGridView.setAdapter(participantsAdatper);
		
		return convertView; 
	}
	
	private String getDistance(float latitude, float longitude) {
		String distance = "";
		DistanceUtil distanceUtil = new DistanceUtil();
		distance = distanceUtil.getDistance(latitude, longitude);
		return distance;
	}
	
	private static class NormalViewHolder {
		ImageView activityIconImageView;
		TextView activityTitleTextView;
		TextView activityDistanceTextView;
		TextView activityStartTimeTextView;
		TextView activityAddressTextView;
		GridView activityParticipantGridView;
	}
	
}
