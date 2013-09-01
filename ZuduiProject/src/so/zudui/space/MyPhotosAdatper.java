package so.zudui.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import so.zudui.database.UpdatePhotosUtil;
import so.zudui.entity.User;
import so.zudui.launch.activity.R;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ListAndArrayConversionUtil;
import so.zudui.webservice.WebServiceUtil;

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
import android.widget.Toast;

public class MyPhotosAdatper extends BaseAdapter {
	
	private static final int PHOTO_LIST_MAX_SIZE = 5;
	private static MyPhotosAdatper myPhotosAdapter = null;
	private Context context;
	private List<String> photoUrlList = new ArrayList<String>();  
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	private User user = EntityTableUtil.getUser();
	
	public MyPhotosAdatper(Context context) {
		this.context = context;
		// TODO 周日修改，这里是缓存机制的改动
		String photoUrls = user.getShowimages();
		if(!photoUrls.equals(",")) {
			String[] photoUrlsArray = photoUrls.split(",");
			photoUrlList = ListAndArrayConversionUtil.arrayToList(photoUrlsArray);
		}
		photoUrlList.add("drawable://" + R.drawable.pic_add_photo);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		initLoaderOptions();
		myPhotosAdapter = this;
	}
	
	public static MyPhotosAdatper getInstance() {
		return myPhotosAdapter;
	}
	
	private void initLoaderOptions() {
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	public List<String> getPhotoUrlList() {
		return photoUrlList;
	}
	
	public void addPicToList(String url) {
		photoUrlList.add(url);
		moveButtonToLast();
		if(photoUrlList.size() == PHOTO_LIST_MAX_SIZE)
			Toast.makeText(context, "您最多只能上传4张图片哦!", Toast.LENGTH_SHORT).show();
	}
	
	private void moveButtonToLast() {
		Collections.swap(photoUrlList, photoUrlList.size() - 1, photoUrlList.size() - 2);
	}
	
	public void deletePicFromList(int position) {
		// TODO 周日修改
		photoUrlList.remove(position);
		String[] photosUrl = ListAndArrayConversionUtil.listToArray(photoUrlList);
		StringBuffer buffer = new StringBuffer(",");
		for (int i = 0; i < photosUrl.length; i++)
			buffer.append(photosUrl[i] + ",");
		user.setShowimages(buffer.toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				WebServiceUtil webServiceUtil = new WebServiceUtil();
				webServiceUtil.deleteUserShowImages(user.getShowimages());
			}
		}).start();
		UpdatePhotosUtil updatePhotosUtil = new UpdatePhotosUtil(context);
		updatePhotosUtil.updatePhotos();
		
	}
	
	@Override
	public int getCount() {
		return photoUrlList.size();
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
		final int index = position;
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_photos_item, null, false);
			holder.photosImageView = (ImageView) convertView.findViewById(R.id.myspace_activity_imageview_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		imageLoader.displayImage(photoUrlList.get(index), holder.photosImageView, options);
		
		return convertView; 
	}
	
	private static class ViewHolder {
		ImageView photosImageView;
	}
	
}
