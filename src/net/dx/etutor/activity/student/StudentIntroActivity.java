package net.dx.etutor.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.CommentListAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.comment.CommentListActivity;
import net.dx.etutor.activity.message.PrivateMessageDetailActivity;
import net.dx.etutor.activity.order.OrderCreateActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.activity.setting.FeedbackActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxComment;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.UiUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class StudentIntroActivity extends BaseActivity implements
		OnClickListener {

	public static String TAG = "StudentIntroActivity";
	private RatingBar mRatingBar;
	private RoundHeadImageView mStudentImage;
	private TextView mStudentName;
	private TextView mNeedTital;
	private TextView mNeedSubjects;
	private TextView mTeacherSex;
	private TextView mLectureType;
	private TextView mStudentIntro;
	private TextView mClassFees;
	private TextView mLectureCount;
	private TextView mLookComment;
	private ListView mListView;
	private LinearLayout mCommentLayout;
	private RelativeLayout mMenuLayout;
	private int identify = 0;
	private float rating;
	private CheckBox mCheckBox;
	private int[] mCheckBoxId = new int[21];

	private Button mCreateOrder;
	private ImageView mPrivateLeter;
	private DxNeed mDxNeed = new DxNeed();
	private TextView mHeaderIcon;// 收藏

	private String userId;
	private String toUserId;
	private String needId;
	private String collectId;
	private String school;
	private int loginStatu;
	private String type;
	private ImageView mIdentify;
	private ImageView mVerify;
	private TextView mTeachingMode;
	private TextView mStudentSchool;
	private TextView mCommentCount;
	private String commentCount;
	private ImageView image_xian;

	private CommentListAdapter mAdapter;
	private Map<String, Object> mMap;
	private List<DxComment> mList = new ArrayList<DxComment>();
	protected int cardFlush = 0;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_student_introduce);
		mDxNeed = (DxNeed) getIntent().getSerializableExtra("dxNeed");
		userId = mApplication.getSpUtil().getUserId();
		loginStatu = mApplication.getSpUtil().getLoginStatu();

		mHeaderIcon = (TextView) findViewById(R.id.main_head_bar_icon);
		mStudentImage = (RoundHeadImageView) findViewById(R.id.imv_student_head);
		mStudentName = (TextView) findViewById(R.id.tv_student_name);
		mStudentSchool = (TextView) findViewById(R.id.tv_student_school);
		mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
		mPrivateLeter = (ImageView) findViewById(R.id.imv_message);
		mIdentify = (ImageView) findViewById(R.id.imv_student_identify);
		mVerify = (ImageView) findViewById(R.id.imv_student_verify);
		mClassFees = (TextView) findViewById(R.id.tv_class_fees);

		mNeedTital = (TextView) findViewById(R.id.tv_student_need_title);
		mNeedSubjects = (TextView) findViewById(R.id.tv_subject);
		mLectureCount = (TextView) findViewById(R.id.tv_lecture_count);
		mLectureType = (TextView) findViewById(R.id.tv_lecture_type);
		mTeachingMode = (TextView) findViewById(R.id.tv_teaching_mode);
		mTeacherSex = (TextView) findViewById(R.id.tv_teacher_sex);
		mStudentIntro = (TextView) findViewById(R.id.tv_student_introduce);

		mMenuLayout = (RelativeLayout) findViewById(R.id.layout_need_menu);
		mCommentLayout = (LinearLayout) findViewById(R.id.layout_look_assess);
		mLookComment = (TextView) findViewById(R.id.tvbtn_look_assess);
		mCommentCount = (TextView) findViewById(R.id.tv_comment_count);
		image_xian = (ImageView) findViewById(R.id.image_xian);
		mListView = (ListView) findViewById(R.id.lv_teacher_comment);

		mCreateOrder = (Button) findViewById(R.id.btn_create_order);

		// mStarLayout = (LinearLayout) findViewById(R.id.linearLayout2);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 21; i++) {
			mCheckBoxId[i] = R.id.rb_1_1 + i;
		}

		type = getIntent().getStringExtra("type");
		if (null != type
				&& (type.equals("needStudent") || type.equals("Collect") || type
						.equals("orderDetail"))) {
			mMenuLayout.setVisibility(View.VISIBLE);
			toUserId = mDxNeed.getDxUser().getUserId().toString();
			school = mDxNeed.getDxUser().getSchool();
			setTitle(R.string.text_student_need_detail);
			collectId = mDxNeed.getCollectId();
			mApplication.getSpUtil().setCollectId(collectId);
			mApplication.getSpUtil().setCollectItemId(toUserId);
			if (!TextUtils.isEmpty(collectId)) {
				setIcon(R.drawable.collect_pressed);
			} else {
				setIcon(R.drawable.collect_normal);
			}
			if (!TextUtils.isEmpty(userId) && userId.equals(toUserId)) {
				mMenuLayout.setVisibility(View.GONE);
				mHeaderIcon.setVisibility(View.GONE);
				mPrivateLeter.setVisibility(View.GONE);
			}
			needId = mDxNeed.getNeedId().toString();
		} else if (null != type && type.equals("preview")) {
			school = mApplication.getSpUtil().getUserSchool();
			mMenuLayout.setVisibility(View.GONE);
			mHeaderIcon.setVisibility(View.GONE);
			mPrivateLeter.setVisibility(View.GONE);
			setTitle(R.string.text_preview);
		} else if (null != type && type.equals("instantane")) {
			toUserId = mDxNeed.getDxUser().getUserId().toString();
			school = mDxNeed.getDxUser().getSchool();
			mMenuLayout.setVisibility(View.GONE);
			mHeaderIcon.setVisibility(View.GONE);
			mPrivateLeter.setVisibility(View.GONE);
			setTitle(R.string.text_instantane);
		}
		if (TextUtils.isEmpty(school)) {
			mStudentSchool.setText(" ");
		} else {
			mStudentSchool.setText(school);
		}

		if (!TextUtils.isEmpty(mDxNeed.getNeedTitle())) {
			mNeedTital.setText(mDxNeed.getNeedTitle());
		} else {
			mNeedTital.setText(R.string.text_teacher);
		}
		if (!TextUtils.isEmpty(mDxNeed.getIntroduce())) {
			mStudentIntro.setText(mDxNeed.getIntroduce());
		} else {
			mStudentIntro.setText("无");
		}
		if (!TextUtils.isEmpty(mDxNeed.getSubjectItemId())) {
			mNeedSubjects.setText(mDxNeed.getSubjectItemId());
		}
		if (mDxNeed.getTradeNumber() == null || mDxNeed.getTradeNumber() == 0) {
			mLectureCount.setText("面议");
		} else {
			mLectureCount.setText(mDxNeed.getTradeNumber() + "次/周  ");
		}
		if (!TextUtils.isEmpty(mDxNeed.getLectureMode())) {
			mLectureType.setText(mDxNeed.getLectureMode());
		}
		if (!TextUtils.isEmpty(mDxNeed.getTeachMode())) {
			mTeachingMode.setText(mDxNeed.getTeachMode());
		}
		if (!TextUtils.isEmpty(mDxNeed.getTeacherSex())) {
			mTeacherSex.setText(mDxNeed.getTeacherSex());
		}
		if (mDxNeed.getPrice() == 0) {
			mClassFees.setText(" 面议  ");
		} else {
			mClassFees.setText(mDxNeed.getPrice() + "元/小时  ");
		}

		if (mDxNeed.getDxUser() != null) {
			rating = mDxNeed.getDxUser().getRank();
			mRatingBar.setRating(rating);

			identify = mDxNeed.getDxUser().getIdentify();
			mStudentName.setText(mDxNeed.getDxUser().getUserName());
			String str = mDxNeed.getDxUser().getAvatarUrl();
			if (!TextUtils.isEmpty(str)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE
								+ mDxNeed.getDxUser().getAvatarUrl(),
						mStudentImage, ImageLoadOptionsUtil.getOptions());
			} else {
				mStudentImage.setImageResource(R.drawable.avatar);
			}
		} else {
			identify = mApplication.getSpUtil().getIdentify();
			rating = mApplication.getSpUtil().getUserRank();
			mRatingBar.setRating(rating);
			mStudentName.setText(mApplication.getSpUtil().getUserName());
			String avatar = EtutorApplication.getInstance().getSpUtil()
					.getUserAvatar();
			if (!TextUtils.isEmpty(avatar)) {
				ImageLoader.getInstance().displayImage(avatar, mStudentImage,
						ImageLoadOptionsUtil.getOptions());
			} else {
				mStudentImage.setImageResource(R.drawable.default_avatar);
			}
		}
		mIdentify.setVisibility(View.GONE);
		mVerify.setVisibility(View.GONE);
		for (int i = 0; i < Constants.LABEL_NUMBER_STUDENT; i++) {
			if ((identify & (1 << i)) != 0) {
				switch (i) {
				case 2:
					mIdentify.setVisibility(View.VISIBLE);
					break;
				case 1:
					mVerify.setVisibility(View.VISIBLE);
					break;
				}
			}
		}

		for (int i = 0; i < 21; i++) {
			// TODO Auto-generated method stub
			mCheckBox = (CheckBox) findViewById(mCheckBoxId[i]);
			if ((Integer.parseInt(mDxNeed.getSubtime()) & (1 << i)) != 0) {
				mCheckBox.setChecked(true);
			} else {
				mCheckBox.setChecked(false);
			}
			mCheckBox.setClickable(false);
		}
		mAdapter = new CommentListAdapter(StudentIntroActivity.this, mList,
				false);
		mListView.setAdapter(mAdapter);
		mMap = new HashMap<String, Object>();
		mMap.put("toUserId", toUserId);
		mMap.put("start", 0);
		mMap.put("pageSize", 2);
		getCommentList(mMap);
	}

	private void getCommentList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String urlString = UrlEngine.getCommentList(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.length() > 0) {
							mList.clear();
							for (int i = 0; i < response.length(); i++) {
								DxComment dxComment = new DxComment();
								dxComment
										.initWithAttributes((JSONObject) response
												.get(i));
								mList.add(dxComment);
							}
							if (mList.size() != 0) {
								commentCount = mList.get(0).getCommentCount();
							} else {
								commentCount = "";
							}
							if (!TextUtils.isEmpty(commentCount)) {
								mCommentCount
										.setText("(" + commentCount + "条)");
								image_xian.setVisibility(View.VISIBLE);
							} else {
								mCommentCount
										.setText(R.string.text_no_comments);
							}
							mAdapter.notifyDataSetChanged();
							mListView.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.GONE);
							mCommentCount.setText(R.string.text_no_comments);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void initEvents() {
		mPrivateLeter.setOnClickListener(this);
		mCommentLayout.setOnClickListener(this);
		mLookComment.setOnClickListener(this);
		mCreateOrder.setOnClickListener(this);
		if (null != type && type.equals("orderDetail")) {
			mCreateOrder.setBackgroundResource(R.drawable.button_address_press);
		} else {
			mCreateOrder.setOnClickListener(this);
		}
	}

	@Override
	public void iconClick() {
		super.iconClick();
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu == 0) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		} else {
			if (TextUtils.isEmpty(collectId)) {
				addCollect(true);
			} else {
				delCollect();
			}
		}
	}

	/**
	 * 取消收藏
	 */
	private void delCollect() {
		// TODO Auto-generated method stub
		String urlString = UrlEngine.deleteCollect(String.valueOf(collectId));
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					UiUtil.setIcon(StudentIntroActivity.this,
							R.drawable.collect_normal, mHeaderIcon);
					collectId = null;
					mApplication.getSpUtil().setCollectId(collectId);
					int i = ++cardFlush % 2;
					if (i == 0) {
						mApplication.getSpUtil().isCardListFlushFlag(false);
					} else {
						mApplication.getSpUtil().isCardListFlushFlag(true);
					}
					UiUtil.setIcon(StudentIntroActivity.this,
							R.drawable.collect_normal, mHeaderIcon);
					showShortToast(R.string.text_collect_delete);
				} else {
					showShortToast(R.string.text_collect_delete);
				}
			}
		});
	}

	/**
	 * 收藏学生
	 */
	public void addCollect(final boolean flag) {

		String urlString = UrlEngine.insertCollect(userId, needId, toUserId);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						UiUtil.setIcon(StudentIntroActivity.this,
								R.drawable.collect_pressed, mHeaderIcon);
						collectId = response.getString("id");
						mApplication.getSpUtil().setCollectId(collectId);
						int i = ++cardFlush % 2;
						if (i == 0) {
							mApplication.getSpUtil().isCardListFlushFlag(false);
						} else {
							mApplication.getSpUtil().isCardListFlushFlag(true);
						}
						if (flag) {
							showShortToast(R.string.text_collect_success);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					showShortToast(R.string.text_collect_success);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		loginStatu = mApplication.getSpUtil().getLoginStatu();

		switch (v.getId()) {
		case R.id.tvbtn_look_assess:
		case R.id.layout_look_assess:
			intent = new Intent(StudentIntroActivity.this,
					CommentListActivity.class);
			intent.putExtra("toUserId", toUserId);
			startActivityByPendingTransition(intent);
			break;
		case R.id.btn_create_order:
			if (loginStatu == 0) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1001);
			} else {
				intent = new Intent(StudentIntroActivity.this,
						OrderCreateActivity.class);
				intent.putExtra("dxNeed", mDxNeed);
				startActivityByPendingTransition(intent);
			}
			break;
		case R.id.imv_message:
			if (loginStatu == 0) {
				intent = new Intent(this, LoginActivity.class);
				// startActivityByPendingTransition(intent);
				startActivityForResultByPendingTransition(intent, 1002);
			} else {
				sendMessage();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 给学生发私信
	 */
	public void sendMessage() {
		showLoadingDialog("请稍后...");
		String url = UrlEngine.getDialogueId(userId, toUserId);
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						dismissLoadingDialog();
						Intent intent = new Intent(StudentIntroActivity.this,
								PrivateMessageDetailActivity.class);
						DxPrivatemsg dxPrivatemsg = new DxPrivatemsg();
						dxPrivatemsg.setReceiverId(toUserId);
						dxPrivatemsg.setSenderId(userId);
						dxPrivatemsg.setDxUser(mDxNeed.getDxUser());
						intent.putExtra("dxPrivatemsg", dxPrivatemsg);
						intent.putExtra("type", "Intro");
						startActivityByPendingTransition(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		userId = mApplication.getSpUtil().getUserId();
		if (resultCode == RESULT_OK) {
			Intent intent;
			switch (requestCode) {
			case 1000:
				addCollect(false);
				break;
			case 1001:
				intent = new Intent(StudentIntroActivity.this,
						OrderCreateActivity.class);
				intent.putExtra("dxNeed", mDxNeed);
				startActivityByPendingTransition(intent);
				break;
			case 1002:
				sendMessage();
				break;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		userId = mApplication.getSpUtil().getUserId();
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
