package net.dx.etutor.activity.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;
import net.dx.etutor.view.imageview.RoundImageView;
import net.dx.etutor.view.listview.NeedExpandableListView;
import net.dx.etutor.view.listview.OwnNeedExpandableListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class StudentNeedListActivity extends BaseActivity implements
		OnClickListener, OnGroupClickListener, OnChildClickListener {

	public static String TAG = "StudentNeedListActivity";
	private StudentNeedAdapter mAdapter;
	private OwnNeedExpandableListView mListView;
	private List<DxNeed> dxNeeds = new ArrayList<DxNeed>();
	private int time = -1;
	private int groupSize;
	private int child[][] = new int[2][4];
	private String[] groupTitle = { "今天", "一周以内", "一个月以内", "很久以前" };
	private RelativeLayout mFinishNetwork;
	private ImageView mNotInfo;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_student_need_list);
		setTitle(R.string.text_need_info);
		setIcon(R.drawable.main_head_bar_icon_add_selector);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mNotInfo = (ImageView) findViewById(R.id.tv_not_info);
		mNotInfo.setVisibility(View.VISIBLE);
		mListView = (OwnNeedExpandableListView) findViewById(R.id.student_need_list);
		mListView.initSlideMode(OwnNeedExpandableListView.MOD_RIGHT);
		mListView.setGroupIndicator(null);
		mListView.setChildDivider(getResources().getDrawable(R.drawable.line));
		mAdapter = new StudentNeedAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setVisibility(View.GONE);
		for (int i = 0; i < mAdapter.getGroupCount(); i++) {
			mListView.expandGroup(i);
		}
		getStudentNeedList();
	}

	private void getStudentNeedList() {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}
		showLoadingDialog("请稍后...");
		String urlString = UrlEngine.studentNeedList(mApplication.getSpUtil()
				.getUserId());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					showLongToast(R.string.text_link_server_error);
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
						groupSize = 0;
						child = new int[2][4];
						time = -1;
						if (response.length() != 0) {
							for (int i = 0; i < response.length(); i++) {
								DxNeed dxNeed = new DxNeed();
								dxNeed.initWithAttributes((JSONObject) response
										.get(i));
								dxNeeds.add(dxNeed);
								getOffectDay(((JSONObject) response.get(i))
										.getString("publishTime"));
							}
							mAdapter.notifyDataSetChanged();
							for (int i = 0; i < mAdapter.getGroupCount(); i++) {
								mListView.expandGroup(i);
							}
							mListView.setVisibility(View.VISIBLE);
							mNotInfo.setVisibility(View.GONE);
						} else {
							mNotInfo.setVisibility(View.VISIBLE);
						}
						dismissLoadingDialog();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public void getOffectDay(String str) {
		int day = DateUtil.getOffectDayInt(str);
		if (day == 0) {
			if (day > time) {
				groupSize++;
				child[1][groupSize - 1] = 0;
			}
			time = 0;
			child[0][groupSize - 1] = child[0][groupSize - 1] + 1;
		} else if (day > 0 && day < 6) {
			if (day > time) {
				groupSize++;
				child[1][groupSize - 1] = 1;
			}
			time = 6;

			child[0][groupSize - 1] = child[0][groupSize - 1] + 1;
		} else if (day > 6 && day < 29) {
			if (day > time) {
				groupSize++;
				child[1][groupSize - 1] = 2;
			}
			time = 29;
			child[0][groupSize - 1] = child[0][groupSize - 1] + 1;
		} else {
			if (day > time) {
				groupSize++;
				child[1][groupSize - 1] = 3;
			}
			time = Integer.MAX_VALUE;
			child[0][groupSize - 1] = child[0][groupSize - 1] + 1;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		DxNeed dxNeed = dxNeeds.get(time(groupPosition, childPosition));
		Intent intent = new Intent(this, StudentNeedCreateActivity.class);
		intent.putExtra("dxNeed", dxNeed);
		intent.putExtra("type", "update");
		startActivityForResult(intent, Constants.STUDENT_NEED_REQUESTCODE);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.STUDENT_NEED_REQUESTCODE:
				dxNeeds.clear();
				getStudentNeedList();
				break;
			}
		}
	}

	@Override
	public void initEvents() {
		mListView.setOnGroupClickListener(this);
		mListView.setOnChildClickListener(this);
		mFinishNetwork.setOnClickListener(this);
	}

	private class StudentNeedAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return groupSize;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return child[0][groupPosition];
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupTitle[child[1][groupPosition]];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return dxNeeds.get(time(groupPosition, childPosition));
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new GroupViewHolder();
				convertView = LayoutInflater.from(StudentNeedListActivity.this)
						.inflate(R.layout.item_student_need_header, null);
				viewHolder.mPublishTime = (TextView) convertView
						.findViewById(R.id.need_publishTime);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (GroupViewHolder) convertView.getTag();
			}
			if (getChildrenCount(groupPosition) == 0) {

			} else {
				viewHolder.mPublishTime
						.setText(groupTitle[child[1][groupPosition]]);
			}
			return convertView;
		}

		@SuppressLint("NewApi")
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final ChildViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(StudentNeedListActivity.this)
						.inflate(R.layout.item_student_need, null);
				viewHolder = new ChildViewHolder();
				viewHolder.mRoundImageView = (RoundHeadImageView) convertView
						.findViewById(R.id.imv_collect_child);
				viewHolder.mNeedTitle = (TextView) convertView
						.findViewById(R.id.tv_child_title);
				viewHolder.mRightLayout = (LinearLayout) convertView
						.findViewById(R.id.llayout_right);
				viewHolder.mSubjectItemName = (TextView) convertView
						.findViewById(R.id.tv_subject);
				viewHolder.mTeachTimes = (TextView) convertView
						.findViewById(R.id.tv_lecture_count);
				viewHolder.mPrice = (TextView) convertView
						.findViewById(R.id.tv_class_fees);
				viewHolder.mClassFeesTitle = (TextView) convertView
						.findViewById(R.id.tv_class_fees_title);
				viewHolder.mClose = (ImageView) convertView
						.findViewById(R.id.img_close);
				viewHolder.mLayout = (LinearLayout) convertView
						.findViewById(R.id.linearLayout1);
				viewHolder.mReleaseRight = (RelativeLayout) convertView
						.findViewById(R.id.release_right_layout);
				viewHolder.mShieldRight = (RelativeLayout) convertView
						.findViewById(R.id.shield_right_layout);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ChildViewHolder) convertView.getTag();
			}
			final DxNeed target = dxNeeds
					.get(time(groupPosition, childPosition));
			String avatar = EtutorApplication.getInstance().getSpUtil()
					.getUserAvatar();
			if (TextUtils.isEmpty(avatar)) {
				viewHolder.mRoundImageView.setImageResource(-1);
			} else {
				ImageLoader.getInstance().displayImage(avatar,
						viewHolder.mRoundImageView,
						ImageLoadOptionsUtil.getOptions());
			}

			double price = target.getPrice();
			if (price == 0) {
				viewHolder.mPrice.setText("面议");
			} else {
				viewHolder.mPrice.setText(price + "元/小时");
			}
			if (TextUtils.isEmpty(target.getNeedTitle())) {
				viewHolder.mNeedTitle.setText("找家教");
			} else {
				viewHolder.mNeedTitle.setText(target.getNeedTitle());
			}
			viewHolder.mSubjectItemName.setText(target.getSubjectItemId()
					.toString());
			if (target.getTradeNumber() == null || target.getTradeNumber() == 0) {
				viewHolder.mTeachTimes.setText("面议");
			} else {
				viewHolder.mTeachTimes.setText(target.getTradeNumber() + "次/周");
			}
			viewHolder.mReleaseRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					releaseAgain(target.getNeedId().toString());
					viewHolder.mClose.setImageResource(-1);
					mListView.slideBack();
				}
			});
			viewHolder.mShieldRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					shield(target.getNeedId().toString());
					viewHolder.mClose.setImageResource(R.drawable.need_shield);
					mListView.slideBack();
				}
			});
			if (target.getStatus().equals("-1")) {
				viewHolder.mClose.setImageResource(R.drawable.need_shield);
				viewHolder.mSubjectItemName.setTextColor(getResources()
						.getColor(R.color.text_need_item_title));
				viewHolder.mNeedTitle.setTextColor(getResources().getColor(
						R.color.text_need_item_title));
				viewHolder.mTeachTimes.setTextColor(getResources().getColor(
						R.color.text_need_item_title));
				viewHolder.mPrice.setTextColor(getResources().getColor(
						R.color.text_login_color));
				viewHolder.mClassFeesTitle.setTextColor(getResources()
						.getColor(R.color.text_login_color));
				viewHolder.mLayout.setBackground(getResources().getDrawable(
						R.drawable.shape_student_shield_price_bg));
				viewHolder.mRoundImageView.setAlpha(0.5f);
			} else {
				viewHolder.mSubjectItemName.setTextColor(getResources()
						.getColor(R.color.text_subject_name));
				viewHolder.mNeedTitle.setTextColor(getResources().getColor(
						R.color.text_need_title));
				viewHolder.mTeachTimes.setTextColor(getResources().getColor(
						R.color.text_need_title));
				viewHolder.mClassFeesTitle.setTextColor(getResources()
						.getColor(R.color.text_blue));
				viewHolder.mPrice.setTextColor(getResources().getColor(
						R.color.text_blue));
				viewHolder.mLayout.setBackground(getResources().getDrawable(
						R.drawable.shape_student_price_bg));
				viewHolder.mClose.setImageResource(-1);
				viewHolder.mRoundImageView.setAlpha(1f);
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	public int time(int groupPosition, int childPosition) {
		int temp = 0;
		for (int i = 0; i < groupPosition; i++) {
			temp += child[0][i];
		}
		temp += childPosition;
		return temp;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_finish_network:
			getStudentNeedList();
			break;

		default:
			break;
		}
	}

	class GroupViewHolder {
		private TextView mPublishTime;
	}

	class ChildViewHolder {
		private RoundHeadImageView mRoundImageView;
		private TextView mSubjectItemName;
		private TextView mNeedTitle;
		private TextView mTeachTimes;
		private TextView mPrice;
		private ImageView mClose;
		private TextView mClassFeesTitle;

		// private RelativeLayout mReleaseLeft;
		// private RelativeLayout mShieldLeft;
		private LinearLayout mLayout;
		private LinearLayout mRightLayout;
		private RelativeLayout mReleaseRight;
		private RelativeLayout mShieldRight;

	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return true;
	}

	@Override
	public void iconClick() {
		Intent intent = new Intent(this, StudentNeedCreateActivity.class);
		intent.putExtra("type", "create");
		startActivityForResultByPendingTransition(intent,
				Constants.STUDENT_NEED_REQUESTCODE);
		super.iconClick();
	}

	/**
	 * 屏蔽
	 */
	public void shield(String needId) {
		String urlString = UrlEngine.shieldNeed(needId, EtutorApplication
				.getInstance().getSpUtil().getUserId());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dxNeeds.clear();
					getStudentNeedList();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				if (statusCode == 0) {
					showShortToast(R.string.text_link_server_error);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	/**
	 * 重新发布
	 * 
	 * @param needId
	 */
	public void releaseAgain(String needId) {
		String urlString = UrlEngine.releaseAgain(needId, EtutorApplication
				.getInstance().getSpUtil().getUserId());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
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
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dxNeeds.clear();
					getStudentNeedList();
				}
			}
		});
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
}
