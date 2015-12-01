package net.dx.etutor.activity.setting;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class SystemSettingActivity extends BaseActivity implements
		OnClickListener {

	public static String TAG = "SystemSettingActivity";
	private TextView mFeedback;
	private TextView mHelp;
	private LinearLayout mHelpLayout;
	private LinearLayout mBackLayout;
	private ToggleButton mPush;
	private int loginStatu;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting);
		setTitle(R.string.text_setting);
		mFeedback = (TextView) findViewById(R.id.setting_back);
		mHelp = (TextView) findViewById(R.id.setting_help);
		mPush = (ToggleButton) findViewById(R.id.setting_push);
		mHelpLayout = (LinearLayout) findViewById(R.id.setting_help_layout);
		mBackLayout = (LinearLayout) findViewById(R.id.setting_back_layout);
		if (EtutorApplication.getInstance().getSpUtil().getPushFlag()) {
			mPush.setChecked(true);
		} else {
			mPush.setChecked(false);
		}
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mFeedback.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mHelpLayout.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		mPush.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String urlString = UrlEngine.registNotifi();
				if (isChecked) {
					// 开启推送
					urlString = UrlEngine.registNotifi();
					JPushInterface.resumePush(SystemSettingActivity.this);
					EtutorApplication.getInstance().getSpUtil()
							.setPushFlag(true);
				} else {
					// 关闭推送
					urlString = UrlEngine.logoutNotifi();
					JPushInterface.stopPush(SystemSettingActivity.this);
					EtutorApplication.getInstance().getSpUtil()
							.setPushFlag(false);
				}
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
							JSONArray response) {
						if (statusCode == Constants.STAT_200) {
						}
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_back:
		case R.id.setting_back_layout:
			loginStatu = mApplication.getSpUtil().getLoginStatu();
			if (loginStatu == 0) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1000);
			} else {
				intent = new Intent(this, FeedbackActivity.class);
				startActivityByPendingTransition(intent);
			}
			break;
		case R.id.setting_help:
		case R.id.setting_help_layout:
			onHelp();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Intent intent;
			switch (requestCode) {
			case 1000:
				intent = new Intent(this, FeedbackActivity.class);
				startActivityByPendingTransition(intent);
				break;
			}
		}
	}

	/**
	 * 使用帮助
	 */
	private void onHelp() {
		Intent intent = new Intent(SystemSettingActivity.this,
				HelpActivity.class);
		startActivityByPendingTransition(intent);
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
