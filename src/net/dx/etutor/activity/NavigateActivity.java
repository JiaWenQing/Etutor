package net.dx.etutor.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.GlobalData;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.db.StatisticDao;
import net.dx.etutor.model.DxStatistic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.ExampleUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.Md5Util;
import net.dx.etutor.util.NetWorkHelperUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("InflateParams")
public class NavigateActivity extends BaseFragmentActivity implements
		OnClickListener {
//	private static final String TAG = "JPush";
	public static String TAG = "NavigateActivity";
	private ViewPager mViewPager;
	private Button mStart;
	private List<View> list = new ArrayList<View>();
	private MyAdapter adapter;
	private View view1, view2, view3,view4;
	private LayoutInflater inflater;
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private StatisticDao dao;
	private long id;
	private ImageView image_btn;

	@SuppressLint("InflateParams")
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_navigate);
		setTitle("navigate");
		mViewPager = (ViewPager) findViewById(R.id.vp_navigate);
		inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.navigate_pager1, null);
		view2 = inflater.inflate(R.layout.navigate_pager2, null);
		view3 = inflater.inflate(R.layout.navigate_pager3, null);
		view4 =inflater.inflate(R.layout.navigate_pager4, null);
		image_btn=(ImageView) view4.findViewById(R.id.image_btn);
		image_btn.setOnClickListener(this);
		dao = new StatisticDao(this);
		initViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	public void initViewPager() {
		list.add(view1);
		list.add(view2);
		list.add(view3);
		list.add(view4);
		adapter = new MyAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		String versionNumber = mApplication.getVersionNumber();
		mApplication.getSdUtil().setVersionNumber(versionNumber);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_btn:
			onBegin();
			break;

		default:
			break;
		}

	}

	private void onBegin() {
		// TODO Auto-generated method stub
		String uuid = mApplication.getSpUtil().getUuid();
		if (TextUtils.isEmpty(uuid)) {
			if (!NetWorkHelperUtil.checkNetState(this)) {
				showShortToast(R.string.network_error);
				return;
			}
			String url = UrlEngine.getUuid();
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
						final String uuid;
						try {
							uuid = response.getString("uuid");
							GlobalData.instance().uuid = uuid;
							EtutorApplication.getInstance().getSpUtil()
									.setUuid(uuid);
							String deviceId = 1 + Md5Util.md52(uuid
									+ mApplication.getImei());
							EtutorApplication.getInstance().getSpUtil()
									.setDeviceId(deviceId);
							EtutorApplication.getInstance().getSpUtil()
									.setPushFlag(true);
							mHandler.sendMessage(mHandler.obtainMessage(
									MSG_SET_ALIAS, deviceId));
							// setStatisticReceiver();
							registNotifi();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		} else {
			registNotifi();
		}

	}

	public void registNotifi() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String url = UrlEngine.registNotifi();
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					startActivity(new Intent(NavigateActivity.this,
							MainFragmentActivity.class));
					NavigateActivity.this.finish();
				}
			}
		});
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager vp = (ViewPager) container;
			vp.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ViewPager vp = (ViewPager) container;
			vp.addView(list.get(position), 0);
			return list.get(position);
		}

	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
			}
		}
	};

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

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
