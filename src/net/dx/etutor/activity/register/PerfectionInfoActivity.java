package net.dx.etutor.activity.register;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.activity.teacher.TeacherSettingCourseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ChoicePicDialog;
import net.dx.etutor.dialog.EducationDialog;
import net.dx.etutor.dialog.EducationDialog.EducationDialogCallBack;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.popupwindow.TeachAgePopupWindow;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PerfectionInfoActivity extends BaseActivity implements
		OnClickListener, OnWheelClickListener {
	
	public static String TAG = "PerfectionInfoActivity";
	private ImageButton iTeachingAge;
	private ImageButton iGraduateTime;
	private ImageButton iEducation;
	private ImageButton iTeacherBirthday;
	private TextView tEducation;
	private TextView tGraguateTime;
	private TextView tTeacherBirthday;
	private TextView mTeacherAge;
	private TextView mNext;
	private EditText eMajor;
	private EditText eSchool;

	private String tBirthday;
	private String school;
	private String education;
	private String tMajor;
	private String graguateTime;
	private String coachTime;

	private LinearLayout mTeacherAgeLayout;
	private LinearLayout mTeacherbirthdayLayout;
	private LinearLayout mGraduatetimeLayout;
	private LinearLayout mEducationdegreeLayout;

	private DxUsers users = new DxUsers();

	private int mYear;
	private int mMonth;
	private int mDay;
	public String filePath = "";
	public String imagePath;
	public String userName;
	private int target;
	private int type;
	private TextView mStatus;
	private long maxTime;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_perfection_info);
		setTitle(R.string.text_release_personal_info);
		showIcon(0, "下一步");
		iGraduateTime = (ImageButton) findViewById(R.id.imb_graduate_time);
		iEducation = (ImageButton) findViewById(R.id.imb_teacher_education_degree);
		iTeacherBirthday = (ImageButton) findViewById(R.id.imb_teacher_birthday);
		iTeachingAge = (ImageButton) findViewById(R.id.imb_teaching_age);
		tEducation = (TextView) findViewById(R.id.tv_education_degree);
		tTeacherBirthday = (TextView) findViewById(R.id.tv_teacher_birthday);
		tGraguateTime = (TextView) findViewById(R.id.tv_graduate_time);
		mTeacherAge = (TextView) findViewById(R.id.tv_teaching_age);
		mStatus = (TextView) findViewById(R.id.tv_status);
		eSchool = (EditText) findViewById(R.id.et_graduate_school);
		eMajor = (EditText) findViewById(R.id.et_teacher_major);

		mTeacherAgeLayout = (LinearLayout) findViewById(R.id.layout_teacher_age);
		mTeacherbirthdayLayout = (LinearLayout) findViewById(R.id.layout_teacher_birthday);
		mGraduatetimeLayout = (LinearLayout) findViewById(R.id.layout_graduate_time);
		mEducationdegreeLayout = (LinearLayout) findViewById(R.id.layout_education_degree);

		final Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		maxTime = System.currentTimeMillis();
	}

	@Override
	public void initEvents() {
		iTeacherBirthday.setOnClickListener(this);
		iGraduateTime.setOnClickListener(this);
		iEducation.setOnClickListener(this);
		iTeachingAge.setOnClickListener(this);

		mTeacherAgeLayout.setOnClickListener(this);
		mTeacherbirthdayLayout.setOnClickListener(this);
		mGraduatetimeLayout.setOnClickListener(this);
		mEducationdegreeLayout.setOnClickListener(this);

		EmojiFilter.checkEmoji(PerfectionInfoActivity.this, eMajor, null, 100);
		EmojiFilter.checkEmoji(PerfectionInfoActivity.this, eSchool, null, 100);

	}

	@Override
	public void iconClick() {
		// TODO Auto-generated method stub
		onSubmit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imb_teaching_age:
		case R.id.teacher_age:
			TeachAgePopupWindow agePop1 = new TeachAgePopupWindow(this,
					Constants.NEED_AGE);
			agePop1.setOnWheelClickListener(this);
			agePop1.showAtLocation(findViewById(R.id.search_teacher),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.imb_teacher_education_degree:
		case R.id.layout_education_degree:
			onEducation();
			break;
		case R.id.imb_graduate_time:
		case R.id.layout_graduate_time:
			onGraduateTime();
			break;
		case R.id.imb_teacher_birthday:
		case R.id.layout_teacher_birthday:
			onTeacherBirthday();
			break;
		default:
			break;
		}

	}

	/**
	 * 教师生日选择
	 */
	private void onTeacherBirthday() {
		// TODO Auto-generated method stub
		DatePickerDialog tDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						String birthday = DateUtil.getBirthday(year,
								monthOfYear, dayOfMonth);
						tTeacherBirthday.setText(birthday);
					}
				}, mYear, mMonth, mDay);
		DatePicker datePicker = tDialog.getDatePicker();
		datePicker.setMaxDate(maxTime);
		tDialog.show();
	}

	/**
	 * 毕业时间选择
	 */
	private void onGraduateTime() {
		// TODO Auto-generated method stub
		DatePickerDialog gDialog = new DatePickerDialog(this,
				new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						String birthday = DateUtil.getBirthday(year,
								monthOfYear, dayOfMonth);
						tGraguateTime.setText(birthday);
					}
				}, mYear, mMonth, mDay);
		DatePicker datePicker = gDialog.getDatePicker();
		datePicker.setMaxDate(maxTime);
		gDialog.show();
	}

	/**
	 * 教师学历选择
	 */
	private void onEducation() {
		// TODO Auto-generated method stub
		EducationDialog dialog = new EducationDialog(this);

		dialog.setEducationDialogCallBack(new EducationDialogCallBack() {

			@Override
			public void setBackValue(Dialog dialog, String value) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				tEducation.setText(value);
			}
		});
		dialog.show();
	}

	public void getRegisterData() {
		school = eSchool.getText().toString().trim();
		tMajor = eMajor.getText().toString().trim();
		school = EmojiFilter.filterEmoji(school);
		tMajor = EmojiFilter.filterEmoji(tMajor);
		graguateTime = tGraguateTime.getText().toString().trim();
		tBirthday = tTeacherBirthday.getText().toString().trim();
		education = tEducation.getText().toString().trim();
	}

	private boolean onCheck() {
		getRegisterData();
		if (TextUtils.isEmpty(coachTime)) {
			showShortToast("辅导教龄不能为空！");
			return false;
		}
		return true;
	}

	private void onSubmit() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (onCheck()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("school", school);
			map.put("education", education);
			map.put("major", tMajor);
			map.put("tBirthday", tBirthday);
			map.put("graduateTime", graguateTime);
			map.put("coachTime", coachTime);
			map.put("userType", 1);
			showLoadingDialog("请稍后……");
			String urlString = UrlEngine.completeTeacherInfo(map);
			HttpUtil.post(urlString, new JsonHttpResponseHandler() {
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (statusCode == Constants.STAT_200) {
						users.initWithAttributes(response);
						EtutorApplication.getInstance().getSpUtil()
								.setToken(users.getToken());
						mApplication.getSpUtil().setUserType(1);
						showShortToast("信息完善成功！");
						dismissLoadingDialog();
						Intent intent = new Intent(PerfectionInfoActivity.this,
								TeacherSettingCourseActivity.class);
						startActivityByPendingTransition(intent);
						PerfectionInfoActivity.this.finish();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
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
	}

	@Override
	public void wheelOnClick(String value, int type) {
		// TODO Auto-generated method stub
		coachTime = value;
		mTeacherAge.setText(value);
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
