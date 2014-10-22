package com.readme.readmenote;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.readmenote.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity implements OnClickListener {
	private TextView time_textview;
	private TabHost tabhost;
	private Intent my_note_intent, new_note_intent, share_intent;
	private RadioButton button1, button2, button3;
	private ViewPager pager;
	
	
	// 这个List<View>类型，我查不到
	List<View> listViews;
	LocalActivityManager manager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		listViews = new ArrayList<View>();
		// 初始化按钮
		initView();
		// 初始化Tabhost
		initTabhost();
		// 初始化viewPager
		initPager();
		/**
		 * 设置默认显示中间的界面，如果不设置就默认显示 pager.setCurrentItem(0)为第一张， 即添加笔记界面
		 */
		pager.setCurrentItem(1);
		// 获取当前时间的方法
		settime();

	}

	// 初始化按钮
	private void initView() {
		button1 = (RadioButton) findViewById(R.id.tab1);
		button2 = (RadioButton) findViewById(R.id.tab2);
		button3 = (RadioButton) findViewById(R.id.tab3);
		// 给各个按钮设置监听
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
	}

	// 初始化Tabhost
	private void initTabhost() {
		// TODO Auto-generated method stub
		// 获取该activity里的tabhost
		tabhost = getTabHost();
		tabhost.setup(manager);
		// 这里新建3个的Intent用于Activity的切换
		my_note_intent = new Intent(this, MyNoteActivity.class);
		new_note_intent = new Intent(this, NewNoteActivity.class);
		share_intent = new Intent(this, Cloud_sharing_activity.class);
		// 这里的listview用于装载各个activity对应的视图，即界面
		listViews.add(getView("NewNoteActivity", new_note_intent));
		listViews.add(getView("MyNoteActivity", my_note_intent));
		listViews.add(getView("Cloud_sharing_activity", share_intent));

		// 每一个标签的样子
		// 因为把Tabhost隐藏了，所以这段设置TabHost的标题、图片的代码也就起不了作用了，可删掉
		/**
		 * View myTab = (View)
		 * LayoutInflater.from(this).inflate(R.layout.tabmim, null); TextView
		 * text0 = (TextView) myTab.findViewById(R.id.tab_label); ImageView img0
		 * = (ImageView) myTab.findViewById(R.id.imageView1);
		 * text0.setText("我的笔记"); img0.setImageResource(R.drawable.my_note);
		 * 
		 * View newTab = (View)
		 * LayoutInflater.from(this).inflate(R.layout.tabmim, null); TextView
		 * text1 = (TextView) newTab.findViewById(R.id.tab_label); ImageView
		 * img1 = (ImageView) newTab.findViewById(R.id.imageView1);
		 * text1.setText("添加笔记"); img1.setImageResource(R.drawable.new_note);
		 * 
		 * View shareTab = (View) LayoutInflater.from(this).inflate(
		 * R.layout.tabmim, null); TextView text2 = (TextView)
		 * shareTab.findViewById(R.id.tab_label); ImageView img2 = (ImageView)
		 * shareTab.findViewById(R.id.imageView1); text2.setText("分享笔记");
		 * img2.setImageResource(R.drawable.share);
		 */

		// 向tabhost里添加tab，这部分已经不起作用了，tabHost 已经被隐藏
		/**
		 * tabhost.addTab(tabhost.newTabSpec("TAB1").setIndicator("添加笔记")
		 * .setContent(new_note_intent));
		 * tabhost.addTab(tabhost.newTabSpec("TAB2").setIndicator("我的笔记")
		 * .setContent(my_note_intent));
		 * tabhost.addTab(tabhost.newTabSpec("TAB3").setIndicator("分享")
		 * .setContent(share_intent));
		 */
	}

	// 初始化viewPager
	/**
	 * ViewPager类提供了多界面切换的新效果。新效果有如下特征： [1] 当前显示一组界面中的其中一个界面。 [2]
	 * 当用户通过左右滑动界面时，当前的屏幕显示当前界面和下一个界面的一部分。 [3] 滑动结束后，界面自动跳转到当前选择的界面中
	 * ViewPager来源于google 的补充组件android-support-v4.jar，位置在androidSDK文件夹
	 */
	private void initPager() {
		// TODO Auto-generated method stub
		// 找到xml文件的ViewPager控件
		pager = (ViewPager) findViewById(R.id.viewpager);
		// 初始化ViewPager控件的适配器
		pager.setAdapter(new MyPageAdapter(listViews));
		// 配置适配器的页面变化事件
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			// 页面选择
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					button1.setChecked(true);
					break;
				case 1:
					button2.setChecked(true);
					break;
				case 2:
					button3.setChecked(true);
					break;

				}
			}

			//默示在前一个页面滑动到后一个页面的时辰，在前一个页面滑动前调用的办法
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			/**arg0 ==1的时辰默示正在滑动，
			 * arg0==2的时辰默示滑动完毕了，
			 * arg0==0的时辰默示什么都没做，就是停在那*/
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private View getView(String id, Intent intent) {
		Log.d("ReadMeNote", "getView() called! id = " + id);
		return manager.startActivity(id, intent).getDecorView();
	}

	// 不另写一个java文件。所以有此内部类，MyPagerAdapter.
	/**
	 * 设置ViewPager控件的适配器 ViewPager的适配器继承于PagerAdapter基类，并实现以下四个方法
	 * 1.销毁position位置的界面 2. 获取当前窗体界面数 3.初始化position位置的界面 4.判断是否由对象生成界面
	 * 
	 * @author CSH
	 */
	private class MyPageAdapter extends PagerAdapter {
		private List<View> list;

		private MyPageAdapter(List<View> list) {
			this.list = list;
		}

		// 销毁position位置的界面
		@Override
		public void destroyItem(ViewGroup view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(list.get(position));
		}

		// 获取当前窗体界面数
		@Override
		public int getCount() {
			return list.size();
		}

		// 初始化position位置的界面
		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}

	// 获取时间的方法
	private void settime() {
		// TODO Auto-generated method stub yyyy年MM月dd日 E hh:mm:ss
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  E  ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		time_textview = (TextView) findViewById(R.id.time);
		time_textview.setText(str);
	}

	// 单击上面的导航栏的按钮时，按钮的选择对应着特定的界面
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab1:
			pager.setCurrentItem(0);
			break;
		case R.id.tab2:
			pager.setCurrentItem(1);
			break;
		case R.id.tab3:
			pager.setCurrentItem(2);
			break;
		}
	}
}
