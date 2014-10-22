package com.readme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private final String TAG = "DBHelper";

	
	private final String UserTable = "create table "
			+ Constants.UserTable.TABLE_NAME + " (" + Constants.UserTable.ID
			+ " text primary key, " + Constants.UserTable.USER_NAME
			+ " text, " + Constants.UserTable.PASSWORD + " text, "
			+ Constants.UserTable.TOTAL_SIZE
			+ " integer, " + Constants.UserTable.USED_SIZE + " integer, "
			+ Constants.UserTable.REGISTER_TIME + " integer, "
			+ Constants.UserTable.LAST_LOGIN_TIME + " integer, "
			+ Constants.UserTable.LAST_MODIFY_TIME + " integer, "
			+ Constants.UserTable.DEFAULT_NOTEBOOK + " text)";
	private final String NotebookTable = "create table "
			+ Constants.NotebookTable.TABLE_NAME + " (" + Constants.NotebookTable.PATH
			+ " text primary key, " + Constants.NotebookTable.NAME
			+ " text, " + Constants.NotebookTable.NOTES + " integer, "
			+ Constants.NotebookTable.CREATE_TIME + " integer, "
			+ Constants.NotebookTable.MODIFY_TIME + " integer)";
	private final String NoteTable = "create table "
			+ Constants.NoteTable.TABLE_NAME + " ("
			+ Constants.NoteTable.PATH + " text primary key, "
			+ Constants.NoteTable.LOCAL_PATH + " text, "
			+ Constants.NoteTable.ID + " text, "
			+ Constants.NoteTable.TITLE + " text, "
			+ Constants.NoteTable.AUTHOR + " text, "
			+ Constants.NoteTable.SOURCE + " text, "
			+ Constants.NoteTable.SIZE + " text, "
			+ Constants.NoteTable.CREATE_TIME + " integer, "
			+ Constants.NoteTable.MODIFY_TIME + " integer, "
			+ Constants.NoteTable.CONTENT + " text)";
	private final String ResourceTable = "create table "
			+ Constants.ResourceTable.TABLE_NAME + " ("
			+ Constants.ResourceTable.URL + " text primary key, "
			+ Constants.ResourceTable.LOCAL_URL + " text, "
			+ Constants.ResourceTable.SRC + " text)";
	

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, Constants.DATABASE_NAME, null, Constants.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			
			db.execSQL(UserTable);
			db.execSQL(NotebookTable);
			db.execSQL(NoteTable);
			db.execSQL(ResourceTable);
			
			System.out.println("created successfully");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "Upgrading from version " + oldVersion + " to" + newVersion
				+ ",which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.NotebookTable.TABLE_NAME);
		
		db.execSQL("drop table if exists " + Constants.UserTable.TABLE_NAME);
		
		db.execSQL("drop table if exists " + Constants.NoteTable.TABLE_NAME);
		
		db.execSQL("drop table if exists " + Constants.ResourceTable.TABLE_NAME);
		onCreate(db);
		System.out.println("update successfully");
	}

}
