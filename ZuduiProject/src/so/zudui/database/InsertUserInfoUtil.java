package so.zudui.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import so.zudui.entity.User;
import so.zudui.util.EntityTableUtil;

public class InsertUserInfoUtil {
	private User user = EntityTableUtil.getUser();
	private Context context;
	
	public InsertUserInfoUtil(Context context) {
		this.context = context;
	}
	
	public void insertUserInfo() {
		new Thread(new DatabaseOperationThread()).start();
	}
	
	private class DatabaseOperationThread implements Runnable {
		
		ContentResolver resolver = context.getContentResolver();
		
		@Override
		public void run() {
			if(isNewUser()) {
				ContentValues values = new ContentValues();
				values.put(Users.ID, user.getId());
				values.put(Users.UID, user.getUid());
				values.put(Users.UNAME, user.getUname());
				values.put(Users.UPICRL, user.getUpicurl());
				values.put(Users.SEX, user.getSex());
				values.put(Users.CITY, user.getCity());
				values.put(Users.SCORE, user.getScore());
				values.put(Users.DEVTOKEN, user.getDevtoken());
				values.put(Users.LONGITUDE, user.getLongitude());
				values.put(Users.LATITUDE, user.getLatitude());
				values.put(Users.FRIEND_IDS, user.getFriendIds());
				values.put(Users.BGIMAGE, user.getBgimage());
				values.put(Users.SHOWIMAGES, user.getShowimages());
				resolver.insert(Users.CONTENT_URI, values);
			}
		}
		
		private boolean isNewUser() {
			String[] projection = { Users.UID }; 
			String selection = Users.UID + "=?";
			String[] selectionArgs = {user.getUid()};
			Cursor cursor = resolver.query(Users.CONTENT_URI, projection, selection, selectionArgs, null);
			if(cursor.getCount() == 0)
				return true;
			return false;
		}
		
	}
	
	
}
