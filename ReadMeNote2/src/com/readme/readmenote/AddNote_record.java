package com.readme.readmenote;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.readmenote.R;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AddNote_record extends Activity {
	private static String mFileName = null;
	Button stop_rocord_button, start_record_button;
	private static final String TAG = "AddNote_record_Activity";
	private static final String LOG_TAG = null;
	boolean U = false;// 当有录音时，为true
	MediaRecorder mRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_note_record_);
		stop_rocord_button = (Button) findViewById(R.id.stop_rocord_button);
		start_record_button = (Button) findViewById(R.id.start_record_button);
		// 为开始录音按钮绑定单击事件监听器
		start_record_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_record_button.setText("录音中...");
				U = true;

				start_record_button.setEnabled(false);
				startRecording();
			}
		});

		// 为保存录音按钮绑定单击事件监听器
		stop_rocord_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(U){
					// TODO Auto-generated method stub
					start_record_button.setText("录音");
					start_record_button.setEnabled(true);
					stopRecording();
					Bundle b = new Bundle();
					b.putString("file", mFileName);
					b.putBoolean("record_or_not", U);
					Intent intent = getIntent();
					intent.putExtras(b);
					AddNote_record.this.setResult(3, intent);
					AddNote_record.this.finish();}
					else{
						Toast record_toast = Toast.makeText(
								getApplicationContext(), "没有录音哦~亲~", 7000);
						record_toast.show();
					}

			}
		});

	}

	private void startRecording() {

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		Toast record_toast = Toast.makeText(getApplicationContext(),
				"文件以ReadMe开头以时间命名，保存在sd卡根目录下", 7000);
		record_toast.show();
	}

	public AddNote_record() throws IOException {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/" + "ReadMe"+"/" + "Record"+"/" + "ReadMe"
				+ new DateFormat().format("yyyyMMdd_hhmmss",// 以ReadMe+时间命名保存在根目录
						Calendar.getInstance(Locale.CHINA)) + ".amr";
		File _file = new File(mFileName);
		// 如果文件夹不存在则创建一个新的文件
		if (!_file.exists()) {
			_file.getParentFile().mkdirs();
			_file.createNewFile();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(U){
			stopRecording();}
			Bundle b = new Bundle();
			b.putString("file", mFileName);
			b.putBoolean("record_or_not", U);
			Intent intent = getIntent();
			intent.putExtras(b);
			AddNote_record.this.setResult(3, intent);
			AddNote_record.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
	}

}
