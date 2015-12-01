package net.dx.etutor.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MessageAdapter;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.message.PrivateMessageDetailActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.LoadingDialog;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;
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
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class MessageListFragment extends BaseFragment implements
		OnRefreshListener<ListView>, OnItemClickListener, OnClickListener {

	public static String TAG = "MessageListFragment";
	private PullToRefreshListView mListView;
	private List<DxPrivatemsg> mList = new ArrayList<DxPrivatemsg>();
	private List<DxPrivatemsg> mListFlush = new ArrayList<DxPrivatemsg>();
	private MessageAdapter mAdapter;

	private LinearLayout mFinishNetwork;

	private Map<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;
	private Integer count;
	private String userId;
	private String imagePath;
	private int[] msgCount;
	private int[] msgCounts;

	public DxPrivatemsg dxPrivatemsg = new DxPrivatemsg();

	private TextView mStatu;
	private String statu = "";

	private int index;

	private TextView mNotInfo;
	private TextView mNotLogin;
	private LinearLayout mNotLoginLayout;
	private int loginStatu;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_main_message, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public MessageListFragment() {
		super();
	}

	public MessageListFragment(EtutorApplication application,
			Activity activity, Context context) {
		super(application, activity, context);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("私信");
		settingIcon(0, true, "");
		mFinishNetwork = (LinearLayout) findViewById(R.id.layout_finish_network);
		mNotInfo = (TextView) findViewById(R.id.tv_not_info);
		mNotInfo.setVisibility(View.GONE);
		mNotLogin = (TextView) findViewById(R.id.tv_login);
		mNotLoginLayout = (LinearLayout) findViewById(R.id.layout_login);
		mNotLoginLayout.setVisibility(View.GONE);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_private_message_list);
		mListView.setVisibility(View.GONE);
		mAdapter = new MessageAdapter(mList,dxPrivatemsg,getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mFinishNetwork.setOnClickListener(this);
		mNotLogin.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		statu = mApplication.getSpUtil().getMsgCount();
		if (!TextUtils.isEmpty(statu)) {
			msgCount = new int[statu.length()];
			char[] c = statu.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '1') {
					msgCount[i] = 1;
				} else {
					msgCount[i] = 0;
				}
			}
		}
		mMap = new HashMap<String, Object>();
		mMap.put("userId", mApplication.getSpUtil().getUserId());
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		if (loginStatu != 0) {
			getMessageList(0);
		}
	}



	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_finish_network:
			if (loginStatu != 0) {
				mMap.put("start", 0);
				mMap.put("pageSize", (start + 1) * pageSize);
				getMessageList(0);
			}
			break;
		case R.id.tv_login:
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 2000);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 101:
				int msgType = data.getIntExtra("NewMsgType", 1);
				mList.get(index).setMessage(null);
				mList.get(index).setRecord(null);
				mList.get(index).setPicture(null);
				mList.get(index).setCreateTime(
						mApplication.getSpUtil().getNewMsgTime());
				switch (msgType) {
				case 1:
					mList.get(index).setMessage(
							mApplication.getSpUtil().getNewMsgContent());
					break;
				case 2:
					mList.get(index).setRecord(
							mApplication.getSpUtil().getNewMsgContent());
					break;
				case 3:
					mList.get(index).setPicture(
							mApplication.getSpUtil().getNewMsgContent());
					break;
				}
				mAdapter.notifyDataSetChanged();
				break;
			case 2000:
				mMap.put("userId", mApplication.getSpUtil().getUserId());
				mMap.put("start", 0);
				mMap.put("pageSize", (start + 1) * pageSize);
				getMessageList(0);
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		index = (int) id;
		DxPrivatemsg privatemsg = mList.get(index);
		statu = "";
		for (int i = 0; i < msgCount.length; i++) {
			if (i == index) {
				msgCount[i] = 0;
			}
			statu = statu + msgCount[i];
		}
		EtutorApplication.getInstance().getSpUtil().setMsgCount(statu);
		mStatu = (TextView) view.findViewById(R.id.tv_message_count);
		mStatu.setVisibility(View.INVISIBLE);
		Intent intent = new Intent(getActivity(),
				PrivateMessageDetailActivity.class);
		intent.putExtra("dxPrivatemsg", privatemsg);
		intent.putExtra("type", "PrivateList");
		intent.putExtra("index", index);
		intent.putExtra("imagePath", privatemsg.getDxUser().getAvatarUrl());
		startActivityForResult(intent, 101);
	}

	/**
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getMessageList(final int type) {
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			Toast.makeText(getActivity(), R.string.network_error, 0).show();
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
		}
		userId = mApplication.getSpUtil().getUserId();
		mMap.put("userId", userId);
		showLoadingDialog("请稍后……");

		String urlString = UrlEngine.getMessageList(mMap);
		System.out.println(TAG + "==" + urlString);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				mListView.onRefreshComplete();
				dismissLoadingDialog();
				if (statusCode == 0) {
					Toast.makeText(getActivity(),
							R.string.text_link_server_error, 0).show();
					mListView.setVisibility(View.GONE);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					Toast.makeText(getActivity(), R.string.text_error_network,
							0).show();
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
							msgCounts = new int[response.length() + start
									* pageSize];
							for (int i = 0; i < response.length(); i++) {
								DxPrivatemsg dxPrivatemsg = new DxPrivatemsg();
								dxPrivatemsg
										.initWithAttributes((JSONObject) response
												.get(i));
								mListFlush.add(dxPrivatemsg);
								msgCounts[i + start * pageSize] = dxPrivatemsg
										.getNewMsg();
							}

							if (TextUtils.isEmpty(statu)) {
								msgCount = new int[response.length() + start
										* pageSize];
								for (int i = 0; i < msgCounts.length; i++) {
									msgCount[i] = msgCounts[i];
								}
							} else {
								if (msgCount.length < msgCounts.length) {
									int sub = msgCounts.length
											- msgCount.length;
									int[] msg = new int[msgCounts.length];
									for (int i = 0; i < msg.length; i++) {
										if (i == 0) {
											if ((msgCount[i] | msgCounts[i]) == 1) {
												msg[i] = 1;
											} else {
												msg[i] = 0;
											}
										} else if (i <= sub) {
											msg[i] = msgCounts[i];
										} else {
											if ((msgCount[i - sub] | msgCounts[i]) == 1) {
												msg[i] = 1;
											} else {
												msg[i] = 0;
											}
										}
									}
									msgCount = msg;
								} else if (msgCount.length == msgCounts.length) {
									for (int i = 0; i < msgCount.length; i++) {
										if ((msgCount[i] | msgCounts[i]) == 1) {
											msgCount[i] = 1;
										} else {
											msgCount[i] = 0;
										}
									}
								} else {
									msgCount = new int[response.length()];
									for (int i = 0; i < msgCounts.length; i++) {
										msgCount[i] = msgCounts[i];
									}
								}
							}

							statu = "";
							for (int i = 0; i < msgCount.length; i++) {
								statu = statu + msgCount[i];
							}
							mApplication.getSpUtil().setMsgCount(statu);

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
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu == 0) {
			mNotLoginLayout.setVisibility(View.VISIBLE);
		} else {
			mNotLoginLayout.setVisibility(View.GONE);
		}
		MobclickAgent.onPageStart(TAG); // 统计页面
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
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getMessageList(0);
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
			getMessageList(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}

}
