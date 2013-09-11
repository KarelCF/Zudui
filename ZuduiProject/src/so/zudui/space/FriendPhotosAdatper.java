package so.zudui.space;


import so.zudui.launch.activity.R;
import so.zudui.util.ImageLoaderUtil;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FriendPhotosAdatper extends BaseAdapter {
	
	private String[] photoUrls;  
	private Context context;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	public FriendPhotosAdatper(Context context, String[] photoUrls) {
		this.context = context;
		this.photoUrls = photoUrls;
		
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		if(photoUrls != null)
			return photoUrls.length;
		return 0;
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
			convertView = inflater.inflate(R.layout.item_friend_photos, null, false);
			holder.photosImageView = (ImageView) convertView.findViewById(R.id.space_activity_imageview_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(photoUrls[position], holder.photosImageView, options);
		return convertView; 
	}
	
	private static class ViewHolder {
		ImageView photosImageView;
	}
	
}
