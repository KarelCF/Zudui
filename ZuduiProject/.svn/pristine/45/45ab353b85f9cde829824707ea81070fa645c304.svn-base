package so.zudui.space;

import java.util.ArrayList;
import java.util.List;

import so.zudui.entity.Activities;
import so.zudui.entity.Activities.Activity;
import so.zudui.launch.activity.R;
import so.zudui.util.ImageLoaderUtil;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivitiesAdatper extends BaseAdapter {
	
	private Context context;
	private List<Activity> myActivities = new ArrayList<Activity>();
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	
	public MyActivitiesAdatper(Context context, List<Activity> myActivities) {
		this.context = context;
		this.myActivities = myActivities;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		return myActivities.size();
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_space_activities, null);
			holder.activityIconImageView = (ImageView) convertView.findViewById(R.id.space_imageview_activity_icon);
			holder.activityStatusTextView = (TextView) convertView.findViewById(R.id.space_textview_activity_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Activity myActivity = myActivities.get(position);
		int status = myActivity.getStatus();
		String iconUrl = myActivity.getIconUrl();
		imageLoader.displayImage(iconUrl, holder.activityIconImageView, options);
		switch (status) {
			case Activities.STATUS_ONGOING:
				holder.activityStatusTextView.setTextColor(context.getResources().getColor(R.color.text_ongoing_activities));
				holder.activityStatusTextView.setText("正在");
				break;
			case Activities.STATUS_UPCOMING:
				holder.activityStatusTextView.setTextColor(context.getResources().getColor(R.color.text_upcoming_activities));
				holder.activityStatusTextView.setText("即将");
				break;
			case Activities.STATUS_FINISHED:
				holder.activityStatusTextView.setTextColor(context.getResources().getColor(R.color.text_finished_activities));
				holder.activityStatusTextView.setText("结束");
				break;
		}
		return convertView; 
	}
	
	private static class ViewHolder {
		ImageView activityIconImageView;
		TextView activityStatusTextView;
	}
	
}
