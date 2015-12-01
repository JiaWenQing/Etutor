package net.dx.etutor.activity.base;

import net.dx.etutor.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseWheelDialog extends Dialog  {
	private Context mContext;// 上下文
	private LinearLayout mLayoutContent;// 内容根布局
	private TextView mBack;
	private TextView mOk;
	private OnClickListener mOnBackClickListener;
	private OnClickListener mOnOkClickListener;

	public BaseWheelDialog(Context context) {
		super(context, R.style.location_theme_dialog);
		mContext = context;
		setContentView(R.layout.wheel_dialog_layout);
		initViews();
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	private void initEvents() {
		mLayoutContent = (LinearLayout) findViewById(R.id.wheel_dialog_layout_content);
		mBack = (TextView) findViewById(R.id.wheel_back);
		mOk = (TextView) findViewById(R.id.wheel_ok);
	}

	private void initViews() {
		// mBack.setOnClickListener(this);
		// mOk.setOnClickListener(this);

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

	public void setButtonBack(DialogInterface.OnClickListener listener) {
		mOnBackClickListener = listener;
	}

	public void setButtonOk(DialogInterface.OnClickListener listener) {
		mOnOkClickListener = listener;
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

/*	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_back:
			if (mOnBackClickListener != null) {
				mOnBackClickListener.onClick(this, 1);
			}
			break;
		case R.id.wheel_ok:
			if (mOnOkClickListener != null) {
				mOnOkClickListener.onClick(this, 1);
			}
			break;
		}
	}*/

}
