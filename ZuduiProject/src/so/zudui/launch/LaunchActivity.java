package so.zudui.launch;

import java.lang.ref.WeakReference;

import so.zudui.condition.HandlerConditions;
import so.zudui.database.InsertUserInfoUtil;
import so.zudui.home.HomePage;
import so.zudui.launch.activity.R;
import so.zudui.webservice.WebServiceUtil;
import so.zudui.weibo.entity.AccessTokenInfo;
import so.zudui.weibo.entity.ProfileInfo;
import so.zudui.weibo.listener.AuthListener;
import so.zudui.weibo.oauth.AccessTokenKeeper;
import so.zudui.weibo.oauth.OauthConstants;
import so.zudui.weibo.util.AccessTokenUtil;
import so.zudui.weibo.util.WeiboInfoUtil;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.sso.SsoHandler;

import android.os.AsyncTask;
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
	// 所有handler
	private InfoReceivedHandler infoReceivedHandler = new InfoReceivedHandler(this);
	
	AuthListener authListener = new AuthListener(this);
	
	private Weibo weiboInstance;   
	private SsoHandler ssoHandler;
	private ImageButton signInBtn = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// 使启动界面全屏显示,并且隐藏最上方的状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_launch_page);
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
				weiboInstance.anthorize(LaunchActivity.this, authListener);
				new LaunchTask().execute();
//				ssoHandler = new SsoHandler(LaunchActivity.this, weiboInstance);
//              ssoHandler.authorize(new AuthListener(LaunchActivity.this, LaunchActivity.this.launchHandler),null);
			}
		});
	}
		
	private ProgressDialog progressDialog = null;
    private WeiboInfoUtil weiboInfoUtil;
    private AccessTokenInfo accessTokenInfo;
    private String token = "";
    private String expiresIn = "";
    private long uid;
	
	private class LaunchTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			AccessTokenUtil util = new AccessTokenUtil();
			String code = "";
			do {
				code = authListener.getCode();
			}
			while (code == null || code.equals(""));
			accessTokenInfo = util.getAccessTokenInfoByCode(code);
			getAccessToken();
			accessToken = new Oauth2AccessToken(token, expiresIn);
			isAccessTokenVaild();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog = ProgressDialog.show(LaunchActivity.this, null, "登陆中...", true);
		}
			
		private void getAccessToken() {
			token = accessTokenInfo.getAccessToken();
			expiresIn = accessTokenInfo.getExpiresIn();
			uid = accessTokenInfo.getUid();
            saveUid();
		}
		
		private void saveUid() {
			SharedPreferences preference = getSharedPreferences("Uid", Activity.MODE_PRIVATE);
			//获得SharedPreferences.Editor对象
	        SharedPreferences.Editor editor = preference.edit();
	        //保存组件中的值
	        editor.putLong("uid", uid);
	        editor.commit();
		}
	}
	
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
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    
	private static WebServiceUtil webServiceUtil;
	
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
				webServiceUtil = new WebServiceUtil(profileInfo);
				launchActivity.new UserLoginTask().execute();
				break;
			}
		}
		
	}
	
	private class UserLoginTask extends AsyncTask<Void, Void, Integer> {
		
		@Override
		protected Integer doInBackground(Void... params) {
			int result = webServiceUtil.userLogin();
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == WebServiceUtil.SUCCESS) {
				saveUserInfoToDatabase();
				if(progressDialog != null)
					progressDialog.dismiss();
				startHomeActivty();
			} else {
				if(progressDialog != null)
					progressDialog.dismiss();
				Toast.makeText(LaunchActivity.this, "登录失败,请再试一次吧", Toast.LENGTH_SHORT).show();
			}
		}
		
		private void saveUserInfoToDatabase() {
			InsertUserInfoUtil insertUserInfoUtil = new InsertUserInfoUtil(LaunchActivity.this);
			insertUserInfoUtil.insertUserInfo();
		}
		
		private void startHomeActivty() {
			Intent oldUserintent = new Intent(LaunchActivity.this, HomePage.class);
			startActivity(oldUserintent);
			finish();
		}
		
	}
	

}
