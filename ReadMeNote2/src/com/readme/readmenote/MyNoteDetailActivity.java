package com.readme.readmenote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import com.example.readmenote.R;
import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.SpeechUtility;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.JsonParser;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.Note;
import com.readme.data.Resource;
import com.readme.database.NoteDBManger;
import com.readme.tools.DealString;
import com.readme.utils.EditTextClickListner;
import com.readme.utils.GestureImageUtils;
import com.readme.utils.Messages;
import com.readme.utils.URLImageGetter;
import com.readme.utils.UpLoadFileTask;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Html;
import android.text.format.DateFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MyNoteDetailActivity extends Activity {

	/** 保存按钮 */
	private ImageButton addnote_save_btn;
	/** 添加图片按钮 */
	private ImageButton addnote_picture_btn;
	/** 添加录音按钮 */
	private ImageButton addnote_record_btn;
	/** 语音输入按钮 */
	private ImageButton addnote_voice2text_btn;
	/** 添加涂鸦按钮 */
	private ImageButton addnote_painting_btn;
	/** 添加附件按钮 */
	private ImageButton addnote_addRescource_btn;
	/**修改按钮*/
	private ImageButton detail_modify_btn;
	/** 笔记添加的标题 */
	private EditText note_title_et;
	/** 笔记的详细输入内容 */
	private EditText note_content_et;
	// 传过去的resultCode
	/** 心情图标 */
	private final int MOOD = 1;
	/** 手势 */
	private final int GESTURE = 2;
	/** 录音 */
	private final int RECORD = 3;
	/** 照相 */
	private final int CAMERA = 98;
	/** 相片 */
	private final int PICTURE = 99;
	/** 附件 */
	private final int THING = 56;
	/** 语音部分的请求码 */
	private final int REQUEST_CODE_SEARCH = 817;
	private Toast mToast;
	private Context context = this;
	NoteDBManger noteDBManger;
	private Note from_mynote;
	
	private static final OAuthServiceProvider SERVICE_PROVIDER = new OAuthServiceProvider(
			ReadMeConstants.REQUEST_TOKEN_URL,
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
		setContentView(R.layout.activity_my_note_detail);
		client.setAccessToken("e2c9c4a1519b86913372bc9bb752f310",
				"3a33324e2e360266b1dd82ef897091e7");
		initialize_button_variables();
		setButtonClickListener();
		initVoice2Text();
		Intent intent = getIntent();			
		from_mynote = (Note) intent.getSerializableExtra("note");
		SetNoteTask noteTask = new SetNoteTask();
		noteTask.execute(from_mynote);
	}
	

	/**
	 * 初始化 心情图标，用户标题，详细内容，保存按钮，添加图片，录音，语音输入，涂鸦，添加附件，分享按钮。
	 * */
	public void initialize_button_variables() {		
		note_title_et = (EditText) findViewById(R.id.user_title);
		note_content_et = (EditText) findViewById(R.id.user_detail);
		addnote_save_btn = (ImageButton) findViewById(R.id.gallery_menu_save);
		addnote_picture_btn = (ImageButton) findViewById(R.id.gallery_menu_picture);
		addnote_record_btn = (ImageButton) findViewById(R.id.gallery_menu_record);
		addnote_voice2text_btn = (ImageButton) findViewById(R.id.gallery_menu_sound_import);
		addnote_painting_btn = (ImageButton) findViewById(R.id.gallery_menu_painting);
		addnote_addRescource_btn = (ImageButton) findViewById(R.id.gallery_menu_add_thing);
		detail_modify_btn = (ImageButton) findViewById(R.id.gallery_menu_modify);
		
		note_title_et.setFocusableInTouchMode(false);
		note_content_et.setFocusableInTouchMode(false);
		addnote_save_btn.setVisibility(View.GONE);
		addnote_picture_btn.setVisibility(View.GONE);
		addnote_record_btn.setVisibility(View.GONE);
		addnote_voice2text_btn.setVisibility(View.GONE);
		addnote_painting_btn.setVisibility(View.GONE);
		addnote_addRescource_btn.setVisibility(View.GONE);
	}

	/**
	 * 设置所有ImageButton的单击时间，包括 心情图标，保存，图片，录音，语音输入，分享，涂鸦，添加附件
	 * */

	public void setButtonClickListener() {
		detail_modify_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addnote_save_btn.setVisibility(View.VISIBLE);
				addnote_picture_btn.setVisibility(View.VISIBLE);
				addnote_record_btn.setVisibility(View.VISIBLE);
				addnote_voice2text_btn.setVisibility(View.VISIBLE);
				addnote_painting_btn.setVisibility(View.VISIBLE);
				addnote_addRescource_btn.setVisibility(View.VISIBLE);
				note_title_et.setFocusableInTouchMode(true);
				note_content_et.setFocusableInTouchMode(true);
				detail_modify_btn.setVisibility(View.GONE);

			}
		});
		// ==============================================================================
		addnote_save_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UiAsyntask uiAsyntask = new UiAsyntask();
				from_mynote.setContent(Html.toHtml(note_content_et.getText()));
				from_mynote.setTitle(note_title_et.getText().toString());
				uiAsyntask.execute(from_mynote);
		
			}
		});

		// ===============================================================================
		addnote_picture_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 声明dialog里面的两个按钮
				ImageButton choose_camera, choose_picture;
				final Dialog choose = new Dialog(MyNoteDetailActivity.this,
						R.style.draw_dialog);
				choose.setContentView(R.layout.addnote_picture);
				// 设置背景模糊参数
				WindowManager.LayoutParams winlp = choose.getWindow()
						.getAttributes();
				winlp.alpha = 0.9f; // 0.0-1.0
				choose.getWindow().setAttributes(winlp);
				choose.show();// 显示弹出框
				choose_camera = (ImageButton) choose
						.findViewById(R.id.choose_camera);
				choose_picture = (ImageButton) choose
						.findViewById(R.id.choose_picture);
				choose_picture.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 打开手机相册的intent action语句
						choose.dismiss();
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						// 取得照片后返回本界面
						startActivityForResult(intent, PICTURE);
					}
				});
				choose_camera.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 打开系统的相机的intent action 语句
						choose.dismiss();
						Intent camera = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(camera, CAMERA);
					}
				});
			}
		});

		// ==================================================================================
		addnote_record_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyNoteDetailActivity.this,
						AddNote_record.class);
				startActivityForResult(intent, RECORD);
			}
		});
		// ==================================================================================
		addnote_voice2text_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 检测是否安装了讯飞语音服务
				if (SpeechUtility.getUtility(MyNoteDetailActivity.this)
						.queryAvailableEngines() == null
						|| SpeechUtility.getUtility(MyNoteDetailActivity.this)
								.queryAvailableEngines().length <= 0) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							MyNoteDetailActivity.this);
					dialog.setMessage(getString(R.string.download_confirm_msg));
					dialog.setNegativeButton(R.string.dialog_cancel_button,
							null);
					dialog.setPositiveButton(
							getString(R.string.dialog_confirm_button),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									String url = SpeechUtility.getUtility(
											MyNoteDetailActivity.this)
											.getComponentUrl();
									String assetsApk = "SpeechService_1.0.1017.apk";
									processInstall(MyNoteDetailActivity.this, url,
											assetsApk);
								}
							});
					dialog.show();
					return;
				}
				Intent intent = new Intent();
				// 指定action，调用讯飞的对话默认窗口
				intent.setAction("com.iflytek.speech.action.voiceinput");
				intent.putExtra(SpeechConstant.PARAMS, "asr_ptt=0");
				intent.putExtra(SpeechConstant.VAD_EOS, "1000");
				// 设置弹出框的两个按钮的名称
				intent.putExtra("title_done", "确定");
				intent.putExtra("title_cancle", "取消");
				startActivityForResult(intent, REQUEST_CODE_SEARCH);
			}
		});
		// ===================================================================================
		addnote_painting_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {			
				Intent intent = new Intent(MyNoteDetailActivity.this,
						AddNote_painting.class);
				startActivityForResult(intent, GESTURE);
			}
		});
		// ===================================================================================
		addnote_addRescource_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyNoteDetailActivity.this,
						ThingDetail.class);
				startActivityForResult(intent, THING);
			}
		});
		
		note_content_et.setOnClickListener(new EditTextClickListner(MyNoteDetailActivity.this, note_content_et));
	}

	/** 添加附件和图片返回的数据 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == PICTURE) {
				ContentResolver resolver = getContentResolver();
				try{Uri uri = data.getData();
	    		Bitmap bit =MediaStore.Images.Media.getBitmap(resolver, uri); 
	    		String[] proj = {MediaStore.Images.Media.DATA};	    		 
	            //好像是android多媒体数据库的封装接口，具体的看Android文档
	            Cursor cursor = managedQuery(uri, proj, null, null, null); 
	            //按我个人理解 这个是获得用户选择的图片的索引值
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            //将光标移至开头 ，这个很重要，不小心很容易引起越界
	            cursor.moveToFirst();
	            //最后根据索引值获取图片路径
	            String imgPath = cursor.getString(column_index);
	            addResourceTag(new File(imgPath), 1);
	        }catch (IOException e) {
	           
	        }
			} else if (requestCode == CAMERA) {
				//Uri uri = data.getData();// 拍照后的图片的路径
	    		String sdStatus = Environment.getExternalStorageState();  
	            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
	               Toast.makeText(this,  "SD card is not avaiable/writeable right now.",Toast.LENGTH_SHORT).show();                 
	                return;  
	            }  
	            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg"; 
	            System.out.println(name);
	            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
	            Bundle bundle = data.getExtras();  
	            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
	          
	            FileOutputStream b = null;  
	            File file = new File("/mnt/sdcard/DCIM/ReadMe/");  
	            file.mkdirs();// 创建文件夹  
	            String fileName = "/mnt/sdcard/DCIM/ReadMe/"+name;  
	  
	            try {  
	                b = new FileOutputStream(fileName);  
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
	            } catch (FileNotFoundException e) {  
	                e.printStackTrace();  
	            } finally {  
	                try {  
	                    b.flush();  
	                    b.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	            addResourceTag(new File(fileName),1);
//	    		picture_path.setText(fileName);
//	    		picture_name.setText(name);
//	    		imageView.setImageBitmap(bitmap);
			}
			if (requestCode == THING) {			
					Bundle bundle = data.getExtras();
					String name_appendix = bundle.getString("name");// 附件名称
					if (name_appendix!=null) {					
						// note_title_et.setText("附件的名字是："+name_appendix);
						String path_appendix = bundle.getString("path");// 附件路径
						// note_title_et.setText("附件的路径是："+path_appendix);
						File file_get = (File) bundle.getSerializable("file");// 这个文件
						// type.setText("附件的类型是："+getMINEType(file_get));
						addResourceTag(file_get, 1);
					}
			
			
			}
			if (requestCode == RECORD) {
				Bundle b = data.getExtras();
				String filepath = b.getString("file");
				addResourceTag(new File(filepath), 1);
			}
			
			if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
				// 取得识别的字符串
				ArrayList<String> results = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String res = results.get(0);// 语音识别的文本
				note_content_et.append(res);
			}
			
			if (requestCode == GESTURE) {
				Bitmap bitmap_painting = null;// 涂鸦图片
				AddNote_painting painting = new AddNote_painting();
				bitmap_painting = painting.getBitmap();// 涂鸦的图片
				String filepath = null;
				try {
					filepath = GestureImageUtils.saveBitmap(bitmap_painting);//返回的是文件地址
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				int index = note_content_et.getSelectionStart();
				Editable edit_text = note_content_et.getEditableText();
				if (index < 0 || index >= edit_text.length()) {
					addResourceTag(new File(filepath),1);
				} else {

				}
			}
	}
	
	
	
//=============================================================================
	/**初始化语音转换文字功能*/
	private void initVoice2Text(){
		// 设置申请到的应用appid
		SpeechUtility.getUtility(this).setAppid("51ece17f");
		// 初始化识别对象
		SpeechRecognizer mIat = new SpeechRecognizer(this, mInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		// 转写会话
		mIat.setParameter(SpeechConstant.PARAMS, "asr_ptt=1");
		mIat.startListening(mRecognizerListener);
		// 转写会话停止
		mIat.stopListening(mRecognizerListener);
		// 转写会话取消
		mIat.cancel(mRecognizerListener);
	}
	
	/**添加网络图标*/
	private void addResourceTag(File file,int scale){
		Handler uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// ui更新消息
				switch (msg.what) {
				case Messages.TASK_SUCCESSS:// 如果成功
					if (msg.obj != null) {// 如果返回数据不为空
						Resource re =		(Resource)msg.obj;
						URLImageGetter imageGetter = new URLImageGetter(client, 1);
						note_content_et.append("\n");
						note_content_et.append(Html.fromHtml(re.toResourceTag(), imageGetter, null));
						note_content_et.append("\n");
					}
					break;
				}
			}
		};
		UpLoadFileTask fileTask = new UpLoadFileTask(client,uiHandler,context);
		fileTask.execute(file);

	}
	
	/**
	 * 初期化监听器。
	 */
	// 语音监听器
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule module, int code) {
			if (code == ErrorCode.SUCCESS) {
				// findViewById(R.id.iat_recognize_bind).setEnabled(true);
				addnote_voice2text_btn.setEnabled(true);
			}
		}
	};

	/**
	 * 识别回调。
	 */
	// 转写回调的一系列回调方法
	private RecognizerListener mRecognizerListener = new RecognizerListener.Stub() {

		@Override
		// 录音音量回调
		public void onVolumeChanged(int v) throws RemoteException {
			showTip("onVolumeChanged：" + v);
		}

		@Override
		// 录音开始回调
		public void onBeginOfSpeech() throws RemoteException {
			// TODO Auto-generated method stub
			showTip("onBeginOfSpeech");
		}

		@Override
		// 录音结束回调
		public void onEndOfSpeech() throws RemoteException {
			// TODO Auto-generated method stub
			showTip("onEndOfSpeech");
		}

		@Override
		// 错误回调
		public void onError(int errorCode) throws RemoteException {
			// TODO Auto-generated method stub
			showTip("onError Code：" + errorCode);
		}

		// 语音转写结果回调
		@Override
		public void onResult(final RecognizerResult result, boolean arg1)
				throws RemoteException {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {
						// 显示
						String iattext = JsonParser.parseIatResult(result
								.getResultString());
						note_content_et.append(iattext);
					} else {
						showTip("无识别结果");
					}
				}
			});
		}
	};

	// 安装语音组件
	protected void processInstall(MyNoteDetailActivity MyNoteDetailActivity, String url,
			String assetsApk) {
		// TODO Auto-generated method stub
		// 直接下载方式
		// ApkInstaller.openDownloadWeb(context, url);
		// 本地安装方式
		if (!ApkInstaller.installFromAssets(MyNoteDetailActivity, assetsApk)) {
			Toast.makeText(MyNoteDetailActivity.this, "安装失败", Toast.LENGTH_SHORT)
					.show();
		}
	}
	// 语音部分公用的一个toast提示方法
	protected void showTip(final String str) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}
	
	//===============================================================================
	private class UiAsyntask extends AsyncTask<Note, Void, Note>{
	
		
		@Override
		protected void onPostExecute(Note result) {
			// TODO Auto-generated method stub
			if (result != null) {
				try {
					noteDBManger = new NoteDBManger(context);
					noteDBManger.open();
					noteDBManger.updatenote(result);
					noteDBManger.close();
				} catch (Exception e) {
					e.printStackTrace();
				
				}
			}
			Intent i = new Intent(MyNoteDetailActivity.this, MainActivity.class);
			startActivity(i);
			finish();
			super.onPostExecute(result);
		}
		@Override
		protected Note doInBackground(Note... params) {
			Note note = null;
			try {
//				System.out.println(params[1]);
//				DealString.cutString(params[1]);
				 updateNote(params[0]);
				System.out.println(note);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return params[0];
		}		
	}
	
	private class SetNoteTask extends AsyncTask<Note, Void, String>{

		@Override
		protected String doInBackground(Note... params) {
	
			note_title_et.setText(params[0].getTitle());
			System.out.println(params[0].getContent());
			String spanned = params[0].getContent();
			return spanned;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.println(result);
			
			URLImageGetter imageGetter = new URLImageGetter(client, 1);	
			
			note_content_et.setText(Html.fromHtml(result, imageGetter, null));
			super.onPostExecute(result);
		}
		
	}
	
	 private static void updateNote(final Note note)
	            throws Exception {
	        try {
	            client.updateNote(note);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            	
	            } else {
	                throw e;
	            }
	        }
	    }
}
