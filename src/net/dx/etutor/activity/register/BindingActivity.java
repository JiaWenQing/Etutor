package net.dx.etutor.activity.register;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.GlobalData;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.UploadHeadUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class BindingActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static String TAG = "BindingActivity";
	private int mReSendTime = 60;
	private Button mButtonSend;
	private EditText mPhoneNumber;
	private String mPhone;
	private ImageButton mButtonConfirm;
	private EditText mEtPwd, mEtRePwd, mEtCode;
	private String mPassword, repassword, code;
	private DxUsers users = new DxUsers();
	private int type;// qq/sina
	private String openId;
	private String userName;
	private String imagePath;
	private String otherId;

	// private String QQopenId;
	// private String SinaopenId;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_register);
		setTitle(R.string.binding_string);

		mEtCode = (EditText) findViewById(R.id.et_verification_code);
		mButtonSend = (Button) findViewById(R.id.btn_register_send);
		mPhoneNumber = (EditText) findViewById(R.id.et_register_phone);
		mButtonConfirm = (ImageButton) findViewById(R.id.btn_register_confirm);
		mEtPwd = (EditText) findViewById(R.id.et_password);
		mEtRePwd = (EditText) findViewById(R.id.et_repassword);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		openId = getIntent().getExtras().getString("openId");
		otherId = openId;
		type = getIntent().getExtras().getInt("LoginType");
		userName = getIntent().getExtras().getString("userName");
		imagePath = getIntent().getExtras().getString("imageUri");
		mPhone = getIntent().getExtras().getString("telephone");
		if (!TextUtils.isEmpty(imagePath)) {
			getOtherAvatar(imagePath, otherId);
		}
		if (!TextUtils.isEmpty(mPhone)) {
			mPhoneNumber.setText(mPhone);
			mPhoneNumber.setClickable(false);
			mPhoneNumber.setFocusable(false);
		} else {
			mPhoneNumber.setClickable(true);
			mPhoneNumber.setFocusable(true);

		}
	}

	@Override
	public void initEvents() {
		mButtonSend.setOnClickListener(this);
		mButtonConfirm.setOnClickListener(this);
		mPhoneNumber.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		mPhone = mPhoneNumber.getText().toString().trim();
		mPassword = mEtPwd.getText().toString().trim();
		switch (v.getId()) {
		case R.id.btn_register_send:
			if (TextUtils.isEmpty(mPhone)) {
				showShortToast("手机号不能为空！");
				return;
			} else {
				if (StrUtil.isMobileNo(mPhone)) {
					String urlString;
					if (type == 2) {
						urlString = UrlEngine.bindqqCode(mPhone);
					} else {
						urlString = UrlEngine.bindsinaCode(mPhone);
					}
					handler.sendEmptyMessage(100);
					HttpUtil.post(urlString, new JsonHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							if (statusCode == Constants.STAT_200) {
								try {
									int status = response.getInt("status");
									if (status == Constants.STAT_4) {
										showShortToast("短信已发送,请注意查收");
									} else if (status == Constants.STAT_ERROR) {
										showShortToast("手机号不存在!");
										handler.sendEmptyMessage(101);
									} else {
										showShortToast("此账号已绑定过第三方账号");
										finish();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					});
				} else {
					Toast.makeText(BindingActivity.this, "手机号码不可用或格式错误",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.btn_register_confirm:
			AnimationUtil.addAnimation(v);
			onSubmit(mPhone, mPassword);
			break;
		}
	}

	private void onSubmit(final String phone, String password) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (!validate()) {
			return;
		} else {
			String urlString;
			if (type == 2) {
				urlString = UrlEngine.bindRegister(phone,
						StrUtil.passwrodMaker(phone, password), code, openId,
						"");
			} else {
				urlString = UrlEngine.bindRegister(phone,
						StrUtil.passwrodMaker(phone, password), code, "",
						openId);
			}
			showLoadingDialog("请稍后……");
			HttpUtil.post(urlString, new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					dismissLoadingDialog();
					if (statusCode == 0) {
						showLongToast(R.string.text_link_server_error);
					}
					if (statusCode == Constants.STAT_401) {
						showShortToast(R.string.text_error_network);
					}
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (statusCode == Constants.STAT_200) {

						try {
							int code = response.getInt("statusCode");
							switch (code) {
							case 1:
								String userId = response.getString("userId");
								String token = response.getString("token");
								mApplication.getSpUtil().setToken(token);
								// setAddUserToUmeng(userId);
								// 全局
								GlobalData.instance().userId = userId;
								// 首选项
								mApplication.getSpUtil().setUserId(userId);
								uploadAvatar();
								Thread.sleep(1500);
								uploadBaseData();
								break;
							case 2:
								dismissLoadingDialog();
								showShortToast("用户不存在!");
								break;
							case 3:
								dismissLoadingDialog();
								showShortToast("验证码不正确!");
								break;
							case 4:
								users.initWithAttributes(response);
								dismissLoadingDialog();
								// 设置登录状态 1登录，0未登录
								mApplication.getSpUtil().setLoginStatu(1);
								mApplication.getSpUtil().setUserPhone(
										users.getTelephone());
								mApplication.getSpUtil().setUserId(
										users.getUserId().toString());
								mApplication.getSpUtil().setUserType(
										Integer.parseInt(users.getUserType()));
								mApplication.getSpUtil().setUserName(
										users.getUserName());
								mApplication.getSpUtil().setLabel(
										users.getIdentify());
								mApplication.getSpUtil().setUserRank(
										users.getRank());
								mApplication.getSpUtil().setToken(
										users.getToken());
								mApplication.getSpUtil().setAvatarUrl(
										users.getAvatarUrl());

								mApplication.getSpUtil().setQQOpedId(
										users.getQqOpenId());
								mApplication.getSpUtil().setSinaUid(
										users.getSinaOpenId());

								String s = users.getAvatarUrl();
								if (!TextUtils.isEmpty(s)) {
									File file = new File(
											Constants.MyAvatarDir
													+ users.getAvatarUrl()
															.split("/")[1]);
									if (!file.exists()) {
										getUserAvatar();
										Thread.sleep(2000);
									} else {
										mApplication.getSpUtil().setUserAvatar(
												"file:////sdcard/etutor/avatar/"
														+ users.getAvatarUrl()
																.split("/")[1]);
									}
								} else {
									uploadAvatar();
								}

								dismissLoadingDialog();
								showShortToast("绑定成功！");
								Intent intent3 = new Intent(
										BindingActivity.this,
										MainFragmentActivity.class);
								startActivityByPendingTransition(intent3);
								finish();
								break;

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			});
		}
	}

	/**
	 * 友盟统计新增注册用户
	 * 
	 * @param userId
	 */
	protected void setAddUserToUmeng(String userId) {
		// TODO Auto-generated method stub
		MobclickAgent.onEvent(this, "adduser", "adduser");
	}

	/**
	 * 上传基本信息
	 */
	public void uploadBaseData() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userName", userName);
		map.put("userType", 0);
		map.put("latitude", mApplication.getLatitude());
		map.put("longitude", mApplication.getLongitude());
		String urlString = UrlEngine.completeUserInfo(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					users.initWithAttributes(response);
					// 设置登录状态 1登录，0未登录
					mApplication.getSpUtil().setLoginStatu(1);

					mApplication.getSpUtil().setQQOpedId(users.getQqOpenId());
					mApplication.getSpUtil().setQQOpedId(users.getSinaOpenId());

					mApplication.getSpUtil().setUserType(0);

					mApplication.getSpUtil().setUserName(users.getUserName());
					mApplication.getSpUtil().setLevel(users.getLevel());
					mApplication.getSpUtil().setIdentify(users.getIdentify());
					dismissLoadingDialog();
					Intent intent = new Intent(BindingActivity.this,
							MainFragmentActivity.class);
					startActivityByPendingTransition(intent);
					BindingActivity.this.finish();
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

	/**
	 * 上传第三方头像
	 */
	public void uploadAvatar() {
		String path = mApplication.getSpUtil().getUserAvatar();
		File file = new File(path);
		mApplication.getSpUtil().setUserAvatar(
				"file:///" + file.getAbsolutePath());
		Map<String, String> param = new HashMap<>();
		param.put("avatarUrl", file.getAbsolutePath());
		param.put("type", "png");
		param.put("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		UploadHeadUtil uploadUtil = UploadHeadUtil.getInstance();
		File fileHead = new File(file.getAbsolutePath());
		uploadUtil.uploadFile(fileHead, "img", UrlEngine.uploadHead(), param);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.toString().length() > 0) {
			char[] chars = s.toString().toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				if (i % 4 == 2) {
					buffer.append(chars[i] + "  ");
				} else {
					buffer.append(chars[i]);
				}
			}

		} else {
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				if (mReSendTime > 1) {
					mReSendTime--;
					mButtonSend.setEnabled(false);
					mButtonSend.setText("重发(" + mReSendTime + ")");
					handler.sendEmptyMessageDelayed(100, 1000);
				} else {
					mReSendTime = 60;
					mButtonSend.setEnabled(true);
					mButtonSend.setText("重    发");
				}
				break;
			case 101:
				mReSendTime = 60;
				handler.removeMessages(100);
				mButtonSend.setEnabled(true);
				mButtonSend.setText("发	送");
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 检查页面数据
	 * 
	 * @return
	 */
	public boolean validate() {
		String pwd = null;
		String rePwd = null;
		code = mEtCode.getText().toString().trim();
		mPhone = mPhoneNumber.getText().toString().trim();
		if (TextUtils.isEmpty(mPhone)) {
			showShortToast("手机号不能为空！");
			return false;
		} else {
			if (!StrUtil.isMobileNo(mPhone)) {
				Toast.makeText(BindingActivity.this, "手机号码不可用或格式错误",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(this, "验证码不能为空！", 0).show();
			return false;
		} else {
			if (code.length() != 6) {
				Toast.makeText(this, "请输入6位数字的验证码！", 0).show();
				return false;
			}
		}
		if (isNull(mEtPwd)) {
			showShortToast("请输入密码");
			mEtPwd.requestFocus();
			return false;
		} else {
			pwd = mEtPwd.getText().toString().trim();
			if (pwd.length() < 6) {
				showShortToast("密码不能小于6位");
				mEtPwd.requestFocus();
				return false;
			}
		}
		if (isNull(mEtRePwd)) {
			showShortToast("请重复输入一次密码");
			mEtRePwd.requestFocus();
			return false;
		} else {
			rePwd = mEtRePwd.getText().toString().trim();
			if (!pwd.equals(rePwd)) {
				showShortToast("两次输入的密码不一致");
				mEtRePwd.requestFocus();
				return false;
			}
		}

		return true;
	}

	protected boolean isNull(EditText editText) {
		String text = editText.getText().toString().trim();
		if (text != null && text.length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 异步获取头像
	 */
	public void getUserAvatar() {
		String urlString = DataParam.REMOTE_SERVE + users.getAvatarUrl();
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
					String avatarName = users.getAvatarUrl().substring(
							users.getAvatarUrl().lastIndexOf("/") + 1);
					PictureUtil.saveBitmap(Constants.MyAvatarDir, avatarName,
							bitmap, true);
					mApplication.getSpUtil().setUserAvatar(
							"file:////sdcard/etutor/avatar/" + avatarName);
				}
			}
		});

	}

	/**
	 * 异步获取第三方头像
	 */
	public void getOtherAvatar(final String urlString, final String avatarName) {
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
					PictureUtil.saveBitmap(Constants.MyAvatarDir, avatarName,
							bitmap, true);
					mApplication.getSpUtil().setUserAvatar(
							Constants.MyAvatarDir + avatarName);
				}
			}
		});

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
