package net.dx.etutor.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.InterestingAdapter;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.forum.ForumTopicListActivity;
import net.dx.etutor.activity.forum.MyPostsActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumBoard;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 闲趣列表
 * 
 * @author tzm
 * 
 */
public class ForumListFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener<ListView> {

	private PullToRefreshListView mListView;

	public static String TAG = "ForumListFragment";
	// private XListView mListView;
	private List<DxForumBoard> mList = new ArrayList<DxForumBoard>();
	private ImageView mImageline;
	private RelativeLayout mFinishNetwork;

	// private Forumadpter mAdapter ;

	public ForumListFragment() {
		super();
	}

	public ForumListFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_forum_layout, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private InterestingAdapter mAdapter;

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("闲趣");
		settingIcon(0, true, "");
		mListView = (PullToRefreshListView) findViewById(R.id.forum_listview);
		mListView.setMode(Mode.PULL_FROM_START);
		mAdapter = new InterestingAdapter(mList,getActivity());
		mListView.setAdapter(mAdapter);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_list);
	}

	@Override
	protected void initEvents() {
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mFinishNetwork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getBoardList();
			}
		});
	}

	@Override
	protected void initData() {

	}

	public void getBoardList() {
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
		}
		String urlString = UrlEngine.getBoardList();
		showLoadingDialog("请稍后……");
		System.out.println(TAG+":"+urlString);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				mListView.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				dismissLoadingDialog();

				if (statusCode == Constants.STAT_200) {
					try {
						mList.clear();
						if (response.length() != 0) {
							for (int i = 0; i < response.length(); i++) {
								DxForumBoard dxForumBoard = new DxForumBoard();
								dxForumBoard.initWithAttributes(response
										.getJSONObject(i));
								mList.add(dxForumBoard);
							}
							if (mList.size() == 0) {
								mListView.setVisibility(View.GONE);
							} else {
								mListView.setVisibility(View.VISIBLE);
							}
							mAdapter.notifyDataSetChanged();
							mListView.onRefreshComplete();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mList.get(position - 1).getId() == 0) {
			startActivity(new Intent(getActivity(), MyPostsActivity.class));
		} else {
			Intent intent = new Intent(getActivity(),
					ForumTopicListActivity.class);
			intent.putExtra("Board", mList.get(position - 1));
			startActivityByPendingTransition(intent);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		getBoardList();
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
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
		}

		// 上拉加载更多 业务代码
		if (refreshView.isShownFooter()) {
			// 设置刷新标签
			mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
			// 设置下拉标签
			mListView.getLoadingLayoutProxy().setPullLabel("上拉加载");
			// 设置释放标签
			mListView.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
		}
		getBoardList();
	}
}
