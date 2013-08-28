package so.zudui.weibo.listener;

import so.zudui.weibo.util.AccessTokenUtil;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class AuthListener implements WeiboAuthListener {
	
	private Context context;
	private Handler handler;
	
	public AuthListener(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	 @Override
     public void onComplete(Bundle values) {
     	String code = values.getString("code");
     	if (code != null) {
     		Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show();
        	AccessTokenUtil util = new AccessTokenUtil();
			util.getAccessTokenByCode(code, handler);
     	}
         
     }

     @Override
     public void onError(WeiboDialogError e) {
         Toast.makeText(context,
             "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
     }

     @Override
     public void onCancel() {
         Toast.makeText(context, "Auth cancel",
             Toast.LENGTH_LONG).show();
     }

     @Override
     public void onWeiboException(WeiboException e) {
         Toast.makeText(context,
             "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
             .show();
     }
}
