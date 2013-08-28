package so.zudui.weibo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import so.zudui.handler.condition.HandlerConditions;
import so.zudui.util.GsonUtil;
import so.zudui.weibo.api.FriendshipsAPI;
import so.zudui.weibo.api.UsersAPI;
import so.zudui.weibo.entity.Bilateral;
import so.zudui.weibo.entity.ProfileInfo;

import android.os.Handler;
import android.os.Message;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class WeiboInfoUtil {
	
	private long uid;
	private Oauth2AccessToken accessToken;
	private Handler infoReceivedHandler;
	
	public WeiboInfoUtil(long uid, Oauth2AccessToken accessToken, Handler handler) {
		this.uid = uid;
		this.accessToken = accessToken;
		this.infoReceivedHandler = handler;
	}
	
	public void getUserInfo() {
    	UsersAPI api = new UsersAPI(accessToken);
    	api.show(uid, new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();				
			}
			
			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();				
			}
			
			@Override
			public void onComplete(String response) {
				ProfileInfo profileInfo = (ProfileInfo) GsonUtil.parseJsonToObject(response, ProfileInfo.class);
				Message profileInfoMsg = infoReceivedHandler.obtainMessage(HandlerConditions.PROFILE_INFO, profileInfo);
				infoReceivedHandler.sendMessage(profileInfoMsg);
			}

			@Override
			public void onComplete4binary(ByteArrayOutputStream stream) {}
		});
    }
	
	public void getBilateral() {
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
			}
		});
    }
	
}
