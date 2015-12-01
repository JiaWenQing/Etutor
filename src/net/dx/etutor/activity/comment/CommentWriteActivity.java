package net.dx.etutor.activity.comment;

import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxOrder;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SelectAreaSubjectUtil;
import net.dx.etutor.view.imageview.RoundImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class CommentWriteActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	
	public static String TAG = "CommentWriteActivity";
	private Button mSubmit;
	private RoundImageView mHead;
	private TextView mName;
	private TextView mCount;
	private TextView mTotal;
	private EditText mComment;
	private RadioGroup mEvaluate;
	private RatingBar mRatingBar;
	private String evaluate = "1";
	private int totalwords = 100;
	private String comment;
	private String score;

	private DxOrder dxOrder;
	private String userId;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_comment);
		setTitle(R.string.text_comment);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		dxOrder = (DxOrder) getIntent().getExtras().getSerializable("dxOrder");
		mSubmit = (Button) findViewById(R.id.btn_submit_comment);
		mHead = (RoundImageView) findViewById(R.id.imv_head);
		mName = (TextView) findViewById(R.id.tv_name);
		mCount = (TextView) findViewById(R.id.tv_surplus_word_number);
		mTotal = (TextView) findViewById(R.id.tv_total_word_number);
		mComment = (EditText) findViewById(R.id.et_write_comment);
		mEvaluate = (RadioGroup) findViewById(R.id.rg_evaluate);
		mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
		initData();
	}

	private void initData() {
		String avatar = dxOrder.getUser().getAvatarUrl();
		if (!TextUtils.isEmpty(avatar)) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + avatar, mHead,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mHead.setImageResource(-1);
		}
		mName.setText(dxOrder.getUser().getUserName());
		if (null != dxOrder.getUser().getRank()) {
			float rating = dxOrder.getUser().getRank();
			mRatingBar.setRating(rating);
		}
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSubmit.setOnClickListener(this);
		mEvaluate.setOnCheckedChangeListener(this);
		
		EmojiFilter.checkEmoji(CommentWriteActivity.this, mComment,
				mTotal, totalwords);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit_comment:
			onSubmit();
			break;

		default:
			break;
		}
	}

	private void onSubmit() {
		// TODO Auto-generated method stub
		comment = mComment.getText().toString().trim();
		comment = EmojiFilter.filterEmoji(comment);
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (TextUtils.isEmpty(comment)) {
			showLongToast("评论不能为空！");
			return;
		} else {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fromUserId", userId);
			map.put("toUserId", dxOrder.getUser().getUserId());
			map.put("orderId", dxOrder.getOrderId());
			map.put("content", comment);
			map.put("starLevel", evaluate);
			showLoadingDialog("请稍后…");
			String urlString = UrlEngine.setComment(map);
			HttpUtil.post(urlString, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (statusCode == Constants.STAT_200) {
						showShortToast("评论成功!");
						Intent intent = new Intent(CommentWriteActivity.this,
								MainFragmentActivity.class);
						mApplication.getSpUtil().isCommentFlag(true);
						startActivityByPendingTransition(intent);
						finish();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					dismissLoadingDialog();
					if (statusCode == 0) {
						showLongToast(R.string.text_link_server_error);
					}
					if (statusCode == Constants.STAT_401
							|| statusCode == Constants.STAT_403
							|| statusCode == Constants.STAT_404) {
						showShortToast(R.string.text_error_network);
					}
				}
			});

		}
	}

	/**
	 * CommentType 好、中、差
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		SelectAreaSubjectUtil.onCheckedChanged(CommentWriteActivity.this,
				group, checkedId);
		evaluate = SelectAreaSubjectUtil.getEvaluate();
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
