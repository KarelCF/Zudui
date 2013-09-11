package so.zudui.space;

import java.util.List;

import so.zudui.database.UpdatePhotosUtil;
import so.zudui.entity.User;
import so.zudui.launch.activity.R;
import so.zudui.space.Constants.Extra;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.ListAndArrayConversionUtil;
import so.zudui.webservice.WebServiceUtil;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MyImagePagerActivity extends SherlockActivity {

	private static final String STATE_POSITION = "STATE_POSITION";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private DisplayImageOptions options;

	private ViewPager pager = null;
	private Button deletePhotoBtn = null;
	
	private int pagerPosition = 0;
	private User user = EntityTableUtil.getMainUser();
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_photos_pager);
		setTitle(user.getUname());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
		String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		pager = (ViewPager) findViewById(R.id.photosPager);
		deletePhotoBtn = (Button) findViewById(R.id.deletePhotoBtn);
		deletePhotoBtn.setOnClickListener(new DeletePhotoListener());
		
		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(pagerPosition);
	}
	
	private class DeletePhotoListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			new DeletePhotoTask().execute(pagerPosition);
//			adapter.deletePicFromList(pagerPosition);
		}
		
	}
	
	private class DeletePhotoTask extends AsyncTask<Integer, Void, Integer> {
		ProgressDialog progressDialog = null;
		MyPhotosAdatper myPhotosAdatper = MyPhotosAdatper.getInstance();
		public DeletePhotoTask() {
			progressDialog = ProgressDialog.show(MyImagePagerActivity.this, null, "É¾³ýÕÕÆ¬ÖÐ...", true);
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = 0;
			int pagerPosition = params[0];
System.out.println("pagerPosition:" + pagerPosition);			
			List<String> photoUrlList = myPhotosAdatper.getPhotoUrlList();
			photoUrlList.remove(pagerPosition);
			String[] photosUrl = ListAndArrayConversionUtil.listToArray(photoUrlList);
			StringBuffer buffer = new StringBuffer(",");
			for (int i = 0; i < photosUrl.length - 1; i++)
				buffer.append(photosUrl[i] + ",");
System.out.println(buffer.toString());
			user.setShowimages(buffer.toString());
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			result = webServiceUtil.deleteUserShowImages(user.getShowimages());
			if (result !=  WebServiceUtil.SUCCESS)
				return result;
			UpdatePhotosUtil updatePhotosUtil = new UpdatePhotosUtil(MyImagePagerActivity.this);
			updatePhotosUtil.updatePhotos();
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result == WebServiceUtil.SUCCESS) {
				myPhotosAdatper.notifyDataSetChanged();
				Toast.makeText(MyImagePagerActivity.this, "ÏàÆ¬ÒÑÉ¾³ý", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MyImagePagerActivity.this, "ÏàÆ¬É¾³ýÊ§°Ü", Toast.LENGTH_SHORT).show();
			}
			MyImagePagerActivity.this.finish();
			super.onPostExecute(result);
		}
		
		
		
	}
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_photo_pager, view, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(MyImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}