package com.readme.readmenote;



import java.io.File;
import java.util.List;

import com.example.readmenote.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{
	LayoutInflater mInflater;
	List<String> items,paths;
	
	public MyListAdapter(Context context, List<String> it,
			List<String> pa) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		TextView text;
		ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			//自定义一个座位layout，我用回上次那个
			convertView = mInflater.inflate(R.layout.lines, null);
			//初始化
			holder = new ViewHolder();
			holder.text = (TextView)convertView.findViewById(R.id.filesname);
			holder.icon = (ImageView)convertView.findViewById(R.id.image_name);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		File f = new File(paths.get(position).toString());
		
		if(items.get(position).equals("b1")){
			holder.text.setText("返回  /mnt");
			holder.icon.setImageResource(R.drawable.folder);
			
		}else if(items.get(position).equals("b2")){
			holder.text.setText("返回上一级");
			holder.icon.setImageResource(R.drawable.folder);
		}else{
			holder.text.setText(f.getName());
			if(f.isDirectory()){
				holder.icon.setImageResource(R.drawable.folder);	
			}else{
			holder.icon.setImageResource(R.drawable.file);
			//System.out.println("zheliaaaaa");
		}
			}
		
		
		return convertView;
	}

}
