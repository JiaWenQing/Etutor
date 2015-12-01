package net.dx.etutor.activity.base;

import net.dx.etutor.R;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.dialog.LoadingDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

	private TextView mHeadbarBack;
	private TextView mHeadbarIcon;
	private String mTitle;
	protected EtutorApplication mApplication;
	protected Activity mActivity;
	protected Context mContext = EtutorApplication.getInstance()
			.getApplicationContext();
	protected View mView;
	protected LoadingDialog mLoadingDialog;
	private boolean mArrowVisible = false, mIconVisible = true;
	private int mResId;
	private String mText;
	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected static int mScreenWidth;
	protected static int mScreenHeight;
	protected float mDensity;

	public BaseFragment() {
		super();
	}

	public BaseFragment(EtutorApplication application, Activity activity,
			Context context) {
		mApplication = application;
		mActivity = activity;
		mContext = context;
		mLoadingDialog = new LoadingDialog(context, "请稍后");

		/**
		 * 获取屏幕宽度、高度、密度
		 */
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}

	public static int getScreenHeight() {
		return mScreenHeight;
	}

	public static int getScreenWidth() {
		return mScreenWidth;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initViews();
		initEvents();
		initData();
		initHeaderbar();
		return mView;
	}

	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	/** 短暂显示Toast提示(来自res) **/
	public void showShortToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	public void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	public void showLongToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	public void showLongToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	private OnClickListener mHeadbarIconListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			iconClick();
		}
	};

	public void initHeaderbar() {
		mHeadbarBack = (TextView) findViewById(R.id.main_head_bar_back);
		TextView headbarTitle = (TextView) findViewById(R.id.main_head_bar_title);
		mHeadbarIcon = (TextView) findViewById(R.id.main_head_bar_icon);
		headbarTitle.setText(mTitle);
		if (mArrowVisible) {
			mHeadbarBack.setVisibility(View.VISIBLE);
		} else {
			mHeadbarBack.setVisibility(View.GONE);
		}

		if (mIconVisible) {
			mHeadbarIcon.setVisibility(View.VISIBLE);
			if (mResId != 0) {
				Drawable drawable = getResources().getDrawable(mResId);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mHeadbarIcon.setCompoundDrawables(null, null, drawable, null);
			} else {
				mHeadbarIcon.setCompoundDrawables(null, null, null, null);
			}
			mHeadbarIcon.setText(mText);
		} else {
			mHeadbarIcon.setVisibility(View.GONE);
		}
		mHeadbarIcon.setOnClickListener(mHeadbarIconListener);
	}

	protected void iconClick() {

	};

	// 显示后退图标,默认不显示
	public void showBackArrow() {
		mArrowVisible = true;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setTitle(int resId) {
		mTitle = getResources().getString(resId);
	}

	public void settingIcon(int resId, boolean flag, String text) {
		mIconVisible = flag;
		mResId = resId;
		mText = text;
	}

	public void startActivityByPendingTransition(Intent intent) {
		startActivity(intent);
		mActivity.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public void startActivityForGroupByPendingTransition(Intent intent) {
		startActivity(intent);
		mActivity.getParent().overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public void startActivityForResultByPendingTransition(Intent intent,
			int requestCode) {
		startActivityForResult(intent, requestCode);
		mActivity.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public void setResultByPendingTransition(int resultCode, Intent intent) {
		mActivity.setResult(resultCode, intent);
		mActivity.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public void finishByPendingTransition() {
		mActivity.overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
	}

	// 初始控件,布局文件
	protected abstract void initViews();

	// 初始事件
	protected abstract void initEvents();

	// 初始数据
	protected abstract void initData();
}
