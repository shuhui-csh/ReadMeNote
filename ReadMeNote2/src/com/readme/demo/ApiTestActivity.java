package com.readme.demo;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import com.example.readmenote.R;
import com.example.readmenote.R.layout;
import com.example.readmenote.R.menu;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ApiTestActivity extends Activity {

	 private static final OAuthServiceProvider SERVICE_PROVIDER =
		        new OAuthServiceProvider(ReadMeConstants.REQUEST_TOKEN_URL,
		        		ReadMeConstants.USER_AUTHORIZATION_URL,
		        		ReadMeConstants.ACCESS_TOKEN_URL);

		   /* // YNote sandbox environment
		    private  final OAuthServiceProvider SANDBOX_SERVICE_PROVIDER =
		        new OAuthServiceProvider(ReadMeConstants.SANDBOX_REQUEST_TOKEN_URL,
		                ReadMeConstants.SANDBOX_USER_AUTHORIZATION_URL,
		                ReadMeConstants.SANDBOX_ACCESS_TOKEN_URL);*/

	    private static final String CONSUMER_KEY = ReadMeConstants.CONSUMER_KEY;
	    private static final String CONSUMER_SECRET = ReadMeConstants.CONSUMER_SECRET;
	    private static final String accessToken = ReadMeConstants.accessToken;
	    private static final String tokenSecret = ReadMeConstants.tokenSecret;
//		    private static final String CONSUMER_KEY = "7bac56b6e961a834313726a060c69b24";
//		    private static final String CONSUMER_SECRET = "b17ce2bf8e52bd4468300cda365b2bb3";
		    // sandbox consumer
		    private static final OAuthConsumer CONSUMER = new OAuthConsumer(null,
		            CONSUMER_KEY, CONSUMER_SECRET, SERVICE_PROVIDER);

		    private static ReadMeClient client = new ReadMeClient(CONSUMER);

		    //public static User user;
		    TextView api_test_textView;
			Button api_test_button ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_api_test);
		api_test_textView = (TextView)findViewById(R.id.api_test_textView);
		api_test_button = (Button)findViewById(R.id.api_test_button);
		 client.setAccessToken(accessToken, tokenSecret);	
		 api_test_button.setOnClickListener(new OnClickListener() {
		   
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AsyncTask() {
						User user;
						@Override
						protected Object doInBackground(Object... params) {
							try {
								
						           
						          user = getUser();
						          System.out.println(user + "55555555555");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Object result) {
							
							api_test_textView.setText("-------" + user.getId());
							super.onPostExecute(result);
							
						}
					}.execute("");
				}
			});
	}

	private static User getUser() throws Exception {
        User user = null;
        try {
            user = client.getUser();
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
                
            } else {
                throw e;
            }
        }
        return user;
    }
}
