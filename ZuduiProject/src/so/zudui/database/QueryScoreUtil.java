package so.zudui.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import so.zudui.entity.User;
import so.zudui.handler.condition.HandlerConditions;
import so.zudui.util.EntityTableUtil;

public class QueryScoreUtil {
	private User user = EntityTableUtil.getUser();
	private Context context;
	private Handler handler;
	
	public QueryScoreUtil(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	public void queryMyScore() {
		new Thread(new DatabaseOperationThread()).start();
	}
	
	private class DatabaseOperationThread implements Runnable {
			
		@Override
		public void run() {
			ContentResolver resolver = context.getContentResolver();
			Uri UserProviderURI = Users.CONTENT_URI;
			String[] projection = { Users.SCORE }; 
			String selection = Users.UID + "=?";
			String[] selectionArgs = {user.getUid()};
			Cursor cursor = resolver.query(Users.CONTENT_URI, projection, selection, selectionArgs, null);
			String score = "";
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				score = cursor.getString(0);
			}
			Message msg = handler.obtainMessage(HandlerConditions.GET_MY_SCORE, score);
			handler.sendMessage(msg);
		
		}
			
	}
}
