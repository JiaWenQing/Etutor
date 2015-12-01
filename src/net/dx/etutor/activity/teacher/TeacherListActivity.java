package net.dx.etutor.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.TeacherAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.activity.register.PerfectionInfoActivity;
import net.dx.etutor.activity.search.SearchTeacherActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.popupwindow.TeacherFilterPopupWindow.FilterOnClickListener;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SortableUtil;
import net.dx.etutor.view.listview.XListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class TeacherListActivity extends BaseActivity implements
		OnClickListener, FilterOnClickListener, OnRefreshListener<ListView> {

	public static String TAG = "TeacherListActivity";
	private LinearLayout mFilter, mFilterSeniority, mFilterAuthenticate,
			mFilterAppraise, mFilterNew;
	private TeacherAdapter mAdapter;
	private List<DxTeacherList> mList = new ArrayList<DxTeacherList>();
	private List<DxTeacherList> mListFlush = new ArrayList<DxTeacherList>();
	private PullToRefreshListView mListView;
	private ImageView mNotInfo;
	private String sortType = "createTime desc";
	private RelativeLayout mFinishNetwork;

	private Map<String, Object> mMap;
	private int start = 0;
	private int pageSize = 20;
	private int surplusPage = 0;
	private String distance;
	private String lectureType;
	private Bundle bundle = new Bundle();
	private int loginStatu;
	private int userType;
	private int listType;
	private RelativeLayout mPublishLayout;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_teacher_list);
		setIcon(R.drawable.main_head_bar_icon_search_selector);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mPublishLayout = (RelativeLayout) findViewById(R.id.layout);
		mNotInfo = (ImageView) findViewById(R.id.imv_not_info);
		mNotInfo.setVisibility(View.GONE);
		mFilter = (LinearLayout) findViewById(R.id.filter_layout);
		mFilterSeniority = (LinearLayout) findViewById(R.id.filter_seniority);
		mFilterAuthenticate = (LinearLayout) findViewById(R.id.filter_authenticate);
		mFilterAppraise = (LinearLayout) findViewById(R.id.filter_appraise);
		mFilterNew = (LinearLayout) findViewById(R.id.filter_new);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_teacher_list);
		mListView.setVisibility(View.GONE);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mMap = new HashMap<String, Object>();
		mMap.put("userId", mApplication.getSpUtil().getUserId());
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		listType = getIntent().getIntExtra("listType",-1);
		if (listType!=-1 ) {
			switch (listType) {
			case 0:
				sortType = "createTime desc";
				setTitle(R.string.text_teacher_list_title_1);
				break;
			case 1:
				sortType = "identify desc,id desc";
				setTitle(R.string.text_teacher_list_title_2);
				break;
			case 2:
				sortType = "createTime desc";
				setTitle(R.string.text_teacher_list_title_3);
				mMap.put("fullTime", "1");
				break;
			}
			mFilter.setVisibility(View.GONE);
			mPublishLayout.setVisibility(View.GONE);
			mMap.put("province", mApplication.getSpUtil().getLocationCity());
		} else {
			setTitle(R.string.text_teacher);
			sortType = "createTime desc";
			mFilter.setVisibility(View.VISIBLE);
		}
		mMap.put("orderType", sortType);
		mAdapter = new TeacherAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		getTeacherList(mMap, 0);
	}

	@Override
	public void iconClick() {
		super.iconClick();
		Intent intent = new Intent(TeacherListActivity.this,
				SearchTeacherActivity.class);
		intent.putExtras(bundle);
		startActivityForResultByPendingTransition(intent,
				Constants.STUDENT_NEED_REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Intent intent;
			switch (requestCode) {
			case Constants.STUDENT_NEED_REQUESTCODE:
				showLoadingDialog("请稍后...");
				mListView.setVisibility(View.GONE);
				bundle = data.getExtras();
				/*
				 * mListView.setPullLoadEnable(true);
				 * mListView.setPullRefreshEnable(true);
				 */
				start = 0;
				mList.clear();
				mAdapter.notifyDataSetChanged();
				distance = data.getStringExtra("distance");
				lectureType = data.getStringExtra("lectureType");
				mMap.put("subject", data.getStringExtra("subject"));
				mMap.put("lectureType", lectureType);
				mMap.put("province", data.getStringExtra("province"));
				mMap.put("city", data.getStringExtra("city"));
				mMap.put("region", data.getStringExtra("region"));
				mMap.put("distance", distance);
				mMap.put("latitude", data.getDoubleExtra("latitude", 31.239004));
				mMap.put("longitude",
						data.getDoubleExtra("longitude", 121.481647));
				mMap.put("start", start);
				mMap.put("pageSize", pageSize);
				getTeacherList(mMap, 0);
				break;
			case Constants.REQUESTCODE_SETTING_CLASS:
				showLoadingDialog("请稍后...");
				mListView.setVisibility(View.GONE);
				mList.clear();
				mAdapter.notifyDataSetChanged();
				getTeacherList(mMap, 0);
				break;
			case 1000:
				userType = mApplication.getSpUtil().getUserType();
				if (userType == 1) {
					intent = new Intent(TeacherListActivity.this,
							TeacherSettingCourseActivity.class);
				} else {
					intent = new Intent(TeacherListActivity.this,
							PerfectionInfoActivity.class);
				}
				startActivityByPendingTransition(intent);
				break;
			}
		}
	}

	@Override
	public void initEvents() {
		mFinishNetwork.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
		mFilterSeniority.setOnClickListener(this);
		mFilterAuthenticate.setOnClickListener(this);
		mFilterAppraise.setOnClickListener(this);
		mFilterNew.setOnClickListener(this);
	}

	public void onRelease(View v) {
		Intent intent = null;
		loginStatu = mApplication.getSpUtil().getLoginStatu();

		if (loginStatu == 0) {
			intent = new Intent(this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		} else {
			userType = mApplication.getSpUtil().getUserType();
			if (userType == 1) {
				intent = new Intent(TeacherListActivity.this,
						TeacherSettingCourseActivity.class);
			} else {
				intent = new Intent(TeacherListActivity.this,
						PerfectionInfoActivity.class);
			}
			startActivityByPendingTransition(intent);
		}
	}

	/**
	 * @param map
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	public void getTeacherList(Map<String, Object> map, final int type) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mFilter.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
		}
		showLoadingDialog("加载中...");
		String url = UrlEngine.getTeacherList(map);
		System.out.println(TAG+" : "+url);
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						surplusPage = response.length() % pageSize;
						if (surplusPage != 0 || response.length() == 0) {
							mListView.onRefreshComplete();
							if (type == 1) {
								showShortToast(R.string.text_loading_last);
							}
						}
						if (response.length() != 0) {
							mList.clear();
							if (type == 0) {
								mListFlush.clear();
							}
							for (int i = 0; i < response.length(); i++) {
								DxTeacherList dxTeacher = new DxTeacherList();
								dxTeacher
										.initWithAttributes((JSONObject) response
												.get(i));
								mListFlush.add(dxTeacher);
							}
							mList.addAll(mListFlush);
							mAdapter.notifyDataSetChanged();
						}
						if (mList.size() == 0) {
							mListView.setVisibility(View.GONE);
							mNotInfo.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.VISIBLE);
							mNotInfo.setVisibility(View.GONE);
						}
						mListView.onRefreshComplete();
						dismissLoadingDialog();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				mListView.onRefreshComplete();
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
					mListView.setVisibility(View.GONE);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_finish_network:
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getTeacherList(mMap, 0);
			break;
		default:
			// 排序
			SortableUtil.cleanSortType();
			SortableUtil.onClickOfSort(TeacherListActivity.this, v, 1);
			sortType = SortableUtil.getSortType();
			if (!TextUtils.isEmpty(sortType)) {
				mMap.put("start", 0);
				mMap.put("pageSize", (start + 1) * pageSize);
				mMap.put("orderType", sortType);
				getTeacherList(mMap, 0);
			}
			break;
		}

	}

	@Override
	public void filterOnClick(String value) {
		Toast.makeText(TeacherListActivity.this, value, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		dismissLoadingDialog();
		String id = mApplication.getSpUtil().getCollectItemId();
		String collectId = mApplication.getSpUtil().getCollectId();
		if (!TextUtils.isEmpty(id) && !id.equals("-1")) {
			if (Integer.valueOf(id)<mList.size()) {
				mList.get(Integer.valueOf(id)).setCollectId(collectId);
			}
		}
		mApplication.getSpUtil().setCollectItemId(null);
		mApplication.getSpUtil().setCollectId(null);
		boolean b = mApplication.getSpUtil().getRefreshFlag();
		if (b) {
			mApplication.getSpUtil().isRefreshFlag(false);
			mMap.put("userId", mApplication.getSpUtil().getUserId());
			start = 0;
			onRefresh(mListView);
		}
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
			getTeacherList(mMap, 0);
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
			getTeacherList(mMap, 1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}
	}
}
