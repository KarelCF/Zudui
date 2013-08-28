package so.zudui.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import so.zudui.entity.User;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.util.EntityTableUtil;

public class UpdateMyLocationUtil {
	private User user = EntityTableUtil.getUser();
	private Context context;
	private Handler handler;
	
	public UpdateMyLocationUtil(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	public void updateMyLocation() {
		new Thread(new DatabaseOperationThread()).start();
	}
	
	private class DatabaseOperationThread implements Runnable {
			
		@Override
		public void run() {
			ContentResolver resolver = context.getContentResolver();
			Uri UserProviderURI = Users.CONTENT_URI;
			String[] projection = { Users.LATITUDE, Users.LONGITUDE }; 
			String selection = Users.UID + "=?";
			String[] selectionArgs = {user.getUid()};
			Cursor cursor = resolver.query(Users.CONTENT_URI, projection, selection, selectionArgs, null);
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put(Users.LONGITUDE, user.getLongitude());
				values.put(Users.LATITUDE, user.getLatitude());
				resolver.update(UserProviderURI, values, selection, selectionArgs);
			}
			handler.sendEmptyMessage(HandlerConditions.UPDATE_MY_LOCATION_SQLITE_SUCCESS);
		
		}
			
	}
}
