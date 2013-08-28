package so.zudui.baidumap;

import so.zudui.handler.condition.HandlerConditions;
import so.zudui.webservice.WebServiceUtil;

import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyLocationListener implements BDLocationListener {
	
	private LocationData locationData = new LocationData();
	private GeoPoint myGeoPoint = null;
	
	private Handler handler = null;
	
	public MyLocationListener(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
            return ;
        locationData.latitude = location.getLatitude();
        locationData.longitude = location.getLongitude();
        myGeoPoint = new GeoPoint( (int) (locationData.latitude * 1e6), (int) (locationData.longitude * 1e6) );
        //�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
        locationData.accuracy = location.getRadius();
        locationData.direction = location.getDerect();
        
        Message msg = handler.obtainMessage(HandlerConditions.GET_MY_LOCATION, locationData);
        handler.sendMessage(msg);
        
	}

	@Override
	public void onReceivePoi(BDLocation poiLocation) {
		if (poiLocation == null)
            return ;
	}
	
	public GeoPoint getMyGeoPoint() {
		return myGeoPoint;
	}
}
