package net.dx.etutor.activity.register;

import java.io.File;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.comment.CommentListActivity;
import net.dx.etutor.activity.forum.MyPostsActivity;
import net.dx.etutor.activity.fragment.OrderListFragment;
import net.dx.etutor.activity.home.CollectActivity;
import net.dx.etutor.activity.home.MyUploadActivity;
import net.dx.etutor.activity.home.PersonalSettingActivity;
import net.dx.etutor.activity.message.PrivateMessageDetailActivity;
import net.dx.etutor.activity.order.OrderDetailActivity;
import net.dx.etutor.activity.student.StudentNeedListActivity;
import net.dx.etutor.activity.teacher.TeacherSettingCourseActivity;
import net.dx.etutor.app.UsersAPI;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxOrder;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.SharePreferenceUtil;
import net.dx.etutor.util.StrUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity implements OnClickListener {

	public static String TAG = "LoginActivity";
	private static EditText eLoginPhone;
	private static EditText mLoginPassword;
	private static String phone;
	private static String password;
	private static ImageView mQQLogin;
	private static ImageView mSinaLogin;
	public static String mAppid;

	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private Tencent mTencent;
	private final static String APP_ID = "1103377550";// 1103377550
	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;
	private DxUsers users = new DxUsers();
	private int target;
	protected DxOrder dxOrder;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_login);
		setTitle("登录");
		target = getIntent().getIntExtra("targetId", -1);
		eLoginPhone = (EditText) findViewById(R.id.et_login_phone);
		mLoginPassword = (EditText) findViewById(R.id.et_login_password);

		mQQLogin = (ImageView) findViewById(R.id.imv_login_use_qq);
		mSinaLogin = (ImageView) findViewById(R.id.imv_login_use_wb);
		mQQLogin.setOnClickListener(this);
		mSinaLogin.setOnClickListener(this);

	}

	@Override
	public void initEvents() {

	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_login_login:
			AnimationUtil.addAnimation(v);
			onClickLogin();
			
			break;
		case R.id.btn_login_register:
			intent = new Intent(this, RegisterActivity.class);
			intent.putExtra("LoginType", 1);
			startActivityByPendingTransition(intent);
			break;
		case R.id.tv_forget_password:
			intent = new Intent(this, ForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.imv_login_use_qq:
			onClickQQLogin();
			break;
		case R.id.imv_login_use_wb:
			onClickSinaLogin();
			break;
		default:
			break;
		}
	}

	/**
	 * 用户登录
	 */
	private void onClickLogin() {
		phone = eLoginPhone.getText().toString().trim();
		password = mLoginPassword.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			showShortToast(R.string.text_account_empty);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			showShortToast(R.string.text_passwrod_empty);
			return;
		}
		if (!StrUtil.isMobileNo(phone)) {
			showShortToast(R.string.text_error_phone);
			return;
		}
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		showShortToast("登录中…");
		String urlString = UrlEngine.userLogin(phone,
				StrUtil.passwrodMaker(phone, password));
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.toString().equals(Constants.LOGIN_ERRORS)) {
							showShortToast("用户名或密码不正确！");
						} else {
							mApplication.getSpUtil().isRefreshFlag(true);
							mApplication.getSpUtil().isCardListFlushFlag(true);
							users.initWithAttributes(response);
							// 设置登录状态 0未登录，1登录，2第三方登录，3广播登录
							if (target == 11) {
								dxOrder = (DxOrder) getIntent()
										.getSerializableExtra("dxOrder");
								mApplication.getSpUtil().setLoginStatu(3);
							} else {
								mApplication.getSpUtil().setLoginStatu(1);
							}
							mApplication.getSpUtil().setUserPhone(
									users.getTelephone());
							mApplication.getSpUtil().setUserId(
									users.getUserId().toString());
							mApplication.getSpUtil().setUserType(
									Integer.parseInt(users.getUserType()));
							mApplication.getSpUtil().setUserName(
									users.getUserName());
							mApplication.getSpUtil().setUserSchool(
									users.getSchool());
							mApplication.getSpUtil().setLevel(users.getLevel());
							mApplication.getSpUtil().setIdentify(
									users.getIdentify());
							mApplication.getSpUtil().setUserRank(
									users.getRank());
							mApplication.getSpUtil().setToken(users.getToken());

							mApplication.getSpUtil().setQQOpedId(
									users.getQqOpenId());
							mApplication.getSpUtil().setSinaUid(
									users.getSinaOpenId());

							if (!TextUtils.isEmpty(users.getAvatarUrl())) {
								mApplication.getSpUtil().setAvatarUrl(
										users.getAvatarUrl());
								String avatarName = users.getAvatarUrl()
										.substring(
												users.getAvatarUrl()
														.lastIndexOf("/") + 1);
								File file = new File(Constants.MyAvatarDir
										+ avatarName);
								if (!file.exists()) {
									getUserAvatar();
								} else {
									mApplication.getSpUtil().setUserAvatar(
											"file:////sdcard/etutor/avatar/"
													+ avatarName);
								}
							}

							showShortToast("登陆成功！");
							Intent intent = new Intent();

							switch (target) {
							case 0:
								intent = new Intent(LoginActivity.this,
										MainFragmentActivity.class);
								startActivityByPendingTransition(intent);
								LoginActivity.this.finish();
								break;
							case 1:
								if (users.getUserId() == Integer
										.parseInt(dxOrder.getToUserId())) {
									intent = new Intent(LoginActivity.this,
											OrderDetailActivity.class);
									intent.putExtra("dxOrder", dxOrder);
								} else {
									mApplication.getSpUtil().setLoginStatu(1);
									showShortToast("预约对象与登录用户不一致");
									intent = new Intent(LoginActivity.this,
											MainFragmentActivity.class);
								}
								startActivityByPendingTransition(intent);
								LoginActivity.this.finish();

								break;
							default:
								LoginActivity.this.setResult(RESULT_OK);
								finish();
								break;
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
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
	 * 新浪微博登录
	 */
	private void onClickSinaLogin() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		mWeiboAuth = new WeiboAuth(LoginActivity.this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mWeiboAuth);

		mSsoHandler.authorize(new AuthListener());

	}

	/**
	 * QQ登录
	 */
	private void onClickQQLogin() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();

				}
			};
			mQQAuth.login(LoginActivity.this, "get_simple_userinfo", listener);
			mTencent.login(LoginActivity.this, "get_simple_userinfo", listener);
		} else {
			mQQAuth.logout(LoginActivity.this);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		final Context context = LoginActivity.this;
		final Context ctxContext = context.getApplicationContext();
		mAppid = APP_ID;
		mQQAuth = QQAuth.createInstance(mAppid, ctxContext);
		mTencent = Tencent.createInstance(mAppid, LoginActivity.this);
		super.onStart();
	}

	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 100;
					mHandler.sendMessage(msg);
					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								try {
									QQImageUri = json
											.getString("figureurl_qq_2");
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = 2;
								msg.what = 101;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		}
	}

	String QQName = null;
	String SinaName = null;
	String QQImageUri = null;
	String SinaImageUri = null;
	String QQId = null;
	String sinaId = null;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						QQName = response.getString("nickname");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 101:
				final int type = (int) msg.obj;
				String url;
				if (type == 2) {
					url = UrlEngine.getOpenId(QQId, "");
				} else {
					url = UrlEngine.getOpenId("", sinaId);
				}
				HttpUtil.post(url, new JsonHttpResponseHandler() {

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

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (statusCode == Constants.STAT_200) {
							try {
								if (!response.toString().equals(
										Constants.LOGIN_ERROR)) {
									mApplication.getSpUtil().isRefreshFlag(true);
									mApplication.getSpUtil().isCardListFlushFlag(true);
									users.initWithAttributes(response);
									mApplication.getSpUtil().setUserPhone(
											users.getTelephone());
									mApplication.getSpUtil().setUserId(
											users.getUserId().toString());
									mApplication.getSpUtil().setUserType(
											Integer.parseInt(users
													.getUserType()));
									mApplication.getSpUtil().setUserName(
											users.getUserName());
									mApplication.getSpUtil().setUserSchool(
											users.getSchool());
									mApplication.getSpUtil().setLevel(
											users.getLevel());
									mApplication.getSpUtil().setIdentify(
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

									String str = users.getAvatarUrl();
									if (!TextUtils.isEmpty(str)) {

										String avatarName = str.substring(str
												.lastIndexOf("/") + 1);
										File file = new File(
												Constants.MyAvatarDir
														+ avatarName);
										if (!file.exists()) {
											getUserAvatar();
										} else {
											mApplication
													.getSpUtil()
													.setUserAvatar(
															"file:////sdcard/etutor/avatar/"
																	+ avatarName);
										}
									}
									dismissLoadingDialog();
									// 设置登录状态 0未登录，1登录，2第三方登录，3广播登录
									mApplication.getSpUtil().setLoginStatu(2);
									Intent intent = new Intent();
									switch (target) {
									case 0:
										intent = new Intent(LoginActivity.this,
												MainFragmentActivity.class);
										startActivityByPendingTransition(intent);
										finish();
										break;
									case 1:
										dxOrder = (DxOrder) getIntent()
												.getSerializableExtra("dxOrder");
										if (users.getUserId() == Integer
												.parseInt(dxOrder.getToUserId())) {
											mApplication.getSpUtil()
													.setLoginStatu(3);
											intent = new Intent(
													LoginActivity.this,
													OrderDetailActivity.class);
											intent.putExtra("dxOrder", dxOrder);
										} else {
											showShortToast("订单对象与登录用户不一致");
											intent = new Intent(
													LoginActivity.this,
													MainFragmentActivity.class);
										}
										startActivityByPendingTransition(intent);
										finish();
										break;
									default:
										LoginActivity.this.setResult(RESULT_OK);
										finish();
										break;
									}
								} else {

									Bundle bundle = new Bundle();
									bundle.putInt("LoginType", type);
									if (type == 2) {
										bundle.putString("openId", QQId);
										bundle.putString("userName", QQName);
										bundle.putString("imageUri", QQImageUri);
									} else {
										bundle.putString("openId", sinaId);
										bundle.putString("userName", SinaName);
										bundle.putString("imageUri",
												SinaImageUri);
									}
									bundle.putString("telephone", "");
									dismissLoadingDialog();
									Intent intent = new Intent(
											LoginActivity.this,
											BindingActivity.class);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}

				});
				break;

			default:
				break;
			}
		}

	};

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			JSONObject jsonObject = (JSONObject) response;
			try {
				QQId = jsonObject.getString("openid");
				// mApplication.getSpUtil().setQQOpedId(openId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showLoadingDialog("请稍后…");
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			// Util.toastMessage(LoginActivity.this, "onError: " +
			// e.errorDetail);
			// Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			// Util.toastMessage(LoginActivity.this, "onCancel: ");
			// Util.dismissDialog();
		}

	}

	/*
	*//**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			Bitmap bm = null;
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 保存 Token 到 SharedPreferences
				long uid = Long.parseLong(mAccessToken.getUid());
				sinaId = StrUtil.passwrodMaker(String.valueOf(uid), "dxshell");
				// mApplication.getSpUtil().setSinaUid(id);
				SharePreferenceUtil.writeAccessToken(LoginActivity.this,
						mAccessToken);

				mUsersAPI = new UsersAPI(mAccessToken);
				mUsersAPI.show(uid, mListener);

			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				// String code = values.getString("code");
				// String message = "授权失败！";
				// if (!TextUtils.isEmpty(code)) {
				// message = message + "\nObtained the code: " + code;
				// }
				// showLongToast(message);
			}
		}

		@Override
		public void onCancel() {
			dismissLoadingDialog();
			showLongToast("取消授权");
		}

		@Override
		public void onWeiboException(WeiboException e) {

		}

	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);
				if (!TextUtils.isEmpty(response)) {

					SinaName = jsonObject.optString("name", "");
					SinaImageUri = jsonObject
							.optString("profile_image_url", "");
					mApplication.getSpUtil().setSinaAvatarUrl(SinaImageUri);
					mApplication.getSpUtil().setSinaName(SinaName);
					Message msg = new Message();
					msg.obj = 3;
					msg.what = 101;
					mHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (mSsoHandler != null) {
			showLoadingDialog("请稍等……");
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
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
