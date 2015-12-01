package net.dx.etutor.activity.register;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.GlobalData;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.StrUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class ForgetPasswordActivity extends BaseActivity implements
		OnClickListener, TextWatcher {
	
	public static String TAG = "ForgetPasswordActivity";
	private int mReSendTime = 60;
	private Button mButtonSend;
	private EditText mPhoneNumber;
	private String mPhone;
	private ImageButton mButtonConfirm;
	private EditText mEtPwd, mEtCode;
	private EditText mEtRePwd;
	private String mPassword, code;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_change_password);
		setTitle(R.string.forget_password);
		mEtCode = (EditText) findViewById(R.id.et_verification_code);
		mButtonSend = (Button) findViewById(R.id.btn_send);
		mPhoneNumber = (EditText) findViewById(R.id.et_user_phone);
		mButtonConfirm = (ImageButton) findViewById(R.id.btn_submit);
		mEtPwd = (EditText) findViewById(R.id.et_password);
		mEtRePwd = (EditText) findViewById(R.id.et_repassword);
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
		case R.id.btn_send:
			if (TextUtils.isEmpty(mPhone)) {
				showShortToast("手机号不能为空！");
				return;
			} else {
				if (StrUtil.isMobileNo(mPhone)) {
					handler.sendEmptyMessage(100);

					String urlString = UrlEngine.updateCode(mPhone);
					HttpUtil.post(urlString, new JsonHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							if (statusCode == 0) {
								showShortToast(R.string.text_link_server_error);
							}
							if (statusCode == Constants.STAT_401
									|| statusCode == Constants.STAT_403
									|| statusCode == Constants.STAT_404) {
								showShortToast(R.string.text_error_network);
							}
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							if (statusCode == Constants.STAT_200) {
								try {
									int status = response.getInt("status");
									if (status == Constants.STAT_0) {
										showShortToast("该手机号还没有注册过！");
										handler.sendEmptyMessage(101);
									} else if (status == Constants.STAT_4) {
										showShortToast("短信已发送,请注意查收");
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					});
				} else {
					showLongToast("手机号码不可用或格式错误");
				}
			}
			break;
		case R.id.btn_submit:
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
		if (validate()) {
			String urlString = UrlEngine.forgetPassword(phone,
					StrUtil.passwrodMaker(phone, password), code);
			HttpUtil.post(urlString, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (statusCode == Constants.STAT_200) {
						try {
							int status = response.getInt("status");
							if (status == Constants.STAT_0) {
								showShortToast("该用户不存在");
							} else if (status == Constants.STAT_3) {
								showShortToast("验证码错误!");
							} else if (status == Constants.STAT_1) {
								showShortToast("修改成功");
								finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
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

	public Boolean onCode() {
		code = mEtCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(this, "验证码不能为空！", 0).show();
		} else {
			if (code.equals("123456")) {
				return true;
			} else {
				showLongToast("手机验证码不正确！请重新输入！");
				mEtCode.setText("");
			}
		}

		return false;

	}

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
				Toast.makeText(ForgetPasswordActivity.this, "手机号码不可用或格式错误",
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
