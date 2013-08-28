package so.zudui.database;

import android.net.Uri;
import android.provider.BaseColumns;

public interface Activities extends BaseColumns {
	
	 // ע��,�˴�AUTHORITYһ��Ҫ��Manifest.xml�е�������ȫ��ͬ
     public static final String AUTHORITY = "so.zudui.activityprovider";
     //TODO
     // ����
     public static final String TABLE_NAME = "ActivityTbl";
     // ���ʱ��������URI
     public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
     // �ֶ���
     public static final String ID = "id";
     public static final String NAME = "name";
     public static final String SHOPNAME = "shopname";
     public static final String ADDRESS = "address";
     public static final String STARTTIME = "starttime";
     public static final String SHOPTEL = "shoptel";
     public static final String LONGITUDE = "longitude";
     public static final String LATITUDE = "latitude";
     public static final String PRE_NUMBER = "pre_number";
     public static final String ACTIVITY_TYPE = "activity_type";
     public static final String INTRODUCE = "introduce";
     public static final String CREATE_USER_UID = "create_user_uid";
     public static final String JOIN_USER_ID = "join_user_id";
     
     // �������
     public static final String SORT_ORDER = "id ASC";
}
