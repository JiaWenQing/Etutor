package net.dx.etutor.activity.search;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.teacher.TeacherListActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.util.NetWorkHelperUtil;
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

public class SearchTeacherActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	public static String TAG = "SearchTeacherActivity";
	private TextView mProvince;
	private TextView mCity;
	private TextView mRegion;
	private TextView mCategory;
	private TextView mSubject;
	private Button mSubmit;
	private RadioGroup mDistance;
	private RadioGroup mLectureType;
	private String subject;
	private String category;
	private String province;
	private String city;
	private String region;
	private String lectureType;
	private String distance;
	private int indexDistance;
	private int indexType;

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
			indexDistance = savedInstanceState.getInt("indexDistance", 0);
			indexType = savedInstanceState.getInt("indexType", 0);
		}
		setContentView(R.layout.activity_search_filtrate_teacher);
		setTitle(R.string.text_teacher_need_filter_detail);
		SelectAreaSubjectUtil.clear();
		mSubmit = (Button) findViewById(R.id.btn_filtrate_complete);

		mProvince = (TextView) findViewById(R.id.tvbtn_province);
		mCity = (TextView) findViewById(R.id.tvbtn_city);
		mRegion = (TextView) findViewById(R.id.tvbtn_region);
		mCategory = (TextView) findViewById(R.id.tvbtn_education_type);
		mSubject = (TextView) findViewById(R.id.tvbtn_subject);

		mDistance = (RadioGroup) findViewById(R.id.rg_distance);
		mLectureType = (RadioGroup) findViewById(R.id.rg_lecture_type);

		((CompoundButton) mDistance.getChildAt(indexDistance)).setChecked(true);
		((CompoundButton) mLectureType.getChildAt(indexType)).setChecked(true);
		mCategory.setText(category);
		mSubject.setText(subject);
		mProvince.setText(province);
		mCity.setText(city);
		mRegion.setText(region);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSubmit.setOnClickListener(this);

		mProvince.setOnClickListener(this);
		mCity.setOnClickListener(this);
		mRegion.setOnClickListener(this);
		mCategory.setOnClickListener(this);
		mSubject.setOnClickListener(this);

		mDistance.setOnCheckedChangeListener(this);
		mLectureType.setOnCheckedChangeListener(this);


	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onCheckedChanged(SearchTeacherActivity.this,
				group, checkedId);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 处理科目和省市区选择的onClick事件
		SelectAreaSubjectUtil.onClick(SearchTeacherActivity.this, v, 1);

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
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (onCheckInfo()) {
			if (subject.equals("请选择")) {
				subject = null;
			}
			MobclickAgent.onEvent(this, "Subject", subject);
			// mApplication.getMyDatabase().getId(region, false);
			Bundle bundle = new Bundle();
			bundle.putString("subject", subject);
			bundle.putString("category", category);
			bundle.putString("province", province);
			bundle.putString("city", city);
			bundle.putString("region", region);
			bundle.putInt("indexDistance", indexDistance);
			bundle.putInt("indexType", indexType);
			Intent intent = new Intent(SearchTeacherActivity.this,
					TeacherListActivity.class);
			intent.putExtras(bundle);
			intent.putExtra("subject", subject);
			intent.putExtra("lectureType", lectureType);
			intent.putExtra("province", province);
			intent.putExtra("city", city);
			intent.putExtra("region", region);
			intent.putExtra("distance", distance);
			intent.putExtra("latitude", mApplication.getLatitude());
			intent.putExtra("longitude", mApplication.getLongitude());
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	/**
	 * 检查数据是否符合要求
	 */
	private boolean onCheckInfo() {
		// TODO Auto-generated method stub
		category = mCategory.getText().toString().trim();
		subject = mSubject.getText().toString().trim();
		province = mProvince.getText().toString().trim();
		city = mCity.getText().toString().trim();
		region = mRegion.getText().toString().trim();

		lectureType = SelectAreaSubjectUtil.getLectureType();
		indexType = SelectAreaSubjectUtil.getLectureTypeIndex();
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
		return true;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		lectureType = null;
		distance = null;
		super.onDestroy();
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
