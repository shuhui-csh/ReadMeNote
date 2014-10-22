package com.readme.utils;

import java.io.File;


import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeException;
import com.readme.data.Note;
import com.readme.data.Resource;
import com.readme.database.NoteDBManger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.EditText;
/**异步上传附件*/
public class UpLoadFileTask extends AsyncTask<File, Void, Resource> {
	private  ReadMeClient client;
	/** 发送消息至其他线程的handler */
	private Handler uiHandler;
	Note note = new Note();
	Resource resource = null;
	NoteDBManger noteDBManger;
	public UpLoadFileTask(ReadMeClient client,Handler handler,Context context) {
		this.client = client;
		this.uiHandler = handler;
		try {
			noteDBManger = new NoteDBManger(context);
			noteDBManger.open();
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
	@Override
	protected Resource doInBackground(File... params) {
		
		Resource res = noteDBManger.findResource(params[0].getAbsolutePath(),null);
		if(res == null){
			try {
				res = uploadResource(params[0]);
				res.setLocal_url(params[0].getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		return res;
		
		
	}

	@Override
	protected void onPostExecute(Resource result) {
		
		noteDBManger.addResource(result);	
		noteDBManger.close();
		Message message = new Message();
		/** 完成后发送消息 */
		message.what = Messages.TASK_SUCCESSS;
		message.obj = result;
		uiHandler.sendMessage(message);
		super.onPostExecute(result);
		
	}
	/** 上传文件 */
	private  Resource uploadResource(final File resource)
			throws Exception {
		try {
			return client.uploadResource(resource);
		} catch (ReadMeException e) {
			if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
				// Toast.makeText(MyNoteActivity.this,
				// "-----我错了",Toast.LENGTH_SHORT).show();
				return null;
			} else {
				throw e;
			}
		}
	}
	
	
}
