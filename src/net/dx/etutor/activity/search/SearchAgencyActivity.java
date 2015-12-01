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

public class SearchAgencyActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	public static String TAG = "SearchAgencyActivity";
	private TextView mProvince;
	private TextView mCity;
	private TextView mRegion;
	private EditText mAgencyName;
	private Button mSubmit;
	private RadioGroup mDistance;
	private String name;
	private String province;
	private String city;
	private String region;
	private String distance;
	private String classify;
	private int indexDistance;
	private TextView mClassify;

	@Override
	public void initViews() {
		initData();
		setContentView(R.layout.activity_search_filtrate_agency);
		setTitle(R.string.text_teacher_need_filter_detail);

		mSubmit = (Button) findViewById(R.id.btn_filtrate_complete);
		mAgencyName = (EditText) findViewById(R.id.et_agency_name);
		mProvince = (TextView) findViewById(R.id.tvbtn_province);
		mCity = (TextView) findViewById(R.id.tvbtn_city);
		mRegion = (TextView) findViewById(R.id.tvbtn_region);
		mClassify = (TextView) findViewById(R.id.tvbtn_agency_classify);
		mDistance = (RadioGroup) findViewById(R.id.rg_distance);

		((CompoundButton) mDistance.getChildAt(indexDistance)).setChecked(true);
		mAgencyName.setText(name);
		mProvince.setText(province);
		mCity.setText(city);
		mRegion.setText(region);
		mClassify.setText(classify);

	}

	private void initData() {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.clear();
		Bundle savedInstanceState = getIntent().getExtras();
		if (null != savedInstanceState) {
			name = savedInstanceState.getString("name");
			province = savedInstanceState.getString("province");
			if (TextUtils.isEmpty(province)) {
				province = mApplication.getAppProvince();
			}
			city = savedInstanceState.getString("city");
			if (TextUtils.isEmpty(city)) {
				city = mApplication.getAppCity();
			}
			region = savedInstanceState.getString("region");
			if (TextUtils.isEmpty(region)) {
				region = mApplication.getAppRegion();
			}
			classify = savedInstanceState.getString("classify");
			if (TextUtils.isEmpty(classify)) {
				classify = mApplication.getSpUtil().getClassify();
			}
			indexDistance = savedInstanceState.getInt("indexDistance", 0);
		}
	}

	@Override
	public void initEvents() {

		mSubmit.setOnClickListener(this);

		mProvince.setOnClickListener(this);
		mCity.setOnClickListener(this);
		mRegion.setOnClickListener(this);
		mClassify.setOnClickListener(this);

		mDistance.setOnCheckedChangeListener(this);

		EmojiFilter.checkEmoji(SearchAgencyActivity.this, mAgencyName, null,
				100);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onCheckedChanged(SearchAgencyActivity.this,
				group, checkedId);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		SelectAreaSubjectUtil.onClick(SearchAgencyActivity.this, v, 2);
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
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putString("province", province);
		bundle.putString("city", city);
		bundle.putString("region", region);
		bundle.putInt("indexDistance", indexDistance);
		bundle.putString("classify", classify);
		Intent intent = new Intent(SearchAgencyActivity.this,
				AcademyListActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("name", name);
		intent.putExtra("province", province);
		intent.putExtra("city", city);
		intent.putExtra("region", region);
		intent.putExtra("classify", classify);
		intent.putExtra("distance", distance);
		setResultByPendingTransition(RESULT_OK, intent);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		distance = null;
		name = null;
	}

	/**
	 * 获取页面数据
	 */
	private void onGetData() {

		name = mAgencyName.getText().toString().trim();
		name = EmojiFilter.filterEmoji(name);
		province = mProvince.getText().toString().trim();
		city = mCity.getText().toString().trim();
		region = mRegion.getText().toString().trim();
		classify = mClassify.getText().toString().trim();
		indexDistance = SelectAreaSubjectUtil.getDistanceIndex();
		switch (indexDistance) {
		case 0:
			distance = null;
			break;
		case 1:
			distance = 1000 + "";
			break;
		case 2:
			distance = 2000 + "";
			break;
		case 3:
			distance = 3000 + "";
			break;
		case 4:
			distance = 5000 + "";
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
