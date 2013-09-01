package so.zudui.handler.condition;


public interface HandlerConditions {
	
	// LaunchActivity使用
	public static final int GET_ACCESS_TOKEN = 0;
	public static final int PROFILE_INFO = 1;
	public static final int GOTO_HOME_ACTIVITY = 2;
	public static final int SERVER_NO_RESPONSE = 3;
	
	// HomeActivity使用
	public static final int DATABASE_OPERATION_START = 4;
	public static final int DATABASE_OPERATION_FINISH = 5;
	public static final int GET_MY_LOCATION = 6;
	public static final int UPDATE_MY_LOCATION_SERVER_SUCCESS = 7;
	public static final int UPDATE_MY_LOCATION_SQLITE_SUCCESS = 8;
	public static final int UPDATE_MY_LOCATION_SERVER_FAILED = 9;
	public static final int UPDATE_MY_LOCATION_SQLITE_FAILED = 10;
	
	// MySpaceActivity使用
	public static final int GET_MY_SCORE = 11;
	public static final int UPLOAD_MY_PHOTOS = 12;
	public static final int DELETE_MY_PHOTOS_BEGIN = 13;
	public static final int DELETE_MY_PHOTOS_FINISH = 14;
	
	
}
