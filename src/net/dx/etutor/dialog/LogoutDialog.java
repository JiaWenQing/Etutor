package net.dx.etutor.dialog;

import net.dx.etutor.R;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class LogoutDialog extends Dialog implements
		android.view.View.OnClickListener {

	private OnLogoutClickListener mOnLogoutClickListener;
	private Button mCancel;
	private Button mConfirm;
	private TextView mMessage;
	private TextView mTitle;
	private String msg;
	private String title;
	private Context context;

	public LogoutDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public interface OnLogoutClickListener {
		/**
		 * 退出的onclick事件
		 * 
		 * @param v
		 */
		void logoutOnClick(View v);
	}

	public void setOnLogoutClickListener(OnLogoutClickListener l, String msg,
			String title) {
		this.mOnLogoutClickListener = l;
		this.msg = msg;
		this.title = title;
		mMessage.setText(Html.fromHtml(msg));
		if (TextUtils.isEmpty(title)) {
			mTitle.setVisibility(View.GONE);
			mConfirm.setText("确定");
			mMessage.setTextSize(20);
		} else {
			mTitle.setVisibility(View.VISIBLE);
			mTitle.setText(title);
			mConfirm.setText("升级");
			mMessage.setTextSize(14);
		}
	}

	public LogoutDialog(Context context) {
		super(context, R.style.LocationDialog);
		setContentView(
				LayoutInflater.from(context).inflate(R.layout.dialog_logout,
						null), new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));

		mCancel = (Button) findViewById(R.id.btn_logout_cancle);
		mConfirm = (Button) findViewById(R.id.btn_logout_confirm);
		mMessage = (TextView) findViewById(R.id.message);
		mTitle = (TextView) findViewById(R.id.tv_mydialog_title);
		mCancel.setOnClickListener(this);
		mConfirm.setOnClickListener(this);
		// 设置点击外围解散
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mOnLogoutClickListener != null) {
			mOnLogoutClickListener.logoutOnClick(v);
			if (isShowing()) {
				this.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

}