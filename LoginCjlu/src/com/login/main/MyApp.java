package com.login.main;


import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;

public class MyApp extends Application {
	private DefaultHttpClient mHttpClient;
	private String myUrl;
	public String getMyUrl() {
		return myUrl;
	}

	public void setMyUrl(String myUrl) {
		this.myUrl = myUrl;
	}

	public DefaultHttpClient getmHttpClient() {
		return mHttpClient;
	}

	public void setmHttpClient(DefaultHttpClient mHttpClient) {
		this.mHttpClient = mHttpClient;
	}
}