package net.dx.etutor.activity.search;

import net.dx.etutor.R;
import net.dx.etutor.activity.academy.AcademyListActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SelectAreaSubjectUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class SearchSchoolActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	public static String TAG = "SearchSchoolActivity";
	private TextView mProvince;
	private TextView mCity;
	private TextView mRegion;
	private EditText mSchoolName;
	private Button mSubmit;
	private RadioGroup mDistance;
	private RadioGroup mSchoolType;
	private RadioGroup mSchoolCategory;
	private String name;
	private String province;
	private String city;
	private String region;
	private String schoolType;
	private String schoolCategory;
	private String distance;
	private int indexDistance;
	private int indexType;
	private int indexCategory;

	@Override
	public void initViews() {
		initData();
		setContentView(R.layout.activity_search_filtrate_school);
		setTitle(R.string.text_teacher_need_filter_detail);
		mSubmit = (Button) findViewById(R.id.btn_filtrate_complete);
		mSchoolName = (EditText) findViewById(R.id.et_school_name);
		mProvince = (TextView) findViewById(R.id.tvbtn_province);
		mCity = (TextView) findViewById(R.id.tvbtn_city);
		mRegion = (TextView) findViewById(R.id.tvbtn_region);
		mDistance = (RadioGroup) findViewById(R.id.rg_distance);
		mSchoolType = (RadioGroup) findViewById(R.id.rg_school_type);
		mSchoolCategory = (RadioGroup) findViewById(R.id.rg_school_category);
		
		((CompoundButton) mDistance.getChildAt(indexDistance)).setChecked(true);
		((CompoundButton) mSchoolType.getChildAt(indexType)).setChecked(true);
		((CompoundButton) mSchoolCategory.getChildAt(indexCategory)).setChecked(true);

		mSchoolName.setText(name);
		mProvince.setText(province);
		mCity.setText(city);
		mRegion.setText(region);
	}

	private void initData() {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.clear();
		Bundle savedInstanceState = getIntent().getExtras();
		if (null!=savedInstanceState) {
			name=savedInstanceState.getString("name");
			province=savedInstanceState.getString("province");
			if (TextUtils.isEmpty(province)) {
				province=mApplication.getAppProvince();
			}
			city=savedInstanceState.getString("city");
			if (TextUtils.isEmpty(city)) {
				city=mApplication.getAppCity();
			}
			region=savedInstanceState.getString("region");
			if (TextUtils.isEmpty(region)) {
				region=mApplication.getAppRegion();
			}
			indexDistance=savedInstanceState.getInt("indexDistance", 0);
			indexType=savedInstanceState.getInt("indexType", 0);
			indexCategory=savedInstanceState.getInt("indexCategory", 0);
		}
	}

	@Override
	public void initEvents() {
		mSubmit.setOnClickListener(this);

		mProvince.setOnClickListener(this);
		mCity.setOnClickListener(this);
		mRegion.setOnClickListener(this);

		mDistance.setOnCheckedChangeListener(this);
		mSchoolType.setOnCheckedChangeListener(this);
		mSchoolCategory.setOnCheckedChangeListener(this);
		
		EmojiFilter.checkEmoji(SearchSchoolActivity.this, mSchoolName,
				null, 100);
		
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onCheckedChanged(SearchSchoolActivity.this,
				group, checkedId);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// 处理省市区选择的onClick事件
		SelectAreaSubjectUtil.onClick(SearchSchoolActivity.this, v, 3);

		switch (v.getId()) {

		case R.id.btn_filtrate_complete:
			onSumbit();
			break;

		default:
			break;
		}

	}

	private void onSumbit() {
		// TODO Auto-generated method stub
		onGetData();

		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		Bundle bundle=new Bundle();
		bundle.putString("name", name);
		bundle.putString("province", province);
		bundle.putString("city", city);
		bundle.putString("region", region);
		bundle.putInt("indexDistance", indexDistance);
		bundle.putInt("indexCategory", indexCategory);
		bundle.putInt("indexType", indexType);
		Intent intent = new Intent(SearchSchoolActivity.this,
				AcademyListActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("name", name);
		intent.putExtra("province", province);
		intent.putExtra("city", city);
		intent.putExtra("region", region);
		intent.putExtra("distance", distance);
		intent.putExtra("category", schoolCategory);
		intent.putExtra("type", schoolType);
		setResultByPendingTransition(RESULT_OK, intent);
		this.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		distance = null;
		name = null;
		schoolCategory = null;
		schoolType = null;
	}
	/**
	 * 获取页面数据
	 */
	private void onGetData() {

		name = mSchoolName.getText().toString().trim();
		name = EmojiFilter.filterEmoji(name);
		province = mProvince.getText().toString().trim();
		city = mCity.getText().toString().trim();
		region = mRegion.getText().toString().trim();
		indexDistance = SelectAreaSubjectUtil.getDistanceIndex();
		schoolCategory = SelectAreaSubjectUtil.getSchoolCategoty();
		indexCategory=SelectAreaSubjectUtil.getSchoolCategotyIndex();
		schoolType = SelectAreaSubjectUtil.getSchoolType();
		indexType=SelectAreaSubjectUtil.getSchoolTypeIndex();
		switch (indexDistance) {
		case 0:
			distance=null;
			break;
		case 1:
			distance=1000+"";
			break;
		case 2:
			distance=2000+"";
			break;
		case 3:
			distance=3000+"";
			break;
		case 4:
			distance=5000+"";
			break;
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
