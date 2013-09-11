package so.zudui.activity.details;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import so.zudui.launch.activity.R;
import so.zudui.util.ImageLoaderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DetailsParticipantsAdapter extends BaseAdapter {
	
	private Context context = null;
	private List<String> participantsAvatarUrls = null;
	private int preNumber = 0;
	
	private ImageLoaderUtil imageLoaderUtil = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	public DetailsParticipantsAdapter(Context context, List<String> participantsAvatarUrls, int preNumber) {
		this.context = context;
		this.participantsAvatarUrls = participantsAvatarUrls;
		this.preNumber = preNumber;
		imageLoaderUtil = new ImageLoaderUtil(context);
		imageLoader = imageLoaderUtil.getInstance();
		options = imageLoaderUtil.getOptions();
	}

	@Override
	public int getCount() {
		return preNumber;
	}

	@Override
	public Object getItem(int arg0) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_detail_participant, null, false);
			holder.participantImageView = (ImageView) convertView.findViewById(R.id.item_imageview_participant);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position < participantsAvatarUrls.size()) {
			imageLoader.displayImage(participantsAvatarUrls.get(position), holder.participantImageView, options);
		} else if (position < preNumber) {
			imageLoader.displayImage("drawable://" + R.drawable.pic_participant_empty, holder.participantImageView, options);
		} 
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView participantImageView;
	}

}
