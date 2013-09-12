package so.zudui.webservice;

public class WebServiceConstants {
	
	// 命名空间
    public static final String NAMESPACE = "http://service.coffe.com";
    
    // 请求服务的地址
    public static final String USER_SERIVCE_END_POINT = "http://121.199.37.35:8080/JJCoffeService/services/UserService?wsdl";
    public static final String ACTIVITY_SERIVCE_END_POINT = "http://121.199.37.35:8080/JJCoffeService/services/ActivityService?wsdl";
    
    // 请求服务的action
    // 登陆时
    public static final String SOAP_ACTION_USER_LOGIN = "http://service.coffe.com/userLogin";
    public static final String SOAP_ACTION_UPDATE_USER_LOCATION = "http://service.coffe.com/updateUserLocation";
    public static final String SOAP_ACTION_UPDATE_USER_DEVICE_TOKEN = "http://service.coffe.com/updateUserDeviceToken";
    // 查找首页活动
    public static final String SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST = "http://service.coffe.com/queryActivityOrderByKongwei";
    // 上传&删除相册
    public static final String SOAP_ACTION_UPLOAD_PHOTOS = "http://service.coffe.com/uploadUserShowimages";
    public static final String SOAP_ACTION_DELETE_PHOTOS = "http://service.coffe.com/deleteUserShowimages";
    // 查找本人参与的活动
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_UPCOMING = "http://service.coffe.com/queryActivityWillAction";
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_ONGOING = "http://service.coffe.com/queryActivityActionIng";
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_FINISHED = "http://service.coffe.com/queryActivityHasAction";
    // 查询游戏种类
    public static final String SOAP_ACTION_QUERY_GAME = "http://service.coffe.com/queryGame";
    // 查询其他用户
    public static final String SOAP_ACTION_QUERY_USER = "http://service.coffe.com/queryUser";
    // 查询好友
    public static final String SOAP_ACTION_QUERY_MY_FRIENDS = "http://service.coffe.com/queryUserFriend";
    // 查询附近的人
    public static final String SOAP_ACTION_QUERY_SURROUNDING_PEOPLE = "http://service.coffe.com/queryUserOrderByLocation";
    // 添加好友
    public static final String SOAP_ACTION_ADD_FRIEND = "http://service.coffe.com/addFriend";
    // 删除好友
    public static final String SOAP_ACTION_DELETE_FRIEND = "http://service.coffe.com/deleteUserFriend";
    
    
    // 调用的方法名
    // 登陆时
    public static final String METHOD_USER_LOGIN = "userLogin";
    public static final String METHOD_UPDATE_USER_LOCATION = "updateUserLocation";
    public static final String METHOD_UPDATE_USER_DEVICE_TOKEN = "updateUserDeviceToken";
    // 查找首页活动
    public static final String METHOD_QUERY_ACTIVITY_ORDER_BY_REST = "queryActivityOrderByKongwei";
    // 上传&删除相册
    public static final String METHOD_UPLOAD_PHOTOS = "uploadUserShowimages";
    public static final String METHOD_DELETE_PHOTOS = "deleteUserShowimages";
    // 查找本人参与的活动
    public static final String METHOD_QUERY_ACTIVITIES_UPCOMING = "queryActivityWillAction";
    public static final String METHOD_QUERY_ACTIVITIES_ONGOING = "queryActivityActionIng";
    public static final String METHOD_QUERY_ACTIVITIES_FINISHED = "queryActivityHasAction";
    // 查询游戏种类
    public static final String METHOD_QUERY_GAME = "queryGame";
    // 查询其他用户
    public static final String METHOD_QUERY_USER = "queryUser";
    // 查询好友
    public static final String METHOD_QUERY_MY_FRIENDS = "queryUserFriend";
    // 查询附近的人
    public static final String METHOD_QUERY_SURROUNDING_PEOPLE = "queryUserOrderByLocation";
    // 添加好友
    public static final String METHOD_ADD_FRIEND = "addFriend";
    // 删除好友
    public static final String METHOD_DELETE_FRIEND = "deleteUserFriend";
    
}
