package net.dx.etutor.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.activity.search.SearchStudentNeedActivity;
import net.dx.etutor.activity.student.StudentIntroActivity;
import net.dx.etutor.activity.student.StudentNeedListActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SortableUtil;
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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class NeedStudentListActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener<ListView>, OnItemClickListener {

	public static String TAG = "NeedStudentListActivity";
	private PullToRefreshListView mListView;
	private List<DxNeed> mList = new ArrayList<DxNeed>();
	private List<DxNeed> mListFlush = new ArrayList<DxNeed>();
	private TeacherNeedAdapter mAdapter;
	private LinearLayout mFilter, mFilterNew, mFilterAuthenticate,
			mFilterAppraise;

	private String orderType = "createTime desc";

	private Map<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;
	private String distance;
	private RelativeLayout mFinishNetwork;
	private TextView mNotInfo;
	private Bundle bundle = new Bundle();

	private int loginStatu;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_need_student_list);
		setTitle(R.string.text_search_need);
		setIcon(R.drawable.main_head_bar_icon_search_selector);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mFilter = (LinearLayout) findViewById(R.id.filter_layout);
		mNotInfo = (TextView) findViewById(R.id.tv_not_info);
		mNotInfo.setVisibility(View.GONE);
		mFilterNew = (LinearLayout) findViewById(R.id.filter_new);
		mFilterAuthenticate = (LinearLayout) findViewById(R.id.filter_authenticate);
		mFilterAppraise = (LinearLayout) findViewById(R.id.filter_appraise);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_teacher_need_list);
		mListView.setVisibility(View.GONE);
		mFilter.setVisibility(View.GONE);
		mAdapter = new TeacherNeedAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setMode(Mode.BOTH);
		mMap = new HashMap<String, Object>();
		mMap.put("orderType", orderType);
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mMap.put("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		getTeacherNeedList(mMap, 0);
	}

	/**
	 * @param map
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getTeacherNeedList(Map<String, Object> map, final int type) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mFilter.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
			mFilter.setVisibility(View.VISIBLE);
		}
		String url = UrlEngine.needStudentList(map);
		showLoadingDialog("请稍后...");
		HttpUtil.post(url, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				mListView.onRefreshComplete();
				dismissLoadingDialog();
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
					mListView.setVisibility(View.GONE);
					mFilter.setVisibility(View.GONE);
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
								DxNeed dxNeed = new DxNeed();
								dxNeed.initWithAttributes((JSONObject) response
										.get(i));
								mListFlush.add(dxNeed);
							}
							mList.addAll(mListFlush);
							mAdapter.notifyDataSetChanged();
						}
						if (mList.size() == 0) {
							mListView.setVisibility(View.GONE);
							mFilter.setVisibility(View.GONE);
							mNotInfo.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.VISIBLE);
							mFilter.setVisibility(View.VISIBLE);
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
		mFilterNew.setOnClickListener(this);
		mFilterAuthenticate.setOnClickListener(this);
		mFilterAppraise.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
	}

	/**
	 * 发布课程信息
	 * 
	 * @param v
	 */
	public void onReleaseCourse(View v) {
		Intent intent = null;
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu == 0) {
			intent = new Intent(this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		} else {
			intent = new Intent(this, StudentNeedListActivity.class);
			startActivityByPendingTransition(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_finish_network:
			mMap.put("start", 0);
			mMap.put("pageSize", (start + 1) * pageSize);
			getTeacherNeedList(mMap, 0);
			break;
		default:
			// 排序
			SortableUtil.cleanSortType();
			SortableUtil.onClickOfSort(NeedStudentListActivity.this, v, 0);
			orderType = SortableUtil.getSortType();
			if (!TextUtils.isEmpty(orderType)) {
				mMap.put("distance", distance);
				mMap.put("orderType", orderType);
				mMap.put("start", 0);
				mMap.put("pageSize", (start + 1) * pageSize);
				getTeacherNeedList(mMap, 0);
			}
			break;
		}

	}

	@Override
	public void iconClick() {
		super.iconClick();
		Intent intent = new Intent(NeedStudentListActivity.this,
				SearchStudentNeedActivity.class);
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
				bundle = data.getExtras();
				start = 0;
				mList.clear();
				mAdapter.notifyDataSetChanged();
				distance = data.getStringExtra("distance");
				mMap.put("subject", data.getStringExtra("subject"));
				mMap.put("province", data.getStringExtra("province"));
				mMap.put("city", data.getStringExtra("city"));
				mMap.put("region", data.getStringExtra("region"));
				mMap.put("distance", data.getStringExtra("distance"));
				mMap.put("latitude", data.getDoubleExtra("latitude", 31.239004));
				mMap.put("longitude",
						data.getDoubleExtra("longitude", 121.481647));
				mMap.put("start", start);
				mMap.put("pageSize", pageSize);
				getTeacherNeedList(mMap, 0);
				break;
			case 1000:
				intent = new Intent(this, StudentNeedListActivity.class);
				startActivityByPendingTransition(intent);
				break;
			}

		}
	}

	private class TeacherNeedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChildViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(NeedStudentListActivity.this)
						.inflate(R.layout.item_collect_student, null);
				viewHolder = new ChildViewHolder();
				viewHolder.mRoundHeadImageView = (RoundHeadImageView) convertView
						.findViewById(R.id.imv_collect_child);
				viewHolder.mNeedTitle = (TextView) convertView
						.findViewById(R.id.title_tv_child);
				viewHolder.mSubjectItemName = (TextView) convertView
						.findViewById(R.id.tv_subject);
				viewHolder.mTeachTimes = (TextView) convertView
						.findViewById(R.id.tv_lecture_count);
				viewHolder.mPrice = (TextView) convertView
						.findViewById(R.id.tv_student_class_fees);
				viewHolder.mIdentify = (ImageView) convertView
						.findViewById(R.id.identify_imv_student);
				viewHolder.mVerify = (ImageView) convertView
						.findViewById(R.id.imv_student_verify);
				viewHolder.mRating = (RatingBar) convertView
						.findViewById(R.id.ratingbar_student);
				viewHolder.mRootLayout = (LinearLayout) convertView
						.findViewById(R.id.student_layout_collect);
				viewHolder.mIdentify.setVisibility(View.GONE);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ChildViewHolder) convertView.getTag();
			}
			final DxNeed dxNeed = (DxNeed) getItem(position);
			if (dxNeed.getDxUser().getAvatarUrl() != null
					&& !dxNeed.getDxUser().getAvatarUrl().equals("")) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE
								+ dxNeed.getDxUser().getAvatarUrl(),
						viewHolder.mRoundHeadImageView,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mRoundHeadImageView
						.setImageResource(R.drawable.avatar);
			}
			double price = dxNeed.getPrice();
			if (price == 0) {
				viewHolder.mPrice.setText("面议");
			} else {
				viewHolder.mPrice.setText(price + "元/小时");
			}

			float rating = dxNeed.getDxUser().getRank();
			viewHolder.mRating.setRating(rating);
			String title = dxNeed.getNeedTitle();
			if (TextUtils.isEmpty(title)) {
				viewHolder.mNeedTitle.setText(R.string.text_teacher);
			} else {
				viewHolder.mNeedTitle.setText(title);
			}
			viewHolder.mSubjectItemName.setText(dxNeed.getSubjectItemId());
			if (dxNeed.getTradeNumber() == null || dxNeed.getTradeNumber() == 0) {
				viewHolder.mTeachTimes.setText("面议");
			} else {
				viewHolder.mTeachTimes.setText(dxNeed.getTradeNumber()
						+ "次/周  ");
			}
			viewHolder.mIdentify.setVisibility(View.GONE);
			viewHolder.mVerify.setVisibility(View.GONE);
			int identify = dxNeed.getDxUser().getIdentify();
			for (int i = 0; i < Constants.LABEL_NUMBER_STUDENT; i++) {
				if ((identify & (1 << i)) != 0) {
					switch (i) {
					case 2:
						viewHolder.mIdentify.setVisibility(View.VISIBLE);
						break;
					case 1:
						viewHolder.mVerify.setVisibility(View.VISIBLE);
						break;
					}
				}
			}
			return convertView;
		}
	}

	class ChildViewHolder {
		public RatingBar mRating;
		private RoundHeadImageView mRoundHeadImageView;
		private TextView mSubjectItemName;
		private TextView mNeedTitle;
		private TextView mTeachTimes;
		private TextView mPrice;
		private ImageView mIdentify;
		private ImageView mVerify;
		private LinearLayout mRootLayout;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		index = (int) id;
		Intent intent = new Intent(NeedStudentListActivity.this,
				StudentIntroActivity.class);
		intent.putExtra("dxNeed", mList.get(index));
		intent.putExtra("type", "needStudent");
		startActivityByPendingTransition(intent);
	}

	private String id;
	private String collectId;
	private int index;

	@Override
	protected void onRestart() {
		super.onRestart();
		dismissLoadingDialog();
		id = mApplication.getSpUtil().getCollectItemId();
		collectId = mApplication.getSpUtil().getCollectId();
		if (!TextUtils.isEmpty(id) && id.equals(mList.get(index).getUserId())) {
			mList.get(index).setCollectId(collectId);
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
			getTeacherNeedList(mMap, 0);
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
			getTeacherNeedList(mMap, 1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}

}
