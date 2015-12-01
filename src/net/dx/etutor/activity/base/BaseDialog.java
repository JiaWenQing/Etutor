package net.dx.etutor.activity.base;

import net.dx.etutor.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaseDialog extends Dialog {
	private Context mContext;// 上下文
	private TextView mTitle;// 标题
	private LinearLayout mLayoutContent;// 内容根布局

	public BaseDialog(Context context) {
		super(context, R.style.location_theme_dialog);
		mContext = context;
		setContentView(R.layout.dialog_common_layout);
		initViews();
		initEvents();
	}

	private void initEvents() {
		mTitle = (TextView) findViewById(R.id.common_dialog_layout_title);
		mLayoutContent = (LinearLayout) findViewById(R.id.common_dialog_layout_content);
	}

	private void initViews() {

	}

	/**
	 * 填充新布局到内容布局
	 * 
	 * @param resource
	 */
	public void setDialogContentView(int resource) {
		View v = LayoutInflater.from(mContext).inflate(resource, null);
		if (mLayoutContent.getChildCount() > 0) {
			mLayoutContent.removeAllViews();
		}
		mLayoutContent.addView(v);
	}

	/**
	 * 填充新布局到内容布局
	 * 
	 * @param resource
	 * @param params
	 */
	public void setDialogContentView(int resource,
			LinearLayout.LayoutParams params) {
		View v = LayoutInflater.from(mContext).inflate(resource, null);
		if (mLayoutContent.getChildCount() > 0) {
			mLayoutContent.removeAllViews();
		}
		mLayoutContent.addView(v, params);
	}

	/**
	 * 设置标题
	 */
	public void setDialogTitle(int resource) {
		String text = mContext.getResources().getString(resource);
		if (text != null) {
			// mLayoutTitle.setVisibility(View.VISIBLE);
			mTitle.setText(text);
		} else {
			mTitle.setText("这是对话框");
		}
	}

	public void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	public void showShortToast(int resId) {
		Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 设置标题
	 */
	public void setDialogTitle(String text) {
		if (text != null) {
			// mLayoutTitle.setVisibility(View.VISIBLE);
			mTitle.setText(text);
		} else {
			mTitle.setText("这是对话框");
		}
	}
}
