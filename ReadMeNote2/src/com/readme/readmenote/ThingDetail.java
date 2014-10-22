package com.readme.readmenote;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.readmenote.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThingDetail extends ListActivity {
	List<String> items = null;// 文件的名称
	List<String> paths = null;// 文件的路径
	String rootPath = "/mnt";
	TextView mPath;
	final int THING = 56;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.path_test);
		mPath = (TextView) findViewById(R.id.text_ni);
		getFileDir(rootPath);// 取得文件结构的方法

	}

	private void getFileDir(String filePath) {
		// 目前所在路径
		mPath.setText("当前路径为：" + filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		// f 是当前文件的父文件夹
		File f = new File(filePath);
		// f 是当前页面的所有文件
		File[] files = f.listFiles();
		// 如果所在位置不是根目录，就是不是/mnt的话第一个和第二个分别设为根目录和返回上一层
		if (!filePath.equals(rootPath)) {// list的第一行设为回到根目录
			items.add("b1");
			paths.add(rootPath);
			// list的第二行设为回到上一层
			items.add("b2");
			paths.add(f.getParent());
		}
		// 把所有文件加入list中
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}
		setListAdapter(new MyListAdapter(this, items, paths));

	}

	// liset被按下之后要做的动作
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		File file = new File(paths.get(position));
		File[] files = file.listFiles();
		if (file.canRead()) {
			if (file.isDirectory()) {
				// 再一次打开里面的内容
				if (files == null || files.length == 0) {
					Toast.makeText(ThingDetail.this, "此文件夹没有文件",
							Toast.LENGTH_SHORT).show();
				} else {
					getFileDir(paths.get(position));

				}
			} else {
				// Toast.makeText(ThingDetail.this, "文件选择是否添加",
				// Toast.LENGTH_SHORT).show();
				addThing(file);// 弹出对话框选择文件是否添加，并向笔记本界面传递数据
			}
		}

	}

	private void addThing(final File file) {

		// TODO Auto-generated method stub
		new AlertDialog.Builder(ThingDetail.this)
				.setIcon(R.drawable.super_mono_3d_part2_42)
				.setTitle("确定添加此附件？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 得到添加的附件的名字和路径

						String name = file.getName();
						String path = file.getPath();
						Toast.makeText(ThingDetail.this,
								"你已经成功添加" + file.getName(), Toast.LENGTH_SHORT)
								.show();
						Intent intent = getIntent();
						// 将要传递的内容放在bundle里面

						Bundle bundle = new Bundle();
						bundle.putString("name", name);
						bundle.putString("path", path);
						bundle.putSerializable("file", file);
						intent.putExtras(bundle);
						ThingDetail.this.setResult(THING, intent);
						ThingDetail.this.finish();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(ThingDetail.this,
								"你未添加" + file.getName(), Toast.LENGTH_SHORT)
								.show();
					}
				}).show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = getIntent();
			// 将要传递的内容放在bundle里面
			Bundle bundle = new Bundle();
			bundle.putString("name", null);
			bundle.putString("path", null);
			bundle.putSerializable("file", null);
			intent.putExtras(bundle);
			ThingDetail.this.setResult(THING, intent);
			ThingDetail.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
