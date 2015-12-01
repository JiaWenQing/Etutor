package net.dx.etutor.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MyCollectAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxCollect;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.listview.XListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class CollectActivity extends BaseActivity implements
		OnRefreshListener<ListView> {

	public static String TAG = "CollectActivity";
	private MyCollectAdapter mAdapter;
	private List<DxCollect> mList = new ArrayList<DxCollect>();
	private List<DxCollect> mListFlush = new ArrayList<DxCollect>();
	private Map<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;
	private String userId;
	private RelativeLayout mFinishNetwork;
	private LinearLayout mNotInfo;
	private PullToRefreshListView mListView;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_collect);
		setTitle(R.string.text_my_collect);
		userId = mApplication.getSpUtil().getUserId();
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mNotInfo = (LinearLayout) findViewById(R.id.layout_no_essence);
		mNotInfo.setVisibility(View.GONE);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_collect_list);
		mListView.setVisibility(View.GONE);
		mAdapter = new MyCollectAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		mMap = new HashMap<String, Object>();
		mMap.put("userId", userId);
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		getCollectList(0);
	}

	/**
	 * @param map
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getCollectList(final int type) {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
		}
		String urlString = UrlEngine.getCollectList(mMap);
		showLoadingDialog("请稍后...");
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
					mListView.setVisibility(View.GONE);
					mListView.onRefreshComplete();
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
					mListView.onRefreshComplete();
				}
			}

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
						mList.clear();
						if (type == 0) {
							mListFlush.clear();
						}
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
							mListFlush.add(dxCollect);

						}
						mList.addAll(mListFlush);
						mAdapter.notifyDataSetChanged();
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
		});

	}

	@Override
	public void initEvents() {
		mListView.setOnRefreshListener(this);
		mFinishNetwork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMap.put("start", 0);
				mMap.put("pageSize", (start + 1) * pageSize);
				getCollectList(0);
			}
		});
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mMap.put("start", 0);
		mMap.put("pageSize", (start + 1) * pageSize);
		getCollectList(0);
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
			getCollectList(0);
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
			getCollectList(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}
}
