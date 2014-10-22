package com.readme.login;

import com.example.readmenote.R;
import com.readme.database.NoteDBManger;
import com.readme.readmenote.MainActivity;
import com.readme.tools.MyAdapter;
import com.readme.tools.TimeTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity{
	private final String TAG = "Register"; 
	Button register, register_back;
	EditText key_ed,name_ed,key_again_ed;
	private NoteDBManger noteDBManger;
	Activity context = this;
	String register_time = TimeTools.getStringDate();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.register_activity);
		key_ed = (EditText)findViewById(R.id.key_register);
		key_again_ed = (EditText)findViewById(R.id.key_again_register);
		name_ed = (EditText)findViewById(R.id.name_register);
		register = (Button)findViewById(R.id.register_register);
		register_back = (Button)findViewById(R.id.register_back);
		
		register.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    noteDBManger = new NoteDBManger(context);
				String  name = name_ed.getText().toString();
				String key = key_ed.getText().toString();
				String key_again = key_again_ed.getText().toString();
				if(name.equals("") || key.equals("")) 
					Toast.makeText(Register.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
				else if(key.equals(key_again)){
					//数据库里面插入多一条数据
					
					try {	
						
						noteDBManger.open();
					
						boolean tag = noteDBManger.check(name);
						if(tag){
							noteDBManger.addUser(name, register_time ,key);
							Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
							Intent intent = getIntent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle bundle = new Bundle();
							bundle.putString("name", name);
							bundle.putString("key", key);
							intent.putExtras(bundle);
							Register.this.setResult(0, intent);
							Register.this.finish();
						}
						else
							Toast.makeText(Register.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
						noteDBManger.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					Toast.makeText(Register.this, "两次输入密码不相同,请重新输入", Toast.LENGTH_SHORT).show();
				}
				
				}
		});
		register_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Register.this,Login.class);
				startActivity(intent);
			}
		});
		
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(Register.this,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

 
}
