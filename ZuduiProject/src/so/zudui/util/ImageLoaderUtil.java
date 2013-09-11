package so.zudui.util;

import so.zudui.launch.activity.R;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderUtil {
	
	private static ImageLoader imageLoader = null;
	private static DisplayImageOptions options = null;
	private Context context = null;
	
	public ImageLoaderUtil(Context context) {
		this.context = context;
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			initLoaderOptions();
		}
	}
	
	private void initLoaderOptions() {
		if (!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	public ImageLoader getInstance() {
		return imageLoader;
	}
	
	public DisplayImageOptions getOptions() {
		return options;
	}
}
