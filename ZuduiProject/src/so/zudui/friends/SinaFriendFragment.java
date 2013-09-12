package so.zudui.friends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import so.zudui.condition.HandlerConditions;
import so.zudui.launch.LaunchActivity;
import so.zudui.launch.activity.R;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.GsonUtil;
import so.zudui.weibo.api.FriendshipsAPI;
import so.zudui.weibo.entity.AccessTokenInfo;
import so.zudui.weibo.entity.Bilateral;
import so.zudui.weibo.entity.Bilateral.Users;
import so.zudui.weibo.oauth.AccessTokenKeeper;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class SinaFriendFragment extends Fragment {
	
	private View sinaFriendView = null;
	private Context context = null;
	
	private SinaFriendAdapter adapter = null;
	private ListView listView = null;
	private List<Users> sinaFriendList;
	
	private ProgressDialog progressDialog = null;
	
	private SinaFriendHandler sinaFriendHandler = new SinaFriendHandler(this);
	
	public SinaFriendFragment(Context context) {
		this.context = context;
		new GetWeiboFriendTask().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sinaFriendView = inflater.inflate(R.layout.layout_weibo_friend, container, false);
		listView = (ListView) sinaFriendView.findViewById(R.id.weibo_friend_listview);
		return sinaFriendView;
	}
	
	private class GetWeiboFriendTask extends AsyncTask<Void, Void, Void> {
		
		public GetWeiboFriendTask() {
			progressDialog = ProgressDialog.show(context, null, "正在查找微博好友...", true);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String uidString = EntityTableUtil.getMainUser().getUid().substring(2);
	        long uid = Long.parseLong(uidString);
			Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
			getBilateral(uid, token);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
		
	}
	
	private void getBilateral(long uid, Oauth2AccessToken accessToken) {
    	FriendshipsAPI friendshipsApi = new FriendshipsAPI(accessToken);
    	int perpageItemCount = 50;
    	int homePageLocation = 1;
    	friendshipsApi.bilateral(uid, perpageItemCount, homePageLocation, new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
			}
			
			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
			}
			
			@Override
			public void onComplete4binary(ByteArrayOutputStream stream) {}
			
			@Override
			public void onComplete(String response) {
				Bilateral bilateral = (Bilateral) GsonUtil.parseJsonToObject(response, Bilateral.class);
				sinaFriendList = bilateral.getUser();
				sinaFriendHandler.sendEmptyMessage(HandlerConditions.GET_SINA_FRIEND);
			}
		});
    }
	
	private static class SinaFriendHandler extends Handler {
		
    	WeakReference<SinaFriendFragment> weakReference;

    	SinaFriendHandler(SinaFriendFragment fragment) {
    		weakReference = new WeakReference<SinaFriendFragment>(fragment);
        }
    	
    	SinaFriendFragment sinaFriendFragment;
		@Override
		public void handleMessage(Message msg) {
			sinaFriendFragment = weakReference.get();
			switch (msg.what) {
			case HandlerConditions.GET_SINA_FRIEND:
				sinaFriendFragment.adapter = new SinaFriendAdapter(sinaFriendFragment.context, sinaFriendFragment.sinaFriendList);
				sinaFriendFragment.listView.setAdapter(sinaFriendFragment.adapter);
				break;
			}
		}
		
	}
}
