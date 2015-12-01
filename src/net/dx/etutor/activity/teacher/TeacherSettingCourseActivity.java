package net.dx.etutor.activity.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxTeacherclass;
import net.dx.etutor.model.DxTeachercourse;
import net.dx.etutor.popupwindow.LecturePopupWindow;
import net.dx.etutor.popupwindow.SubjectPopupWindow;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SelectAreaSubjectUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class TeacherSettingCourseActivity extends BaseActivity implements
		OnClickListener, OnWheelClickListener {

	public static String TAG = "TeacherSettingCourseActivity";
	private Button mSumbit;
	private TextView mSubject1;
	private TextView mSubject2;
	private TextView mSubject3;
	private TextView mProvince;
	private TextView mCity;
	private TextView mRegion;
	private TextView mSubject;
	private TextView mStartTime;
	private EditText mFees;
	private EditText mAddress;
	private EditText mIntro;
	private EditText mTitle;
	private EditText mClassIntro;
	private EditText mClassNumber;
	private EditText mOriginal;
	private RadioButton mListenY;
	private RadioButton mListenN;
	private RadioButton mShield1Y;
	private RadioButton mShield1N;
	private RadioButton mShield2Y;
	private RadioButton mShield2N;
	private RadioButton mModel1;
	private RadioButton mModel2;

	private CheckBox mCheckBox1;
	private CheckBox mCheckBox2;
	private CheckBox mCheckBox3;
	private CheckBox mCheckBox4;
	private CheckBox mCheckBox5;
	private CheckBox mCheckBox6;
	private CheckBox mCheckBox7;
	private CheckBox mCheckBox8;
	private CheckBox mCheckBox9;
	private CheckBox mCheckBox10;
	private CheckBox mCheckBox11;
	private CheckBox mCheckBox12;
	private CheckBox mCheckBox13;
	private CheckBox mCheckBox14;
	private CheckBox mCheckBox15;
	private CheckBox mCheckBox16;
	private CheckBox mCheckBox17;
	private CheckBox mCheckBox18;
	private CheckBox mCheckBox19;
	private CheckBox mCheckBox20;
	private CheckBox mCheckBox21;
	private CheckBox mCheckBox[] = { mCheckBox1, mCheckBox2, mCheckBox3,
			mCheckBox4, mCheckBox5, mCheckBox6, mCheckBox7, mCheckBox8,
			mCheckBox9, mCheckBox10, mCheckBox11, mCheckBox12, mCheckBox13,
			mCheckBox14, mCheckBox15, mCheckBox16, mCheckBox17, mCheckBox18,
			mCheckBox19, mCheckBox20, mCheckBox21 };
	private TextView mLectureType;
	private int item = 0;
	private String title;// 小班标题
	private String subject;// 小班科目
	private String classNumber;
	private String classIntro;
	private String startTime;

	private String subject1;
	private String subject2;
	private String subject3;
	private String shield1;
	private String shield2;
	private String listenInfo = "不可以";
	private int courseTime;
	private String province;
	private String city;
	private String region;
	private String teacherIntro;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int total = 100;
	private String userId;

	private static List<View> list = new ArrayList<View>();
	private LayoutInflater inflater;
	private ViewPager mViewPager;
	private View mModel1View, mModel2View;
	private MyAdapter adapter;
	private DxTeachercourse dxTeachercourse = new DxTeachercourse();
	private DxTeacherclass dxTeacherclass = new DxTeacherclass();
	private TextView mSurplusNumber1, mSurplusNumber2;
	private ImageView mImageShield1,mImageShield2;
	private LinearLayout mLayout1,mLayout2;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_set_course_info);
		setTitle(R.string.text_release_personal_info);
		mProvince = (TextView) findViewById(R.id.tvbtn_province);
		mCity = (TextView) findViewById(R.id.tvbtn_city);
		mRegion = (TextView) findViewById(R.id.tvbtn_region);
		mListenY = (RadioButton) findViewById(R.id.rb_listening_y);
		mListenN = (RadioButton) findViewById(R.id.rb_listening_n);
		SelectAreaSubjectUtil.clear();
		mViewPager = (ViewPager) findViewById(R.id.view_pager_set_course);
		mViewPager.setOffscreenPageLimit(1);
		inflater = getLayoutInflater();
		initViewPager();
		for (int i = 0; i < 21; i++) {
			mCheckBox[i] = (CheckBox) mModel1View.findViewById(R.id.rb_1_1 + i);
		}
		mSumbit = (Button) findViewById(R.id.btn_submit_set_course);

		mImageShield1=(ImageView) mModel1View.findViewById(R.id.imv_shield1);
		mLayout1=(LinearLayout) mModel1View.findViewById(R.id.layout_shield1);
		mSubject1 = (TextView) mModel1View.findViewById(R.id.tvbtn_subject1);
		mSubject2 = (TextView) mModel1View.findViewById(R.id.tvbtn_subject2);
		mSubject3 = (TextView) mModel1View.findViewById(R.id.tvbtn_subject3);
		mFees = (EditText) mModel1View.findViewById(R.id.et_class_fees);
		mShield1Y = (RadioButton) mModel1View
				.findViewById(R.id.rb_shield1_y);
		mShield1N = (RadioButton) mModel1View
				.findViewById(R.id.rb_shield1_n);
		mLectureType = (TextView) mModel1View
				.findViewById(R.id.tvbtn_lecture_mode1);
		mIntro = (EditText) mModel1View.findViewById(R.id.et_introduce);
		mSurplusNumber1 = (TextView) mModel1View
				.findViewById(R.id.tv_intro_surplus_number1);
		
		
		mImageShield2=(ImageView) mModel2View.findViewById(R.id.imv_shield2);
		mLayout2=(LinearLayout) mModel2View.findViewById(R.id.layout_shield2);
		mTitle = (EditText) mModel2View.findViewById(R.id.et_class_title);
		mSubject = (TextView) mModel2View
				.findViewById(R.id.tvbtn_class_subject);
		mClassIntro = (EditText) mModel2View
				.findViewById(R.id.et_class_introduce);
		mShield2Y = (RadioButton) mModel2View
				.findViewById(R.id.rb_shield2_y);
		mShield2N = (RadioButton) mModel2View
				.findViewById(R.id.rb_shield2_n);
		mClassNumber = (EditText) mModel2View
				.findViewById(R.id.et_student_number);
		mOriginal = (EditText) mModel2View.findViewById(R.id.et_original_price);
		mStartTime = (TextView) mModel2View
				.findViewById(R.id.tvbtn_class_start_time);
		mSurplusNumber2 = (TextView) mModel2View
				.findViewById(R.id.tv_class_surplus_number2);

		mModel1 = (RadioButton) findViewById(R.id.rb_model_1);
		mModel2 = (RadioButton) findViewById(R.id.rb_model_2);

		final Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		showLoadingDialog("请稍后...");
		initCourseData();
		initClassData();
	}

	private void initCourseData() {
		String url = UrlEngine.getCourseDetail(userId);
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.toString().equals(Constants.LOGIN_ERROR)) {
							dismissLoadingDialog();
						} else {
							dxTeachercourse.initWithAttributes(response);
							setData();
							dismissLoadingDialog();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	private void initClassData() {
		String url = UrlEngine.getClassDetail(userId);
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.toString().equals(Constants.LOGIN_ERROR)) {
							dismissLoadingDialog();
						} else {
							dxTeacherclass.initWithAttributes(response);
							initSmallClass();
							dismissLoadingDialog();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	/**
	 * 初始话一对一数据
	 */
	public void setData() {
		shield1=dxTeachercourse.getShield();
		if (!TextUtils.isEmpty(shield1)&&shield1.equals("屏蔽")) {
			mShield1Y.setChecked(true);
			mImageShield1.setVisibility(View.VISIBLE);
			mLayout1.setBackgroundResource(R.color.bg_shield);
		}else {
			mShield1N.setChecked(true);
			mImageShield1.setVisibility(View.GONE);
			mLayout1.setBackgroundResource(R.color.text_white);
		}
		courseTime = dxTeachercourse.getSubtime();
		mSubject1.setText(dxTeachercourse.getSubjectItemId1().toString());
		mSubject2.setText(dxTeachercourse.getSubjectItemId2().toString());
		mSubject3.setText(dxTeachercourse.getSubjectItemId3().toString());
		if (dxTeachercourse.getPrice() == 0) {
			mFees.setText("");
		} else {
			mFees.setText(dxTeachercourse.getPrice().toString());
		}
		if (dxTeachercourse.getListenTest().equals(mListenY.getText())) {
			mListenY.setChecked(true);
			mListenN.setChecked(false);
		} else {
			mListenN.setChecked(true);
			mListenY.setChecked(false);
		}
		mLectureType.setText(dxTeachercourse.getLectureType().toString());
		for (int i = 0; i < 21; i++) {
			if ((courseTime & (1 << i)) != 0) {
				mCheckBox[i].setChecked(true);
			} else {
				mCheckBox[i].setChecked(false);
			}
		}
		mIntro.setText(dxTeachercourse.getIntroduce().toString());
		mSurplusNumber1.setText(100 - dxTeachercourse.getIntroduce().length()
				+ "");
		province = dxTeachercourse.getArea().getProvince();
		city = dxTeachercourse.getArea().getCity();
		region = dxTeachercourse.getArea().getRegion();
		if (TextUtils.isEmpty(province)) {
			province = mApplication.getAppProvince();
		}
		if (TextUtils.isEmpty(city)) {
			city = mApplication.getAppCity();
		}
		if (TextUtils.isEmpty(region)) {
			region = mApplication.getAppRegion();
		}
		mProvince.setText(province);
		mCity.setText(city);
		mRegion.setText(region);
	}

	/**
	 * 初始化小班数据
	 */
	public void initSmallClass() {
		shield2=dxTeacherclass.getShield();
		if (!TextUtils.isEmpty(shield2)&&shield2.equals("屏蔽")) {
			mShield2Y.setChecked(true);
			mImageShield2.setVisibility(View.VISIBLE);
			mLayout2.setBackgroundResource(R.color.bg_shield);
		}else {
			mShield2N.setChecked(true);
			mImageShield2.setVisibility(View.GONE);
			mLayout2.setBackgroundResource(R.color.text_white);
		}
		
		mTitle.setText(dxTeacherclass.getTitle().toString());
		mSubject.setText(dxTeacherclass.getSubjectItemId().toString());

		province = dxTeacherclass.getArea().getProvince();
		city = dxTeacherclass.getArea().getCity();
		region = dxTeacherclass.getArea().getRegion();
		mProvince.setText(province);
		mCity.setText(city);
		mRegion.setText(region);

		mClassIntro.setText(dxTeacherclass.getIntroduce().toString());
		mSurplusNumber2.setText(100 - dxTeacherclass.getIntroduce().length()
				+ "");
		mClassNumber.setText(dxTeacherclass.getClassSize().toString());
		mOriginal.setText(dxTeacherclass.getPrice().toString());
		mStartTime.setText(dxTeacherclass.getBeginTime().toString());
	}

	/**
	 * 初始化ViewPager
	 */
	public void initViewPager() {
		mModel1View = inflater.inflate(R.layout.fragment_model_1to1, null);
		mModel2View = inflater.inflate(R.layout.fragment_model_class, null);
		list.add(mModel1View);
		list.add(mModel2View);
		adapter = new MyAdapter(list);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void initEvents() {

		mModel1.setOnClickListener(this);
		mModel2.setOnClickListener(this);
		mListenY.setOnClickListener(this);
		mListenN.setOnClickListener(this);
		mShield2Y.setOnClickListener(this);
		mShield2N.setOnClickListener(this);
		mShield1Y.setOnClickListener(this);
		mShield1N.setOnClickListener(this);

		mProvince.setOnClickListener(this);
		mCity.setOnClickListener(this);
		mRegion.setOnClickListener(this);

		mLectureType.setOnClickListener(this);
		mSubject1.setOnClickListener(this);
		mSubject2.setOnClickListener(this);
		mSubject3.setOnClickListener(this);

		mSubject.setOnClickListener(this);
		mStartTime.setOnClickListener(this);

		EmojiFilter.checkEmoji(TeacherSettingCourseActivity.this, mIntro,
				mSurplusNumber1, total);
		EmojiFilter.checkEmoji(TeacherSettingCourseActivity.this, mTitle, null,
				total);
		EmojiFilter.checkEmoji(TeacherSettingCourseActivity.this, mClassIntro,
				mSurplusNumber2, total);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					mModel1.setChecked(true);
					break;
				case 1:
					mModel2.setChecked(true);
					break;
				default:
					mModel1.setChecked(true);
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		mSumbit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SelectAreaSubjectUtil.onClick(TeacherSettingCourseActivity.this, v, 4);
		switch (v.getId()) {
		case R.id.tvbtn_class_subject:
			SubjectPopupWindow subjectPop = new SubjectPopupWindow(this,
					getScreenHeight() / 4, Constants.NEED_SUBJECT);
			subjectPop.setOnWheelClickListener(this);
			subjectPop.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.tvbtn_subject1:
			SubjectPopupWindow subjectPop1 = new SubjectPopupWindow(this,
					getScreenHeight() / 4, Constants.NEED_SUBJECT1);
			subjectPop1.setOnWheelClickListener(this);
			subjectPop1.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.tvbtn_subject2:
			if (TextUtils.isEmpty(mSubject1.getText().toString())) {
				showShortToast("请先选择前面的科目!");
				return;
			}
			SubjectPopupWindow subjectPop2 = new SubjectPopupWindow(this,
					getScreenHeight() / 4, Constants.NEED_SUBJECT2);
			subjectPop2.setOnWheelClickListener(this);
			subjectPop2.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.tvbtn_subject3:
			if (TextUtils.isEmpty(mSubject2.getText().toString())) {
				showShortToast("请先选择前面的科目!");
				return;
			}
			SubjectPopupWindow subjectPop3 = new SubjectPopupWindow(this,
					getScreenHeight() / 4, Constants.NEED_SUBJECT3);
			subjectPop3.setOnWheelClickListener(this);
			subjectPop3.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.tvbtn_lecture_mode1:
			LecturePopupWindow letPop = new LecturePopupWindow(this,
					Constants.NEED_LECTURE);
			letPop.setOnWheelClickListener(this);
			letPop.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;

		case R.id.rb_model_1:
			item = 0;
			mViewPager.setCurrentItem(item, true);
			break;
		case R.id.rb_model_2:
			item = 1;
			mViewPager.setCurrentItem(item, true);
			break;
		case R.id.rb_listening_y:
			listenInfo = mListenY.getText().toString().trim();
			break;
		case R.id.rb_listening_n:
			listenInfo = mListenN.getText().toString().trim();
			break;
		case R.id.rb_shield1_y:
			shield1 = mShield1Y.getText().toString().trim();
			mImageShield1.setVisibility(View.VISIBLE);
			mLayout1.setBackgroundResource(R.color.bg_shield);
			break;
		case R.id.rb_shield1_n:
			shield1 = mShield1N.getText().toString().trim();
			mImageShield1.setVisibility(View.GONE);
			mLayout1.setBackgroundResource(R.color.text_white);
			break;
		case R.id.rb_shield2_y:
			shield2 = mShield2Y.getText().toString().trim();
			mImageShield2.setVisibility(View.VISIBLE);
			mLayout2.setBackgroundResource(R.color.bg_shield);
			break;
		case R.id.rb_shield2_n:
			shield2 = mShield2N.getText().toString().trim();
			mImageShield2.setVisibility(View.GONE);
			mLayout2.setBackgroundResource(R.color.text_white);
			break;
		case R.id.tvbtn_class_start_time:
			getStartTime();
			break;

		case R.id.btn_submit_set_course:
			onsumbit();
			break;

		default:
			break;
		}

	}

	/**
	 * 获取小班开课时间
	 */
	private void getStartTime() {
		// TODO Auto-generated method stub
		DatePickerDialog pDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						startTime = DateUtil.getBirthday(year, monthOfYear,
								dayOfMonth);
						mStartTime.setText(startTime);

					}
				}, mYear, mMonth, mDay);
		pDialog.show();
	}

	/**
	 * 完成课程信息设置
	 */
	private void onsumbit() {
		getDate();
		if (onCheckCourse()) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("teacherId", userId);
			map1.put("subjectItemId1", subject1);
			map1.put("subjectItemId2", subject2);
			map1.put("subjectItemId3", subject3);
			map1.put("price", mFees.getText().toString());
			map1.put("listenTest", listenInfo);
			map1.put("shield", shield1);
			map1.put("lectureType", mLectureType.getText().toString());
			map1.put("subtime", courseTime);

			map1.put("province", province);
			map1.put("city", city);
			map1.put("region", region);
			map1.put("introduce", teacherIntro);

			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("title", title);
			map2.put("subjectItemId", subject);
			map2.put("introduce", classIntro);
			map2.put("listenTest", listenInfo);
			map2.put("shield", shield2);
			map2.put("classSize", mClassNumber.getText().toString().trim());
			map2.put("price", mOriginal.getText().toString().trim());
			map2.put("beginTime", mStartTime.getText().toString().trim());
			map2.put("teacherId", userId);
			showLoadingDialog("请稍后...");
			insertCourse(map1);
			insertClass(map2);
		}
	}

	/**
	 * 获取数据
	 */
	private void getDate() {
		// TODO Auto-generated method stub
		title = mTitle.getText().toString().trim();
		classIntro = mClassIntro.getText().toString().trim();
		teacherIntro = mIntro.getText().toString().trim();
		title = EmojiFilter.filterEmoji(title);
		classIntro = EmojiFilter.filterEmoji(classIntro);
		teacherIntro = EmojiFilter.filterEmoji(teacherIntro);

		subject = mSubject.getText().toString().trim();
		classNumber = mClassNumber.getText().toString().trim();
		startTime = mStartTime.getText().toString().trim();

		subject1 = mSubject1.getText().toString().trim();
		subject2 = mSubject2.getText().toString().trim();
		subject3 = mSubject3.getText().toString().trim();
		courseTime = 0;
		for (int i = 0; i < 21; i++) {
			// TODO Auto-generated method stub
			if (mCheckBox[i].isChecked()) {
				courseTime = (courseTime ^ (1 << i));
			}
		}
		province = mProvince.getText().toString().trim();
		city = mCity.getText().toString().trim();
		region = mRegion.getText().toString().trim();
		teacherIntro = mIntro.getText().toString().trim();
	}

	public void insertCourse(Map<String, Object> map) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}

		String urlString = UrlEngine.setCourseInfo(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						EtutorApplication.getInstance().getSpUtil()
								.setToken(response.getString("token"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showLongToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	public void insertClass(Map<String, Object> map) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.setClassInfo(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						showShortToast("设置课程信息成功!");
						EtutorApplication.getInstance().getSpUtil()
								.setToken(response.getString("token"));
						dismissLoadingDialog();
						Intent intent = new Intent(
								TeacherSettingCourseActivity.this,
								TeacherListActivity.class);
						setResult(RESULT_OK, intent);
						TeacherSettingCourseActivity.this.finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showLongToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	/**
	 * 检查一对一模式数据是否符合要求
	 */
	private boolean onCheckCourse() {
		// TODO Auto-generated method stub
		boolean flag = true;
		if (!TextUtils.isEmpty(mSubject1.getText().toString())) {
			if (!TextUtils.isEmpty(mSubject2.getText().toString())) {
				if (!TextUtils.isEmpty(mSubject3.getText().toString())) {
					if (mSubject1.getText().toString()
							.equals(mSubject3.getText().toString())
							&& mSubject3.getText().toString()
									.equals(mSubject2.getText().toString())) {
						showShortToast("科目不能重复！请重新选择！");
						mSubject2.setText("");
						mSubject3.setText("");
						flag = false;
					} else if (mSubject1.getText().toString()
							.equals(mSubject3.getText().toString())
							|| mSubject3.getText().toString()
									.equals(mSubject2.getText().toString())) {
						showShortToast("科目不能重复！请重新选择！");
						mSubject3.setText("");
						flag = false;
					}
				}
				if (mSubject1.getText().toString()
						.equals(mSubject2.getText().toString())) {
					showShortToast("科目不能重复！请重新选择！");
					mSubject2.setText("");
					flag = false;
				}
			}
		}else {
			flag = false;
			showShortToast("请选择科目");
		}
		if ("请选择".equals(region)) {
			showShortToast("请选择授课区域");
			flag = false;
		}
		if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(subject)) {
			showShortToast("请选择小班科目");
			flag = false;
		}
		return flag;
	}


	@Override
	public void wheelOnClick(String value, int type) {
		// TODO Auto-generated method stub
		if ("请选择".equals(value)) {
			value = "";
		}
		subject1 = mSubject1.getText().toString().trim();
		subject2 = mSubject2.getText().toString().trim();
		subject3 = mSubject3.getText().toString().trim();
		switch (type) {
		case Constants.NEED_SUBJECT:
			mSubject.setText(value);
			break;
		case Constants.NEED_SUBJECT1:
			if (TextUtils.isEmpty(value)) {
				if (TextUtils.isEmpty(subject2)) {
					if (!TextUtils.isEmpty(subject3)) {
						value=subject3;
						mSubject3.setText("");
					}
				}else {
					value=subject2;
					mSubject2.setText(subject3);
					mSubject3.setText("");
				}
			}
			mSubject1.setText(value);
			break;
		case Constants.NEED_SUBJECT2:
			if (TextUtils.isEmpty(value)) {
				if (!TextUtils.isEmpty(subject3)) {
					value=subject3;
					mSubject3.setText("");
				}
			}
			mSubject2.setText(value);
			break;
		case Constants.NEED_SUBJECT3:
			mSubject3.setText(value);
			break;
		case Constants.NEED_LECTURE:
			mLectureType.setText(value);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		list.clear();
	}

	private class MyAdapter extends PagerAdapter {
		List<View> viewLists;

		public MyAdapter(List<View> lists) {
			viewLists = lists;
		}

		@Override
		public int getCount() { // 获得size
			// TODO Auto-generated method stub
			return viewLists.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object object) // 销毁Item
		{
			((ViewPager) view).removeView(viewLists.get(position));
		}

		@Override
		public Object instantiateItem(View view, int position) // 实例化Item
		{
			((ViewPager) view).addView(viewLists.get(position), 0);
			return viewLists.get(position);
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
