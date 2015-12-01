package net.dx.etutor.activity.register;

import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.setting.RuleActivity;
import net.dx.etutor.data.GlobalData;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.InvitationCodeDialog;
import net.dx.etutor.dialog.InvitationCodeDialog.OnInvitationCodeListener;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.view.MyEditText;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		TextWatcher,OnInvitationCodeListener {
	
	public static String TAG = "RegisterActivity";
	private int mReSendTime = 60;
	private Button mButtonSend;
	private TextView mAgreement;
	private CheckBox mCheckBox;
	private EditText mPhoneNumber;
	private String mPhone;
	private ImageButton mButtonConfirm;
	private InvitationCodeDialog mCodeDialog;
	private EditText mEtPwd, mEtCode;
	private String mPassword, repassword, code,invitationCode;
	protected boolean registerFlag;
	protected DxUsers users = new DxUsers();

	@Override
	public void initViews() {
		setContentView(R.layout.activity_register);
		setTitle(R.string.text_register);
		showIcon(0, "下一步");
		mEtCode = (EditText) findViewById(R.id.et_verification_code);
		mButtonSend = (Button) findViewById(R.id.btn_register_send);
		mPhoneNumber = (EditText) findViewById(R.id.et_register_phone);
		mAgreement = (TextView) findViewById(R.id.tv_agreement);
		mCheckBox = (CheckBox) findViewById(R.id.checkBox1);
		mButtonConfirm = (ImageButton) findViewById(R.id.btn_register_confirm);
		mEtPwd = (EditText) findViewById(R.id.et_password);

	}

	@Override
	public void initEvents() {
		mAgreement.setOnClickListener(this);
		mButtonSend.setOnClickListener(this);
		mButtonConfirm.setOnClickListener(this);
		mPhoneNumber.addTextChangedListener(this);

	}

	@Override
	public void iconClick() {
		// TODO Auto-generated method stub
		if (validate()) {
			showLoadingDialog("");
			mCodeDialog=new InvitationCodeDialog(this);
			mCodeDialog.setInvitationCodeListener(this);
			mCodeDialog.show();
		}
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
					handler.sendEmptyMessage(100);
					String urlString = UrlEngine.sendCode(mPhone);
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
									if (status == Constants.STAT_0) {
										showShortToast("此账号已注册");
										handler.sendEmptyMessage(101);
									} else if (status == Constants.STAT_4) {
										showShortToast("短信已发送,请注意查收");
									} else if (status == Constants.STAT_ERROR) {
										showShortToast("手机号不存在!");
										handler.sendEmptyMessage(101);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					});
				} else {
					Toast.makeText(RegisterActivity.this, "手机号码不可用或格式错误",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.btn_register_confirm:
			AnimationUtil.addAnimation(v);
			if (validate()) {
				showLoadingDialog("");
				mCodeDialog=new InvitationCodeDialog(this);
				mCodeDialog.setInvitationCodeListener(this);
				mCodeDialog.show();
			}
			break;
		case R.id.tv_agreement:
			Intent intent = new Intent(this, RuleActivity.class);
			startActivityByPendingTransition(intent);
			break;

		}
	}

	private void register(final String phone, String password) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		showLoadingDialog("请稍后……");
		String urlString = UrlEngine.userRegister(phone,
				StrUtil.passwrodMaker(phone, password), code,invitationCode);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						int status = response.getInt("status");
						if (status == Constants.STAT_0) {
							dismissLoadingDialog();
							showShortToast("此手机号已注册过了!");
						} else if (status == Constants.STAT_3) {
							dismissLoadingDialog();
							showShortToast("验证码错误!");
						} else if (status == Constants.STAT_1) {
							String userId = response.getString("userId");
							String token = response.getString("token");
							mApplication.getSpUtil().setToken(token);
							setAddUserToUmeng(userId);
							// 全局
							GlobalData.instance().userId = userId;
							// 首选项
							mApplication.getSpUtil().setUserId(userId);
							uploadBaseData();
						}else {
							dismissLoadingDialog();
						}
						
					} catch (JSONException e) {
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

	/**
	 * 上传基本信息
	 */
	public void uploadBaseData() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userName", "易家教用户");
		map.put("latitude", mApplication.getLatitude());
		map.put("longitude", mApplication.getLongitude());
		map.put("userType", 0);
		String urlString = UrlEngine.completeUserInfo(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				dismissLoadingDialog();
				if (statusCode == Constants.STAT_200) {
					users.initWithAttributes(response);
					// 设置登录状态 1登录，0未登录
					mApplication.getSpUtil().setLoginStatu(1);
					mApplication.getSpUtil().setQQOpedId(users.getQqOpenId());
					mApplication.getSpUtil().setQQOpedId(users.getSinaOpenId());
					mApplication.getSpUtil().setUserType(
							Integer.parseInt(users.getUserType()));
					mApplication.getSpUtil().setToken(users.getToken());
					mApplication.getSpUtil().setUserName(users.getUserName());
					mApplication.getSpUtil().setLabel(users.getIdentify());
					mApplication.getSpUtil().setAvatarUrl(users.getAvatarUrl());
					Intent intent = new Intent(RegisterActivity.this,
							MainFragmentActivity.class);
					startActivityByPendingTransition(intent);
					RegisterActivity.this.finish();
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
	 * 友盟统计新增注册用户
	 * 
	 * @param userId
	 */
	protected void setAddUserToUmeng(String userId) {
		// TODO Auto-generated method stub
		MobclickAgent.onEvent(this, "adduser", "adduser");
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
				Toast.makeText(RegisterActivity.this, "手机号码不可用或格式错误",
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
		if (!mCheckBox.isChecked()) {
			showShortToast("请先同意《易家教用户注册协议》");
			return false;
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

	@Override
	public void CodeOnClick(View v, MyEditText mCode) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		invitationCode=null;
		switch (v.getId()) {
		case R.id.btn_logout_confirm:
			invitationCode=mCode.getEditNumber();
			break;
		case R.id.btn_logout_cancle:
			mCodeDialog.dismiss();
			break;
		}
		register(mPhone, mPassword);
	}

}
