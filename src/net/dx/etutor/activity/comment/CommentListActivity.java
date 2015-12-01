package net.dx.etutor.activity.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.CommentListAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxComment;
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

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class CommentListActivity extends BaseActivity implements
		OnRefreshListener<ListView>, OnClickListener {

	public static String TAG = "CommentListActivity";
	private PullToRefreshListView mListView;
	private CommentListAdapter mAdapter;
	private static List<DxComment> mList = new ArrayList<DxComment>();

	private String toUserId;
	private RelativeLayout mFinishNetwork;
	// private Button mFinish;
	private Map<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;
	private TextView mNoComment;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_comment_list);
		setTitle(R.string.title_comment_list);
		toUserId = getIntent().getStringExtra("toUserId");
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mNoComment = (TextView) findViewById(R.id.tv_no_comment);
		mNoComment.setVisibility(View.GONE);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_comment_list);
		mListView.setVisibility(View.GONE);
		mAdapter = new CommentListAdapter(this, mList, true);
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		mMap = new HashMap<String, Object>();
		mMap.put("toUserId", toUserId);
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		getCommentList(0);
	}

	/**
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getCommentList(final int type) {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
		}
		String urlString = UrlEngine.getCommentList(mMap);
		showLoadingDialog("请稍后...");
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
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
						if (type == 0) {
							mList.clear();
						}
						for (int i = 0; i < response.length(); i++) {
							DxComment dxComment = new DxComment();
							dxComment.initWithAttributes((JSONObject) response
									.get(i));
							mList.add(dxComment);
						}
						if (mList.size() == 0) {
							mNoComment.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.VISIBLE);
							mNoComment.setVisibility(View.GONE);
						}
						mAdapter.notifyDataSetChanged();
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
		mFinishNetwork.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mMap.put("start", 0);
		mMap.put("pageSize", (start + 1) * pageSize);
		getCommentList(0);
	}

	/*
	 * @Override public void onRefresh() { mListView.postDelayed(new Runnable()
	 * {
	 * 
	 * @Override public void run() { mMap.put("start", 0); mMap.put("pageSize",
	 * (start + 1) * pageSize); // 设置更新时间 String time =
	 * DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
	 * mListView.setRefreshTime(time); getCommentList(0); stop(); } }, 1000); }
	 * 
	 * public void stop() { mListView.stopLoadMore(); mListView.stopRefresh(); }
	 * 
	 * @Override public void onLoadMore() { mListView.postDelayed(new Runnable()
	 * {
	 * 
	 * @Override public void run() { start++; mMap.put("start", start * pageSize
	 * + 1); mMap.put("pageSize", pageSize); getCommentList(1); stop(); } },
	 * 1000);
	 * 
	 * }
	 */
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
			getCommentList(0);
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
			getCommentList(1);
		}

	}
}
