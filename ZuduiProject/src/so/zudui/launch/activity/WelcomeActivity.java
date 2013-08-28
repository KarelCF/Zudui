package so.zudui.launch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {
	
	private int releaseTime = 1800;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// ʹ��������ȫ����ʾ,�����������Ϸ���״̬��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_welcome_activity);
        
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* �������ػ��� */
                Intent launchIntent = new Intent(WelcomeActivity.this, LaunchActivity.class);
                WelcomeActivity.this.startActivity(launchIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                WelcomeActivity.this.finish();
            }
        }, releaseTime); //time for release
	}
	
}
