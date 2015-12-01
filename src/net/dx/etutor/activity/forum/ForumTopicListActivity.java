package net.dx.etutor.activity.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MyPostsAdapter;
import net.dx.etutor.activity.base.BaseFragmentActivity;
import net.dx.etutor.activity.fragment.forum.ForumEssenceFragment;
import net.dx.etutor.activity.fragment.forum.ForumHotFragment;
import net.dx.etutor.activity.fragment.forum.ForumNewFragment;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumBoard;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 闲趣详情模块(每日一叨、精彩活动、放学别走、你问我答等)
 * 
 * @author tzm
 * 
 */
public class ForumTopicListActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnRefreshListener<ListView>,
		OnCheckedChangeListener, OnTouchListener, OnClickListener {
	private DxForumBoard mDxForumBoard = new DxForumBoard();
	private ViewPager mViewPager;
	public static String TAG = "ForumTopicListActivity";
	private RadioGroup mRadioGroup;
	private RadioButton mNew;
	private RadioButton mHot;
	private RadioButton mEssence;

	private ForumNewFragment mForumNewFragment;
	private ForumHotFragment mForumHotFragment;
	private ForumEssenceFragment mForumEssenceFragment;

	private MyAdapter adapter;
	private FragmentManager fm;
	private EditText mEditText;
	private TextView mTextOK;
	private LinearLayout mlinearlist;
	private LinearLayout mSearchlist;
	private static List<Fragment> list = new ArrayList<Fragment>();
	private PullToRefreshListView mListView;
	private PullToRefreshListView xListView;
	private RelativeLayout mFinishNetwork;
	private MyPostsAdapter mAdapter;
	private List<DxForumTopic> mList = new ArrayList<DxForumTopic>();
	private HashMap<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	// private LinearLayout list_layout;
	private ImageView image_search;
	private TextView mTextView_nothing;
	private int loginStatu;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_forumlist_layout);
		Bundle bundle = getIntent().getExtras();
		mDxForumBoard = (DxForumBoard) bundle.get("Board");
		setTitle(mDxForumBoard.getName());
		showIcon(0, "发帖");
		fm = getSupportFragmentManager();
		// list_layout = (LinearLayout) findViewById(R.id.forum_list_layout);
		mTextView_nothing = (TextView) findViewById(R.id.text_nothing);
		mlinearlist = (LinearLayout) findViewById(R.id.forum_zhulist);
		mSearchlist = (LinearLayout) findViewById(R.id.souduo_list);
		mRadioGroup = (RadioGroup) findViewById(R.id.forum_list);
		mEditText = (EditText) findViewById(R.id.forumlst_edit);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mEditText.addTextChangedListener(textWatcher);
		mTextOK = (TextView) findViewById(R.id.forum_text_OK);
		mNew = (RadioButton) findViewById(R.id.forum_new);
		mHot = (RadioButton) findViewById(R.id.forum_hot);
		// mTextView=(TextView)
		// findViewById(R.id.xlistview_footer_hint_textview);

		image_search = (ImageView) findViewById(R.id.image_search);
		mEssence = (RadioButton) findViewById(R.id.forum_essence);
		mListView = (PullToRefreshListView) findViewById(R.id.forum_search_list);
		mListView.setMode(Mode.BOTH);
		mAdapter = new MyPostsAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		xListView = (PullToRefreshListView) findViewById(R.id.lv_school);
		mViewPager = (ViewPager) findViewById(R.id.forumlist_vpager);
		mViewPager.setOffscreenPageLimit(3);
		initViewPager();

		// 搜索
		mMap = new HashMap<String, Object>();
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mMap.put("boardId", mDxForumBoard.getId());
	}

	private Boolean meditflag = true;
	private TextView mTextView;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 285386:
				if (meditflag) {
					getOK();
					meditflag = false;
					// 系统默认隐藏软键盘的方法
					// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
					// .hideSoftInputFromWindow(ForumTopicListActivity.this
					// .getCurrentFocus().getWindowToken()
					// , InputMethodManager.HIDE_NOT_ALWAYS);
				}
				break;
			case 105:
				mTextView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}

	};

	private void getOK() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 隐藏软键盘
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
		mlinearlist.setVisibility(View.VISIBLE);
		mSearchlist.setVisibility(View.GONE);
		mTextOK.setText("确定");
		mTextOK.setTextColor(getResources().getColor(R.color.text_OK));
		mEditText.setText("");
		mButtonflag = true;
		mTextOK.setVisibility(View.GONE);
		mEditText.setFocusable(false);
		mEditText.setFocusableInTouchMode(false);
	};

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (TextUtils.isEmpty(s.toString())) {
				Message msg = new Message();
				msg.what = 285386;
				handler.sendMessage(msg);
			}

		}
	};

	private void initViewPager() {
		// TODO Auto-generated method stub
		list.clear();
		mForumNewFragment = new ForumNewFragment(mApplication,
				ForumTopicListActivity.this, this, mDxForumBoard);
		mForumHotFragment = new ForumHotFragment(mApplication,
				ForumTopicListActivity.this, this, mDxForumBoard);
		mForumEssenceFragment = new ForumEssenceFragment(mApplication,
				ForumTopicListActivity.this, this, mDxForumBoard);
		list.add(mForumNewFragment);
		list.add(mForumHotFragment);
		list.add(mForumEssenceFragment);
		adapter = new MyAdapter(fm);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
	}

	private class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}

	@Override
	public void initEvents() {
		mListView.setOnRefreshListener(this);
		mFinishNetwork.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mTextOK.setOnClickListener(this);
		// mEditText.setOnClickListener(this);
		mEditText.setOnTouchListener(this);
	}

	@Override
	public void iconClick() {
		Intent intent = new Intent(this, SendPostsActivity.class);
		DxForumTopic topic = new DxForumTopic();
		topic.setBoardId(mDxForumBoard.getId());
		topic.setTitle(mDxForumBoard.getName());
		topic.setReplyType(0);
		intent.putExtra("topic", topic);
		startActivityForResultByPendingTransition(intent, 111);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			mNew.setChecked(true);
			break;
		case 1:
			mHot.setChecked(true);
			break;
		case 2:
			mEssence.setChecked(true);
			break;
		default:
			mNew.setChecked(true);
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int item = 0;
		switch (checkedId) {
		case R.id.forum_new:
			item = 0;
			break;
		case R.id.forum_hot:
			item = 1;
			break;
		case R.id.forum_essence:
			item = 2;
			break;
		}
		mViewPager.setCurrentItem(item, true);

	}

	private Boolean mButtonflag = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forum_text_OK:

			if (mButtonflag) {
				if (!TextUtils.isEmpty(mEditText.getText())) {

					mlinearlist.setVisibility(View.GONE);
					mSearchlist.setVisibility(View.VISIBLE);
					mTextOK.setText("取消");
					mTextOK.setTextColor(getResources().getColor(
							R.color.button_checked));
					mButtonflag = false;
					meditflag = true;
					mEditText.setFocusable(false);
					mEditText.setFocusableInTouchMode(false);

				}
				mMap.put("title", mEditText.getText().toString());
				getBoardList(0);

			} else {
				if (!TextUtils.isEmpty(mEditText.getText())) {
					getOK();

					mEditText.setCursorVisible(false);

				}
			}
			break;
		case R.id.forumlst_edit:
			// InputMethodManager imm = (InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE);
			// // 显示软键盘
			// imm.showSoftInputFromInputMethod(mEditText.getWindowToken(), 0);
			mEditText.setCursorVisible(true);
			mTextOK.setVisibility(View.VISIBLE);
			mEditText.setFocusable(true);
			mEditText.setFocusableInTouchMode(true);
		default:
			break;
		}
	}

	private int surplusPage = 0;

	public void getBoardList(final int type) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.getTopicList(mMap);
		showLoadingDialog("请稍后……");

		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					dismissLoadingDialog();
					surplusPage = response.length() % pageSize;
					if (surplusPage != 0 || response.length() == 0) {
						mListView.onRefreshComplete();
						if (type == 1) {
							showShortToast(R.string.text_loading_last);
						}
					}
					// 清除之前加载的list
					if (type == 0) {
						mList.clear();
					}
					try {
						for (int i = 0; i < response.length(); i++) {
							DxForumTopic mDxForumTopic = new DxForumTopic();
							mDxForumTopic.initWithAttributes(response
									.getJSONObject(i));
							mList.add(mDxForumTopic);
						}
						mListView.onRefreshComplete();
						mAdapter.notifyDataSetChanged();
						if (mList.size() != 0) {
							mListView.setVisibility(View.VISIBLE);
							image_search.setVisibility(View.GONE);
							mTextView_nothing.setVisibility(View.GONE);
						} else {
							mListView.setVisibility(View.GONE);
							image_search.setVisibility(View.VISIBLE);
							mTextView_nothing.setVisibility(View.VISIBLE);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// 发帖成功，在这里刷新数据
			switch (requestCode) {
			case 111:
				Toast.makeText(this, "发帖成功！", Toast.LENGTH_SHORT).show();
				mForumNewFragment.onRefresh();
				mForumHotFragment.onRefresh();
				break;
			case 500:
				mForumNewFragment.onActivityResult(requestCode, resultCode,
						data);
				mForumHotFragment.onActivityResult(requestCode, resultCode,
						data);
				mForumEssenceFragment.onActivityResult(requestCode, resultCode,
						data);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// // 显示软键盘
		imm.showSoftInputFromInputMethod(mEditText.getWindowToken(), 0);
		mEditText.setCursorVisible(true);
		mTextOK.setVisibility(View.VISIBLE);
		mEditText.setFocusable(true);
		mEditText.setFocusableInTouchMode(true);
		return false;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		// 设置上一次刷新的提示标签
		refreshView.getLoadingLayoutProxy()
				.setLastUpdatedLabel("最后更新时间:" + str);

		// 下拉刷新 业务代码
		if (refreshView.isShownHeader()) {
			// 设置刷新标签
			mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
			// 设置下拉标签
			mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
			// 设置释放标签
			mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getBoardList(0);
		}

		// 上拉加载更多 业务代码
		if (refreshView.isShownFooter()) {
			// 设置刷新标签
			mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
			// 设置下拉标签
			mListView.getLoadingLayoutProxy().setPullLabel("上拉加载");
			// 设置释放标签
			mListView.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
			start++;
			mMap.put("start", start * pageSize);
			mMap.put("pageSize", pageSize);
			getBoardList(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}

}
