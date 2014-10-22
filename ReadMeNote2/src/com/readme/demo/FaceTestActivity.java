package com.readme.demo;



import com.example.readmenote.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.widget.EditText;

public class FaceTestActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note_demo);
		EditText face = (EditText) findViewById(R.id.add_note_content);
		face.setText("hello world, This is testActivity!");
		ImageGetter imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			}
		};
		CharSequence cs = Html.fromHtml("<img src='" + R.drawable.a12345
				+ "'/>", imageGetter, null);
		face.getText().append(cs);
		String faceContent = FilterHtml(Html.toHtml(face.getText()));
		System.out.println(faceContent);
		face.setText(faceContent);
        String tem = face.getText().toString();
        System.out.println(tem);
	}

	public static String FilterHtml(String str) {
		str = str.replaceAll("<(?!br|img)[^>]+>", "").trim();
		return UnicodeToGBK2(str);
	}

	public static String UnicodeToGBK2(String s) {
		String[] temp = s.split(";");
		String rs = "";
		for (int i = 0; i < temp.length; i++) {
			int strIndex = temp[i].indexOf("&#");
			String newstr = temp[i];
			if (strIndex > -1) {
				String kstr = "";
				if (strIndex > 0) {
					kstr = newstr.substring(0, strIndex);
					rs += kstr;
					newstr = newstr.substring(strIndex);
				}
				int m = Integer.parseInt(newstr.replace("&#", ""));
				char c = (char) m;
				rs += c;
			} else {
				rs += temp[i];
			}
		}
		return rs;
	}

}
