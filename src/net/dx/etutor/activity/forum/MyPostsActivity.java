package net.dx.etutor.activity.forum;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.activity.fragment.posts.PostsCollectFragment;
import net.dx.etutor.activity.fragment.posts.PostsIssueFragment;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

/**
 * 我的帖子
 * 
 * @author jwq
 * 
 */
public class MyPostsActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {

	public static String TAG = "MyPostsActivity";
	private static List<Fragment> list = new ArrayList<Fragment>();
	private FragmentManager fm;
	private RadioGroup mRadioGroup;
	private RadioButton mPostsIssue;
	private RadioButton mPostsCollect;
	private TextView mEdit;

	private PostsIssueFragment issueFragment;
	private PostsCollectFragment collectFragment;
	

	private MyAdapter adapter;
	private ViewPager mViewPager;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_my_posts);
		setTitle(R.string.text_my_posts);
		showIcon(0,"编辑");
		fm = getSupportFragmentManager();
		mEdit=(TextView) findViewById(R.id.main_head_bar_icon);
		mRadioGroup = (RadioGroup) findViewById(R.id.rg_my_posts);
		mPostsIssue = (RadioButton) findViewById(R.id.rb_posts_issue);
		mPostsCollect = (RadioButton) findViewById(R.id.rb_posts_collect);

		mViewPager = (ViewPager) findViewById(R.id.vp_posts);
		mViewPager.setOffscreenPageLimit(1);
		initViewPager();

	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		list.clear();
		issueFragment = new PostsIssueFragment(mApplication,
				MyPostsActivity.this, this);
		collectFragment = new PostsCollectFragment(mApplication,
				MyPostsActivity.this, this);
		list.add(issueFragment);
		list.add(collectFragment);
		adapter = new MyAdapter(fm);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void iconClick() {
		// TODO Auto-generated method stub
		String str=mEdit.getText().toString().trim();
		if (str.equals("编辑")) {
			mEdit.setText("取消");
			issueFragment.updateListView(true);
			collectFragment.updateListView(true);
		}else {
			mEdit.setText("编辑");
			issueFragment.updateListView(false);
			collectFragment.updateListView(false);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 500:
				issueFragment.onActivityResult(requestCode, resultCode, data);
				collectFragment.onActivityResult(requestCode, resultCode, data);
				break;
			}
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int item = 0;
		mEdit.setText("编辑");
		issueFragment.updateListView(false);
		collectFragment.updateListView(false);
		switch (checkedId) {
		case R.id.rb_posts_issue:
			item = 0;
			break;
		case R.id.rb_posts_collect:
			item = 1;
			break;
		}
		mViewPager.setCurrentItem(item, true);
		if (mApplication.getSpUtil().getTopicFlag()) {
			issueFragment.onRefresh();
			mApplication.getSpUtil().setTopicFlag(false);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mEdit.setText("编辑");
		issueFragment.updateListView(false);
		collectFragment.updateListView(false);
		switch (arg0) {
		case 0:
			mPostsIssue.setChecked(true);
			break;
		case 1:
			mPostsCollect.setChecked(true);
			break;
		default:
			mPostsIssue.setChecked(true);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	private class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

}
