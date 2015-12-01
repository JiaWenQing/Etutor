package net.dx.etutor.activity.setting;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutOurActivity extends BaseActivity {

	public static String TAG = "AboutOurActivity";
	private TextView mAgreement;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_about_our);
		setTitle(R.string.text_about_our);
		mAgreement = (TextView) findViewById(R.id.tv_agreement);
	}

	@Override
	public void initEvents() {
		mAgreement.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AboutOurActivity.this, RuleActivity.class);
				startActivityByPendingTransition(intent);
			}
		});
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
