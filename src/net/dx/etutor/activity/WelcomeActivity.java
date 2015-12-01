package net.dx.etutor.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.db.StatisticDao;
import net.dx.etutor.model.DxStatistic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.TokenCheckUtil;

import org.apache.http.Header;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class WelcomeActivity extends BaseActivity{
	
	public static String TAG = "WelcomeActivity";
	private boolean isFirstEnter = true;
	private int loginStatu;
	private String userAvatar;
	private String avatarUrl;
	public String AppProvince;
	public String AppCity;
	public String AppRegion;
	private long id;
	private StatisticDao dao;
	private DxStatistic dxStatistic;
	private String startTime;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 100:
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						startTime = DateUtil.getTimeOfBaidu();
						dxStatistic = new DxStatistic();
						dxStatistic.setStartTime(startTime);
						dxStatistic.setUploadFlag(0);
						mApplication.getSdUtil().setStatisticId(-1);
						mApplication.getSdUtil().setFlag(false);
						new Timer().schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!TextUtils.isEmpty(startTime)) {
									id = dao.addDxStatistic(dxStatistic);
									mApplication.getSdUtil().setStatisticId(id);
								}
								mApplication.getSdUtil().setFlag(true);
							}
						}, 15 * 1000);
					}

				}.start();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_welcome);
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
		}
		setTitle("welcome");
		isFirstEnter = mApplication.getSpUtil().getFlag();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		userAvatar = mApplication.getSpUtil().getUserAvatar();
		avatarUrl = mApplication.getSpUtil().getAvatarUrl();
		if (loginStatu != 0) {
			TokenCheckUtil.checkToken();
		}
		dao = new StatisticDao(this);
		updateStatisticUpLoadFlag();
		checkUserAvatar();
		initStatistic();
		enterApplicationChoose();
		handler.sendEmptyMessage(100);
		mApplication.getSpUtil().isFirstEnterOrder(true);
		mApplication.reflushLocation();
	}

	private void updateStatisticUpLoadFlag() {
		// TODO Auto-generated method stub
		dao.updateFlagAll();
	}

	private void checkUserAvatar() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(userAvatar) && !TextUtils.isEmpty(avatarUrl)
				&& loginStatu != 0) {
			File file = new File(userAvatar.substring(8));
			if (!file.exists()) {
//				getUserAvatar();
			}
		}
	}

	private void initStatistic() {
		// TODO Auto-generated method stub
		mApplication.getSdUtil().setEnterTime(System.currentTimeMillis());

		setStatisticReceiver();
		// 友盟统计
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);// 关闭activity自带的页面统计
		MobclickAgent.updateOnlineConfig(this);// 发送策略
		AnalyticsConfig.enableEncrypt(true);// 日志加密
		AnalyticsConfig.setChannel(Constants.APP_CHANNEL);// 渠道
		MobclickAgent.onEvent(this, "startNumber", "startNumber");

	}

	private void enterApplicationChoose() {
		// TODO Auto-generated method stub

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isFirstEnter) {
					Intent intent = new Intent(WelcomeActivity.this,
							NavigateActivity.class);
					startActivity(intent);
				} else {
					startActivity(new Intent(WelcomeActivity.this,
							MainFragmentActivity.class));
				}
				finish();
			}
		}, 1000);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
	}

	/**
	 * 异步获取头像
	 */
	public void getUserAvatar() {
		String urlString = DataParam.REMOTE_SERVE + avatarUrl;
		HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
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
					byte[] responseBody) {
				if (statusCode == Constants.STAT_200) {
					BitmapFactory factory = new BitmapFactory();
					@SuppressWarnings("static-access")
					Bitmap bitmap = factory.decodeByteArray(responseBody, 0,
							responseBody.length);
					PictureUtil.saveBitmap(
							Constants.MyAvatarDir,
							avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1),
							bitmap, true);
					EtutorApplication
							.getInstance()
							.getSpUtil()
							.setUserAvatar(
									"file:////sdcard/etutor/avatar/"
											+ avatarUrl.substring(avatarUrl
													.lastIndexOf("/") + 1));
				}
			}
		});

	}

	private void setStatisticReceiver() {
		// TODO Auto-generated method stub
		AlarmManager am = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("net.dx.etutor.receiver.StatisticReceiver");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mApplication.getSdUtil().setFirstUseTime(
				mApplication.getSdUtil().getFirstUseTime());
		long timeInMillis = mApplication.getSdUtil().getFirstUseTime();
		long intervalMillis = 60000;
		// long intervalMillis = AlarmManager.INTERVAL_HALF_HOUR;
		long nowTime = System.currentTimeMillis();
		do {
			timeInMillis = timeInMillis + intervalMillis;
		} while (timeInMillis < nowTime);
		am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalMillis,
				pi);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause
		MobclickAgent.onPause(this);
	}
}
