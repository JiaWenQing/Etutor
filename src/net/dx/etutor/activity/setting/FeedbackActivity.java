package net.dx.etutor.activity.setting;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.util.NetWorkHelperUtil;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 意见反馈
 * 
 * @author app
 * 
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	
	public static String TAG = "FeedbackActivity";
	private static EditText mFeedback;
	private TextView mWordsNumber;
	private Button mSubmit;
	private static String feedback;
	private static String userId;
	private static int total = 200;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_feedback);
		setTitle(R.string.title_feedback);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		mWordsNumber = (TextView) findViewById(R.id.tv_total_words);
		mFeedback = (EditText) findViewById(R.id.et_feedback);
		mSubmit = (Button) findViewById(R.id.btn_submit);

		mWordsNumber.setText(total + "");
	}

	@Override
	public void initEvents() {
		mSubmit.setOnClickListener(this);
		mFeedback.addTextChangedListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			onSubmit();
			break;

		default:
			break;
		}
	}

	/**
	 * 提交意见
	 */
	private void onSubmit() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		feedback = mFeedback.getText().toString().trim();
		if (TextUtils.isEmpty(feedback)) {
			showShortToast(R.string.text_opinion);
			return;
		} else {
			if (feedback.length() < 5) {
				showShortToast(R.string.text_opinion);
				return;
			}
		}
		Intent data = new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse("mailto:support@deshell.com"));
		data.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
		data.putExtra(Intent.EXTRA_TEXT, feedback);
		startActivity(data);
	}
	

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		int count = total - s.length();
		mWordsNumber.setText(count + "");
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
