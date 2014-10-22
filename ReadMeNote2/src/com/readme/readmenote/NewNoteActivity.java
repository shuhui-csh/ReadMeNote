package com.readme.readmenote;

import com.example.readmenote.R;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;
public class NewNoteActivity extends Activity {

	ImageButton picture, record, record_input, camera, painting, edittext,
			add_thing;

	Bundle data_painting = new Bundle();

	static int label = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_note);
		init();
		edittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 1;
				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);
			}
		});
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 2;
				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);

			}
		});
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 3;
				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);

			}
		});
		record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 4;
				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);
			}
		});
		record_input.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 5;
				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);

			}
		});
		painting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				label = 6;

				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);

			}
		});

		add_thing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = 7;

				Intent intent1 = new Intent(NewNoteActivity.this,
						AddNoteActivity.class);
				startActivity(intent1);

			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		picture = (ImageButton) findViewById(R.id.picture);
		record = (ImageButton) findViewById(R.id.record);
		record_input = (ImageButton) findViewById(R.id.record_input);
		camera = (ImageButton) findViewById(R.id.camera);
		painting = (ImageButton) findViewById(R.id.painting);
		edittext = (ImageButton) findViewById(R.id.edittext);
		add_thing = (ImageButton) findViewById(R.id.add_thing);

	}

	public static int getlabel() {
		return label;
	}

}
