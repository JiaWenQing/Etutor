package net.dx.etutor.activity.forum;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.academy.AgencyDetailActivity;
import net.dx.etutor.activity.academy.CorrectionActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumBoard;
import net.dx.etutor.model.DxForumReply;
import net.dx.etutor.model.DxForumReplySecond;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.view.imageview.ClickImageView;
import net.dx.etutor.view.imageview.RoundHeadImageView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 举报
 * 
 * @author jwq
 * 
 */
public class InformPostsActivity extends BaseActivity implements
		OnClickListener {
	public static String TAG = "InformPostsActivity";

	private RoundHeadImageView mUserAvatar;
	private TextView mUserName;
	private TextView mPostsTitle;
	private RadioGroup mInform;
	private Button mSubmit;

	private DxForumTopic dxForumTopic;

	private EditText mEdit;
	private DxForumReply reply;
	private DxForumReplySecond replySecond;
	private TextView mPostsContent;
	private ClickImageView mImage0;
	private ClickImageView mImage1;
	private ClickImageView mImage2;
	private ImageView[] mImages = { mImage0, mImage1, mImage2 };

	private int type;
	private String avatar;
	private String name;
	private String title;
	private String content;
	private String replyId;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_posts_inform);
		setTitle(R.string.text_inform);
		mUserAvatar = (RoundHeadImageView) findViewById(R.id.imv_poster_avatar);
		mUserName = (TextView) findViewById(R.id.tv_poster_name);
		mPostsTitle = (TextView) findViewById(R.id.tv_posts_title);
		mPostsContent = (TextView) findViewById(R.id.tv_content);
		mInform = (RadioGroup) findViewById(R.id.rg_inform);
		mSubmit = (Button) findViewById(R.id.btn_submit_posts);
		mEdit = (EditText) findViewById(R.id.et_feedback_posts);
		for (int i = 0; i < 3; i++) {
			mImages[i] = (ClickImageView) findViewById(R.id.img_1_1 + i);
		}
		mImages[0].setVisibility(View.GONE);
		mImages[1].setVisibility(View.GONE);
		mImages[2].setVisibility(View.GONE);
		dxForumTopic = (DxForumTopic) getIntent().getExtras().get(
				"dxForumTopic");
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		type = dxForumTopic.getReplyType();

		switch (type) {
		case 3:
			mPostsTitle.setVisibility(View.GONE);
			reply = dxForumTopic.getDxForumReply();
			replyId = reply.getId() + "";
			name = reply.getUserName().toString();
			content = reply.getContent().toString();
			avatar = reply.getAvatarUrl().toString();
			break;
		case 4:
			mUserAvatar.setVisibility(View.GONE);
			mPostsTitle.setVisibility(View.GONE);
			replySecond = dxForumTopic.getDxForumReply()
					.getDxForumReplySeconds().get(0);
			replyId = replySecond.getId() + "";
			name = replySecond.getUserName().toString();
			content = replySecond.getContent().toString();
			break;
		case 5:
			replyId = dxForumTopic.getReplyId() + "";
			name = dxForumTopic.getUserName().toString();
			title = dxForumTopic.getTitle().toString();
			content = dxForumTopic.getDxForumReply().getContent().toString();
			avatar = dxForumTopic.getAvatarUrl().toString();
			break;
		}
		mUserName.setText(name);
		mPostsTitle.setText(title + "");
		if (!TextUtils.isEmpty(content)) {
			List<String> list = StrUtil.getImgSrc(content);
			if (list.size() != 0) {
				mPostsContent.setText(Html.fromHtml(content.substring(0,
						content.lastIndexOf("<br/>"))));
				for (int i = 0; i < list.size(); i++) {
					mImages[i].setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							DataParam.REMOTE_SERVE + list.get(i), mImages[i],
							ImageLoadOptionsUtil.getOptions());
					mImages[i].setTag(list.get(i));
				}
			} else {
				mPostsContent.setText(Html.fromHtml(content));
			}
		}
		if (!TextUtils.isEmpty(avatar)) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + avatar, mUserAvatar,
					ImageLoadOptionsUtil.getOptions());
		}else {
			mUserAvatar.setImageResource(R.drawable.avatar);
		}

	}

	private int flag = 5;

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSubmit.setOnClickListener(this);
		mInform.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				switch (checkedId) {
				case R.id.rb_inform_1:
					flag = 1;
					break;
				case R.id.rb_inform_2:
					flag = 2;
					break;
				case R.id.rb_inform_3:
					flag = 3;
					break;
				case R.id.rb_inform_4:
					flag = 4;
					break;
				case R.id.rb_inform_5:
					flag = 5;
					break;
				}

			}
		});
	}

	private int loginStatu;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit_posts:
			loginStatu = mApplication.getSpUtil().getLoginStatu();
			if (loginStatu == 0) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivityByPendingTransition(intent);
			} else {
				onSubmit();
			}

			break;

		default:
			break;
		}
	}

	private HashMap<String, Object> mMap;

	private void onSubmit() {
		mMap = new HashMap<String, Object>();
		mMap.put("reportType", flag);
		mMap.put("reportContent", mEdit.getText().toString());
		mMap.put("replyId", replyId);
		String urlString = UrlEngine.getReport(mMap);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					showShortToast("举报成功！谢谢您的举报！");
					finish();
				}
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
