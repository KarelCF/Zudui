package so.zudui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "zudui.db";
    private static final int DATABASE_VERSION = 1;
	
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " 
				+ Users.TABLE_NAME + " ("
	            + Users.ID + " INTEGER ,"
	            + Users.UID + " STRING PRIMARY KEY,"
	            + Users.UNAME + " STRING,"
	            + Users.UPICRL + " STRING,"
	            + Users.SEX + " INTEGER,"
	            + Users.CITY + " STRING,"
	            + Users.SCORE + " INTEGER,"
	            + Users.DEVTOKEN + " STRING,"
	            + Users.LONGITUDE + " FLOAT,"
	            + Users.LATITUDE + " FLOAT,"
	            + Users.FRIEND_IDS + " STRING,"
	            + Users.BGIMAGE + " STRING,"
	            + Users.SHOWIMAGES + " STRING"
	            + ");");
		
		db.execSQL("CREATE TABLE " 
				+ Activities.TABLE_NAME + " ("
				+ Activities.ID + " INTEGER PRIMARY KEY,"
				+ Activities.NAME + " STRING,"
				+ Activities.SHOPNAME + " STRING,"
				+ Activities.ADDRESS + " STRING,"
				+ Activities.STARTTIME + " INTEGER,"
				+ Activities.SHOPTEL + " STRING,"
				+ Activities.PRE_NUMBER + " INTEGER,"
				+ Activities.ACTIVITY_TYPE + " INTEGER,"
				+ Activities.INTRODUCE + " STRING,"
				+ Activities.LONGITUDE + " FLOAT,"
				+ Activities.LATITUDE + " FLOAT,"
				+ Activities.CREATE_USER_UID + " STRING,"
				+ Activities.JOIN_USER_ID + " STRING"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Activities.TABLE_NAME);
		onCreate(db);
	}

}
