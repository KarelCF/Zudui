package so.zudui.friends;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import so.zudui.entity.Friends.Friend;
import so.zudui.entity.User;
import so.zudui.launch.activity.R;
import so.zudui.util.DistanceUtil;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ImageLoaderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SurroundingFriendAdapter extends BaseAdapter {
	
	private Context context;
	private List<Friend> surroundings;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	private List<String> friendBufferList = new ArrayList<String>();
	
	public List<String> getFriendBufferList() {
		return friendBufferList;
	}

	public SurroundingFriendAdapter(Context context, List<Friend> surroundings) {
		this.context = context;
		this.surroundings = surroundings;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		return surroundings.size();
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
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_friend_surroundings, null, false);
			viewHolder.surroundingAvatar = (ImageView) convertView.findViewById(R.id.surroundings_imageview_avatar);
			viewHolder.surroundingGender = (ImageView) convertView.findViewById(R.id.surroundings_imageview_gender);
			viewHolder.surroundingName = (TextView) convertView.findViewById(R.id.surroundings_textview_name);
			viewHolder.surroundingDistance = (TextView) convertView.findViewById(R.id.surroundings_textview_distance);
			viewHolder.surroundingAddCheckBox = (CheckBox) convertView.findViewById(R.id.surroundings_checkbox_add);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Friend surrounding = surroundings.get(position);
		String avatarUrl = surrounding.getUpicurl();
		String name = surrounding.getUname();
		String distance = getDistance(surrounding.getLatitude(), surrounding.getLongitude());
		
		imageLoader.displayImage(avatarUrl, viewHolder.surroundingAvatar, options);
		if (surrounding.getSex() == 1) {
			viewHolder.surroundingGender.setImageResource(R.drawable.pic_male);
		} else {
			viewHolder.surroundingGender.setImageResource(R.drawable.pic_female);
		}
		viewHolder.surroundingName.setText(name);
		viewHolder.surroundingDistance.setText(distance);
		
		// 准备被添加为好友的用户id缓存
		// 修改ListView中CheckBox的错乱问题 
		viewHolder.surroundingAddCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					surrounding.setChecked(true);
					// 注意：只有正常方式创建的list可增删object,通过Array转换的list不可,会报出UnsupportedOperationException
					friendBufferList.add(surrounding.getUid() + ",");
				} else {
					surrounding.setChecked(false);
					friendBufferList.remove(surrounding.getUid() + ",");
				}
			}
		});
		return convertView;
	}
	
	private String getDistance(double latitude, double longitude) {
		String distance = "";
		DistanceUtil distanceUtil = new DistanceUtil();
		distance = distanceUtil.getDistance(latitude, longitude);
		return distance;
	}
	
	private static class ViewHolder {
		ImageView surroundingAvatar;
		ImageView surroundingGender;
		TextView surroundingName;
		TextView surroundingDistance;
		CheckBox surroundingAddCheckBox;
	}
	
	
}
