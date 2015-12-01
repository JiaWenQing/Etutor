package net.dx.etutor.activity.student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.popupwindow.LecturePopupWindow;
import net.dx.etutor.popupwindow.SubjectPopupWindow;
import net.dx.etutor.popupwindow.TeachModePopupWindow;
import net.dx.etutor.popupwindow.WheelPopupWindow;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class StudentNeedCreateActivity extends BaseActivity implements
		OnClickListener, OnWheelClickListener {
	
	public static String TAG = "StudentNeedCreateActivity";
	private ImageButton mSubject;
	private ImageButton mSex;
	private ImageButton mTeachMode;
	private ImageButton mLectureMode;

	private TextView mItemName;
	private TextView mTecaherSex;
	private TextView mTeachrMode;
	private TextView mLecturesMode;
	private TextView mWordsNumber;

	private EditText mIntroduce;
	private EditText mClassfees;
	private EditText mLectureCount;
	private EditText mNeedTitle;
	private Button mRelease;
	private Button mPreview;
	private int[] mCheckBoxId = new int[21];
	private int courseTime;
	private int needId = -1;
	private int total = 100;
	private DxNeed mDxNeed = new DxNeed();;
	private String type;
	private String title;
	private String subject;
	private String intro;
	private String price = "0";
	private String count;
	private String sex = "不限";
	private String teacherMode = "不限";
	private String lecturesMode = "不限";

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

	@Override
	public void initViews() {
		setContentView(R.layout.activity_create_student_need);
		type = getIntent().getStringExtra("type");
		for (int i = 0; i < 21; i++) {
			// mCheckBoxId[i] = R.id.rb_1_1 + i;
			mCheckBox[i] = (CheckBox) findViewById(R.id.rb_1_1 + i);
		}

		mSubject = (ImageButton) findViewById(R.id.imb_subject);
		mSex = (ImageButton) findViewById(R.id.imb_choose_teacher_sex);
		mTeachMode = (ImageButton) findViewById(R.id.imb_choose_teaching_mode);
		mLectureMode = (ImageButton) findViewById(R.id.imb_choose_lecture_mode);

		mNeedTitle = (EditText) findViewById(R.id.et_student_need_title);
		mItemName = (TextView) findViewById(R.id.tv_add_need_itemname);
		mTecaherSex = (TextView) findViewById(R.id.tv_add_teacher_sex);
		mTeachrMode = (TextView) findViewById(R.id.tv_add_teaching_mode);
		mLecturesMode = (TextView) findViewById(R.id.tv_add_lecture_mode);
		mWordsNumber = (TextView) findViewById(R.id.tv_total_words);

		mIntroduce = (EditText) findViewById(R.id.et_student_introduce);
		mClassfees = (EditText) findViewById(R.id.et_class_fees);
		mLectureCount = (EditText) findViewById(R.id.et_lecture_count);

		mRelease = (Button) findViewById(R.id.btn_student_need_release);
		mPreview = (Button) findViewById(R.id.btn_student_need_preview);

		for (int i = 0; i < 21; i++) {
			mCheckBoxId[i] = R.id.rb_1_1 + i;
		}
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		if (type.equals("update")) {
			mRelease.setText("修改");
			setTitle(R.string.text_update_need);
			mDxNeed = (DxNeed) getIntent().getSerializableExtra("dxNeed");
			needId = Integer.parseInt(mDxNeed.getNeedId());
			courseTime = Integer.parseInt(mDxNeed.getSubtime());
			title = mDxNeed.getNeedTitle();
			if (TextUtils.isEmpty(title)) {
				title="找家教";
			}
			subject = mDxNeed.getSubjectItemId();
			sex = mDxNeed.getTeacherSex();
			lecturesMode = mDxNeed.getLectureMode();
			teacherMode = mDxNeed.getTeachMode();
			intro = mDxNeed.getIntroduce();
			price = mDxNeed.getPrice()+"";
			count = mDxNeed.getTradeNumber().toString();

			mNeedTitle.setText(title);
			mItemName.setText(subject);
			mTecaherSex.setText(sex);
			mLecturesMode.setText(lecturesMode);
			mTeachrMode.setText(teacherMode);
			mIntroduce.setText(intro);
			mClassfees.setText(price);
			mLectureCount.setText(count);
			mWordsNumber.setText(100 - mDxNeed.getIntroduce().length() + "");
			for (int i = 0; i < 21; i++) {
				// TODO Auto-generated method stub
				if ((courseTime & (1 << i)) != 0) {
					mCheckBox[i].setChecked(true);
				} else {
					mCheckBox[i].setChecked(false);
				}
			}

		} else {
			mRelease.setText("发布");
			setTitle(R.string.text_create_need);
			mWordsNumber.setText("100");
		}
	}

	@Override
	public void initEvents() {
		mSubject.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mTeachMode.setOnClickListener(this);
		mLectureMode.setOnClickListener(this);
		mRelease.setOnClickListener(this);
		mPreview.setOnClickListener(this);
		mItemName.setOnClickListener(this);
		mTecaherSex.setOnClickListener(this);
		mTeachrMode.setOnClickListener(this);
		mLecturesMode.setOnClickListener(this);
		EmojiFilter.checkEmoji(StudentNeedCreateActivity.this, mIntroduce,
				mWordsNumber, 100);
		EmojiFilter.checkEmoji(StudentNeedCreateActivity.this, mNeedTitle,
				null, 100);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add_need_itemname:
		case R.id.imb_subject:
			SubjectPopupWindow subjectPop = new SubjectPopupWindow(this,
					getScreenHeight() / 4, Constants.NEED_SUBJECT);
			subjectPop.setOnWheelClickListener(this);
			subjectPop.showAtLocation(findViewById(R.id.create_student_need),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.tv_add_teacher_sex:
		case R.id.imb_choose_teacher_sex:
			WheelPopupWindow wheelPopupWindow = new WheelPopupWindow(this,
					Constants.NEED_SEX);
			wheelPopupWindow.setOnWheelClickListener(this);
			wheelPopupWindow.showAtLocation(
					findViewById(R.id.create_student_need), Gravity.BOTTOM, 0,
					0);
			break;
		case R.id.tv_add_teaching_mode:
		case R.id.imb_choose_teaching_mode:
			TeachModePopupWindow teachPopupWindow = new TeachModePopupWindow(
					this, Constants.NEED_TEACH);
			teachPopupWindow.setOnWheelClickListener(this);
			teachPopupWindow.showAtLocation(
					findViewById(R.id.create_student_need), Gravity.BOTTOM, 0,
					0);
			break;
		case R.id.tv_add_lecture_mode:
		case R.id.imb_choose_lecture_mode:
			LecturePopupWindow lecturePopupWindow = new LecturePopupWindow(
					this, Constants.NEED_LECTURE);
			lecturePopupWindow.setOnWheelClickListener(this);
			lecturePopupWindow.showAtLocation(
					findViewById(R.id.create_student_need), Gravity.BOTTOM, 0,
					0);
			break;
		case R.id.btn_student_need_release:
			release();

			break;
		case R.id.btn_student_need_preview:
			preview();
			break;
		}
	}

	/**
	 * 预览
	 */
	private void preview() {
		getData();
		if (onCheckInfo()) {
			Intent intent = new Intent(this, StudentIntroActivity.class);
			intent.putExtra("dxNeed", mDxNeed);
			intent.putExtra("type", "preview");
			startActivityByPendingTransition(intent);
		}
	}

	private boolean onCheckInfo() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(title)) {
			showShortToast("请填写课程标题");
			return false;
		}
		if (TextUtils.isEmpty(subject)) {
			showShortToast("请填写求教科目");
			return false;
		}
		if (Integer.parseInt(count) > 21) {
			showShortToast("授课次数不正确！");
			return false;
		}

		return true;
	}

	/**
	 * 发布
	 */
	private void release() {
		getData();
		if (onCheckInfo()) {
			if (!NetWorkHelperUtil.checkNetState(this)) {
				showShortToast(R.string.network_error);
				return;
			} else {
				showLoadingDialog("请稍后……");
				String urlString = UrlEngine.addStudentNeed(mDxNeed);
				HttpUtil.post(urlString, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (statusCode == Constants.STAT_200) {
							dismissLoadingDialog();
							showShortToast("成功！");
							Intent intent = new Intent(
									StudentNeedCreateActivity.this,
									StudentNeedListActivity.class);
							setResult(RESULT_OK, intent);
							StudentNeedCreateActivity.this.finish();

						}
						if (statusCode == 500) {
							showShortToast(R.string.text_error_input);
							return;
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						dismissLoadingDialog();
						if (statusCode == 0) {
							showLongToast(R.string.text_link_server_error);
						} else if (statusCode == Constants.STAT_401
								|| statusCode == Constants.STAT_403
								|| statusCode == Constants.STAT_404) {
							showShortToast(R.string.text_error_network);
						} else if (statusCode == 500) {
							showShortToast(R.string.text_error_input);
						}
					}

				});
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void getData() {
		title = mNeedTitle.getText().toString().trim();
		price = mClassfees.getText().toString().trim();
		count = mLectureCount.getText().toString().trim();
		subject = mItemName.getText().toString().trim();
		intro = mIntroduce.getText().toString().trim();
		subject = EmojiFilter.filterEmoji(subject);
		intro = EmojiFilter.filterEmoji(intro);
		if (TextUtils.isEmpty(price)) {
			price = "0";
		}
		if (TextUtils.isEmpty(count)) {
			count = "0";
		}
		courseTime = 0;
		for (int i = 0; i < 21; i++) {
			// TODO Auto-generated method stub
			if (mCheckBox[i].isChecked()) {
				courseTime = (courseTime ^ (1 << i));
			}
		}
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
				DateUtil.dateFormatYMDHMS);
		mDxNeed.setUserId(EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		mDxNeed.setNeedId(needId + "");
		mDxNeed.setNeedTitle(title);
		mDxNeed.setSubjectItemId(subject);
		mDxNeed.setIntroduce(intro);
		mDxNeed.setPrice(Integer.parseInt(price));
		mDxNeed.setTeacherSex(sex);
		mDxNeed.setLectureMode(lecturesMode);
		mDxNeed.setTeachMode(teacherMode);
		mDxNeed.setTradeNumber(Integer.parseInt(count));
		mDxNeed.setToken(EtutorApplication.getInstance().getSpUtil().getToken());
		mDxNeed.setPublishTime(mSimpleDateFormat.format(new Date()));
		mDxNeed.setSubtime(String.valueOf(courseTime));
	}

	@Override
	public void wheelOnClick(String value, int type) {
		switch (type) {
		case Constants.NEED_SEX:
			sex = value;
			mTecaherSex.setText(value);
			break;
		case Constants.NEED_TEACH:
			teacherMode = value;
			mTeachrMode.setText(value);
			break;
		case Constants.NEED_LECTURE:
			lecturesMode = value;
			mLecturesMode.setText(value);
			break;
		case Constants.NEED_SUBJECT:
			if ("请选择".equals(value)) {
				value = "";
			}
			subject = value;
			mItemName.setText(value);
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
