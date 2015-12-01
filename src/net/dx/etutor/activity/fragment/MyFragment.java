package net.dx.etutor.activity.fragment;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.home.CollectActivity;
import net.dx.etutor.activity.home.MyUploadActivity;
import net.dx.etutor.activity.home.PersonalSettingActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.activity.setting.AboutOurActivity;
import net.dx.etutor.activity.setting.SystemSettingActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.app.UsersAPI;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.BindChoiceDialog;
import net.dx.etutor.dialog.BindChoiceDialog.OnBindChoiceClickListener;
import net.dx.etutor.dialog.LogoutDialog;
import net.dx.etutor.dialog.LogoutDialog.OnLogoutClickListener;
import net.dx.etutor.model.DxUsers;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SharePreferenceUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.TokenCheckUtil;
import net.dx.etutor.util.UpdateManager;
import net.dx.etutor.view.imageview.RoundImageView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshScrollView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
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

public class MyFragment extends BaseFragment implements OnClickListener,
		OnBindChoiceClickListener, OnLogoutClickListener,
		OnRefreshListener<ScrollView> {

	public static String TAG = "MyFragment";
	private TextView mHeadbarIcon;
	private RoundImageView mAvatar;
	private TextView mUserName;
	private ImageView mIdentify;
	private ImageView mVerify;
	private ImageView mListen;
	private TextView mCollect;
	private TextView mUpload;
	private TextView mUpdate;
	private TextView mVersionInfo;
	private TextView mAbout;
	private TextView mBinding;
	private TextView mSetting;

	private LinearLayout mSettingInfo;
	private LinearLayout mCollectLayout;
	private LinearLayout mUploadLayout;
	private LinearLayout mUpdateLayout;
	private LinearLayout mAboutLayout;
	private LinearLayout mBindLayout;
	private LinearLayout mSettingLayout;

	private PullToRefreshScrollView mScrollView;

	private RatingBar mRatingBar;
	private String versionName;
	private int loginStatu;

	private String avatar;
	private String userName;
	private String userId;
	private int label;

	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private Tencent mTencent;
	public static String mAppid;
	// private final static String APP_ID = "1103377550";//
	// 1103377550测试时使用，真正发布的时候要换成自己的APP_ID
	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;
	private DxUsers users = new DxUsers();
	private String sinaId;
	private float rating;
	private int version = -1;
	private LogoutDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_personal, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public MyFragment() {
		super();
	}

	public MyFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}

	@Override
	protected void initViews() {
		setTitle("个人中心");
		settingIcon(0, true, "");
		mHeadbarIcon = (TextView) findViewById(R.id.main_head_bar_icon);

		mAvatar = (RoundImageView) findViewById(R.id.imv_home_head);
		mUserName = (TextView) findViewById(R.id.tv_user_name);
		mRatingBar = (RatingBar) findViewById(R.id.ratingbar_student);
		mIdentify = (ImageView) findViewById(R.id.imv_identity);
		mVerify = (ImageView) findViewById(R.id.imv_verify);
		mListen = (ImageView) findViewById(R.id.imv_listen);

		mCollectLayout = (LinearLayout) findViewById(R.id.layout_my_collect);
		mCollect = (TextView) findViewById(R.id.tvbtn_my_collect);
		mUploadLayout = (LinearLayout) findViewById(R.id.layout_my_upload);
		mUpload = (TextView) findViewById(R.id.tvbtn_my_upload);
		mUpdateLayout = (LinearLayout) findViewById(R.id.layout_check_update);
		mUpdate = (TextView) findViewById(R.id.tvbtn_check_update);
		mVersionInfo = (TextView) findViewById(R.id.tv_version_info);
		mAboutLayout = (LinearLayout) findViewById(R.id.layout_about_our);
		mAbout = (TextView) findViewById(R.id.tvbtn_about_our);
		mBindLayout = (LinearLayout) findViewById(R.id.layout_binding);
		mBinding = (TextView) findViewById(R.id.tvbtn_binding);
		mSettingInfo = (LinearLayout) findViewById(R.id.layout_setting_info);
		mSettingLayout = (LinearLayout) findViewById(R.id.layout_setting);
		mSetting = (TextView) findViewById(R.id.tvbtn_setting);
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_home);
		mScrollView.setMode(Mode.PULL_FROM_START);
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		userId = mApplication.getSpUtil().getUserId();
	}

	@Override
	protected void initEvents() {
		mCollectLayout.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		mUploadLayout.setOnClickListener(this);
		mUpload.setOnClickListener(this);
		mUpdateLayout.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
		mAboutLayout.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		mBindLayout.setOnClickListener(this);
		mBinding.setOnClickListener(this);
		mSettingLayout.setOnClickListener(this);
		mSetting.setOnClickListener(this);
		mSettingInfo.setOnClickListener(this);
		mHeadbarIcon.setOnClickListener(this);
		mUserName.setOnClickListener(this);
		mScrollView.setOnRefreshListener(this);

	}

	@Override
	protected void initData() {

	}

	public void refreshData() {
		mIdentify.setVisibility(View.GONE);
		mVerify.setVisibility(View.GONE);
		mListen.setVisibility(View.GONE);
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		userName = mApplication.getSpUtil().getUserName();
		rating = mApplication.getSpUtil().getUserRank();

		version = mApplication.getSpUtil().getVersionInfo();
		if (version == -1) {
			mVersionInfo.setText("");
		} else {
			mVersionInfo.setText(version);
		}
		mUserName.setText("未登录");
		mRatingBar.setRating(0);
		mAvatar.setImageResource(-1);
		mHeadbarIcon.setText("注册/登录");
		if (loginStatu != 0) {
			refreshAvatar();
			checkLabel();
			mUserName.setText(userName);
			mRatingBar.setRating(rating);
			mHeadbarIcon.setText("注销");
		}
	}

	private void checkLabel() {
		// TODO Auto-generated method stub
		label = mApplication.getSpUtil().getLabel();
		for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
			if ((label & (1 << i)) != 0) {
				switch (i) {
				case 2:
					mIdentify.setVisibility(View.VISIBLE);
					break;
				case 1:
					mVerify.setVisibility(View.VISIBLE);
					break;
				case 0:
					mListen.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	}

	@Override
	public void iconClick() {
		super.iconClick();
		String text = mHeadbarIcon.getText().toString().trim();
		if (!TextUtils.isEmpty(text) && text.equals("注销")) {
			showLoadingDialog("");
			dialog = new LogoutDialog(getActivity());
			dialog.setOnLogoutClickListener(this, "确定要注销吗？", "");
			dialog.show();
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		switch (v.getId()) {
		case R.id.layout_setting_info:
			if (loginStatu != 0) {
				intent = new Intent(getActivity(),
						PersonalSettingActivity.class);
				startActivityByPendingTransition(intent);
			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 5000);
			}
			break;
		case R.id.layout_my_collect:
		case R.id.tvbtn_my_collect:
			if (loginStatu != 0) {
				intent = new Intent(getActivity(), CollectActivity.class);
				startActivityByPendingTransition(intent);
			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 5001);
			}
			break;
		case R.id.tvbtn_my_upload:
		case R.id.layout_my_upload:
			if (loginStatu != 0) {
				intent = new Intent(getActivity(), MyUploadActivity.class);
				startActivityByPendingTransition(intent);
			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 5002);
			}
			break;
		case R.id.tvbtn_check_update:
		case R.id.layout_check_update:
			UpdateManager manager = new UpdateManager(getActivity());
			manager.checkUpdate();
			break;
		case R.id.tvbtn_about_our:
		case R.id.layout_about_our:
			intent = new Intent(getActivity(), AboutOurActivity.class);
			startActivityByPendingTransition(intent);
			break;
		case R.id.tvbtn_binding:
		case R.id.layout_binding:
			if (loginStatu != 0) {
				BindChoiceDialog dialog = new BindChoiceDialog(getActivity(),
						getScreenHeight());
				dialog.setOnBindChoiceClickListener(this);
				dialog.show();
			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivityByPendingTransition(intent);
			}
			break;
		case R.id.tvbtn_setting:
		case R.id.layout_setting:
			intent = new Intent(getActivity(), SystemSettingActivity.class);
			startActivityByPendingTransition(intent);
			break;
		}
	}

	@Override
	public void logoutOnClick(View v) {

		dismissLoadingDialog();
		switch (v.getId()) {
		case R.id.btn_logout_confirm:
			mApplication.getSpUtil().clearSharePerference();
			mApplication.getSpUtil().isCardListFlushFlag(true);
			refreshData();
			break;
		case R.id.btn_logout_cancle:
			dialog.dismiss();
			break;

		default:
			break;
		}
	}

	private void refreshAvatar() {

		String avatar = mApplication.getSpUtil().getUserAvatar();
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, mAvatar,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mAvatar.setImageResource(-1);
		}
	}

	@Override
	public void choiceOnClick(View v) {

		switch (v.getId()) {
		case R.id.binding_cancel:

			break;
		case R.id.binding_qq:
			onBindQQ();
			break;
		case R.id.binding_sina:
			onBindSina();
			break;

		default:
			break;
		}
	}

	/**
	 * 绑定QQ
	 */
	private void onBindQQ() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			showShortToast(R.string.text_error_network);
			return;
		}
		String qId = mApplication.getSpUtil().getQQOpedId();
		if (!TextUtils.isEmpty(qId)) {
			showLongToast("您已绑定过QQ账号！");
			return;
		} else {
			if (!mQQAuth.isSessionValid()) {
				IUiListener listener = new BaseUiListener() {
					@Override
					protected void doComplete(JSONObject values) {
						updateUserInfo();
					}
				};
				mQQAuth.login(getActivity(), "all", listener);
				mTencent.login(getActivity(), "all", listener);
			} else {
				mQQAuth.logout(getActivity());
			}
		}

	}

	/**
	 * 绑定新浪微博
	 */
	private void onBindSina() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			showShortToast(R.string.text_error_network);
			return;
		}
		String sId = mApplication.getSpUtil().getSinaUid();
		if (!TextUtils.isEmpty(sId)) {
			showLongToast("您已绑定过新浪微博账号！");
			return;
		} else {
			mWeiboAuth = new WeiboAuth(mApplication.getApplicationContext(),
					Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
			mSsoHandler = new SsoHandler(getActivity(), mWeiboAuth);

			mSsoHandler.authorize(new AuthListener());
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		final Context context = getActivity();
		final Context ctxContext = context.getApplicationContext();
		mAppid = Constants.APP_ID_QQ;
		mQQAuth = QQAuth.createInstance(mAppid, ctxContext);
		mTencent = Tencent.createInstance(mAppid, getActivity());
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
								Bitmap bm = null;
								try {
									QQImageUri = json
											.getString("figureurl_qq_2");
									mApplication.getSpUtil().setQQAvatarUrl(
											QQImageUri);
									// bm = Util.getbitmap(QQImageUri);
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
			mInfo = new UserInfo(getActivity(), mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		}
	}

	String QQName = null;
	String SinaName = null;
	String QQImageUri = null;
	String SinaImageUri = null;
	String QQId = null;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				checkLabel();
				refreshAvatar();
				break;
			case 100:
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						QQName = response.getString("nickname");
						mApplication.getSpUtil().setQQName(QQName);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 101:
				final int type = (int) msg.obj;
				String url;
				dismissLoadingDialog();
				if (type == 2) {
					url = UrlEngine.getOpenIdLogin(userId, QQId, "");
				} else {
					url = UrlEngine.getOpenIdLogin(userId, "", sinaId);
				}
				HttpUtil.post(url, new JsonHttpResponseHandler() {

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
								if (response.toString().equals(
										Constants.BINDING_ERROR)) {
									if (type == 2) {
										showLongToast("该QQ账号已有绑定用户");
									} else {
										showLongToast("该新浪微博账号已有绑定用户");
									}
								} else {
									String token = response.getString("token");
									mApplication.getSpUtil().setToken(token);
									if (type == 2) {
										mApplication.getSpUtil().setQQOpedId(
												QQId);
									} else {
										mApplication.getSpUtil().setSinaUid(
												sinaId);
									}
									showLongToast("绑定成功！");
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
			// Util.toastMessage(PersonalHomeActivity.this, "onError: "
			// + e.errorDetail);
			// Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			// Util.toastMessage(PersonalHomeActivity.this, "onCancel: ");
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
				SharePreferenceUtil.writeAccessToken(getActivity(),
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (mSsoHandler != null) {
			showLoadingDialog("请稍等……");
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (resultCode == Activity.RESULT_OK) {
			Intent intent;
			switch (requestCode) {
			case 5000:
				intent = new Intent(getActivity(),
						PersonalSettingActivity.class);
				startActivityByPendingTransition(intent);
				break;
			case 5001:
				intent = new Intent(getActivity(), CollectActivity.class);
				startActivityByPendingTransition(intent);
				break;
			case 5002:
				intent = new Intent(getActivity(), MyUploadActivity.class);
				startActivityByPendingTransition(intent);
				break;
			}
		}
	}

	private void onRefresh() {
		// TODO Auto-generated method stub
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu != 0) {
			TokenCheckUtil.checkToken();
			mHandler.sendEmptyMessageDelayed(1, 1000);
		}
		refreshData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onRefresh();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		String str = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 设置上一次刷新的提示标签
		refreshView.getLoadingLayoutProxy()
				.setLastUpdatedLabel("最后更新时间:" + str);
		// 下拉刷新 业务代码
		if (refreshView.isShownHeader()) {
			// 设置刷新标签
			mScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
			// 设置下拉标签
			mScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
			// 设置释放标签
			mScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
			onRefresh();
			mScrollView.onRefreshComplete();
		}
		// 上拉加载更多 业务代码
		if (refreshView.isShownFooter()) {
			// 设置刷新标签
			mScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
			// 设置下拉标签
			mScrollView.getLoadingLayoutProxy().setPullLabel("上拉加载");
			// 设置释放标签
			mScrollView.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
		}
	}
}
