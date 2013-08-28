package so.zudui.database;

import android.net.Uri;
import android.provider.BaseColumns;

public interface Activities extends BaseColumns {
	
	 // 注意,此处AUTHORITY一定要和Manifest.xml中的配置完全相同
     public static final String AUTHORITY = "so.zudui.activityprovider";
     //TODO
     // 表名
     public static final String TABLE_NAME = "ActivityTbl";
     // 访问本表所需的URI
     public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
     // 字段名
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
     
     // 排序操作
     public static final String SORT_ORDER = "id ASC";
}
