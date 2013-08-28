package so.zudui.database;

import android.net.Uri;
import android.provider.BaseColumns;

public interface Users extends BaseColumns {
	
	 // ע��,�˴�AUTHORITYһ��Ҫ��Manifest.xml�е�������ȫ��ͬ
     public static final String AUTHORITY = "so.zudui.userprovider";
     //TODO
     // ����
     public static final String TABLE_NAME = "UserTbl";
     // ���ʱ��������URI
     public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
     // �ֶ���
     public static final String ID = "id";
     public static final String UID = "uid";
     public static final String UNAME = "uname";
     public static final String UPICRL = "upicurl";
     public static final String SEX = "sex";
     public static final String CITY = "city";
     public static final String SCORE = "score";
     public static final String DEVTOKEN = "devtoken";
     public static final String LONGITUDE = "longitude";
     public static final String LATITUDE = "latitude";
     public static final String FRIEND_IDS = "friend_ids";
     public static final String BGIMAGE = "bgimage";
     public static final String SHOWIMAGES = "showimages";
     
}
