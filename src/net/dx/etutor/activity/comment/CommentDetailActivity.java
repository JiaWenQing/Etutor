package net.dx.etutor.activity.comment;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;

public class CommentDetailActivity extends BaseActivity {
	
	public static String TAG = "CommentDetailActivity";

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_create_order);
		setTitle(R.string.title_order_detail);

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub

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
