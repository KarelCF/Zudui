package so.zudui.friends;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import so.zudui.launch.activity.R;
import so.zudui.util.ImageLoaderUtil;
import so.zudui.weibo.entity.Bilateral.Users;

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

public class SinaFriendAdapter extends BaseAdapter {
	
	private Context context;
	private List<Users> sinaFriends;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	public SinaFriendAdapter(Context context, List<Users> sinaFriendList) {
		this.context = context;
		this.sinaFriends = sinaFriendList;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		return sinaFriends.size();
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
			convertView = inflater.inflate(R.layout.item_friend_weibo, null, false);
			viewHolder.sinaFriendAvatar = (ImageView) convertView.findViewById(R.id.weibos_imageview_avatar);
			viewHolder.sinaFriendGender = (ImageView) convertView.findViewById(R.id.weibos_imageview_gender);
			viewHolder.sinaFriendName = (TextView) convertView.findViewById(R.id.weibos_textview_name);
			viewHolder.sinaFriendAddCheckBox = (CheckBox) convertView.findViewById(R.id.weibos_checkbox_add);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Users sinaFriend = sinaFriends.get(position);
		String avatarUrl = sinaFriend.getBilateralImageUrl();
		String name = sinaFriend.getName();
		
		imageLoader.displayImage(avatarUrl, viewHolder.sinaFriendAvatar, options);
		if (sinaFriend.getGender().equals("m")) {
			viewHolder.sinaFriendGender.setImageResource(R.drawable.pic_male);
		} else {
			viewHolder.sinaFriendGender.setImageResource(R.drawable.pic_female);
		}
		viewHolder.sinaFriendName.setText(name);
		
		// TODO 修改ListView中CheckBox的错乱问题
		viewHolder.sinaFriendAddCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					sinaFriend.setChecked(true);
				} else {
					sinaFriend.setChecked(false);
				}
			}
		});
		
		if (sinaFriend.isChecked()) {
			viewHolder.sinaFriendAddCheckBox.setChecked(true);
		} else {
			viewHolder.sinaFriendAddCheckBox.setChecked(false);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView sinaFriendAvatar;
		ImageView sinaFriendGender;
		TextView sinaFriendName;
		CheckBox sinaFriendAddCheckBox;
	}
	
}
