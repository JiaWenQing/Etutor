package net.dx.etutor.activity.search;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.teacher.NeedStudentListActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.util.SelectAreaSubjectUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;

public class SearchStudentNeedActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	public static String TAG = "SearchStudentNeedActivity";
	private TextView mEducationalType;
	private TextView mSubject;
	private Button mSubmit;
	private RadioGroup mDistance;
	private String subject;
	private String distance;
	private String category;
	private int indexDistance;

	@Override
	public void initViews() {
		Bundle savedInstanceState = getIntent().getExtras();
		if (null != savedInstanceState) {
			category = savedInstanceState.getString("category");
			subject = savedInstanceState.getString("subject");
			if (TextUtils.isEmpty(category)) {
				category = "请选择";
			}
			if (TextUtils.isEmpty(subject)) {
				subject = "请选择";
			}
			indexDistance = savedInstanceState.getInt("indexDistance", 0);
		}
		setContentView(R.layout.activity_search_filtrate_need);
		setTitle(R.string.text_teacher_need_filter_detail);
		SelectAreaSubjectUtil.clear();

		mSubmit = (Button) findViewById(R.id.btn_filtrate_complete);
		mEducationalType = (TextView) findViewById(R.id.tvbtn_education_type);
		mSubject = (TextView) findViewById(R.id.tvbtn_subject);

		mDistance = (RadioGroup) findViewById(R.id.rg_distance);
		((CompoundButton) mDistance.getChildAt(indexDistance)).setChecked(true);
		mEducationalType.setText(category);
		mSubject.setText(subject);

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSubmit.setOnClickListener(this);
		mEducationalType.setOnClickListener(this);
		mSubject.setOnClickListener(this);

		mDistance.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onCheckedChanged(SearchStudentNeedActivity.this,
				group, checkedId);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onClick(SearchStudentNeedActivity.this, v, 0);
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

		if (subject.equals("请选择")) {
			subject = null;
		}
		Bundle bundle = new Bundle();
		bundle.putString("subject", subject);
		bundle.putString("category", category);
		bundle.putInt("indexDistance", indexDistance);
		Intent intent = new Intent(SearchStudentNeedActivity.this,
				NeedStudentListActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("subject", subject);
		intent.putExtra("distance", distance);
		intent.putExtra("latitude", mApplication.getLatitude());
		intent.putExtra("longitude", mApplication.getLongitude());
		setResult(RESULT_OK, intent);
		finish();

	}

	/**
	 * 获取页面数据
	 */
	private void onGetData() {

		category = mEducationalType.getText().toString().trim();
		subject = mSubject.getText().toString().trim();
		indexDistance = SelectAreaSubjectUtil.getDistanceIndex();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		distance = null;
		subject = null;
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
