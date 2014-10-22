package com.login.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetNet extends Activity {

	private RadioGroup wGroup = null;
	private RadioButton nwButton = null;
	private RadioButton wwButton = null;
	private Button setBut;
	private String myUrl = "222.200.98.206";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setnet);

		wGroup = (RadioGroup) findViewById(R.id.netGroup);
		nwButton = (RadioButton) findViewById(R.id.netnButton);
		wwButton = (RadioButton) findViewById(R.id.netwButton);

		setBut = (Button) findViewById(R.id.setBut);
		setBut.setOnClickListener(setClick);

		wGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (nwButton.getId() == checkedId) {
					myUrl = "222.200.98.206";
					MyApp myApp = (MyApp) getApplication();
					myApp.setMyUrl(myUrl);
				} else if (wwButton.getId() == checkedId) {
					myUrl = "222.200.98.206";
					MyApp myApp = (MyApp) getApplication();
					myApp.setMyUrl(myUrl);
				}
			}
		});

	}

	OnClickListener setClick = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			MyApp myApp = (MyApp) getApplication();
			myApp.setMyUrl(myUrl);
			SetNet.this.finish();
		}
	};
}