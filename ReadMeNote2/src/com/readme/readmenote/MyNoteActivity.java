package com.readme.readmenote;

import android.os.AsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import com.example.readmenote.R;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.Note;
import com.readme.data.Notebook;
import com.readme.database.NoteDBManger;


import com.readme.tools.MyAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

public class MyNoteActivity extends Activity {
	GridView gridView;
	Button but_upload;//现在暂时把这个上传得按钮变成删除的
	private SearchView sv;
	private final String TAG = "MyNoteActivity";
	private NoteDBManger noteDBManger;
	Activity context = this;
	public MyAdapter myAdapter;
	private List<Note> list = new ArrayList<Note>();
	private String user_name;
	private SharedPreferences preferences;
	private boolean isCheckBox = false;
	private List<Integer> listItem_ID = new ArrayList<Integer>();//判断有多少个被选中
	List<Notebook> notebooks = new ArrayList<Notebook>();
	List<String> notes = new ArrayList<String>();
	List<Note> list_note = new ArrayList<Note>();
	List<Integer>Resourceid = new ArrayList<Integer>();
	private static String notebookPath = "/4AA210FB0DBA41B2A997E893D9F32813";
	 private long mExitTime;// 按两次返回，退出，时间长度
	  private static final OAuthServiceProvider SERVICE_PROVIDER =
		        new OAuthServiceProvider(ReadMeConstants.REQUEST_TOKEN_URL,
		        		ReadMeConstants.USER_AUTHORIZATION_URL,
		        		ReadMeConstants.ACCESS_TOKEN_URL);

			private static final String CONSUMER_KEY = ReadMeConstants.CONSUMER_KEY;
			private static final String CONSUMER_SECRET = ReadMeConstants.CONSUMER_SECRET;

			private static final OAuthConsumer CONSUMER = new OAuthConsumer(null,
					CONSUMER_KEY, CONSUMER_SECRET, SERVICE_PROVIDER);
			private static ReadMeClient client = new ReadMeClient(CONSUMER);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_note);
		client.setAccessToken("e2c9c4a1519b86913372bc9bb752f310", "3a33324e2e360266b1dd82ef897091e7");
		gridView = (GridView) findViewById(R.id.gridView1);
		but_upload = (Button) findViewById(R.id.but_delete);
		noteDBManger = new NoteDBManger(this);
		preferences = getSharedPreferences("count", MODE_PRIVATE);
	    user_name = preferences.getString("name", null);
	    Log.i(TAG, user_name);   
		Resourceid.add(R.drawable.q11);
		Resourceid.add(R.drawable.q12);
		Resourceid.add(R.drawable.q13);
		Resourceid.add(R.drawable.q14);
		Resourceid.add(R.drawable.q15);
		Resourceid.add(R.drawable.q16);
		Resourceid.add(R.drawable.q17);
		Resourceid.add(R.drawable.q18);
		getData();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Note note = (Note)myAdapter.getItem(arg2);
				Intent intent = new Intent(MyNoteActivity.this, MyNoteDetailActivity.class);
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("note", note);
				intent.putExtras(bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				but_upload.setVisibility(View.VISIBLE);
				isCheckBox = true;
				myAdapter = new MyAdapter(context,list,isCheckBox,position,Resourceid);
				gridView.setAdapter(myAdapter);
			
				return true;
			}
		});
		but_upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				listItem_ID.clear();
				for(int i=0 ;i<myAdapter.mChecked.size();i++){
					if(myAdapter.mChecked.get(i)){
						listItem_ID.add(i);
					}
				}
				if(listItem_ID.size() != 0){
					StringBuilder sb = new StringBuilder();
					sb.append("将");
					for(int i=0;i<listItem_ID.size();i++){
						sb.append(listItem_ID.get(i)+".");
					}
					sb.append("笔记删除？");
					AlertDialog builder = new AlertDialog.Builder(MyNoteActivity.this).setIcon(R.drawable.ic_launcher)
							.setTitle(sb.toString()).
							setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DeleteTask deleteTask = new DeleteTask();
							deleteTask.execute(listItem_ID.size());
						
							
							
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create();
					builder.show();
				}else{
				Toast.makeText(MyNoteActivity.this, "未选中笔记", Toast.LENGTH_SHORT).show();
				}
			}
		});
		 
	

		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(isCheckBox == true){//如果有长按被选择，则跳出选择
				getData();
				gridView.setAdapter(myAdapter);
				//System.out.println("jijiyy");
				return true;
			}else{
				 if ((System.currentTimeMillis() - mExitTime) > 2000) {//判断时间尝过2s,则弹出toast，没有则finish
                     Object mHelperUtils;
                     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
            	 System.exit(0);
             }
             return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class DeleteTask extends AsyncTask<Integer, Void, String>{

		@Override
		protected String doInBackground(Integer... params) {
			for(int i=0;i<params[0];i++){
				try {
					deleteNote(list.get(listItem_ID.get(i)).getPath());
					noteDBManger.open();
					noteDBManger.deletenote(list.get(listItem_ID.get(i)).getPath());
					noteDBManger.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
	
	private void getData() {
		list.clear();
		isCheckBox = false;
		but_upload.setVisibility(View.GONE);
		NoteGetFormServerTask serverTask = new NoteGetFormServerTask();
		serverTask.execute("");
		//uiAsyntask.execute(note_title_et.getText().toString(), Html.toHtml(note_content_et.getText()));
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
		gridView.setAdapter(myAdapter);
	}
	
	
	
	public void reflash(){
		try {
			noteDBManger = new NoteDBManger(context);
			noteDBManger.open();
			noteDBManger.updatenote(list_note);
			list = noteDBManger.getdiaries("rhw");
			noteDBManger.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		myAdapter = new MyAdapter(context,list,isCheckBox,Resourceid);
		gridView.setAdapter(myAdapter);
	}
	
	 
	 private class NoteGetFormServerTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			
			try {
				/*String path = createNotebook("newbook",null);
				System.out.println(path);
				notebooks = getAllNotebooks();
				//deleteNotebook(notebooks.get(0).getPath());
				System.out.println(notebooks);*/
				notes = listNotes(notebookPath);
				
				System.out.println(notes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 
			return null;						
		}

		@Override
		protected void onPostExecute(Object result) {	
			// TODO Auto-generated method stub
			GetNoteTask getNoteTask = new GetNoteTask();
			getNoteTask.execute(notes);
			super.onPostExecute(result);
		}
		 
	 }
	  private static List<String> listNotes(final String notebook) throws Exception {
	        List<String> notes = new ArrayList<String>();
	        try {
	            notes = client.listNotes(notebook);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	        return notes;
	    }
	  
	  private static List<Notebook> getAllNotebooks() throws Exception {
	        List<Notebook> notebooks = new ArrayList<Notebook>();
	        try {
	            notebooks = client.getAllNotebooks();
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	        return notebooks;
	    }
	  private static void deleteNotebook(final String notebook) throws Exception {
	        try {
	            client.deletedNotebook(notebook);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	    }
	  
	  private static Note getNote(final String notePath) throws Exception {
	        try {
	            return client.getNote(notePath);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            	return null;
	            } else {
	                throw e;
	            }
	        }
	    }
	  private static void deleteNote(final String notePath) throws Exception {
	        try {
	            client.deleteNote(notePath);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	    }
	  private class GetNoteTask extends AsyncTask<List<String>, Void, List<Note>>{

		@Override
		protected List<Note> doInBackground(List<String>... params) {
			Note note = null;
			try {
				for (int i = 0; i < params[0].size(); i++) {
					note = getNote(params[0].get(i));
					list_note.add(note);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list_note;
		}

		@Override
		protected void onPostExecute(List<Note> result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				reflash();				
			}

			super.onPostExecute(result);
		}
		  
	  }	  
	  private static String createNotebook(final String name, final String group) throws Exception {
	        String notebook = null;
	        try {
	            notebook = client.createNotebook(name, group);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	        return notebook;
	    }
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		getData();
		super.onStart();
	}
	 
 }

