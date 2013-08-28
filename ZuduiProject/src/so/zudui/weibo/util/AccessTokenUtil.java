package so.zudui.weibo.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import so.zudui.handler.condition.HandlerConditions;
import so.zudui.util.GsonUtil;
import so.zudui.util.HttpUtil;
import so.zudui.weibo.entity.AccessTokenInfo;
import so.zudui.weibo.oauth.OauthConstants;

import android.os.Handler;
import android.os.Message;


public class AccessTokenUtil {
	
	private static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String result;
	private AccessTokenInfo accessTokenInfo = null;
	private Handler launchHandler;
	
	public void getAccessTokenByCode(String code, Handler handler) {
		launchHandler = handler;
		preparePostRequestParams(code);
		new Thread(new Runnable() {
			@Override
			public void run() {
				result = HttpUtil.queryStringForPost(ACCESS_TOKEN_URL, params);
				accessTokenInfo = (AccessTokenInfo) GsonUtil.parseJsonToObject(result, AccessTokenInfo.class);
				// ͨ������UI�̵߳�Handler���������߳���������ķ���ֵ,����UI�߳��е�
				// Handler���д�������,��������߳������̼߳䲻ͬ�������µĿ�ָ���쳣����				
				Message notifyMsg = launchHandler.obtainMessage(HandlerConditions.GET_ACCESS_TOKEN, accessTokenInfo);
				launchHandler.sendMessage(notifyMsg);
			}
		}).start();
	}
	
	private void preparePostRequestParams(String code) {
		params.add(new BasicNameValuePair("client_id", OauthConstants.APP_KEY));
		params.add(new BasicNameValuePair("client_secret", OauthConstants.APP_SECRET));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("redirect_uri", OauthConstants.REDIRECT_URL));
		params.add(new BasicNameValuePair("code", code));
	}
	
}
