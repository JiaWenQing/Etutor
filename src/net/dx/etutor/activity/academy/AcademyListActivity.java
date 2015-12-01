package net.dx.etutor.activity.academy;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.activity.fragment.academy.AgencyListFragment;
import net.dx.etutor.activity.fragment.academy.SchoolListFragment;
import net.dx.etutor.activity.search.SearchAgencyActivity;
import net.dx.etutor.activity.search.SearchSchoolActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

public class AcademyListActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {

	public static String TAG = "AcademyListActivity";
	private boolean isSchool=true;
	private static List<Fragment> list = new ArrayList<Fragment>();
	
	private RadioGroup mRadioGroup;
	private RadioButton mTitleSchool;
	private RadioButton mTitleAgency;

	private FragmentManager fm;
	private MyAdapter adapter;
	private ViewPager mViewPager;
	

	private Bundle schoolBundle = new Bundle();
	private Bundle agencyBundle = new Bundle();
	
	private SchoolListFragment schoolListFragment;
	private AgencyListFragment agencyListFragment;
	

	@Override
	public void initViews() {
		setContentView(R.layout.activity_academy_list);
		setTitle("");
		setIcon(R.drawable.main_head_bar_icon_search_selector);
		fm = getSupportFragmentManager();
		mRadioGroup = (RadioGroup) findViewById(R.id.rg_choose_title);
		mTitleSchool = (RadioButton) findViewById(R.id.rb_title_school);
		mTitleAgency = (RadioButton) findViewById(R.id.rb_title_agency);
		mViewPager = (ViewPager) findViewById(R.id.vp_academy);
		initViewPager();

	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		list.clear();
		if (null == schoolListFragment) {
			schoolListFragment = new SchoolListFragment(mApplication,
					AcademyListActivity.this, this);
		}
		if (null == agencyListFragment) {
			agencyListFragment = new AgencyListFragment(mApplication,
					AcademyListActivity.this, this);
		}
		list.add(schoolListFragment);
		list.add(agencyListFragment);

		if (null == adapter) {
			adapter = new MyAdapter(fm);
		}
		mViewPager.setAdapter(adapter);
		if (isSchool) {
			mViewPager.setCurrentItem(0);
		}else {
			mViewPager.setCurrentItem(1);
		}
		mViewPager.setOffscreenPageLimit(0);
	}

	@Override
	public void initEvents() {
		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void iconClick() {
		super.iconClick();
		Intent intent;
		if (isSchool) {
			intent = new Intent(AcademyListActivity.this,
					SearchSchoolActivity.class);
			intent.putExtras(schoolBundle);
			startActivityForResultByPendingTransition(intent,100);
		}else {
			intent = new Intent(AcademyListActivity.this,
					SearchAgencyActivity.class);
			intent.putExtras(agencyBundle);
			startActivityForResultByPendingTransition(intent,101);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 100:
				schoolBundle = data.getExtras();
				schoolListFragment.onActivityResult(requestCode, resultCode, data);
				break;
			case 101:
				agencyBundle = data.getExtras();
				agencyListFragment.onActivityResult(requestCode, resultCode, data);
				break;
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int item = 0;
		switch (checkedId) {
		case R.id.rb_title_school:
			item = 0;
			isSchool=true;
			break;
		case R.id.rb_title_agency:
			item = 1;
			isSchool=false;
			break;
		}
		mViewPager.setCurrentItem(item, true);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			mTitleSchool.setChecked(true);
			isSchool=true;
			break;
		case 1:
			mTitleAgency.setChecked(true);
			isSchool=false;
			break;

		default:
			mTitleSchool.setChecked(true);
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
