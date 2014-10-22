package com.readme.readmenote;

import com.example.readmenote.R;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddNote_painting extends Activity {
	GestureOverlayView gestureView; // 相关内容在 疯狂android讲义 435页
	static Bitmap bitmap;
	ImageButton red, blue, green;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gesture_dialog);
		// 获取手势编辑器视图
		gestureView = (GestureOverlayView) findViewById(R.id.gesture);
		// 设置手势宽度
		gestureView.setGestureStrokeWidth(4);
		// 设置手势颜色
		gestureView.setGestureColor(Color.RED);

		// 实例化imagebutton
		red = (ImageButton) findViewById(R.id.gesture_red);
		blue = (ImageButton) findViewById(R.id.gesture_blue);
		
		green = (ImageButton) findViewById(R.id.gesture_green);
		red.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gestureView.setGestureColor(Color.RED);
				
				// 手势完成单击事件绑定事件监听器
				gestureView
						.addOnGesturePerformedListener(new OnGesturePerformedListener() {
							@Override
							public void onGesturePerformed(GestureOverlayView overlay,
									final Gesture gesture) { // 根据gesture包含的手势创建位图
								bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
								// 跳转回添加笔记页面
								Intent intent1 = new Intent(AddNote_painting.this,
										NewNoteActivity.class);
								intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AddNote_painting.this.setResult(2, intent1);
								AddNote_painting.this.finish();
							}
						});
			}
		});
		blue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gestureView.setGestureColor(Color.BLUE);
				// 手势完成单击事件绑定事件监听器
				gestureView
						.addOnGesturePerformedListener(new OnGesturePerformedListener() {
							@Override
							public void onGesturePerformed(GestureOverlayView overlay,
									final Gesture gesture) { // 根据gesture包含的手势创建位图
								bitmap = gesture.toBitmap(128, 128, 10, 0xff0000ff);
								// 跳转回添加笔记页面
								Intent intent1 = new Intent(AddNote_painting.this,
										NewNoteActivity.class);
								intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AddNote_painting.this.setResult(2, intent1);
								AddNote_painting.this.finish();
							}
						});
			}
		});
		
		green.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gestureView.setGestureColor(Color.GREEN);
				
				// 手势完成单击事件绑定事件监听器
				gestureView
						.addOnGesturePerformedListener(new OnGesturePerformedListener() {
							@Override
							public void onGesturePerformed(GestureOverlayView overlay,
									final Gesture gesture) { // 根据gesture包含的手势创建位图
								bitmap = gesture.toBitmap(128, 128, 10, 0xff00ff00);
								// 跳转回添加笔记页面
								Intent intent1 = new Intent(AddNote_painting.this,
										NewNoteActivity.class);
								intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AddNote_painting.this.setResult(2, intent1);
								AddNote_painting.this.finish();
							}
						});
				
			}
			
		});

		// 手势完成单击事件绑定事件监听器
		gestureView
				.addOnGesturePerformedListener(new OnGesturePerformedListener() {
					@Override
					public void onGesturePerformed(GestureOverlayView overlay,
							final Gesture gesture) { // 根据gesture包含的手势创建位图
						bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
						// 跳转回添加笔记页面
						Intent intent1 = new Intent(AddNote_painting.this,
								NewNoteActivity.class);
						intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						AddNote_painting.this.setResult(2, intent1);
						AddNote_painting.this.finish();
					}
				});

	}

	public static Bitmap getBitmap() {
		return bitmap;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
        return super.onKeyDown(keyCode, event);
	}
	

}
