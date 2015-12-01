package net.dx.etutor.activity.setting;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;

public class HelpActivity extends BaseActivity {
	
	public static String TAG = "HelpActivity";
	private WebView mWebView;

	@SuppressLint("NewApi")
	@Override
	public void initViews() {
		setContentView(R.layout.activity_user_help);
		setTitle(R.string.setting_help);
		mWebView = (WebView) findViewById(R.id.help_webview);
		mWebView.loadUrl("file:///android_asset/help.html");
		mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
	}

	@Override
	public void initEvents() {

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
