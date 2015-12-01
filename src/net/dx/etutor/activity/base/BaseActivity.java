package net.dx.etutor.activity.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.dialog.LoadingDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 基类Activity
 * 
 * @author app
 * 
 */

public abstract class BaseActivity extends Activity {

	private TextView mHeadbarBack;
	private TextView mHeadbarIcon;
	private String mTitle;
	private String mNext;
	private int mResId;
	public EtutorApplication mApplication;

	/** 屏幕宽度 **/
	protected static int mScreenWidth;
	/** 屏幕高度 **/
	protected static int mScreenHeight;
	/** 屏幕密度 **/
	protected float mDensity;
	/** 进程任务 **/
	protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();
	protected LoadingDialog mLoadingDialog;
	private boolean mArrowVisible = true, mIconVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		mApplication = EtutorApplication.getInstance();
		mLoadingDialog = new LoadingDialog(this, "请稍后");

		// registerReceiver();
		initViews();
		initEvents();
		initHeaderbar();
	}

	public static int getScreenHeight() {
		return mScreenHeight;
	}

	public static int getScreenWidth() {
		return mScreenWidth;
	}

	/** 添加AsyncTask **/
	public void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
		mAsyncTasks.add(asyncTask.execute());
	}

	/** 初始化视图 **/
	public abstract void initViews();

	/** 初始化事件 **/
	public abstract void initEvents();

	/** 短暂显示Toast提示(来自res) **/
	public void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	public void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	public void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	public void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/** Debug输出Log日志 **/
	public void showLogDebug(String tag, String msg) {
	}

	/** 显示自定义Toast提示(来自res) **/
	protected void showCustomToast(int resId) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text))
				.setText(getString(resId));
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** Error输出Log日志 **/
	public void showLogError(String tag, String msg) {
	}

	/** 含有标题和内容的对话框 **/
	public AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	public AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 含有标题、内容、图标、两个按钮的对话框 **/
	public AlertDialog showAlertDialog(String title, String message, int icon,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 清空 **/
	public void clearAsyncTask() {
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks
				.iterator();
		while (iterator.hasNext()) {
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}
		mAsyncTasks.clear();
	}

	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
			mLoadingDialog.setCanceledOnTouchOutside(false);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	/** 默认退出 **/
	public void defaultFinish() {
		int loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu == 3) {
			Intent intent = new Intent(BaseActivity.this,
					MainFragmentActivity.class);
			startActivityByPendingTransition(intent);
			finish();
		} else {
			BaseActivity.this.finishByPendingTransition();
		}
	}

	/**
	 * 初始化标题栏
	 * 
	 */
	public void initHeaderbar() {
		mHeadbarBack = (TextView) findViewById(R.id.main_head_bar_back);
		TextView headbarTitle = (TextView) findViewById(R.id.main_head_bar_title);
		mHeadbarIcon = (TextView) findViewById(R.id.main_head_bar_icon);
		headbarTitle.setText(mTitle);
		mHeadbarBack.setOnClickListener(mHeadbarBackListener);

		if (mArrowVisible) {
			mHeadbarBack.setVisibility(View.VISIBLE);
		} else {
			mHeadbarBack.setVisibility(View.GONE);
		}

		if (mIconVisible) {
			if (mResId == 0) {
				mHeadbarIcon.setText(mNext);
			} else {
				mHeadbarIcon.setVisibility(View.VISIBLE);
				Drawable drawable = getResources().getDrawable(mResId);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mHeadbarIcon.setCompoundDrawables(null, null, drawable, null);
			}
		} else {
			mHeadbarIcon.setVisibility(View.GONE);
		}
		mHeadbarIcon.setOnClickListener(mHeadbarIconListener);
	}

	/**
	 * 设置显示和隐藏
	 * 
	 * @param backVisibility
	 * @param iconVisibility
	 */
	public void setCommonTitle(int backVisibility, int iconVisibility) {
		mHeadbarBack.setVisibility(backVisibility);
		mHeadbarIcon.setVisibility(iconVisibility);
	}

	public void setCommonTitle(int iconVisibility) {
		mHeadbarIcon.setVisibility(iconVisibility);
	}

	/**
	 * 后退按钮按钮事件
	 */
	private OnClickListener mHeadbarBackListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			defaultFinish();
		}
	};

	private OnClickListener mHeadbarIconListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			iconClick();
		}
	};

	public void showIcon(int resId) {
		mIconVisible = true;
		mResId = resId;

	}

	public void showIcon(int resId, String text) {
		mIconVisible = true;
		mResId = resId;
		mNext = text;

	}

	public void hiddenBackArrow() {
		mArrowVisible = false;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setTitle(int resId) {
		mTitle = getResources().getString(resId);
	}

	public void setIcon(int icon) {
		mIconVisible = true;
		mResId = icon;
	}

	public void startActivityByPendingTransition(Intent intent) {
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public void startActivityForGroupByPendingTransition(Intent intent) {
		startActivity(intent);
		getParent().overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public void startActivityForResultByPendingTransition(Intent intent,
			int requestCode) {
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public void setResultByPendingTransition(int resultCode, Intent intent) {
		setResult(resultCode, intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public void finishByPendingTransition() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void iconClick() {
	};

	/*
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * super.onDestroy(); if (connectionReceiver != null) {
	 * unregisterReceiver(connectionReceiver); } }
	 *//**
	 * 注册receiver
	 */
	/*
	 * private void registerReceiver() { // TODO Auto-generated method stub
	 * IntentFilter intentFilter = new IntentFilter();
	 * intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 * registerReceiver(connectionReceiver, intentFilter); } BroadcastReceiver
	 * connectionReceiver = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) {
	 * ConnectivityManager connectMgr = (ConnectivityManager)
	 * getSystemService(CONNECTIVITY_SERVICE); NetworkInfo mobNetInfo =
	 * connectMgr .getNetworkInfo(ConnectivityManager.TYPE_MOBILE); NetworkInfo
	 * wifiNetInfo = connectMgr .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	 * if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) { if
	 * (mNetStatu) { AlertDialog.Builder ab = new AlertDialog.Builder(context);
	 * // 设定标题 ab.setMessage("当前网络不可用，请检查网络"); // 设定退出按钮
	 * 
	 * // 网络设置按钮 ab.setPositiveButton("确定", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { //
	 * TODO Auto-generated method stub dialog.dismiss();
	 * BaseActivity.this.finish(); } }).show(); } mNetStatu = false; } else {
	 * mNetStatu = true; } }
	 * 
	 * };
	 */
}
