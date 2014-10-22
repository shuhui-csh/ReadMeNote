package com.readme.utils;

import java.io.File;

import com.readme.data.Resource;
import com.readme.database.NoteDBManger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditTextClickListner implements OnClickListener {

	private Context c;
	private EditText eText;
	Resource resource = null;
	NoteDBManger noteDBManger;
	Activity activity;
	public EditTextClickListner(Activity context,EditText editText) {
		this.c = context;
		this.eText = editText;
		this.activity = context;
		try {
			noteDBManger = new NoteDBManger(context);
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
	@Override
	public void onClick(View v) {
		Spanned s = eText.getText();
		ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		int selectionStart = eText.getSelectionStart();
		for (ImageSpan span : imageSpans) {
			String image_src = span.getSource();
			noteDBManger.open();
			resource = noteDBManger.findResource(null,image_src);
			System.out.println(resource.getLocal_url());
			noteDBManger.close();			
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);                
			if (selectionStart >= start && selectionStart < end)// 找到图片
			{
			openFile(new File(resource.getLocal_url())); 			
				Toast.makeText(c, "找到图片"+image_src, Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}
	
	private void openFile(File f) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMINEType(f);//判断附件的类型
		intent.setDataAndType(Uri.fromFile(f),//师兄这个地方，如果用路径的话，要调用Uri.parse(uriString)这个方法？？但是我试过然后文件无法载入T_T，所以还是用回要直接用file这个方法了
				type);
		activity.startActivity(intent);

	}

	private String getMINEType(File f) {
		// TODO Auto-generated method stub
		String type = "";
		String fName = f.getName();

		String end = fName.substring(
				fName.lastIndexOf(".") + 1,
				fName.length()).toLowerCase();

		if (end.equals("m4a")
				|| end.equals("mp3")
				|| end.equals("mid")
				|| end.equals("xmf")
				|| end.equals("ogg")
				|| end.equals("wav")) {
			type = "audio";
		} else if (end.equals("mp4")
				|| end.equals("3gp")) {
			type = "video";
		} else if (end.equals("jpg")
				|| end.equals("gif")
				|| end.equals("png")
				|| end.equals("bmp")
				|| end.equals("jpeg")) {
			type = "image";
		} else if(end.equals("doc")
				||end.equals("docx")
				||end.equals("ppt")
				||end.equals("xls")
				||end.equals("xlsx")
				||end.equals("ppt")
				||end.equals("pptx")
				||end.equals("apk")
				||end.equals("bin")
				||end.equals("class")
				||end.equals("exe")
				||end.equals("gtar")
				||end.equals("gz")
				||end.equals("jar")
				||end.equals("js")
				||end.equals("mpc")
				||end.equals("msg")
				||end.equals("pps")
				||end.equals("tar")
				||end.equals("tgz")
				||end.equals("wps")
				||end.equals("z")
				||end.equals("zip")
				){
			type = "application";
		}else if(end.equals("c")
				||end.equals("cpp")
				||end.equals("conf")
				||end.equals("h")
				||end.equals("html")
				||end.equals("htm")
				||end.equals("java")
				||end.equals("log")
				||end.equals("rc")
				||end.equals("prop")
				||end.equals("sh")
				||end.equals("txt")
				||end.equals("xml")
				){
			type = "text";
		}else {

			type = "*";
		}
		type += "/*";
		return type;
	}

}
