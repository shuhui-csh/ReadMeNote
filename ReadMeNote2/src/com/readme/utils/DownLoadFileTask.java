package com.readme.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownLoadFileTask extends AsyncTask<String, Void, File> {

	private ReadMeClient client;


	public DownLoadFileTask(ReadMeClient client) {
		this.client = client;
	}
	@Override
	protected File doInBackground(String... params) {
		byte[] bytes;
		try {
			bytes = downloadResource(params[0]);
			FileOutputStream output = new FileOutputStream(new File(params[1]));
			output.write(bytes);
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(params[1]);
		if (file.exists()) {
			return file;
		}else {
			return null;
		}	
	}

	/** 下载附件 */
	private byte[] downloadResource(final String url) throws Exception {
		InputStream body = null;
		try {
			body = client.downloadResource(url);
		} catch (ReadMeException e) {
			if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
			} else {
				throw e;
			}
		}
		return toBytes(body);
	}

	/** 将输出流转换为字符数组 */
	private static byte[] toBytes(InputStream input) throws IOException {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int n = -1;
			while ((n = input.read(buffer)) != -1) {
				bytes.write(buffer, 0, n);
			}
			bytes.close();
			return bytes.toByteArray();
		} finally {
			input.close();
		}
	}

}
