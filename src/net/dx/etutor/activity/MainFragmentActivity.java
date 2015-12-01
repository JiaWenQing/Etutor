package net.dx.etutor.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.activity.fragment.ForumListFragment;
import net.dx.etutor.activity.fragment.HomePageFragment;
import net.dx.etutor.activity.fragment.MessageListFragment;
import net.dx.etutor.activity.fragment.MyFragment;
import net.dx.etutor.activity.fragment.OrderListFragment;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.db.StatisticDao;
import net.dx.etutor.dialog.LogoutDialog;
import net.dx.etutor.dialog.LogoutDialog.OnLogoutClickListener;
import net.dx.etutor.model.DxStatistic;
import net.dx.etutor.model.Version;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.UpdateManager;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class MainFragmentActivity extends BaseFragmentActivity implements
		OnLogoutClickListener, OnClickListener {

	public static String TAG = "MainFragmentActivity";
	private ImageButton mHomeButton, mMessageButton, mOrderButton, mMineButton,
			mForumButton;
	private HomePageFragment homePageFragment;
	private MessageListFragment messageFragment;
	private OrderListFragment orderFragment;
	private MyFragment myFragment;
	private ForumListFragment forumListFragment;
	private ImageView mMessagePoint;
	private ImageView mOrderPoint;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private Intent mServiceIntent;
	private boolean flag;
	private Version version = new Version();
	private LogoutDialog dialog;
	protected StatisticDao dao;
	private int i = 1;
	private View currentButton;
	private boolean msgStatu;

	/**
	 * 1 我的收藏 2 新入住教师4 推荐教师8我的帖子
	 */
	private List<Integer> cardType;
	private List<Map<Integer, Object>> mCardList = new ArrayList<Map<Integer, Object>>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setTitle("MainActivity");
		flag = mApplication.getSpUtil().getFlag();
		handler.sendEmptyMessage(100);
		mHomeButton = (ImageButton) findViewById(R.id.rb_menu_homepage);
		mMessageButton = (ImageButton) findViewById(R.id.rb_menu_message);
		mOrderButton = (ImageButton) findViewById(R.id.rb_menu_order);
		mMineButton = (ImageButton) findViewById(R.id.rb_menu_personal);
		mForumButton = (ImageButton) findViewById(R.id.rb_menu_forum);
		mMessagePoint = (ImageView) findViewById(R.id.iv_message_point);
		mOrderPoint = (ImageView) findViewById(R.id.iv_order_point);

		mOrderPoint.setVisibility(View.INVISIBLE);

		checkNewMsg();

		mApplication.getSpUtil().setFlag(false);

		fragmentManager = getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();

		initData();

	}

	public void checkNewMsg() {
		msgStatu = mApplication.getSpUtil().getMsgStatu();
		if (msgStatu) {
			mMessagePoint.setVisibility(View.VISIBLE);
		} else {
			mMessagePoint.setVisibility(View.INVISIBLE);
			String count = mApplication.getSpUtil().getMsgCount();
			if (!TextUtils.isEmpty(count)) {
				char[] c = count.toCharArray();
				for (int i = 0; i < c.length; i++) {
					if (c[i] == '1') {
						mMessagePoint.setVisibility(View.VISIBLE);
						return;
					}
				}
			}
		}
	}

	private void initData() {
		dao = new StatisticDao(this);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mHomeButton.setOnClickListener(this);
		mMessageButton.setOnClickListener(this);
		mOrderButton.setOnClickListener(this);
		mMineButton.setOnClickListener(this);
		mForumButton.setOnClickListener(this);
		mHomeButton.performClick();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 100:
				checkVersion();
				break;
			case 102:
				if (null == forumListFragment) {
					forumListFragment = new ForumListFragment(mApplication,
							MainFragmentActivity.this,
							MainFragmentActivity.this);
				}
				transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.fragment_main, forumListFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				// mForumButton.setPressed(true);
				mForumButton.performClick();
				// mForumButton.setBackgroundResource(R.drawable.menu_forum_pressed);
				break;
			case 103:
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// String time = DateUtil.getTimeOfBaidu();
						String time = DateUtil.getStringByFormat(
								System.currentTimeMillis(),
								DateUtil.dateFormatYMDHMS);
						int id = mApplication.getSdUtil().getStatisticId();
						DxStatistic dxStatistic;
						boolean flag = mApplication.getSdUtil().getFlag();
						if (flag) {
							if (!TextUtils.isEmpty(time)) {
								if (id != -1) {
									dxStatistic = new DxStatistic();
									dxStatistic.setEndTime(time);
									dxStatistic.setUploadFlag(1);
									dao.update(id, dxStatistic);
								} else {
									dxStatistic = new DxStatistic();
									dxStatistic.setEndTime(time);
									dxStatistic.setUploadFlag(1);
									dao.addDxStatistic(dxStatistic);
								}
							}
						}
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}

				}.start();

				break;
			default:
				break;
			}
		}

	};

	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	private void checkVersion() {
		// TODO Auto-generated method stub
		final String versionName;
		try {

			versionName = mApplication.getVersionNumber();
			String url = UrlEngine.checkVersionInfo();
			System.out.println(TAG + "==" + url);
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
					if (statusCode == Constants.STAT_200) {
						version.initWithAttributes(response);
						if (!versionName.equals(version.getVersion())) {
							updateVersion();
							mApplication.getSpUtil().setVersionInfo(
									R.string.text_update_version);
						} else {
							mApplication.getSpUtil().setVersionInfo(-1);
						}
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateVersion() {
		// TODO Auto-generated method stub
		String info = version.getDescription().toString().trim();
		showLoadingDialog("");
		dialog = new LogoutDialog(this);
		dialog.setOnLogoutClickListener(this, info,
				"检测到新版本V" + version.getVersion());
		dialog.show();
	}

	@Override
	public void logoutOnClick(View v) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		switch (v.getId()) {
		case R.id.btn_logout_confirm:// 是否注销
			if (TextUtils.isEmpty(version.getDescription())) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(version.getUrl()));
				startActivity(intent);
			} else {
				if (version.getDescription().equals("MyServer")) {
					UpdateManager manager = new UpdateManager(
							MainFragmentActivity.this, version);
					manager.showDownloadDialog();
				} else {
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(version.getUrl()));
					startActivity(intent);
				}
			}
			dialog.dismiss();
			break;
		case R.id.btn_logout_cancle:
			dialog.dismiss();
			break;
		}

	}

	private long time = 0;
	public long endTime;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - time > 2000) {
			time = System.currentTimeMillis();
			// stopService(mServiceIntent);
			Toast.makeText(this, "再按一次退出", 0).show();
		} else {
			MobclickAgent.onKillProcess(this);// 保存统计数据
			handler.sendEmptyMessage(103);
		}
	}

	public class getNetWorkTime extends AsyncTask<String, String, Long> {

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub

			endTime = result;
			if (endTime != 0) {
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						endTime += 1000;
					}
				}, 1000, 1000);
			}
		}

		@Override
		protected Long doInBackground(String... params) {
			return DateUtil.getLongTimeOfBaidu();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_menu_homepage:
			if (null == homePageFragment) {
				homePageFragment = new HomePageFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this,
						handler);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, homePageFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.rb_menu_message:
			if (null == messageFragment) {
				messageFragment = new MessageListFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, messageFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.rb_menu_order:
			if (null == orderFragment) {
				orderFragment = new OrderListFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, orderFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.rb_menu_forum:
			if (null == forumListFragment) {
				forumListFragment = new ForumListFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, forumListFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.rb_menu_personal:
			if (null == myFragment) {
				myFragment = new MyFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, myFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		default:
			if (null == forumListFragment) {
				forumListFragment = new ForumListFragment(mApplication,
						MainFragmentActivity.this, MainFragmentActivity.this);
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_main, forumListFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		}
		setButton(v);
	}

	public List<Integer> getCardType() {
		return cardType;
	}

	public void setCardType(List<Integer> cardType) {
		this.cardType = cardType;
	}

	public List<Map<Integer, Object>> getmCardList() {
		return mCardList;
	}

	public void setmCardList(List<Map<Integer, Object>> mCardList) {
		this.mCardList = mCardList;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		new getNetWorkTime().execute();
		super.onStop();
	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		int id = mApplication.getSdUtil().getStatisticId();
		DxStatistic dxStatistic;
		boolean flag = mApplication.getSdUtil().getFlag();
		String eTime = DateUtil.getStringByFormat(endTime,
				DateUtil.dateFormatYMDHMS);
		if (flag) {
			if (!TextUtils.isEmpty(eTime)) {
				if (id != -1) {
					dxStatistic = new DxStatistic();
					dxStatistic.setEndTime(eTime);
					dxStatistic.setUploadFlag(1);
					dao.update(id, dxStatistic);
				} else {
					dxStatistic = new DxStatistic();
					dxStatistic.setEndTime(eTime);
					dxStatistic.setUploadFlag(1);
					dao.addDxStatistic(dxStatistic);
				}
			}
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		checkNewMsg();
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
