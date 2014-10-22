package com.readme.demo;

import java.io.File;
import java.io.IOException;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import com.example.readmenote.R;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.Resource;
import com.readme.utils.EditTextClickListner;
import com.readme.utils.URLImageGetter;
import com.readme.utils.UpLoadFileTask;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TestEditTextActivity extends Activity {

	private static final String TAG = "Test_editTextActivity";
	EditText textView;
	Button show_content_btn;
	Button add_image_btn;
	Resource resource1 = new Resource("sdcard/3.png");
	String url = resource1.toResourceTag();
	Resource resource;
	ImageView imageView;
	private Context context = this;
//	String resourceUrl = "http://note.youdao.com/yws/open/resource/download/483/46B22ED5D6944DF49F68A55A4E2A85DC";

	private static final OAuthServiceProvider SERVICE_PROVIDER = new OAuthServiceProvider(
			ReadMeConstants.REQUEST_TOKEN_URL,
			ReadMeConstants.USER_AUTHORIZATION_URL,
			ReadMeConstants.ACCESS_TOKEN_URL);

	String paString = "/4AA210FB0DBA41B2A997E893D9F32813/94F8823F7800449BA50F8B3C6D962C7B";
	private static final String CONSUMER_KEY = ReadMeConstants.CONSUMER_KEY;
	private static final String CONSUMER_SECRET = ReadMeConstants.CONSUMER_SECRET;

	private static final OAuthConsumer CONSUMER = new OAuthConsumer(null,
			CONSUMER_KEY, CONSUMER_SECRET, SERVICE_PROVIDER);

	private static ReadMeClient client = new ReadMeClient(CONSUMER);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note_demo);
		client.setAccessToken("e2c9c4a1519b86913372bc9bb752f310",
				"3a33324e2e360266b1dd82ef897091e7");
		textView = (EditText) findViewById(R.id.add_note_content);
		show_content_btn = (Button) findViewById(R.id.get_note_btn);
		add_image_btn = (Button) findViewById(R.id.creat_note_btn);
		imageView = (ImageView) findViewById(R.id.add_view);
		EditTextClickListner editTextClickListner = new EditTextClickListner(TestEditTextActivity.this, textView);
		show_content_btn.setOnClickListener(new ButtonClickListener());
		add_image_btn.setOnClickListener(new ButtonClickListener());
		textView.setOnClickListener(editTextClickListner);

	}

	/** 按钮触发事件类 */
	private class ButtonClickListener implements OnClickListener {
		

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.creat_note_btn:
				try {
					UpLoadFileTask upLoadResourceTask = new UpLoadFileTask(
							client,new Handler(),context);
					upLoadResourceTask.execute(new File("/mnt/sdcard/4.txt"));
					resource = upLoadResourceTask.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				URLImageGetter imageGetter = new URLImageGetter(client, 1);
				textView.append(Html.fromHtml(resource.toResourceTag(),
						imageGetter, null));
				textView.append("\n\n");
//				setSpanClickable();
				break;
			case R.id.get_note_btn:
				String i = null;
				try {
					i = client.getShare(paString);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadMeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textView.setText(i);
				System.out.println("i+++++++");
				System.out.println(i+"===========");
//				String html = textView.getText().toString();
//				String i = Html.toHtml(textView.getText());
//				Log.i(TAG, i);
				break;
			default:
				break;
			}
		}
	}
	

	
	
//	private OnClickListener textListener = new OnClickListener()
//	{//确实不靠谱
//		@Override
//		public void onClick(View v)
//		{
//	   //关闭软键盘
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
//			Spanned s = textView.getText();
//			ImageSpan[]	imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
//			int selectionStart = textView.getSelectionStart();
//			for (ImageSpan span : imageSpans)
//			{
//					int start = s.getSpanStart(span);
//					int end = s.getSpanEnd(span);
//					if (selectionStart >= start && selectionStart < end)//找到图片
//						{		
//						 Toast.makeText(getApplicationContext(), "找到图片", Toast.LENGTH_SHORT).show();
//							return;
//						}
//			}
//		}
//	};
	
	public void setSpanClickable() {
		//此方法比较靠谱
		Spanned s = textView.getText();
		 int spanEnd;
		//setMovementMethod很重要，不然ClickableSpan无法获取点击事件。
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		
		for (ImageSpan span : imageSpans) {
			final String image_src = span.getSource();
			final int start = s.getSpanStart(span);
			final int end = s.getSpanEnd(span);
			spanEnd = end;
			
			Log.i(TAG,"setSpanClickable , image_src = "+image_src+" , start = "+start+" , end = "+end);
			ClickableSpan click_span = new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					textView.setCursorVisible(false);
					Log.i(TAG , "click_span , onClick , "+textView.getSelectionStart());
					Toast.makeText(TestEditTextActivity.this,
							"Image Clicked " + image_src, Toast.LENGTH_SHORT)
							.show();
				}
			};

			ClickableSpan[] click_spans = s.getSpans(start, end,
					ClickableSpan.class);
			Log.i(TAG,"click_spans.length = "+click_spans.length);
			if (click_spans.length != 0) {
				// remove all click spans
				for (ClickableSpan c_span : click_spans) {
					((Spannable) s).removeSpan(c_span);
				}
			}

			((Spannable) s).setSpan(click_span, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			Log.i(TAG,"length = "+s.getSpans(start, end,ClickableSpan.class).length);
		}
	}
}
	
