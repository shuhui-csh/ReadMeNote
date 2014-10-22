package com.readme.login;

import com.example.readmenote.R;
import com.readme.database.NoteDBManger;
import com.readme.readmenote.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	private final String TAG = "Login"; 
	private Button log,register;
	private EditText name_ed,key_ed;
	private String name,key;
	private NoteDBManger noteDBManger;	
	Activity context = this;
	SharedPreferences preferences;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.login);
		//初始化
		intView();
		
		log.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noteDBManger = new NoteDBManger(context);
				name = name_ed.getText().toString();
				key = key_ed.getText().toString();
				
				
				preferences = getSharedPreferences("count", MODE_PRIVATE);
				Editor editor = preferences.edit();
				//把最近一次使用的用户的用户名放进去
				editor.putString("name", name).commit();
				editor.putString("key", key).commit();
				//然后就是数据库那边的了，验证正确的话就跳转
				if(name.equals("") || key.equals("")) 
					Toast.makeText(Login.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
				else{
					try{
						noteDBManger.open();
						boolean tag = noteDBManger.check(name);
						if(tag){
							
							Toast.makeText(Login.this, "用户不存在", Toast.LENGTH_SHORT).show();
						}else{
							tag = noteDBManger.check(name, key);
							if(tag){
								Toast.makeText(Login.this, "密码不正确", Toast.LENGTH_SHORT).show();
							}
							else{
								Intent intent = new Intent(Login.this,MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								Login.this.finish();
							}
						}
						noteDBManger.close();
					}catch(Exception e){
						 e.printStackTrace();
					}
				}
			}
		});
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳到注册界面，到时候是否要从注册页面获得数据然后直接把刚注册的用户的用户名和密码填在edittext里面？？
				Intent intent = new Intent(Login.this,Register.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent,0);
				
			}
		});
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 0 || resultCode == 0){
			
			Bundle bundle = data.getExtras();
			String name_registered = bundle.getString("name");
			String key_registered = bundle.getString("key");
			name_ed.setText(name_registered);
			key_ed.setText(key_registered);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void intView() {
		// TODO Auto-generated method stub
		log = (Button)findViewById(R.id.but_log);
		register = (Button)findViewById(R.id.but_register);
		name_ed = (EditText)findViewById(R.id.name_ed);
		key_ed = (EditText)findViewById(R.id.key_ed);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
