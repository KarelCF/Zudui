package so.zudui.webservice;

import java.io.IOException;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Message;

import so.zudui.condition.HandlerConditions;
import so.zudui.entity.Activities;
import so.zudui.entity.Activities.Activity;
import so.zudui.entity.Friends;
import so.zudui.entity.Friends.Friend;
import so.zudui.entity.Games;
import so.zudui.entity.User;
import so.zudui.util.EntityTableUtil;
import so.zudui.util.GsonUtil;
import so.zudui.weibo.entity.ProfileInfo;

public class WebServiceUtil {
	
	public static final int SUCCESS = 32768;
	public static final int EMPTY = 32769;
	public static final int FAILED = 32770;
	
	// 下面两个字段必须设置为static,否则每次都会新建实例导致uid为空
    private static ProfileInfo profileInfo = null;
    private static String sinaUid = null;
    private Handler handler = null;
    
    public WebServiceUtil() {}
    
    public WebServiceUtil(Handler handler) {
    	this.handler = handler;
    }
    
    public WebServiceUtil(ProfileInfo profile, Handler handler) {
    	profileInfo = profile;
    	sinaUid = "s_" + profileInfo.getId();
    	this.handler = handler;
    }
    
    public WebServiceUtil(ProfileInfo profile) {
    	profileInfo = profile;
    	sinaUid = "s_" + profileInfo.getId();
    }
    
	public int userLogin() {
		// 获得SoapObject对象
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_USER_LOGIN);
        so.addProperty("uid", sinaUid);
        so.addProperty("uname", profileInfo.getName());
        so.addProperty("upicurl", profileInfo.getProfileImageUrl());
        so.addProperty("sex", profileInfo.getGender());
        
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        
        // AndroidHttpTransport类已经过期不再使用
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_USER_LOGIN, sse);
            // 判断有无回应，若有，调用方法解析返回的xml
            if (sse.getResponse() != null) {
                // 从返回数据串中提取Json数据
            	String userInfoInJson = catchJson(sse.bodyIn.toString());
//System.out.println(userInfoInJson);
	    		EntityTableUtil.setMainUser( (User) GsonUtil.parseJsonToObject(userInfoInJson, User.class) );
	    		return SUCCESS;
            }      
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return FAILED;
	}
	
	public int updateUserLocation(double longitude, double latitude) {
		// 获得SoapObject对象
		String longitudeString = longitude + "";
		String latitudeString = latitude + "";
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_UPDATE_USER_LOCATION);
        so.addProperty("uid", sinaUid);
        so.addProperty("longitude", longitudeString);
        so.addProperty("latitude", latitudeString);
        
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_UPDATE_USER_LOCATION, sse);
            // 判断有无回应，若有，调用方法解析返回的xml
            if (sse.getResponse() != null) {
                // 从返回数据串中提取Json数据
            	String feedback = catchJson(sse.bodyIn.toString());
            	if(feedback.equals("ok")) {
            		return SUCCESS;
            	} else {
            		return FAILED;
            	}
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return FAILED;
	}
	
	public void queryGame() {
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_GAME);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.ACTIVITY_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_GAME, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String gamesInfo = "{\"games\":" + catchJson(sse.bodyIn.toString()) + "}";
				Games games = (Games) GsonUtil.parseJsonToObject(gamesInfo, Games.class);
				// 存入Games实体类
				EntityTableUtil.setGames(games);
				
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	
	public int queryActivityOrderByRest(double longitude, double latitude, String hasid) {
		// 获得SoapObject对象
		String longitudeString = longitude + "";
		String latitudeString = latitude + "";
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_ACTIVITY_ORDER_BY_REST);
        so.addProperty("longitude", longitudeString);
        so.addProperty("latitude", latitudeString);
        so.addProperty("hasid", hasid);
        so.addProperty("uid", sinaUid);
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.ACTIVITY_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST, sse);
            // 判断有无回应，若有，调用方法解析返回的xml
            if (sse.getResponse() != null) {
				String result = catchJson(sse.bodyIn.toString());
				if (result.equals("[]")) {
				//	handler.sendEmptyMessage(HandlerConditions.NO_ACTIVITIES);
					return EMPTY;
				}
				String activitiesInfo = "{\"activities\":" + result + "}";
				Activities homeActivities = (Activities) GsonUtil.parseJsonToObject(activitiesInfo, Activities.class);
				// 存入Activities实体类
				EntityTableUtil.setHomePageActivities(homeActivities);
				return SUCCESS;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return FAILED;
	}
	
	public String uploadUserShowImages(byte[] picdata) {
		// 获得SoapObject对象
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_UPLOAD_PHOTOS);
        // 注意,此处添加参数要把ByteArray转为String格式(必须是Base64),否则会报Cannot Serialize错误
        String base64String = Base64.encode(picdata);
        so.addProperty("picdata", base64String);
        so.addProperty("uid", sinaUid);
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_UPLOAD_PHOTOS, sse);
            // 判断有无回应,若有,调用方法解析返回的xml
            if (sse.getResponse() != null) {
            	String photoUrl = catchPhotoUrls(sse.bodyIn.toString());
            	return photoUrl;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public int deleteUserShowImages(String showimages) {
		// 获得SoapObject对象
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_DELETE_PHOTOS);
        so.addProperty("uid", sinaUid);
        so.addProperty("showimages", showimages);
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_DELETE_PHOTOS, sse);
            // 判断有无回应，若有，调用方法解析返回的xml
            if (sse.getResponse() != null) {
            	String feedback = catchJson(sse.bodyIn.toString());
            	if(feedback.equals("ok")) {
            		return SUCCESS;
            	} else {
            		return FAILED;
            	}
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return FAILED;
	}
	
	public int queryActivitiesUpComing(String uid) {
		// 获得SoapObject对象
        SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_ACTIVITIES_UPCOMING);
        so.addProperty("uid", uid);
        // 获得Enveloper对象
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.bodyOut = so;
        sse.dotNet = false;
        sse.setOutputSoapObject(so);
        HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.ACTIVITY_SERIVCE_END_POINT);
        try {
            htse.call(WebServiceConstants.SOAP_ACTION_QUERY_ACTIVITIES_UPCOMING, sse);
            // 判断有无回应，若有，调用方法解析返回的xml
            if (sse.getResponse() != null) {
            	String result = catchJson(sse.bodyIn.toString());
            	if (result.equals("[]")) {
            		return EMPTY;
            	}
            	String activitiesInfo = "{\"activities\":" + result + "}";
            	Activities myActivities = (Activities) GsonUtil.parseJsonToObject(activitiesInfo, Activities.class);
            	// 存入Activities实体类
            	EntityTableUtil.setMyActivities(myActivities);
            	return SUCCESS;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return FAILED;
	}
	
	public int queryActivitiesOnGoing(String uid) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_ACTIVITIES_ONGOING);
		so.addProperty("uid", uid);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.ACTIVITY_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_ACTIVITIES_ONGOING, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String result = catchJson(sse.bodyIn.toString());
				if (result.equals("[]")) {
					return EMPTY;
				}
				String activitiesInfo = "{\"activities\":" + result + "}";
				Activities myActivities = (Activities) GsonUtil.parseJsonToObject(activitiesInfo, Activities.class);
				// 存入Activities实体类
				EntityTableUtil.setMyActivities(myActivities);
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int queryActivitiesFinished(String uid) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_ACTIVITIES_FINISHED);
		so.addProperty("uid", uid);
		// TODO 第二参数作为分页请求的起始位置，待处理
		so.addProperty("startTag", "0");
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.ACTIVITY_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_ACTIVITIES_FINISHED, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String result = catchJson(sse.bodyIn.toString());
				if (result.equals("[]"))
					return EMPTY;
				String activitiesInfo = "{\"activities\":" + result + "}";
				Activities myActivities = (Activities) GsonUtil.parseJsonToObject(activitiesInfo, Activities.class);
				// 存入Activities实体类
				EntityTableUtil.setMyActivities(myActivities);
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int queryParticipantUser(String uid, Activity activity) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_USER);
		so.addProperty("uid", uid);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_USER, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String userInfoInJson = catchJson(sse.bodyIn.toString());
				if (!userInfoInJson.equals("no user")) {
					EntityTableUtil.setParticipantUser((User) GsonUtil.parseJsonToObject(userInfoInJson, User.class));
					// TODO 同时添加FriendsList
					EntityTableUtil.addFriendsList((Friend) GsonUtil.parseJsonToObject(userInfoInJson, Friend.class));
					activity.addToParticipantsAvatar(EntityTableUtil.getParticipantUser().getUpicurl());
				}
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int queryFriendUser(String uid, Friend friend) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_USER);
		so.addProperty("uid", uid);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_USER, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String userInfoInJson = catchJson(sse.bodyIn.toString());
				if (!userInfoInJson.equals("no user")) {
					EntityTableUtil.setFriendUser((User) GsonUtil.parseJsonToObject(userInfoInJson, User.class));
					friend.setShowimages(EntityTableUtil.getFriendUser().getShowimages());
				}
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int queryMyFriends(String friendsList) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_MY_FRIENDS);
		so.addProperty("friendsList", friendsList);
		// TODO startTag暂时给0
		so.addProperty("startTag", 0);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_MY_FRIENDS, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
			String result = catchJson(sse.bodyIn.toString());
				if (result.equals("[]"))
					return EMPTY;
				String friendsInfo = "{\"friends\":" + result + "}";
				Friends myfriends = (Friends) GsonUtil.parseJsonToObject(friendsInfo, Friends.class);
				// 存入Friends实体类
				EntityTableUtil.setCheckedFriends(myfriends);
				EntityTableUtil.setCheckedFriendsList(EntityTableUtil.getCheckedFriends().getFriendsList());
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int querySurrounding(double longitude, double latitude) {
		String longitudeString = longitude + "";
		String latitudeString = latitude + "";
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_QUERY_SURROUNDING_PEOPLE);
		so.addProperty("uid", sinaUid);
		so.addProperty("longitude", longitudeString);
		so.addProperty("latitude", latitudeString);
		// TODO 分页处理,姑且先填0
		so.addProperty("startTag", 0);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_QUERY_SURROUNDING_PEOPLE, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
			String result = catchJson(sse.bodyIn.toString());
				if (result.equals("[]"))
					return EMPTY;
				String surroundingInfo = "{\"friends\":" + result + "}";
				Friends surroundings = (Friends) GsonUtil.parseJsonToObject(surroundingInfo, Friends.class);
				// 存入Friends实体类
				EntityTableUtil.setSurroundings(surroundings);
				EntityTableUtil.clearSurroundingsList();
				EntityTableUtil.setSurroundingsList(surroundings.getFriendsList());
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int addFriend(String friendIds) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_ADD_FRIEND);
		so.addProperty("uid", sinaUid);
		so.addProperty("friendid", friendIds);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_ADD_FRIEND, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
			String result = catchJson(sse.bodyIn.toString());
			if (result.equals("ok"))
				return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	public int deleteFriend(String friendId) {
		// 获得SoapObject对象
		SoapObject so = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_DELETE_FRIEND);
		so.addProperty("uid", sinaUid);
		so.addProperty("fuid", friendId);
		// 获得Enveloper对象
		SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.bodyOut = so;
		sse.dotNet = false;
		sse.setOutputSoapObject(so);
		HttpTransportSE htse = new HttpTransportSE(WebServiceConstants.USER_SERIVCE_END_POINT);
		try {
			htse.call(WebServiceConstants.SOAP_ACTION_DELETE_FRIEND, sse);
			// 判断有无回应，若有，调用方法解析返回的xml
			if (sse.getResponse() != null) {
				String result = catchJson(sse.bodyIn.toString());
				if (result.equals("ok"))
					return SUCCESS;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return FAILED;
	}
	
	private String catchJson(String infofromserver) {
		String jsonString = "";
		int startPosition = infofromserver.lastIndexOf("return=") + 7;
		int endPosition = infofromserver.indexOf("; }");
		jsonString = infofromserver.substring(startPosition, endPosition);
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
