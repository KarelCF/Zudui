package so.zudui.webservice;

import java.io.IOException;
import java.util.Arrays;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Message;

import so.zudui.entity.User;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.GsonUtil;
import so.zudui.weibo.entity.ProfileInfo;

public class WebServiceUtil {

    private static ProfileInfo profileInfo = null;
    private Handler handler = null;
    
    public WebServiceUtil() {}
    
    public WebServiceUtil(Handler handler) {
    	this.handler = handler;
    }
    
    public WebServiceUtil(ProfileInfo profile, Handler handler) {
    	profileInfo = profile;
    	this.handler = handler;
    }
    
	public void userLogin() {
		// ���SoapObject����
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_USER_LOGIN);
        so.addProperty("uid", profileInfo.getId());
        so.addProperty("uname", profileInfo.getName());
        so.addProperty("upicurl", profileInfo.getProfileImageUrl());
        so.addProperty("sex", profileInfo.getGender());
        
        // ���Enveloper����
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        
        // AndroidHttpTransport���Ѿ����ڲ���ʹ��
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_USER_LOGIN, sse);
            // �ж����޻�Ӧ�����У����÷����������ص�xml
            if (sse.getResponse() != null) {
                // �ӷ������ݴ�����ȡJson����
            	String userInfoInJson = catchJson(sse.bodyIn.toString());
	    		EntityTableUtil.setUser( (User) GsonUtil.parseJsonToObject(userInfoInJson, User.class) );
	    		handler.sendEmptyMessage(HandlerConditions.GOTO_HOME_ACTIVITY);
            }      
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
	}
	
	public void updateUserLocation(double longitude, double latitude) {
		// ���SoapObject����
		String longitudeString = longitude + "";
		String latitudeString = latitude + "";
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_UPDATE_USER_LOCATION);
        so.addProperty("uid", profileInfo.getId());
        so.addProperty("longitude", longitudeString);
        so.addProperty("latitude", latitudeString);
        
        // ���Enveloper����
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_UPDATE_USER_LOCATION, sse);
            // �ж����޻�Ӧ�����У����÷����������ص�xml
            if (sse.getResponse() != null) {
                // �ӷ������ݴ�����ȡJson����
            	String feedback = catchJson(sse.bodyIn.toString());
            	if(feedback.equals("ok")) {
            		handler.sendEmptyMessage(HandlerConditions.UPDATE_MY_LOCATION_SERVER_SUCCESS);
            	} else {
            		handler.sendEmptyMessage(HandlerConditions.UPDATE_MY_LOCATION_SERVER_FAILED);
            	}
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
	}
	
	public void queryActivityOrderByRest(double longitude, double latitude) {
		// ���SoapObject����
		String longitudeString = longitude + "";
		String latitudeString = latitude + "";
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_ACTIVITY_ORDER_BY_REST);
        so.addProperty("longitude", longitudeString);
        so.addProperty("latitude", latitudeString);
        // ���Enveloper����
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST, sse);
            // �ж����޻�Ӧ�����У����÷����������ص�xml
            if (sse.getResponse() != null) {
                // �ӷ������ݴ�����ȡJson����
System.out.println(sse.bodyIn.toString());
            	// TODO ����Ľӿڿ��ܴ���, ���޸�
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
	}
	
	public void uploadUserShowImages(byte[] picdata) {
		// ���SoapObject����
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_UPLOAD_PHOTOS);
        // ע��,�˴���Ӳ���Ҫ��ByteArrayתΪString��ʽ,����ᱨCannot Serialize����
        so.addProperty("picdata", Arrays.toString(picdata));
        so.addProperty("uid", profileInfo.getId());
        // ���Enveloper����
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_UPLOAD_PHOTOS, sse);
            // �ж����޻�Ӧ�����У����÷����������ص�xml
            if (sse.getResponse() != null) {
            	String photoUrl = catchPhotoUrls(sse.bodyIn.toString());
            	Message msg = handler.obtainMessage(HandlerConditions.UPLOAD_MY_PHOTOS, photoUrl);
            	handler.sendMessage(msg);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
	}
	
	public void deleteUserShowImages(String showimages) {
		// TODO �����޸�
		handler.sendEmptyMessage(HandlerConditions.DELETE_MY_PHOTOS_BEGIN);
		// ���SoapObject����
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_DELETE_PHOTOS);
        so.addProperty("uid", profileInfo.getId());
        so.addProperty("showimages", showimages);
        // ���Enveloper����
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_DELETE_PHOTOS, sse);
            // �ж����޻�Ӧ�����У����÷����������ص�xml
            if (sse.getResponse() != null) {
System.out.println(sse.bodyIn.toString());
				handler.sendEmptyMessage(HandlerConditions.DELETE_MY_PHOTOS_FINISH);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
	}
	
	private String catchJson(String userInfoFromServer) {
		String jsonString = "";
		int startPosition = userInfoFromServer.lastIndexOf("return=") + 7;
		int endPosition = userInfoFromServer.indexOf("; }");
		jsonString = userInfoFromServer.substring(startPosition, endPosition);
		return jsonString;
	}
	
	private String catchPhotoUrls(String photoUrlsFromServer) {
		String PhotoUrls = "";
		int startPosition = photoUrlsFromServer.lastIndexOf("ok_") + 3;
		int endPosition = photoUrlsFromServer.indexOf("; }");
		PhotoUrls = photoUrlsFromServer.substring(startPosition, endPosition);
		return PhotoUrls;
	}

}
