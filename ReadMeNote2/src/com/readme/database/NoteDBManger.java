package com.readme.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.readme.data.Note;
import com.readme.data.Resource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @description 对频道表的数据库管理类
 * 
 * */
public class NoteDBManger {
	private final String TAG = "noteDbManger";
	private SQLiteDatabase db;
	private final Context context;
	private final DBHelper dbHelper;

	public NoteDBManger(Context c) {
		context = c;
		dbHelper = new DBHelper(c, Constants.DATABASE_NAME, null,
				Constants.VERSION);
	}

	/**
	 * 关闭数据库
	 * 
	 * */

	public void close() {
		db.close();
	}

	/**
	 * 开启数据库
	 * */
	public void open() throws SQLiteException {

		try {
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage());
			db = dbHelper.getReadableDatabase();
		}

	}

	/**
	 * 增加表中数据
	 * 
	 * @param 保存笔记到note表
	 * 
	 */
	public void addnote(Note note) {

		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(Constants.NoteTable.PATH, note.getPath());
			contentValues.put(Constants.NoteTable.TITLE, note.getTitle());
			contentValues.put(Constants.NoteTable.AUTHOR, note.getAuthor());
			contentValues.put(Constants.NoteTable.SOURCE, note.getSource());
			contentValues.put(Constants.NoteTable.SIZE, note.getSize());
			contentValues.put(Constants.NoteTable.CREATE_TIME,
					note.getCreateTime());
			contentValues.put(Constants.NoteTable.MODIFY_TIME,
					note.getModifyTime());
			contentValues.put(Constants.NoteTable.CONTENT, note.getContent());
			contentValues.put(Constants.NoteTable.LOCAL_PATH,
					note.getLocal_path());

			db.insert(Constants.NoteTable.TABLE_NAME, null, contentValues);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());

		}

	}

	/**
	 * 增加表中数据
	 * 
	 * @param 保存笔记到note表
	 * 
	 */
	public void addResource(Resource res) {

		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(Constants.ResourceTable.SRC, res.getIcon());
			contentValues.put(Constants.ResourceTable.URL, res.getUrl());

			contentValues.put(Constants.ResourceTable.LOCAL_URL,
					res.getLocal_url());

			db.insert(Constants.ResourceTable.TABLE_NAME, null, contentValues);

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());

		}

	}

	/**
	 * 删除表中的记录
	 * 
	 * @param whereClausein
	 *            删除条件 如：( id>? and time>?)
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * @return 返回删除的条数 也可以作为判断值，如果是正数则表示删除成功，反之不成功
	 */
	public int deletenote(String path) {
		return db
				.delete(Constants.NoteTable.TABLE_NAME, "path=?",new  String[]{path});
	}

	/**
	 * 查找表中记录
	 * 
	 * @return List<Note>
	 */
	public List<Note> getdiaries(String user_name) {
		/**
		 * 查询数据
		 * 
		 * @param table
		 *            表名
		 * @param columns
		 *            要查询的列名
		 * @param selection
		 *            查询条件 如：( id=?)
		 * @param selectionArgs
		 *            条件里的参数，用来替换"?"
		 * @param orderBy
		 *            排序 如：id desc
		 * @return 返回Cursor String[] columns={"kind","textnum","region"};
		 *         //你要的数据 String 条件字段="NUMWEEK=? and YEAR=?", String[]
		 *         selectionArgs={”星期一"，"2013"}；
		 */
		List<Note> list = new ArrayList<Note>();

		Cursor cn = db.query(Constants.NoteTable.TABLE_NAME, null, "author=?",
				new String[] { user_name }, null, null, null);
		if (cn.moveToFirst()) {
			do {
				Note note = new Note();

				note.setId(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.ID)));
				note.setAuthor(user_name);

				note.setPath(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.PATH)));
				note.setTitle(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.TITLE)));
				note.setSource(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.SOURCE)));

				note.setSize(cn.getLong(cn
						.getColumnIndex(Constants.NoteTable.SIZE)));
				note.setCreateTime(cn.getLong(cn
						.getColumnIndex(Constants.NoteTable.CREATE_TIME)));
				note.setModifyTime(cn.getLong(cn
						.getColumnIndex(Constants.NoteTable.MODIFY_TIME)));
				note.setContent(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.CONTENT)));
				note.setLocal_path(cn.getString(cn
						.getColumnIndex(Constants.NoteTable.LOCAL_PATH)));
				list.add(note);
			} while (cn.moveToNext());

		}
		Log.i(TAG, String.valueOf(list.size()));
		System.out.println(list.size());
		return list;

	}

	/**
	 * 更改表中的记录
	 * 
	 * @param channel
	 *            Channel类
	 * 
	 * @param whereClause
	 *            修改条件 如：( id=?)
	 * 
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * 
	 * @return 返回修改的条数 也可以作为判断值，如果是正数则表示更改成功，反之不成功
	 */
	public void updatenote(List<Note> notes) {
		for(Note note : notes)
		try {			
			addnote(note);
//			ContentValues contentValues = new ContentValues();
//			contentValues.put(Constants.NoteTable.PATH, note.getPath());
//			contentValues.put(Constants.NoteTable.TITLE, note.getTitle());
//			contentValues.put(Constants.NoteTable.AUTHOR, note.getAuthor());
//			contentValues.put(Constants.NoteTable.SOURCE, note.getSource());
//			contentValues.put(Constants.NoteTable.SIZE, note.getSize());
//			contentValues.put(Constants.NoteTable.CREATE_TIME,
//					note.getCreateTime());
//			contentValues.put(Constants.NoteTable.MODIFY_TIME,
//					note.getModifyTime());
//			contentValues.put(Constants.NoteTable.CONTENT, note.getContent());
//			contentValues.put(Constants.NoteTable.LOCAL_PATH,
//					note.getLocal_path());
//			db.update(Constants.NoteTable.TABLE_NAME, contentValues, "path=?", new String[]{note.getPath()});	
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			
		}

	}
	
	public void updatenote(Note note) {
		try {			
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.NoteTable.PATH, note.getPath());
			contentValues.put(Constants.NoteTable.TITLE, note.getTitle());
			contentValues.put(Constants.NoteTable.AUTHOR, note.getAuthor());
			contentValues.put(Constants.NoteTable.SOURCE, note.getSource());
			contentValues.put(Constants.NoteTable.SIZE, note.getSize());
			contentValues.put(Constants.NoteTable.CREATE_TIME,
					note.getCreateTime());
			contentValues.put(Constants.NoteTable.MODIFY_TIME,
					note.getModifyTime());
			contentValues.put(Constants.NoteTable.CONTENT, note.getContent());
			contentValues.put(Constants.NoteTable.LOCAL_PATH,
					note.getLocal_path());
			db.update(Constants.NoteTable.TABLE_NAME, contentValues, "path=?", new String[]{note.getPath()});	
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			
		}

	}

	public boolean check(String user_name) {
		Cursor cursor = db.query(Constants.UserTable.TABLE_NAME, null, "user=?", new String[]{user_name},
				 null, null, null);

		if (!cursor.moveToFirst()) {

			return true;
		} else
			return false;
	}

	public boolean check(String user_name, String password) {
		Cursor cursor = db.query(Constants.UserTable.TABLE_NAME, null, "user=? and password=?", new String[]{user_name,password},
				 null, null, null);
		if (!cursor.moveToFirst()) {
			return true;
		} else
			return false;
	}

	public void addUser(String user_name, String register_time, String password) {
		// Youdao youdao = getYoudao();
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.UserTable.USER_NAME, user_name);

			contentValues.put(Constants.UserTable.PASSWORD, password);
			contentValues.put(Constants.UserTable.REGISTER_TIME, register_time);

			db.insert(Constants.UserTable.TABLE_NAME, null, contentValues);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public Resource findResource(String filePath, String icon) {
		Resource res = null;
		Cursor cursor;
		/*
		 * if(icon == null){ cursor =
		 * db.query(Constants.ResourceTable.TABLE_NAME, null, "local_url=?", new
		 * String[]{filePath}, null, null, null); } else{ cursor =
		 * db.query(Constants.ResourceTable.TABLE_NAME, null, "src=?", new
		 * String[]{icon}, null, null, null); }
		 */
		if (icon == null) {
			cursor = db.query(Constants.ResourceTable.TABLE_NAME, null,
					"local_url=?", new String[] { filePath }, null, null, null);
		} else {
			cursor = db.query(Constants.ResourceTable.TABLE_NAME, null,
					"src=?", new String[] { icon }, null, null, null);
			if (cursor.moveToFirst()) {
			} else {
				cursor = db.query(Constants.ResourceTable.TABLE_NAME, null,
						"url=?", new String[] { icon }, null, null, null);
			}
		}
		if (cursor.moveToFirst()) {
			do {
				res = new Resource(cursor.getString(cursor
						.getColumnIndex(Constants.ResourceTable.URL)));
				res.setIcon((cursor.getString(cursor
						.getColumnIndex(Constants.ResourceTable.SRC))));
				res.setLocal_url((cursor.getString(cursor
						.getColumnIndex(Constants.ResourceTable.LOCAL_URL))));

			} while (cursor.moveToNext());
		}
		return res;
	}
}
