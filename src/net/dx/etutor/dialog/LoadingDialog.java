package net.dx.etutor.dialog;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseDialog;
import android.content.Context;
import android.widget.TextView;

public class LoadingDialog extends BaseDialog {

	private TextView mTextView;
	private String mText;

	public LoadingDialog(Context context) {
		super(context);
	}

	public LoadingDialog(Context context, String text) {
		super(context);
		mText = text;
		init();
	}

	private void init() {
		setContentView(R.layout.activity_progress_load_layout);
		mTextView = (TextView) findViewById(R.id.progress_tips_text);
		mTextView.setText(mText);
	}

	public void setText(String text) {
		mText = text;
		mTextView.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
