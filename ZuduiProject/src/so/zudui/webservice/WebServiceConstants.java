package so.zudui.webservice;

public class WebServiceConstants {
	
	// �����ռ�
    public static final String NAMESPACE = "http://service.coffe.com";
    // �������ĵ�ַ
    public static final String END_POINT = "http://172.16.1.190:8080/JJCoffeService/services/UserService?wsdl";
    
    // ��������action
    // ��½ʱ
    public static final String SOAP_ACTION_USER_LOGIN  = "http://service.coffe.com/userLogin";
    public static final String SOAP_ACTION_UPDATE_USER_LOCATION  = "http://service.coffe.com/updateUserLocation";
    public static final String SOAP_ACTION_UPDATE_USER_DEVICE_TOKEN  = "http://service.coffe.com/updateUserDeviceToken";
    // ���һ
    public static final String SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST  = "http://service.coffe.com/queryActivityOrderByKongwei";
    
    
    // ���õķ�����
    // ��½ʱ
    public static final String METHOD_USER_LOGIN = "userLogin";
    public static final String METHOD_UPDATE_USER_LOCATION = "updateUserLocation";
    public static final String METHOD_UPDATE_USER_DEVICE_TOKEN = "updateUserDeviceToken";
    // ���һ
    public static final String METHOD_QUERY_ACTIVITY_ORDER_BY_REST  = "queryActivityOrderByKongwei";
}
