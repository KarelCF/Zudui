package so.zudui.webservice;

public class WebServiceConstants {
	
	// �����ռ�
    public static final String NAMESPACE = "http://service.coffe.com";
    
    // �������ĵ�ַ
    public static final String USER_SERIVCE_END_POINT = "http://121.199.37.35:8080/JJCoffeService/services/UserService?wsdl";
    public static final String ACTIVITY_SERIVCE_END_POINT = "http://121.199.37.35:8080/JJCoffeService/services/ActivityService?wsdl";
    
    // ��������action
    // ��½ʱ
    public static final String SOAP_ACTION_USER_LOGIN = "http://service.coffe.com/userLogin";
    public static final String SOAP_ACTION_UPDATE_USER_LOCATION = "http://service.coffe.com/updateUserLocation";
    public static final String SOAP_ACTION_UPDATE_USER_DEVICE_TOKEN = "http://service.coffe.com/updateUserDeviceToken";
    // ������ҳ�
    public static final String SOAP_ACTION_QUERY_ACTIVITY_ORDER_BY_REST = "http://service.coffe.com/queryActivityOrderByKongwei";
    // �ϴ�&ɾ�����
    public static final String SOAP_ACTION_UPLOAD_PHOTOS = "http://service.coffe.com/uploadUserShowimages";
    public static final String SOAP_ACTION_DELETE_PHOTOS = "http://service.coffe.com/deleteUserShowimages";
    // ���ұ��˲���Ļ
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_UPCOMING = "http://service.coffe.com/queryActivityWillAction";
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_ONGOING = "http://service.coffe.com/queryActivityActionIng";
    public static final String SOAP_ACTION_QUERY_ACTIVITIES_FINISHED = "http://service.coffe.com/queryActivityHasAction";
    // ��ѯ��Ϸ����
    public static final String SOAP_ACTION_QUERY_GAME = "http://service.coffe.com/queryGame";
    // ��ѯ�����û�
    public static final String SOAP_ACTION_QUERY_USER = "http://service.coffe.com/queryUser";
    // ��ѯ����
    public static final String SOAP_ACTION_QUERY_MY_FRIENDS = "http://service.coffe.com/queryUserFriend";
    // ��ѯ��������
    public static final String SOAP_ACTION_QUERY_SURROUNDING_PEOPLE = "http://service.coffe.com/queryUserOrderByLocation";
    // ��Ӻ���
    public static final String SOAP_ACTION_ADD_FRIEND = "http://service.coffe.com/addFriend";
    // ɾ������
    public static final String SOAP_ACTION_DELETE_FRIEND = "http://service.coffe.com/deleteUserFriend";
    
    
    // ���õķ�����
    // ��½ʱ
    public static final String METHOD_USER_LOGIN = "userLogin";
    public static final String METHOD_UPDATE_USER_LOCATION = "updateUserLocation";
    public static final String METHOD_UPDATE_USER_DEVICE_TOKEN = "updateUserDeviceToken";
    // ������ҳ�
    public static final String METHOD_QUERY_ACTIVITY_ORDER_BY_REST = "queryActivityOrderByKongwei";
    // �ϴ�&ɾ�����
    public static final String METHOD_UPLOAD_PHOTOS = "uploadUserShowimages";
    public static final String METHOD_DELETE_PHOTOS = "deleteUserShowimages";
    // ���ұ��˲���Ļ
    public static final String METHOD_QUERY_ACTIVITIES_UPCOMING = "queryActivityWillAction";
    public static final String METHOD_QUERY_ACTIVITIES_ONGOING = "queryActivityActionIng";
    public static final String METHOD_QUERY_ACTIVITIES_FINISHED = "queryActivityHasAction";
    // ��ѯ��Ϸ����
    public static final String METHOD_QUERY_GAME = "queryGame";
    // ��ѯ�����û�
    public static final String METHOD_QUERY_USER = "queryUser";
    // ��ѯ����
    public static final String METHOD_QUERY_MY_FRIENDS = "queryUserFriend";
    // ��ѯ��������
    public static final String METHOD_QUERY_SURROUNDING_PEOPLE = "queryUserOrderByLocation";
    // ��Ӻ���
    public static final String METHOD_ADD_FRIEND = "addFriend";
    // ɾ������
    public static final String METHOD_DELETE_FRIEND = "deleteUserFriend";
    
}
