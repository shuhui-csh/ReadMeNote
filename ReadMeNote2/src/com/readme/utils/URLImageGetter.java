package com.readme.utils;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeException;
import com.readme.tools.BitMapTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html.ImageGetter;
import android.widget.EditText;


public class URLImageGetter implements ImageGetter {


	ReadMeClient client;
	int scale;
	
	public URLImageGetter(ReadMeClient client,int scale) {
		this.client =client;
		this.scale = scale;
	}

	@Override
	public Drawable getDrawable(String source) {
		ImageGetterAsyncTask getImageTask = new ImageGetterAsyncTask();
		getImageTask.execute(source);
		BitmapDrawable drawable = null;
		try {
			Bitmap bitmap =  getImageTask.get();
			drawable = new BitmapDrawable(bitmap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//Drawable drawable = Drawable.createFromPath(source);
		// Drawable drawable =
		// getResources().getDrawable(getResourecId(source));
		drawable.setBounds(0, 0,drawable.getIntrinsicWidth()*3,
				drawable.getMinimumHeight()*3);
		return drawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {

	
		
		@Override
		protected void onPostExecute(Bitmap result) {
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bitmap = null;
			try {
				bitmap = BitMapTools.getBitmap(downloadImage(params[0]),scale);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			return bitmap;
		}


		/**下载图片*/
	    private  InputStream downloadImage(final String url) throws Exception {
	        InputStream body = null;
	        try {
	        	   body = client.downloadResource(url);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {

	            } else {
	                throw e;
	            }
	        }
			return body;
	    }

	}

}
