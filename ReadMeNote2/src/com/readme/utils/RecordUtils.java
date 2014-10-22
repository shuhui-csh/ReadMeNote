package com.readme.utils;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.readmenote.R;
import com.readme.readmenote.AddNoteActivity;

public class RecordUtils {
	/**录音播放类*/
	private static MediaPlayer recordPlayer = null;
	
	/**
	 * 为按钮添加单击时间，选着 播放录音 还是 删除录音
	 * */
	public  static void playRecord(final Context context,final String filename) {
				ImageButton record_play, record_delete;// 播放录音，删除录音按钮
				final Dialog record_choose = new Dialog(context,
						R.style.draw_dialog);
				record_choose.setContentView(R.layout.addnote_record);
				// 设置背景模糊参数
				WindowManager.LayoutParams winlp = record_choose.getWindow()
						.getAttributes();
				winlp.alpha = 0.9f; // 0.0-1.0
				record_choose.getWindow().setAttributes(winlp);
				record_choose.show();// 显示弹出框
				record_play = (ImageButton) record_choose
						.findViewById(R.id.record_play);
				record_delete = (ImageButton) record_choose
						.findViewById(R.id.record_delete);
				record_play.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 播放录音
						Toast record_toast = Toast.makeText(
								context, "播放录音", 7000);
						record_toast.show();
						// TODO Auto-generated method stub
						recordPlayer = new MediaPlayer();
						try {
							recordPlayer.setDataSource(filename);
							recordPlayer.prepare();
							recordPlayer.start();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						recordPlayer.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								recordPlayer.release();
							}
						});
						record_choose.cancel();
					}
				});
				record_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast record_toast = Toast.makeText(
								context, "您单击了删除按钮", 7000);
						record_toast.show();
						record_choose.cancel();
					}
				});
			}
}
