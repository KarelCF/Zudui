package so.zudui.webservice;

public class WebServiceConstants {
	
	// 命名空间
    public static final String NAMESPACE = "http://service.coffe.com";
    // 请求服务的地址
    public static final String END_POINT = "http://172.16.1.190:8080/JJCoffeService/services/UserService?wsdl";
    
    // 请求服务的action
    // 登陆时
    public static final String SOAP_ACTION_USER_LOGIN  = "http://service.coffe.com/userLogin";
    public static final String SOAP_ACTION_UPDATE_USER_LOCATION  = "http://service.coffe.com/updateUserLocation";
    public static final String SOAP_ACTION_UPDATE_USER_DEVICE_TOKEN  = "http://service.coffe.com/updateUserDeviceToken";
    // 查找活动
    public static final String SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST  = "http://service.coffe.com/queryActivityOrderByKongwei";
    
    
    // 调用的方法名
    // 登陆时
    public static final String METHOD_USER_LOGIN = "userLogin";
    public static final String METHOD_UPDATE_USER_LOCATION = "updateUserLocation";
    public static final String METHOD_UPDATE_USER_DEVICE_TOKEN = "updateUserDeviceToken";
    // 查找活动
    public static final String METHOD_QUERY_ACTIVITY_ORDER_BY_REST  = "queryActivityOrderByKongwei";
}
