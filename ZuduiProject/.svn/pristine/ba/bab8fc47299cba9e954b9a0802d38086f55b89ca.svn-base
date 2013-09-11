package so.zudui.home;

import java.util.List;

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

public class ParticipantsAdatper extends BaseAdapter {
	
	private static final int MAX_LENGTH = 6;
	private Context context;
	
	private List<String> participantsAvatar;
	private int preNumber = 0;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public ParticipantsAdatper(Context context, List<String> participantsAvatar, int preNumber) {
		this.context = context;
		this.preNumber = preNumber;
		this.participantsAvatar = participantsAvatar;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}
	
	@Override
	public int getCount() {
		return MAX_LENGTH;
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
			convertView = inflater.inflate(R.layout.item_participant, null);
			holder.participantImageView = (ImageView) convertView.findViewById(R.id.item_imageview_participant);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position < participantsAvatar.size()) {
			imageLoader.displayImage(participantsAvatar.get(position), holder.participantImageView, options);
		} else if (position < preNumber) {
			imageLoader.displayImage("drawable://" + R.drawable.pic_participant_empty, holder.participantImageView, options);
		} else {
			imageLoader.displayImage("drawable://" + R.drawable.pic_participant_forbit, holder.participantImageView, options);
		}
		return convertView; 
	}
	
	
	private static class ViewHolder {
		ImageView participantImageView;
	}
	
}
