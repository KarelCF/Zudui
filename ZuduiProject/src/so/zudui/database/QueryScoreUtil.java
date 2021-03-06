package so.zudui.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import so.zudui.entity.User;
import so.zudui.util.EntityTableUtil;

public class QueryScoreUtil {
	private User user = EntityTableUtil.getMainUser();
	private Context context;
	private String score = "";
	
	public QueryScoreUtil(Context context) {
		this.context = context;
	}
	
	public void queryMyScore() {
		ContentResolver resolver = context.getContentResolver();
		String[] projection = { Users.SCORE }; 
		String selection = Users.UID + "=?";
		String[] selectionArgs = {user.getUid()};
		Cursor cursor = resolver.query(Users.CONTENT_URI, projection, selection, selectionArgs, null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			score = cursor.getString(0);
		}
	}
	
	public String getScore() {
		return score;
	}
	
}
