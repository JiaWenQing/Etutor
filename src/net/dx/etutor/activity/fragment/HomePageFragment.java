package net.dx.etutor.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.CardContralActivity;
import net.dx.etutor.activity.LocationCityActivity;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.academy.AcademyListActivity;
import net.dx.etutor.activity.adapter.CardSortListAdapter;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.fragment.banner.BannerFragment;
import net.dx.etutor.activity.teacher.NeedStudentListActivity;
import net.dx.etutor.activity.teacher.TeacherListActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.Channel;
import net.dx.etutor.model.DxCollect;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxSystemmsg;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.view.AutoTextView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshScrollView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@SuppressLint({ "HandlerLeak", "UseSparseArrays", "InflateParams" })
public class HomePageFragment extends BaseFragment implements OnClickListener,
		OnRefreshListener<ScrollView> {

	public static String TAG = "HomePageFragment";
	// private ScrollView myScrollView;
	private ListView mListView;
	private PullToRefreshScrollView mScrollView;
	private BannerFragment bannerFragment;
	private AutoTextView mSysMessage;
	private TextView mCity;
	private TextView mCards;
	private TextView mStudent;
	private TextView mTeacher;
	private TextView mForum;
	private TextView mAcademy;
	private List<String> bannerList = new ArrayList<String>();
	private List<DxSystemmsg> msgList = new ArrayList<DxSystemmsg>();
	private String loactionCity;
	private String userId;
	/**
	 * 用户选定
	 */
	private List<Channel> userChannelList = new ArrayList<Channel>();
	/**
	 * 1 我的收藏 2 新入住教师4 推荐教师8我的帖子16专职教师
	 */
	private List<Integer> cardType;
	private List<Map<Integer, Object>> mCardList = new ArrayList<Map<Integer, Object>>();
	private List<DxCollect> mCollectList = new ArrayList<DxCollect>();
	private List<DxTeacherList> mRecommendTeacherList = new ArrayList<DxTeacherList>();// 推荐教师
	private List<DxTeacherList> mLatestTeacherList = new ArrayList<DxTeacherList>();//新入住教师
	private List<DxTeacherList> mFullTimeTeacherList = new ArrayList<DxTeacherList>();//专职教师
	private List<DxForumTopic> mForumList = new ArrayList<DxForumTopic>();
	private CardSortListAdapter mAdapter;

	private int index = 0;
	private int i = 0;
	private Boolean flag = true;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mSysMessage.setText(msgList.get(index % 10).getTitle());
				mSysMessage.next();
				index++;
				break;
			default:
				break;
			}

		};
	};

	public HomePageFragment() {
		super();
	}

	public HomePageFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}

	private Handler handler;
	private int pageSize = 2;
	private int start = 0;

	public HomePageFragment(EtutorApplication application, Activity activity,
			Context context, Handler handler) {
		super(application, activity, context);
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (null == mApplication) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_main_homepage, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		loactionCity = mApplication.getSpUtil().getLocationCity();
		userId = mApplication.getSpUtil().getUserId();
		setTitle("首页");
		settingIcon(R.drawable.selector_arrow_more, true, loactionCity);
		// myScrollView = (ScrollView) findViewById(R.id.sv_home);
		// myScrollView.smoothScrollTo(0, 0);
		mCity = (TextView) findViewById(R.id.main_head_bar_icon);
		mStudent = (TextView) findViewById(R.id.tv_homepage_student);
		mTeacher = (TextView) findViewById(R.id.tv_homepage_teacher);
		mForum = (TextView) findViewById(R.id.tv_homepage_forum);
		mAcademy = (TextView) findViewById(R.id.tv_homepage_academy);
		mSysMessage = (AutoTextView) findViewById(R.id.tv_homepage_system_message);
		mCards = (TextView) findViewById(R.id.tv_homepage_cards);
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_home);
		mListView = (ListView) findViewById(R.id.lv_homepage_card);
		mScrollView.setMode(Mode.PULL_FROM_START);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mCity.setOnClickListener(this);
		mStudent.setOnClickListener(this);
		mTeacher.setOnClickListener(this);
		mForum.setOnClickListener(this);
		mAcademy.setOnClickListener(this);
		mCards.setOnClickListener(this);
		mScrollView.setOnRefreshListener(this);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mCity.setText(loactionCity);
		getBannerData();
		if (flag) {
			getSystemMessage();
			flag = false;
		}
		mAdapter = new CardSortListAdapter(getActivity(), mCardList);
		mListView.setAdapter(mAdapter);
		if (cardType != null && cardType.size() != 0) {
			mAdapter.setCardType(cardType);
		}
		if (i == 0) {
			getCardList();
			i++;
		}
	}

	@Override
	protected void iconClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), LocationCityActivity.class);
		startActivityForResult(intent, 100);

	}

	private void getSystemMessage() {
		String urlString = UrlEngine.getSystemMessage();
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
						msgList.clear();
						if (response.length() != 0) {
							for (int i = 0; i < response.length(); i++) {
								DxSystemmsg dxSystemmsg = new DxSystemmsg();
								dxSystemmsg.initWithAttributes(response
										.getJSONObject(i));
								msgList.add(dxSystemmsg);
							}
						}
						if (msgList.size() != 0) {
							startSysMessage();
						} else {
							mSysMessage.setText("msgList==null");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	private void getBannerData() {
		// TODO Auto-generated method stub
		String urlString = UrlEngine.getBanner();
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
						bannerList.clear();
						if (response.length() != 0) {
							for (int i = 0; i < response.length(); i++) {
								DxSystemmsg dxSystemmsg = new DxSystemmsg();
								dxSystemmsg.initWithAttributes(response
										.getJSONObject(i));
								bannerList.add(dxSystemmsg.getUrl());
							}
							bannerFragment = new BannerFragment(bannerList);
							FragmentManager fm = getChildFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							// showShortToast("进来次数");
							ft.add(R.id.fl_banner, bannerFragment);
							ft.commit();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	protected void startSysMessage() {
		// TODO Auto-generated method stub
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(1);
			}
		}, 0, 2000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_homepage_student:
			intent = new Intent(getActivity(), NeedStudentListActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_homepage_teacher:
			intent = new Intent(getActivity(), TeacherListActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_homepage_forum:
			Message msg = new Message();
			msg.what = 102;
			handler.sendMessage(msg);

			break;
		case R.id.tv_homepage_academy:
			intent = new Intent(getActivity(), AcademyListActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_homepage_cards:
			intent = new Intent(getActivity(), CardContralActivity.class);
			startActivityForResult(intent, 101);
			break;

		default:
			break;
		}
		AnimationUtil.addAnimation(v);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 100:
				String s = data.getStringExtra("city");
				loactionCity = mApplication.getSpUtil().getLocationCity();
				if (!loactionCity.equals(s)) {
					mApplication.getSpUtil().setLocationCity(s);
					mCity.setText(s);
					mCardList.clear();
					mAdapter.notifyDataSetChanged();
					getCardList();
				}
				break;
			case 101:
				boolean isChang = data.getBooleanExtra("isChange", false);
				if (isChang) {
					mCardList.clear();
					mAdapter.notifyDataSetChanged();
					getCardList();
				}
				break;
			}
		}

	}

	private void getUserChannelList() {
		String cardSort = EtutorApplication.getInstance().getSpUtil()
				.getCardSort();
		userId = mApplication.getSpUtil().getUserId();
		try {
			userChannelList.clear();
			JSONArray jsonArray = new JSONArray(cardSort);
			for (int i = 0; i < jsonArray.length(); i++) {
				Channel channel = new Channel();
				channel.initWithAttributes(jsonArray.getJSONObject(i));
				userChannelList.add(channel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String str;
	private int strIndex;

	private void getCardList() {
		// TODO Auto-generated method stub
		mCardList.clear();
		getUserChannelList();
		str = "";
		strIndex = 0;
		for (int i = 0; i < userChannelList.size(); i++) {
			int id = userChannelList.get(i).getId();
			int cardStatu = userChannelList.get(i).getSelected();
			if (cardStatu == 1) {
				if (id == 1) {
					if (!TextUtils
							.isEmpty(mApplication.getSpUtil().getUserId())) {
						str += "1";
					}
				} else if (id == 2) {
					str += "2";
				} else if (id == 4) {
					str += "4";
				} else if (id == 8) {
					str += "8";
				}else if (id == 16) {
					str += "f";
				}
			}
		}
		if (!TextUtils.isEmpty(str)) {
			switch (str.charAt(strIndex)) {
			case '1':
				getCollectList();
				break;
			case '2':
				getTeacherList("createTime desc", false);
				break;
			case '4':
				getTeacherList("identify desc,id desc", false);
				break;
			case '8':
				getPostsList();
				break;
			case 'f':
				getTeacherList("createTime desc", true);
				break;
			}
			strIndex++;
		}
	}

	private void getCollectList() {
		// TODO Auto-generated method stub

		Map<String, Object> mMap = new HashMap<String, Object>();
		mMap.put("userId", mApplication.getSpUtil().getUserId());
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		String urlString = UrlEngine.getCollectList(mMap);
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
						mCollectList.clear();
						for (int i = 0; i < response.length(); i++) {
							DxCollect dxCollect = new DxCollect();
							if (response.get(i).toString().contains("subtime")) {
								DxNeed dxNeed = new DxNeed();
								dxNeed.initWithAttributes((JSONObject) response
										.get(i));
								dxCollect.setNeed(dxNeed);
								dxCollect.setType(0);
							} else {
								DxTeacherList teacher = new DxTeacherList();
								teacher.initWithAttributes((JSONObject) response
										.get(i));
								dxCollect.setDxTeacherList(teacher);
								dxCollect.setType(1);
							}
							mCollectList.add(dxCollect);
						}
						getNextList();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void getTeacherList(final String sortType,final boolean isFullTimeTeacher) {
		// TODO Auto-generated method stub

		Map<String, Object> mMap = new HashMap<String, Object>();
		mMap.put("userId", mApplication.getSpUtil().getUserId());
		mMap.put("orderType", sortType);
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mMap.put("province", mApplication.getSpUtil().getLocationCity());
		if (isFullTimeTeacher) {
			mMap.put("fullTime", "1");
		}
		String urlString = UrlEngine.getTeacherList(mMap);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (sortType.equals("createTime desc")) {
							if (isFullTimeTeacher) {
								mFullTimeTeacherList.clear();
								for (int i = 0; i < response.length(); i++) {
									DxTeacherList dxTeacher = new DxTeacherList();
									dxTeacher
									.initWithAttributes((JSONObject) response
											.get(i));
									mFullTimeTeacherList.add(dxTeacher);
								}
							}else {
								mLatestTeacherList.clear();
								for (int i = 0; i < response.length(); i++) {
									DxTeacherList dxTeacher = new DxTeacherList();
									dxTeacher
									.initWithAttributes((JSONObject) response
											.get(i));
									mLatestTeacherList.add(dxTeacher);
								}
							}
						} else {
							mRecommendTeacherList.clear();
							for (int i = 0; i < response.length(); i++) {
								DxTeacherList dxTeacher = new DxTeacherList();
								dxTeacher
										.initWithAttributes((JSONObject) response
												.get(i));
								mRecommendTeacherList.add(dxTeacher);
							}
						}
						getNextList();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void getPostsList() {
		// TODO Auto-generated method stub

		Map<String, Object> mMap = new HashMap<String, Object>();
		mMap.put("start", 0);
		mMap.put("pageSize", 2);
		String urlString = UrlEngine.getPostsIssueList(mMap);
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
						mForumList.clear();
						for (int i = 0; i < response.length(); i++) {
							DxForumTopic dxForumTopic = new DxForumTopic();
							dxForumTopic
									.initWithAttributes((JSONObject) response
											.get(i));
							mForumList.add(dxForumTopic);
						}
						getNextList();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void getNextList() {
		// TODO Auto-generated method stub
		if (str.length() > strIndex) {
			switch (str.charAt(strIndex)) {
			case '1':
				getCollectList();
				break;
			case '2':
				getTeacherList("createTime desc", false);
				break;
			case '4':
				getTeacherList("identify desc,id desc", false);
				break;
			case '8':
				getPostsList();
				break;
			case 'f':
				getTeacherList("createTime desc", true);
				break;
			}
			strIndex++;
		} else {
			initCardData();
		}
	}

	private void initCardData() {
		// TODO Auto-generated method stub
		getUserChannelList();
		cardType = new ArrayList<Integer>();
		List<Integer> cardTypeList = new ArrayList<Integer>();
		for (int i = 0; i < userChannelList.size(); i++) {
			String cardName = userChannelList.get(i).getName();
			int id = userChannelList.get(i).getId();
			int cardStatu = userChannelList.get(i).getSelected();
			if (cardStatu == 1) {
				if (cardName.equals("我的收藏")) {
					if (!TextUtils.isEmpty(userId)) {
						if (mCollectList.size() != 0) {
							cardTypeList.add(id);
						}
					}
				} else if (cardName.equals("新入住教师")) {
					if (mLatestTeacherList.size() != 0) {
						cardTypeList.add(id);
					}
				} else if (cardName.equals("专职教师")) {
					if (mFullTimeTeacherList.size() != 0) {
						cardTypeList.add(id);
					}
				} else if (cardName.equals("我的帖子")) {
					if (!TextUtils.isEmpty(userId)) {
						if (mForumList.size() != 0) {
							cardTypeList.add(id);
						}
					}
				} else {
					if (mRecommendTeacherList.size() != 0) {
						cardTypeList.add(id);
					}
				}
			}
		}

		cardType.addAll(cardTypeList);
		Map<Integer, Object> map;
		for (int i = 0; i < cardType.size(); i++) {
			switch (cardType.get(i)) {
			case 1:
				map = new HashMap<Integer, Object>();
				map.put(1, mCollectList);
				mCardList.add(map);
				break;
			case 2:
				map = new HashMap<Integer, Object>();
				map.put(2, mLatestTeacherList);
				mCardList.add(map);
				break;
			case 4:
				map = new HashMap<Integer, Object>();
				map.put(4, mRecommendTeacherList);
				mCardList.add(map);
				break;
			case 8:
				map = new HashMap<Integer, Object>();
				map.put(8, mForumList);
				mCardList.add(map);
				break;
			case 16:
				map = new HashMap<Integer, Object>();
				map.put(16, mFullTimeTeacherList);
				mCardList.add(map);
				break;
			}
		}
		mAdapter.setCardType(cardType);

	}

	@Override
	public void onResume() {
		super.onResume();
		boolean flush = mApplication.getSpUtil().getCardListFlushFlag();
		if (flush) {
			mApplication.getSpUtil().isCardListFlushFlag(false);
			if (null != cardType) {
				for (int i = 0; i < cardType.size(); i++) {
					if (cardType.get(i) == 4) {
						flush = true;
					}
				}
				if (flush) {
					mCardList.clear();
					getCardList();
				}
			}
		}
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		String str = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 设置上一次刷新的提示标签
		refreshView.getLoadingLayoutProxy()
				.setLastUpdatedLabel("最后更新时间:" + str);
		// 下拉刷新 业务代码
		if (refreshView.isShownHeader()) {
			// 设置刷新标签
			mScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
			// 设置下拉标签
			mScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
			// 设置释放标签
			mScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
			getCardList();
			mScrollView.onRefreshComplete();
		}
		// 上拉加载更多 业务代码
		if (refreshView.isShownFooter()) {
			// 设置刷新标签
			mScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
			// 设置下拉标签
			mScrollView.getLoadingLayoutProxy().setPullLabel("上拉加载");
			// 设置释放标签
			mScrollView.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
		}
	}

}
