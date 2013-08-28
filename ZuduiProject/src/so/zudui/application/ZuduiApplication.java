package so.zudui.application;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ZuduiApplication extends Application {

	private static final String BAIDU_MAP_APP_KEY = "53495cbe94d268ea860fc824bb77a370";
	private static ZuduiApplication mapApplicationInstance = null;
	private static BMapManager mapManager = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mapApplicationInstance = this;
		initBMapManager(this);
	}
	
	public void initBMapManager(Context context) {
		if (mapManager == null)
			mapManager = new BMapManager(context);
		boolean initSucceed = mapManager.init(BAIDU_MAP_APP_KEY, new MyGeneralListener());
        if (!initSucceed)
            Toast.makeText(mapApplicationInstance.getApplicationContext(), "��ͼ��������ʼ������", Toast.LENGTH_SHORT).show();
	}
	
	public static ZuduiApplication getInstance() {
		return mapApplicationInstance;
	}
	
	// �����¼���������������ͨ�������������Ȩ��֤�����
    private static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT)
                Toast.makeText(ZuduiApplication.getInstance().getApplicationContext(), "�������Ӵ���", Toast.LENGTH_SHORT).show();
            else if (iError == MKEvent.ERROR_NETWORK_DATA)
                Toast.makeText(ZuduiApplication.getInstance().getApplicationContext(), "�������ݴ���", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED)
                Toast.makeText(ZuduiApplication.getInstance().getApplicationContext(), "����MapApplication.java�ļ�������ȷ����ȨKey", Toast.LENGTH_LONG).show();
        }
    }
	
	public static BMapManager getBMapManager() {
		return mapManager;
	}
	
}