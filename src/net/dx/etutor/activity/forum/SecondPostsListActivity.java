package net.dx.etutor.activity.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.SecondPostsReplyAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumReply;
import net.dx.etutor.model.DxForumReplySecond;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 楼中楼(2楼)
 * 
 * @author jwq
 * 
 */
public class SecondPostsListActivity extends BaseActivity implements
		OnRefreshListener<ListView> {

	public static String TAG = "PostsListActivity";

	private SecondPostsReplyAdapter mAdapter;
	private PullToRefreshListView mListView;
	private ArrayList<DxForumReplySecond> mList = new ArrayList<DxForumReplySecond>();
	private List<DxForumReplySecond> mListRefresh = new ArrayList<DxForumReplySecond>();
	private DxForumReply dxForumReply;
	private DxForumTopic dxForumTopic;

	private String floor;
	private int topicId;
	private String replyIndex;

	private HashMap<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_posts_reply_list);

		dxForumTopic = (DxForumTopic) getIntent().getSerializableExtra(
				"dxForumTopic");
		dxForumReply = dxForumTopic.getDxForumReply();
		mListView = (PullToRefreshListView) findViewById(R.id.lv_posts_second_reply);
		initData();
	}

	@Override
	public void initEvents() {
		mListView.setOnRefreshListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		topicId = dxForumTopic.getId();
		replyIndex = dxForumReply.getReplyIndex();

		floor = dxForumReply.getReplyIndex();
		setTitle(floor + "楼");

		mAdapter = new SecondPostsReplyAdapter(this, dxForumReply, mList,1);
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		mMap = new HashMap<String, Object>();
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mMap.put("topicId", topicId);
		mMap.put("replyIndex", replyIndex);
		getSecondRelpyList(0);
	}

	/**
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getSecondRelpyList(final int type) {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.getRelpyList(2, mMap);
		showLoadingDialog("请稍后...");
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

						if (response.length() != 0) {
							dxForumReply.getDxForumReplySeconds().clear();
							mList.clear();
							if (type == 0) {
								mListRefresh.clear();
							}
							dxForumReply.initWithAttributes(response
									.getJSONObject(0));
							mListRefresh.addAll(dxForumReply
									.getDxForumReplySeconds());
							mList.addAll(mListRefresh);
							mAdapter.notifyDataSetChanged();
						}
						if (mList.size() == 0) {
							mListView.setVisibility(View.GONE);
						} else {
							mListView.setVisibility(View.VISIBLE);
						}
						surplusPage = mList.size() % pageSize;
						if (surplusPage != 0 || response.length() == 0) {
							mListView.onRefreshComplete();
							if (type == 1) {
								showShortToast(R.string.text_loading_last);
							}
						}
						dismissLoadingDialog();
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
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 101:
				start = 0;
				dxForumReply.getDxForumReplySeconds().clear();
				mAdapter.notifyDataSetChanged();
				mListView.setMode(Mode.BOTH);
				mMap.put("start", start);
				mMap.put("pageSize", pageSize);
				getSecondRelpyList(0);
				break;
			}
		}
	}

	public void onRefresh() {
		onRefresh(mListView);
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
			getSecondRelpyList(0);
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
			getSecondRelpyList(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(
					dxForumReply.getDxForumReplySeconds().size() - 1);
		}

	}

}
