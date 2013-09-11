package so.zudui.util;

import so.zudui.entity.User;
import android.location.Location;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class DistanceUtil {
	
	private  Location start = new Location("start");  
	private  Location end = new Location("end");
	private  GeoPoint startPoint;
	private  GeoPoint endPoint;
	private  float distance = 0;
	private User user = null;
	
	public DistanceUtil() {
		user = EntityTableUtil.getMainUser();
	}
	
	public String getDistance(double latitude, double longitude) {
		startPoint = new GeoPoint( (int) ( user.getLatitude()* 1e6), (int) (user.getLongitude() * 1e6) );
		endPoint = new GeoPoint( (int) (latitude * 1e6), (int) (longitude * 1e6) );
		getStartAndEndLocation();
    	distance = start.distanceTo(end);
		String distanceString = getDistaceToString();
		return distanceString;
	}
	
	private void getStartAndEndLocation() {
    	start.setLatitude(startPoint.getLatitudeE6() / 1E6);  
		start.setLongitude(startPoint.getLongitudeE6() / 1E6);  
		end.setLatitude(endPoint.getLatitudeE6() / 1E6);  
		end.setLongitude(endPoint.getLongitudeE6() / 1E6);  
    }
	
	private String getDistaceToString() {
		double distanceTemp = distance / 1000;
		StringBuffer distaceToString = new StringBuffer();
		if (distanceTemp < 1) {
			distanceTemp = distanceTemp * 1000;
			distaceToString.append(String.format("%.0f", distanceTemp));
			distaceToString.append("m");
		} else {
			distaceToString.append(String.format("%.2f", distanceTemp));
			distaceToString.append("km");
		}
		return distaceToString.toString();
	}
	
}
