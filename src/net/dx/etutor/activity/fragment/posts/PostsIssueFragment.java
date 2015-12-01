package net.dx.etutor.activity.fragment.posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MyPostsAdapter;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.listview.XListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 我发布的帖子
 * 
 * @author app
 * 
 */
public class PostsIssueFragment extends BaseFragment implements
		OnClickListener, OnRefreshListener<ListView> {
	/**
	 * 友盟统计
	 */
	protected boolean isCreated = false;

	public static String TAG = "PostsIssueFragment";
	private PullToRefreshListView mListView;
	private MyPostsAdapter mAdapter;
	private List<DxForumTopic> mList = new ArrayList<DxForumTopic>();
	private List<DxForumTopic> mListFlush = new ArrayList<DxForumTopic>();

	private Map<String, Object> mMap;
	private LinearLayout mNotCollectData;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;

	public PostsIssueFragment() {
		super();
	}

	public PostsIssueFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isCreated = true;
	}

	/**
	 * 此方法目前仅适用于标示ViewPager中的Fragment是否真实可见 For 友盟统计的页面线性不交叉统计需求
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (!isCreated) {
			return;
		}

		if (isVisibleToUser) {
			umengPageStart();
		} else {
			umengPageEnd();
		}

	}

	private void umengPageStart() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageStart(TAG);
	}

	private void umengPageEnd() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_posts_issue_list, container,
				false);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mNotCollectData = (LinearLayout) findViewById(R.id.layout_no_issue);
		mNotCollectData.setVisibility(View.GONE);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_posts_issue);
		mListView.setVisibility(View.GONE);
		mMap = new HashMap<String, Object>();
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mAdapter = new MyPostsAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		getPostsIssueList(0);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(this);
	}

	/**
	 * @param map
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getPostsIssueList(final int type) {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			mNotCollectData.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.getPostsIssueList(mMap);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
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
								DxForumTopic dxForumTopic = new DxForumTopic();
								dxForumTopic
										.initWithAttributes((JSONObject) response
												.get(i));
								mListFlush.add(dxForumTopic);
							}
							mList.addAll(mListFlush);
							mListView.onRefreshComplete();
							mAdapter.notifyDataSetChanged();
						}
						if (mList.size() == 0) {
							mListView.setVisibility(View.GONE);
							mNotCollectData.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.VISIBLE);
							mNotCollectData.setVisibility(View.GONE);
						}
						// dismissLoadingDialog();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_finish_network:
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getPostsIssueList(0);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 500:
				int isBelaud = data.getIntExtra("isBelaud", 0);
				int belauds = data.getIntExtra("belauds", 0);
				int clicks = data.getIntExtra("clicks", 0);
				int count = data.getIntExtra("count", 0);
				int replyId = data.getIntExtra("replyId", -1);
				String collectId = data.getStringExtra("collectId");
				for (int i = 0; i < mList.size(); i++) {
					if (replyId == mList.get(i).getReplyId()) {
						mList.get(i).setIsBelaud(isBelaud);
						mList.get(i).setBelauds(belauds);
						mList.get(i).setClicks(clicks);
						mList.get(i).setCount(count);
						mList.get(i).setCollectId(collectId);
						break;
					}
				}
				mAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

	public void updateListView(boolean isShow) {
		mAdapter.setIsShow(isShow);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	public void onRefresh() {
		onRefresh(mListView);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
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
			mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
			// 设置下拉标签
			mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
			// 设置释放标签
			mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getPostsIssueList(0);
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
			getPostsIssueList(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}
	}

}
