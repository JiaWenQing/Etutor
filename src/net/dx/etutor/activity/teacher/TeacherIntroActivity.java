package net.dx.etutor.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.ImageShowActivity;
import net.dx.etutor.activity.adapter.CommentListAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.activity.comment.CommentListActivity;
import net.dx.etutor.activity.fragment.teacherInfo.SmallClassFragment;
import net.dx.etutor.activity.fragment.teacherInfo.TeacherAbilityFragment;
import net.dx.etutor.activity.fragment.teacherInfo.TeacherIntroFragment;
import net.dx.etutor.activity.message.PrivateMessageDetailActivity;
import net.dx.etutor.activity.order.OrderCreateActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxCertificate;
import net.dx.etutor.model.DxComment;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.model.DxUsers;
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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class TeacherIntroActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener, OnClickListener,
		OnItemClickListener {

	public static String TAG = "TeacherIntroActivity";
	private static List<Fragment> list = new ArrayList<Fragment>();
	private FragmentManager fm;

	private RadioGroup mRadioGroup;
	private RadioButton mIntro;
	private RadioButton mCourseInfo;
	private RadioButton mCourseTime;
	private Button mOrder;
	private ImageView mPhone;
	private ImageView mMessage;
	private TextView mCommentCount;
	private RatingBar mRatingbar;
	private TextView mTeacherName;
	private TextView mFullTime;
	private TextView mIdentify;
	private TextView mVerify;
	private ViewPager mViewPager;
	private RoundHeadImageView mTeacherAvatar;
	private LinearLayout mLayoutLookAssess;

	private MyAdapter adapter;
	private FragmentTransaction ft;

	private SmallClassFragment courseTimeFragment;
	private TeacherIntroFragment introFragment;
	private TeacherAbilityFragment courseInfoFragment;

	private String teacherId;
	private String userId;
	private String type;
	private float rating;
	private int loginStatu;
	private String collectId;
	private DxTeacherList dxTeacher;
	private DxTeacherinfo dxTeacherinfo;
	private RelativeLayout mMenuLayout;
	private TextView mHeaderIcon;

	private CommentListAdapter mCommentAdapter;
	private Map<String, Object> mMap;
	private List<DxComment> mList = new ArrayList<DxComment>();
	private ListView mListView;

	private List<DxCertificate> certificateList = new ArrayList<DxCertificate>();
	private List<String> urls = new ArrayList<String>(); // 所有图片地址List
	private List<String> summarys = new ArrayList<String>(); // 所有图片说明List
	// private DxTeachercourse dxTeachercourse = new DxTeachercourse();
	private Gallery mGallery;
	private ImageAdapter imageAdapter;
	private int identify;
	private int cardFlush = 0;
	private int index = -1;
	private String commentCount;
	private boolean showFlag1;
	private boolean showFlag2;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_teacher_introduce);
		setTitle(R.string.text_choose_teacher);
		userId = mApplication.getSpUtil().getUserId();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		mMenuLayout = (RelativeLayout) findViewById(R.id.layout_need_menu);
		mLayoutLookAssess = (LinearLayout) findViewById(R.id.layout_look_assess);
		mTeacherAvatar = (RoundHeadImageView) findViewById(R.id.imv_teacher_head);
		fm = getSupportFragmentManager();
		mRatingbar = (RatingBar) findViewById(R.id.ratingbar_teacher);
		mHeaderIcon = (TextView) findViewById(R.id.main_head_bar_icon);
		mIntro = (RadioButton) findViewById(R.id.rb_person_introduce);
		mCourseInfo = (RadioButton) findViewById(R.id.rb_course_info);
		mCourseTime = (RadioButton) findViewById(R.id.rb_course_time);
		mCommentCount = (TextView) findViewById(R.id.tv_comment_count);
		mOrder = (Button) findViewById(R.id.btn_create_order);
		mPhone = (ImageView) findViewById(R.id.imv_phone);
		mMessage = (ImageView) findViewById(R.id.imv_message);

		mRadioGroup = (RadioGroup) findViewById(R.id.rg_teacher_introduce);

		mTeacherName = (TextView) findViewById(R.id.tv_teacher_name);
		mFullTime = (TextView) findViewById(R.id.tv_fulltime);
		mIdentify = (TextView) findViewById(R.id.tv_identify);
		mVerify = (TextView) findViewById(R.id.tv_verify);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setOffscreenPageLimit(2);
		mListView = (ListView) findViewById(R.id.lv_student_comment);
		mGallery = (Gallery) findViewById(R.id.gallery);
		MarginLayoutParams params = (MarginLayoutParams) mGallery
				.getLayoutParams();
		params.height = getScreenHeight() / 4;
		params.setMargins(-getScreenWidth() * 13 / 24, 0, 0, 0);
		mGallery.setLayoutParams(params);

		initData();
		introFragment = new TeacherIntroFragment();
		courseInfoFragment = new TeacherAbilityFragment();
		courseTimeFragment = new SmallClassFragment();
		list.add(introFragment);
		list.add(courseInfoFragment);
		list.add(courseTimeFragment);
	}

	private void initData() {
		initBaseData();
		initCommentData();
		initIdentifyData();
	}

	private void initBaseData() {
		// TODO Auto-generated method stub
		index = getIntent().getIntExtra("index", -1);
		dxTeacherinfo = (DxTeacherinfo) getIntent().getSerializableExtra(
				"dxTeacherinfo");
		showFlag1=!TextUtils.isEmpty(dxTeacherinfo.getDxTeachercourse().getSubjectItemId1());
		showFlag2=!TextUtils.isEmpty(dxTeacherinfo.getDxTeacherclass().getSubjectItemId());
		
		certificateList = dxTeacherinfo.getDxCertificates();
		if (certificateList.size() == 0) {
			MarginLayoutParams params = (MarginLayoutParams) mGallery
					.getLayoutParams();
			params.height = 0;
			params.setMargins(-getScreenWidth() * 13 / 24, 0, 0, 0);
			mGallery.setLayoutParams(params);
		}

		for (DxCertificate dx : certificateList) {
			urls.add(dx.getCertificateUrl());
			summarys.add(dx.getSummary());
		}

		dxTeacher = (DxTeacherList) getIntent().getSerializableExtra(
				"dxTeacher");
		type = getIntent().getStringExtra("type");
		if (!TextUtils.isEmpty(type) && type.equals("instantane")) {
			mMenuLayout.setVisibility(View.GONE);
		}
		collectId = dxTeacher.getCollectId();
		teacherId = dxTeacher.getId();
		mApplication.getSpUtil().setCollectId(collectId);
		mApplication.getSpUtil().setCollectItemId(index + "");
		if (loginStatu != 0) {
			if (TextUtils.isEmpty(collectId)) {
				setIcon(R.drawable.collect_normal);
			} else {
				setIcon(R.drawable.collect_pressed);
			}
			if (teacherId.equals(userId)) {
				mMenuLayout.setVisibility(View.GONE);
				mPhone.setVisibility(View.GONE);
				mMessage.setVisibility(View.GONE);
			}
		} else {
			setIcon(R.drawable.collect_normal);
		}

		mTeacherName.setText(dxTeacher.getUserName());
		if (dxTeacher.getAvatarUrl() != null
				&& !dxTeacher.getAvatarUrl().equals("")) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + dxTeacher.getAvatarUrl(),
					mTeacherAvatar, ImageLoadOptionsUtil.getOptions());
		} else {
			mTeacherAvatar.setImageResource(R.drawable.avatar);
		}

		mFullTime.setVisibility(View.GONE);
		mIdentify.setVisibility(View.GONE);
		mVerify.setVisibility(View.GONE);
		identify = dxTeacher.getIdentify();
		for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
			if ((identify & (1 << i)) != 0) {
				switch (i) {
				case 3:
					mFullTime.setVisibility(View.VISIBLE);
					break;
				case 2:
					mIdentify.setVisibility(View.VISIBLE);
					break;
				case 1:
					mVerify.setVisibility(View.VISIBLE);
					break;
				}
			}
		}

		if (null != dxTeacher.getRank()) {
			rating = dxTeacher.getRank();
		} else {
			rating = 0;
		}
		mRatingbar.setRating(rating);
		mMap = new HashMap<String, Object>();
		mMap.put("toUserId", teacherId);
		mMap.put("start", 0);
		mMap.put("pageSize", 2);
		getCommentList(mMap);
	}

	private void initCommentData() {
		// TODO Auto-generated method stub
		mCommentAdapter = new CommentListAdapter(TeacherIntroActivity.this,
				mList, false);
		mListView.setAdapter(mCommentAdapter);
	}

	private void initIdentifyData() {
		// TODO Auto-generated method stub
		imageAdapter = new ImageAdapter(this);
		mGallery.setAdapter(imageAdapter);
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
							} else {
								mCommentCount
										.setText(R.string.text_no_comments);
							}
							mCommentAdapter.notifyDataSetChanged();
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
	public void iconClick() {
		super.iconClick();
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (TextUtils.isEmpty(type)) {
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
	}

	/**
	 * 取消收藏
	 */
	private void delCollect() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
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
					UiUtil.setIcon(TeacherIntroActivity.this,
							R.drawable.collect_normal, mHeaderIcon);
					collectId = null;
					mApplication.getSpUtil().setCollectId(collectId);
					int i = ++cardFlush % 2;
					if (i == 0) {
						mApplication.getSpUtil().isCardListFlushFlag(false);
					} else {
						mApplication.getSpUtil().isCardListFlushFlag(true);
					}
					showShortToast(R.string.text_collect_delete);
				}
			}
		});
	}

	/**
	 * 添加收藏
	 * 
	 * @param flag
	 */
	public void addCollect(final boolean flag) {
		String urlString = UrlEngine.insertCollect(userId, "-1", teacherId);
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
						UiUtil.setIcon(TeacherIntroActivity.this,
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
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter(fm);
		mViewPager.setAdapter(adapter);
		
		if (showFlag1) {
			mViewPager.setCurrentItem(1);
			mCourseInfo.setChecked(true);
		}else if (showFlag2) {
			mViewPager.setCurrentItem(2);
			mCourseTime.setChecked(true);
		}else{
			mViewPager.setCurrentItem(0);
			mIntro.setChecked(true);
		}

		if (!TextUtils.isEmpty(type) && type.equals("orderDetail")) {
			mOrder.setBackgroundResource(R.color.text_gray);
		} else if (!TextUtils.isEmpty(type) && type.equals("instantane")) {

		} else {
			mOrder.setOnClickListener(this);
		}
		mPhone.setOnClickListener(this);
		mMessage.setOnClickListener(this);
		mGallery.setOnItemClickListener(this);

		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mLayoutLookAssess.setOnClickListener(this);
	}

	@SuppressWarnings("null")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		switch (v.getId()) {
		case R.id.btn_create_order:
			if (loginStatu != 0) {
				intent = new Intent(TeacherIntroActivity.this,
						OrderCreateActivity.class);
				DxNeed mDxNeed = new DxNeed();
				mDxNeed.setPrice(Double.parseDouble(dxTeacher.getPrice()));//
				DxUsers dxUser = new DxUsers();
				dxUser.setUserId(Integer.parseInt(dxTeacher.getId()));
				dxUser.setUserName(dxTeacher.getUserName());
				dxUser.setUserType("1");
				mDxNeed.setDxUser(dxUser);
				mDxNeed.setNeedId("-1");
				mDxNeed.setSubjectItemId(dxTeacher.getSubject());
				intent.putExtra("fullTime", dxTeacher.getFullTime());
				intent.putExtra("dxNeed", mDxNeed);
				startActivityByPendingTransition(intent);
			} else {
				intent = new Intent(TeacherIntroActivity.this,
						LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1001);
			}
			break;
		case R.id.imv_phone:
			Intent intentDial = new Intent();
			intentDial.setAction("android.intent.action.DIAL");// DIAL：掉拨号界面
																// CALL：直接拨号
			intentDial.setData(Uri.parse("tel:"
					+ dxTeacher.getTelephone().toString()));
			startActivity(intentDial);
			break;
		case R.id.imv_message:

			if (loginStatu != 0) {
				sendMessage();
			} else {
				intent = new Intent(TeacherIntroActivity.this,
						LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1002);
			}
			break;
		case R.id.layout_look_assess:
			intent = new Intent(TeacherIntroActivity.this,
					CommentListActivity.class);
			intent.putExtra("toUserId", dxTeacher.getId());
			startActivityByPendingTransition(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 给老师发私信
	 */
	public void sendMessage() {
		Intent intent = new Intent(TeacherIntroActivity.this,
				PrivateMessageDetailActivity.class);
		DxPrivatemsg dxPrivatemsg = new DxPrivatemsg();
		dxPrivatemsg.setReceiverId(teacherId);
		dxPrivatemsg.setSenderId(userId);
		DxUsers user = new DxUsers();
		user.setAvatarUrl(dxTeacher.getAvatarUrl());
		dxPrivatemsg.setDxUser(user);
		intent.putExtra("dxPrivatemsg", dxPrivatemsg);
		intent.putExtra("type", "Intro");
		startActivityByPendingTransition(intent);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int item = 0;
		switch (checkedId) {
		case R.id.rb_person_introduce:
			item = 0;
			break;
		case R.id.rb_course_info:
			item = 1;
			break;
		case R.id.rb_course_time:
			item = 2;
			break;

		default:

			break;
		}
		mViewPager.setCurrentItem(item, true);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			mIntro.setChecked(true);
			break;
		case 1:
			mCourseInfo.setChecked(true);
			break;
		case 2:
			mCourseTime.setChecked(true);
			break;

		default:
			mIntro.setChecked(true);
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public class ImageAdapter extends BaseAdapter {

		private Context context;

		public ImageAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return urls.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return DataParam.REMOTE_SERVE + urls.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(context);
			ImageLoader.getInstance().displayImage(getItem(position),
					imageView, ImageLoadOptionsUtil.getOptions());
			// 表示当前ImageView 把图片 不按比例扩大/缩小到View的大小显示
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setBackgroundColor(Color.TRANSPARENT);
			// 设置ImageView的大小
			imageView.setLayoutParams(new Gallery.LayoutParams(BaseActivity
					.getScreenWidth() / 3, LayoutParams.WRAP_CONTENT));
			return imageView;
		}

	}

	private class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String url = DataParam.REMOTE_SERVE + urls.get(position);
		Intent intent = new Intent(this, ImageShowActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
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
				intent = new Intent(TeacherIntroActivity.this,
						OrderCreateActivity.class);
				DxNeed mDxNeed = new DxNeed();
				mDxNeed.setPrice(Integer.parseInt(dxTeacher.getPrice()));
				DxUsers dxUser = new DxUsers();
				dxUser.setUserId(Integer.parseInt(dxTeacher.getId()));
				dxUser.setUserName(dxTeacher.getUserName());
				dxUser.setUserType("1");
				mDxNeed.setDxUser(dxUser);
				mDxNeed.setNeedId("-1");
				mDxNeed.setSubjectItemId(dxTeacher.getSubject());
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
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		list.clear();
	}

}
