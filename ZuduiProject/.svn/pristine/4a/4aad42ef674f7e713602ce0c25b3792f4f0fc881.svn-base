package so.zudui.friends;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import so.zudui.entity.Friends.Friend;
import so.zudui.launch.activity.R;
import so.zudui.util.DistanceUtil;
import so.zudui.util.ImageLoaderUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFriendsAdapter extends BaseAdapter {
	
	private Context context = null;
	private List<Friend> friendsList = null;
	private static final int[] FRIEND_BACKGROUND = {
		R.drawable.pic_bg_friend_red,
		R.drawable.pic_bg_friend_pink,
		R.drawable.pic_bg_friend_yellow,
		R.drawable.pic_bg_friend_green,
		R.drawable.pic_bg_friend_blue,
	};
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	public MyFriendsAdapter(Context context, List<Friend> friendsList) {
		this.context = context;
		this.friendsList = friendsList;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		return friendsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View leftView = null;
		View rightView = null;
		
		Friend friend = friendsList.get(position);
		String friendAvatarUrl = friend.getUpicurl();
		int friendGender = friend.getSex();
		String friendName = friend.getUname();
		String friendDistance = getDistance(friend.getLatitude(), friend.getLongitude());
		String friendFaith = String.valueOf(friend.getScore());
		
		if(!isPositionOdd(position)) {
			LeftViewHolder leftHolder = null;
			if (leftView == null) {
				leftHolder = new LeftViewHolder();
				leftView = inflater.inflate(R.layout.item_friend_left, null);
				leftHolder.friendLayout = (RelativeLayout) leftView.findViewById(R.id.friends_activity_relative_layout);
				leftHolder.friendAvatar = (ImageView) leftView.findViewById(R.id.friends_activity_imageview_avatar);
				leftHolder.friendGender = (ImageView) leftView.findViewById(R.id.friends_activity_imageview_gender);
				leftHolder.friendName = (TextView) leftView.findViewById(R.id.friends_activity_textview_name);
				leftHolder.friendDistance = (TextView) leftView.findViewById(R.id.friends_activity_textview_distance);
				leftHolder.friendFaith = (TextView) leftView.findViewById(R.id.friends_activity_textview_faith);
				leftView.setTag(leftHolder);
			} else {
				leftHolder = (LeftViewHolder) leftView.getTag();
			}
			leftHolder.friendLayout.setBackgroundResource(FRIEND_BACKGROUND[position % 5]);
			imageLoader.displayImage(friendAvatarUrl, leftHolder.friendAvatar, options);
			if (friendGender == 1) {
				leftHolder.friendGender.setImageResource(R.drawable.pic_male);
			} else {
				leftHolder.friendGender.setImageResource(R.drawable.pic_female);
			}
			leftHolder.friendName.setText(friendName);
			leftHolder.friendDistance.setText(friendDistance);
			leftHolder.friendFaith.setText(friendFaith);
			return leftView;
			
		} else {
			RightViewHolder rightHolder = null;
			if (rightView == null) {
				rightHolder = new RightViewHolder();
				rightView = inflater.inflate(R.layout.item_friend_right, null);
				rightHolder.friendLayout = (RelativeLayout) rightView.findViewById(R.id.friends_activity_relative_layout);
				rightHolder.friendAvatar = (ImageView) rightView.findViewById(R.id.friends_activity_imageview_avatar);
				rightHolder.friendGender = (ImageView) rightView.findViewById(R.id.friends_activity_imageview_gender);
				rightHolder.friendName = (TextView) rightView.findViewById(R.id.friends_activity_textview_name);
				rightHolder.friendDistance = (TextView) rightView.findViewById(R.id.friends_activity_textview_distance);
				rightHolder.friendFaith = (TextView) rightView.findViewById(R.id.friends_activity_textview_faith);
				rightView.setTag(rightHolder);
			} else {
				rightHolder = (RightViewHolder) rightView.getTag();
			}
			rightHolder.friendLayout.setBackgroundResource(FRIEND_BACKGROUND[position % 5]);
			imageLoader.displayImage(friendAvatarUrl, rightHolder.friendAvatar, options);
			if (friendGender == 1) {
				rightHolder.friendGender.setImageResource(R.drawable.pic_male);
			} else {
				rightHolder.friendGender.setImageResource(R.drawable.pic_female);
			}
			rightHolder.friendName.setText(friendName);
			rightHolder.friendDistance.setText(friendDistance);
			rightHolder.friendFaith.setText(friendFaith);
			return rightView;
		}
		
	}
	
	private String getDistance(double latitude, double longitude) {
		String distance = "";
		DistanceUtil distanceUtil = new DistanceUtil();
		distance = distanceUtil.getDistance(latitude, longitude);
		return distance;
	}
	
	private boolean isPositionOdd(int position) {
		if (position % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private static class LeftViewHolder {
		RelativeLayout friendLayout;
		ImageView friendAvatar;
		ImageView friendGender;
		TextView friendName;
		TextView friendDistance;
		TextView friendFaith;
	}
	
	private static class RightViewHolder {
		RelativeLayout friendLayout;
		ImageView friendAvatar;
		ImageView friendGender;
		TextView friendName;
		TextView friendDistance;
		TextView friendFaith;
	}

}
