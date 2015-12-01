package net.dx.etutor.dialog;

import net.dx.etutor.R;
import net.dx.etutor.view.MyEditText;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class InvitationCodeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private OnInvitationCodeListener mOnInvitationCodeListener;
	private Button mCancel;
	private Button mConfirm;
	private TextView mTitle;
	private Context context;
	private MyEditText mCode;

	public InvitationCodeDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public interface OnInvitationCodeListener {
		/**
		 * 退出的onclick事件
		 * 
		 * @param v
		 */
		void CodeOnClick(View v, MyEditText mCode);
	}

	public void setInvitationCodeListener(OnInvitationCodeListener l) {
		this.mOnInvitationCodeListener = l;
	}

	public InvitationCodeDialog(Context context) {
		super(context, R.style.LocationDialog);
		setContentView(
				LayoutInflater.from(context).inflate(
						R.layout.dialog_invitation_code, null),
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));

		mCancel = (Button) findViewById(R.id.btn_logout_cancle);
		mConfirm = (Button) findViewById(R.id.btn_logout_confirm);
		mCode = (MyEditText) findViewById(R.id.ed_invitation_code);
		mTitle = (TextView) findViewById(R.id.tv_mydialog_title);
		mCancel.setOnClickListener(this);
		mConfirm.setOnClickListener(this);
		// 设置点击外围解散
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mOnInvitationCodeListener != null) {
			mOnInvitationCodeListener.CodeOnClick(v, mCode);
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