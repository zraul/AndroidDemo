package com.example.database;

import android.R.integer;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zhengraul
 * 数据库操作类
 */
public class DBHelper extends SQLiteOpenHelper{

	private static final String DB_NAME = "download.db";
	private static final int VERSION = 1;
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement,"
			+ " thread_id integer, url text, start integer, end integer, finished integer)";
	private static final String SQL_DROP = "DROP TABLE IF EXISTS thread_info";
	private static DBHelper sHelper = null;
	
	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	private DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	/**
	 * 获得数据库类的对象
	 */
	public static DBHelper getInstance(Context context){
		if (sHelper == null) {
			sHelper = new DBHelper(context);
		}
		
		return sHelper;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}

}
