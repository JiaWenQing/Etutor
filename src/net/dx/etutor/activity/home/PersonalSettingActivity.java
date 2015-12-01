package net.dx.etutor.activity.home;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.academy.LocationActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ChoicePicDialog;
import net.dx.etutor.dialog.ChoicePicDialog.OnChoicePicClickListener;
import net.dx.etutor.dialog.EducationDialog;
import net.dx.etutor.dialog.EducationDialog.EducationDialogCallBack;
import net.dx.etutor.dialog.UserSexDialog;
import net.dx.etutor.dialog.UserSexDialog.UserSexDialogCallBack;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.popupwindow.TeachAgePopupWindow;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.TokenCheckUtil;
import net.dx.etutor.util.UpdateHeadIconUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class PersonalSettingActivity extends BaseActivity implements
		OnWheelClickListener, OnClickListener, OnChoicePicClickListener {

	public static String TAG = "PersonalSettingActivity";
	private LinearLayout mHeadLayout;
	private LinearLayout mIdcardLayout;
	private LinearLayout mUserSexLayout;
	private LinearLayout mUserBirthdayLayout;
	private LinearLayout mLinearLayout;
	private LinearLayout mGraduateTimeLayout;
	private LinearLayout mTeachingTimeLayout;
	private LinearLayout mEducationLayout;

	private ImageButton iChooseHead;
	private ImageButton iUploadIdcard;
	private ImageButton iEducation;
	private ImageButton iGraduateTime;
	private ImageButton iTeachingTime;
	private ImageButton iUserBirthday;
	private ImageButton iUserSex;

	private ImageView mUserHead;
	private ImageView mIdCard;
	private ImageView mLocation;

	private EditText mAddress;
	private EditText mUserName;
	private EditText mSchool;
	private EditText mMajor;

	private TextView mStatus;
	private TextView mUserSex;
	private TextView mEducation;
	private TextView mPhone;
	private TextView mGraguateTime;
	private TextView mTeachingTime;
	private TextView mUserBirthday;

	private Button mSubmit;

	private DxUsers dxUsers = new DxUsers();;

	public String filePath = "";
	private String userId;
	private int target = 0;

	private int userType;
	private String sex;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting_personal_info);
		setTitle(R.string.text_setting_register_info);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		userType = mApplication.getSpUtil().getUserType();

		mHeadLayout = (LinearLayout) findViewById(R.id.layout_head);
		iChooseHead = (ImageButton) findViewById(R.id.imb_choose_head);
		mUserHead = (ImageView) findViewById(R.id.imv_choose_head);

		mUserName = (EditText) findViewById(R.id.et_username);
		mPhone = (TextView) findViewById(R.id.tv_phone);
		mAddress = (EditText) findViewById(R.id.et_address);
		mLocation = (ImageView) findViewById(R.id.imv_location);

		mUserSexLayout = (LinearLayout) findViewById(R.id.layout_user_sex);
		iUserSex = (ImageButton) findViewById(R.id.imb_user_sex);
		mUserSex = (TextView) findViewById(R.id.tv_user_sex);

		mSchool = (EditText) findViewById(R.id.et_graduate_school);

		mIdcardLayout = (LinearLayout) findViewById(R.id.layout_idcard);
		iUploadIdcard = (ImageButton) findViewById(R.id.imb_upload_idcard);
		mStatus = (TextView) findViewById(R.id.tv_status);
		mIdCard = (ImageView) findViewById(R.id.imv_upload_idcard);

		mLinearLayout = (LinearLayout) findViewById(R.id.layout_teacher_setting);

		mGraduateTimeLayout = (LinearLayout) findViewById(R.id.layout_graduate_time);
		iGraduateTime = (ImageButton) findViewById(R.id.imb_graduate_time);
		mGraguateTime = (TextView) findViewById(R.id.tv_graduate_time);

		mTeachingTimeLayout = (LinearLayout) findViewById(R.id.layout_teacher_age);
		iTeachingTime = (ImageButton) findViewById(R.id.imb_teaching_age);
		mTeachingTime = (TextView) findViewById(R.id.tv_teaching_age);

		mEducationLayout = (LinearLayout) findViewById(R.id.layout_education_degree);
		iEducation = (ImageButton) findViewById(R.id.imb_teacher_education_degree);
		mEducation = (TextView) findViewById(R.id.tv_education_degree);

		mUserBirthdayLayout = (LinearLayout) findViewById(R.id.layout_user_birthday);
		iUserBirthday = (ImageButton) findViewById(R.id.imb_user_birthday);
		mUserBirthday = (TextView) findViewById(R.id.tv_user_birthday);

		mMajor = (EditText) findViewById(R.id.et_teacher_major);

		mSubmit = (Button) findViewById(R.id.btn_submit);

		final Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		initData();

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mLocation.setOnClickListener(this);
		mSubmit.setOnClickListener(this);

		iChooseHead.setOnClickListener(this);
		iUploadIdcard.setOnClickListener(this);
		iGraduateTime.setOnClickListener(this);
		iTeachingTime.setOnClickListener(this);
		iEducation.setOnClickListener(this);
		iUserBirthday.setOnClickListener(this);
		iUserSex.setOnClickListener(this);

		mIdcardLayout.setOnClickListener(this);
		mHeadLayout.setOnClickListener(this);
		mGraduateTimeLayout.setOnClickListener(this);
		mTeachingTimeLayout.setOnClickListener(this);
		mEducationLayout.setOnClickListener(this);
		mUserBirthdayLayout.setOnClickListener(this);
		mUserSexLayout.setOnClickListener(this);

		EmojiFilter.checkEmoji(PersonalSettingActivity.this, mUserName, null,
				100);
		EmojiFilter.checkEmoji(PersonalSettingActivity.this, mAddress, null,
				100);
		EmojiFilter.checkEmoji(PersonalSettingActivity.this, mMajor, null, 100);
		EmojiFilter
				.checkEmoji(PersonalSettingActivity.this, mSchool, null, 100);

	}

	public void initData() {
		maxTime = System.currentTimeMillis();
		showLoadingDialog("请稍后...");
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			dismissLoadingDialog();
			return;
		}
		if (userType == 0) {
			mLinearLayout.setVisibility(View.GONE);
		} else {
			mLinearLayout.setVisibility(View.VISIBLE);
		}
		TokenCheckUtil.checkToken();

		String url = UrlEngine.getUserDetail(userId);
		System.out.println(TAG+"=="+url);
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@SuppressLint("ResourceAsColor")
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						dxUsers.initWithAttributes(response);
						mUserName.setText(dxUsers.getUserName());
						mPhone.setText(dxUsers.getTelephone());
						mAddress.setText(dxUsers.getAddress());

						latitude=dxUsers.getLatitude();
						longitude=dxUsers.getLongitude();
						sex = dxUsers.getSex();
						if (TextUtils.isEmpty(sex)) {
							sex = "保密";
						} else {
							if ("保密".equals(sex)) {
								sex = "保密";
							} else if ("男".equals(sex)) {
								sex = "男";
							} else {
								sex = "女";
							}
						}
						mUserSex.setText(sex);
						String status = dxUsers.getStatus();
						if (TextUtils.isEmpty(status)) {
							mStatus.setText("上传手持证件照");
						} else {
							switch (status) {
							case "-1":
								mStatus.setText("上传手持证件照(未通过)");
								break;
							case "1":
								mStatus.setText("上传手持证件照(已通过)");
								break;
							case "0":
								mStatus.setText("上传手持证件照(待审核)");
								break;
							}
						}
						if (userType == 1) {
							mSchool.setText(dxUsers.getSchool());
							mGraguateTime.setText(dxUsers.getGraduateTime());
							mTeachingTime.setText(StrUtil.setCoachTime(Integer
									.parseInt(dxUsers.getCoachTime())));
							mEducation.setText(dxUsers.getEducation());
							mMajor.setText(dxUsers.getMajor());
							mUserBirthday.setText(dxUsers.getTeacherBirthday()
									+ "");
						}

						if (!TextUtils.isEmpty(dxUsers.getAvatarUrl())) {
							EtutorApplication.getInstance().getSpUtil()
									.setAvatarUrl(dxUsers.getAvatarUrl());
							String avatarName = dxUsers.getAvatarUrl()
									.substring(
											dxUsers.getAvatarUrl().lastIndexOf(
													"/") + 1);
							File file = new File(Constants.MyAvatarDir
									+ avatarName);
							if (!file.exists()) {
								getUserAvatar();
							} else {
								UpdateHeadIconUtil.refreshAvatar(mUserHead);
							}

						}
						if (!TextUtils.isEmpty(dxUsers.getCertificatePhotoUrl())) {
							String credentialName = dxUsers
									.getCertificatePhotoUrl().substring(
											dxUsers.getCertificatePhotoUrl()
													.lastIndexOf("/") + 1);
							mApplication.getSpUtil().setUploadPath(
									"file:///" + Constants.MyIdCardDir
											+ credentialName);
							File file = new File(Constants.MyIdCardDir
									+ credentialName);
							if (!file.exists()) {
								getUserCertificate();
							} else {
								UpdateHeadIconUtil.refreshIdCard(mIdCard);
							}
						}
						dismissLoadingDialog();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						dismissLoadingDialog();
						e.printStackTrace();
					}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imb_choose_head:
		case R.id.layout_head:
			ChoicePicDialog dialogHead = new ChoicePicDialog(this,
					getScreenHeight());
			dialogHead.setOnChoicePicClickListener(this, 0);
			target = 0;
			dialogHead.show();
			break;
		case R.id.imv_location:
			target=2;
			Intent intent=new Intent(this, LocationPersonalActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
			break;
		case R.id.imb_graduate_time:
		case R.id.layout_graduate_time:
			onGraduateTime();
			break;
		case R.id.imb_teaching_age:
		case R.id.layout_teacher_age:
			TeachAgePopupWindow agePop = new TeachAgePopupWindow(this,
					Constants.NEED_AGE);
			agePop.setOnWheelClickListener(this);
			agePop.showAtLocation(findViewById(R.id.personal_setting_layout),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.imb_user_sex:
		case R.id.layout_user_sex:
			onUserSex();
			break;
		case R.id.imb_teacher_education_degree:
		case R.id.layout_education_degree:
			onEducation();
			break;
		case R.id.imb_user_birthday:
		case R.id.layout_user_birthday:
			onUserBirthday();
			break;
		case R.id.imb_upload_idcard:
		case R.id.layout_idcard:
			ChoicePicDialog dialogIdCard = new ChoicePicDialog(this,
					getScreenHeight());
			dialogIdCard.setOnChoicePicClickListener(this, 1);
			target = 1;
			dialogIdCard.show();
			break;
		case R.id.btn_submit:
			onSubmit();
			break;
		default:
			break;
		}

	}

	private void onUserSex() {
		// TODO Auto-generated method stub
		UserSexDialog dialog = new UserSexDialog(this);

		dialog.setUserSexDialogCallBack(new UserSexDialogCallBack() {

			@Override
			public void setBackValue(Dialog dialog, String value) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				mUserSex.setText(value);
			}
		});
		dialog.show();
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
				mEducation.setText(value);
			}
		});
		dialog.show();

	}

	private int mYear;
	private int mMonth;
	private int mDay;
	private String birthday;
	private String education;
	private String major;
	private String school;
	private String address;
	private String phone;
	private String coachTime;
	private String graguateTime;
	private String userName;
	private long maxTime;
	private double latitude;
	private double longitude;

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
						mGraguateTime.setText(birthday);
					}
				}, mYear, mMonth, mDay);
		gDialog.show();
	}

	private void onUserBirthday() {
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
						mUserBirthday.setText(birthday);
					}
				}, mYear, mMonth, mDay);
		DatePicker datePicker = tDialog.getDatePicker();
		datePicker.setMaxDate(maxTime);
		tDialog.show();
	}


	private void onSubmit() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		userName = mUserName.getText().toString().trim();
		address = mAddress.getText().toString().trim();
		school = mSchool.getText().toString().trim();
		major = mMajor.getText().toString().trim();
		userName = EmojiFilter.filterEmoji(userName);
		address = EmojiFilter.filterEmoji(address);
		school = EmojiFilter.filterEmoji(school);
		sex = mUserSex.getText().toString().trim();
		major = EmojiFilter.filterEmoji(major);

		phone = mPhone.getText().toString().trim();
		graguateTime = mGraguateTime.getText().toString().trim();
		coachTime = mTeachingTime.getText().toString().trim();
		education = mEducation.getText().toString().trim();
		birthday = mUserBirthday.getText().toString().trim();

		int length;
		if (TextUtils.isEmpty(userName)) {
			showShortToast("姓名不能为空！");
			return;
		} else {
			length = userName.replaceAll("[\u4e00-\u9fa5]", "00").length();
			if (length > 12) {
				showShortToast("姓名长度不符合要求(6六个汉字或12字母)");
				return;
			}
		}
		if (TextUtils.isEmpty(phone)) {
			showShortToast("手机号不能为空！");
			return;
		}
		if (TextUtils.isEmpty(address)) {
			showShortToast("地址不能为空！");
			return;
		}
		if (userType == 1) {
			if (TextUtils.isEmpty(coachTime)) {
				showShortToast("辅导教龄不能为空！");
				return;
			}
		} else {
			education = "";
			coachTime = "";
			graguateTime = "";
			major = "";
			birthday = "";
		}

		map.put("school", school);
		map.put("education", education);
		map.put("major", major);
		map.put("coachTime", coachTime);
		map.put("graduateTime", graguateTime);
		map.put("teacherBirthday", birthday);

		map.put("userType", userType);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("phone", phone);
		map.put("address", address);
		map.put("sex", sex);
		map.put("latitude", latitude);
		map.put("longitude", longitude);

		String urlString = UrlEngine.setPersonInfo(map);
		showLoadingDialog("请稍后……");
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (statusCode == Constants.STAT_200) {
						if (response.toString().equals(Constants.TOKEN_ILLEGAL)
								|| response.toString().equals(
										Constants.TOKEN_TIMEOUT)) {
							showLongToast(R.string.token_error);
							mApplication.getSpUtil().clearSharePerference();
							Intent intent = new Intent(
									PersonalSettingActivity.this,
									LoginActivity.class);
							intent.putExtra("targetId", 0);
							startActivityByPendingTransition(intent);
							PersonalSettingActivity.this.finish();
						} else {
							showShortToast("设置个人信息成功!");
							String token = response.getString("token");
							EtutorApplication.getInstance().getSpUtil()
									.setToken(token);
							EtutorApplication.getInstance().getSpUtil()
									.setUserName(userName);
							finish();
						}
						dismissLoadingDialog();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (target == 0) {
			UpdateHeadIconUtil.onActivityResultToHead(
					PersonalSettingActivity.this, mUserHead, requestCode,
					resultCode, data, null);
		} else if(target==1) {
			UpdateHeadIconUtil.onActivityResultToHead(
					PersonalSettingActivity.this, mIdCard, requestCode,
					resultCode, data, mStatus);
		}else {
			if (resultCode == Activity.RESULT_OK) {
				address = data.getStringExtra("address");
				latitude =data.getDoubleExtra("latitude", mApplication.getLatitude());
				longitude = data.getDoubleExtra("longitude",mApplication.getLongitude());
				mAddress.setText(address);
			}
		}
	}

	@Override
	public void choiceOnClick(View v, int flag) {
		UpdateHeadIconUtil.choiceOnClick(PersonalSettingActivity.this, v, flag);

	}


	/**
	 * 异步获取头像
	 */
	public void getUserAvatar() {
		String urlString = DataParam.REMOTE_SERVE + dxUsers.getAvatarUrl();
		HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == Constants.STAT_200) {
					BitmapFactory factory = new BitmapFactory();
					@SuppressWarnings("static-access")
					Bitmap bitmap = factory.decodeByteArray(responseBody, 0,
							responseBody.length);
					String avatarName = dxUsers.getAvatarUrl().substring(
							dxUsers.getAvatarUrl().lastIndexOf("/") + 1);
					PictureUtil.saveBitmap(Constants.MyAvatarDir, avatarName,
							bitmap, true);
					mApplication.getSpUtil().setUserAvatar(
							"file:////sdcard/etutor/avatar/" + avatarName);
					UpdateHeadIconUtil.refreshAvatar(mUserHead);
				}
			}
		});

	}

	/**
	 * 异步获取证件照
	 */
	public void getUserCertificate() {
		String urlString = DataParam.REMOTE_SERVE
				+ dxUsers.getCertificatePhotoUrl();
		HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == Constants.STAT_200) {
					BitmapFactory factory = new BitmapFactory();
					@SuppressWarnings("static-access")
					Bitmap bitmap = factory.decodeByteArray(responseBody, 0,
							responseBody.length);
					String certificateName = dxUsers.getCertificatePhotoUrl()
							.substring(
									dxUsers.getCertificatePhotoUrl()
											.lastIndexOf("/") + 1);
					PictureUtil.saveBitmap(Constants.MyIdCardDir,
							certificateName, bitmap, true);
					EtutorApplication
							.getInstance()
							.getSpUtil()
							.setUploadPath(
									"file:///" + Constants.MyIdCardDir
											+ certificateName);
					UpdateHeadIconUtil.refreshIdCard(mIdCard);

				}
			}
		});

	}

	@Override
	public void wheelOnClick(String value, int type) {
		// TODO Auto-generated method stub
		coachTime = value;
		mTeachingTime.setText(value);
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
