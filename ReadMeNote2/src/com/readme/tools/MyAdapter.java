package com.readme.tools;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.readmenote.R;
import com.readme.data.Note;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter
{

	
		private Activity context;
		private List<Note> list = new ArrayList<Note>();
		LayoutInflater myLayoutInflater;
		public Note note;
		public boolean isCheckBox;
		private int position_checked;
		public List<Boolean> mChecked;//记住选中的状态
		private List<Integer>id;
		
		HashMap<Integer, View> map = new HashMap<Integer, View>();

		/*LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View itemView = inflater.inflate(R.layout.note_show, null);*/
		public MyAdapter(Activity context, List<Note> list,
				boolean isCheckBox,List<Integer> id) {
			this.context = context;
			this.list = list;
			this.isCheckBox = isCheckBox;
			this.id = id;

			this.myLayoutInflater = LayoutInflater.from(context);
			mChecked = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				mChecked.add(false);
			}

			
		}
		public MyAdapter(Activity context, List<Note> list,
				boolean isCheckBox,int position_checked,List<Integer> id) {
			this.context = context;
			this.list = list;
			this.isCheckBox = isCheckBox;
	        this.position_checked = position_checked;
			this.myLayoutInflater = LayoutInflater.from(context);
			this.id = id;

			mChecked = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				mChecked.add(false);
			}
			mChecked.set(position_checked, true);
		}

		static class ViewHolder {
			public ImageView image;
			public TextView title;
			public TextView body;
			public CheckBox check;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = myLayoutInflater.inflate(R.layout.note_show, null);
				note = list.get(position);
				holder.image = (ImageView) convertView
						.findViewById(R.id.mynote_imageView1);
				holder.image.setBackgroundResource(id.get(position));
				holder.title = (TextView) convertView.findViewById(R.id.mynote_textView1);
				holder.body = (TextView) convertView.findViewById(R.id.mynote_textView2);
				holder.check = (CheckBox) convertView.findViewById(R.id.mynote_check);
				// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag

				final int p = position;
				//map.put(position, convertView);
				if (isCheckBox == false) {
					holder.check.setVisibility(View.GONE);
				} else {
					holder.check.setVisibility(View.VISIBLE);
					
					holder.check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							CheckBox cb = (CheckBox) v;
							mChecked.set(p, cb.isChecked());// 如果点击的话就将原来false的变成true
						}
					});

				}

				convertView.setTag(holder);
			} else {
				//convertView = map.get(position);
				holder = (ViewHolder) convertView.getTag();
			}
			DateFormat dateFormat = DateFormat.getDateInstance();
			String newsTime = dateFormat
					.format(note.getCreateTime());
			// 现在可以设置了
			holder.body.setText(newsTime);
			holder.title.setText(note.getTitle());
			/*if(!note.getPicture_list().isEmpty())
				{BitMapTools bitMapTools = new BitMapTools();
				Bitmap bitmap = bitMapTools.getBitmap(note.getPicture_list().get(0).getPicture(), 150, 150);
				holder.image.setImageBitmap(bitmap);}*/
			holder.check.setChecked(mChecked.get(position) == false ? false : true);

			return convertView;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	

	
}
