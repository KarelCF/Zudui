package so.zudui.launch.activity;

import java.lang.ref.WeakReference;

import so.zudui.database.InsertUserInfoUtil;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.home.HomeActivity;
import so.zudui.webservice.WebServiceUtil;
import so.zudui.weibo.entity.AccessTokenInfo;
import so.zudui.weibo.entity.ProfileInfo;
import so.zudui.weibo.listener.AuthListener;
import so.zudui.weibo.oauth.AccessTokenKeeper;
import so.zudui.weibo.oauth.OauthConstants;
import so.zudui.weibo.util.WeiboInfoUtil;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.sso.SsoHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class LaunchActivity extends Activity {
	
	private static Oauth2AccessToken accessToken;
	private LaunchHandler launchHandler = new LaunchHandler(this);
	
	private Weibo weiboInstance;   
	private SsoHandler ssoHandler;
	
	private ListView bilateralListView;
	private ImageButton signInBtn = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// 使启动界面全屏显示,并且隐藏最上方的状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_launch_activity);
        initWeiboOauth();
        initLaunchActivityView();
        isAccessTokenVaild();
	}
	
	private void initWeiboOauth() {
		weiboInstance = Weibo.getInstance(OauthConstants.APP_KEY, OauthConstants.REDIRECT_URL, OauthConstants.SCOPE);
		accessToken = AccessTokenKeeper.readAccessToken(this);  
		
	}
	
	private void initLaunchActivityView() {
		signInBtn = (ImageButton) findViewById(R.id.launchactivity_button_sign_in);
		signInBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				weiboInstance.anthorize(LaunchActivity.this, new AuthListener(LaunchActivity.this, LaunchActivity.this.launchHandler));
//				ssoHandler = new SsoHandler(LaunchActivity.this, weiboInstance);
//              ssoHandler.authorize(new AuthListener(LaunchActivity.this, LaunchActivity.this.launchHandler),null);
			}
		});
	}
		
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
	
    private WeiboInfoUtil weiboInfoUtil;
    private AccessTokenInfo accessTokenInfo;
    private String token = "";
    private String expiresIn = "";
    private long uid;
    
	private static class LaunchHandler extends Handler {
    	// 持有弱引用，消除因Handler类设置static关键字而产生的
    	// "This Handler class should be static or leaks might occur"警告
    	WeakReference<LaunchActivity> weakReference;
    	LaunchActivity launchActivity;
    	LaunchHandler(LaunchActivity activity) {
    		weakReference = new WeakReference<LaunchActivity>(activity);
        }
		@Override
		public void handleMessage(Message msg) {
			launchActivity = weakReference.get();
			switch (msg.what) {
			case HandlerConditions.GET_ACCESS_TOKEN:
				
				launchActivity.accessTokenInfo = (AccessTokenInfo) msg.obj;
				getAccessToken(launchActivity.accessTokenInfo);
				accessToken = new Oauth2AccessToken(launchActivity.token, launchActivity.expiresIn);
				launchActivity.isAccessTokenVaild();
				break;
			}
		}
		
		private void getAccessToken(AccessTokenInfo accessTokenInfo) {
			launchActivity.token = accessTokenInfo.getAccessToken();
			launchActivity.expiresIn = accessTokenInfo.getExpiresIn();
			launchActivity.uid = accessTokenInfo.getUid();
            saveUid();
		}
		
		private void saveUid() {
			SharedPreferences preference = launchActivity.getSharedPreferences("Uid", Activity.MODE_PRIVATE);
			//获得SharedPreferences.Editor对象
	        SharedPreferences.Editor editor = preference.edit();
	        //保存组件中的值
	        editor.putLong("uid", launchActivity.uid);
	        editor.commit();
		}
		
    }
	
	private InfoReceivedHandler infoReceivedHandler = new InfoReceivedHandler(this);
	
	private boolean isAccessTokenVaild() {
		if (accessToken.isSessionValid()) {
			AccessTokenKeeper.keepAccessToken(this, accessToken);
			//获得SharedPreferences对象
	        SharedPreferences myPreferences = getSharedPreferences("Uid", Activity.MODE_PRIVATE);
	        uid = myPreferences.getLong("uid", 0);
			weiboInfoUtil = new WeiboInfoUtil(uid, accessToken, infoReceivedHandler);
			weiboInfoUtil.getUserInfo();
			return true;
	    }
		return false;
    }

	private static WebServiceUtil webServiceUtil;
	private StartHomeActivityHandler startHomeActivityHandler = new StartHomeActivityHandler(this);
	
	private static class InfoReceivedHandler extends Handler {
		WeakReference<LaunchActivity> weakReference;
		InfoReceivedHandler(LaunchActivity activity) {
			weakReference = new WeakReference<LaunchActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			LaunchActivity launchActivity = weakReference.get();
			switch (msg.what) {
			case HandlerConditions.PROFILE_INFO:
				ProfileInfo profileInfo = (ProfileInfo) msg.obj;
				webServiceUtil = new WebServiceUtil(profileInfo, launchActivity.startHomeActivityHandler);
				new Thread(new Runnable() {
					@Override
					public void run() {
						webServiceUtil.userLogin();
					}
				}).start();
				break;
			}
		}
		
	}
	
	
	private static class StartHomeActivityHandler extends Handler {
		WeakReference<LaunchActivity> weakReference;
		LaunchActivity launchActivity;
		StartHomeActivityHandler(LaunchActivity activity) {
			weakReference = new WeakReference<LaunchActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			launchActivity = weakReference.get();
			switch (msg.what) {
			case HandlerConditions.GOTO_HOME_ACTIVITY:
				saveUserInfoToDatabase();
				startHomeActivty();
				break;
			}
		}
		
		private void saveUserInfoToDatabase() {
			InsertUserInfoUtil insertUserInfoUtil = new InsertUserInfoUtil(launchActivity);
			insertUserInfoUtil.insertUserInfo();
		}
		
		private void startHomeActivty() {
			Intent oldUserintent = new Intent(launchActivity, HomeActivity.class);
			launchActivity.startActivity(oldUserintent);
			launchActivity.finish();
		}
		
	}
	

}
