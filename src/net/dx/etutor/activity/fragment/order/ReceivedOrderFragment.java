package net.dx.etutor.activity.fragment.order;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MyOrderAdapter;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.order.OrderDetailActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxOrder;
import net.dx.etutor.model.DxOrders;
import net.dx.etutor.model.DxStautsOrders;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class ReceivedOrderFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	/**
	 * 友盟统计
	 */
	protected boolean isCreated = false;
	public static String TAG = "ReceivedOrderFragment";
	private ListView mOrderList;
	private List<DxOrders> mLists = new ArrayList<DxOrders>();
	private List<DxOrder> mList = new ArrayList<DxOrder>();
	private MyOrderAdapter mAdapter;
	private String userId;
	private TextView mStatu0;
	private TextView mStatu1;
	private TextView mStatu2;
	private TextView mStatu3;
	private TextView mStatu4;
	private String status;
	private String type = "0";
	private TextView mNotInfo;

	public ReceivedOrderFragment() {
		super();
	}

	public ReceivedOrderFragment(EtutorApplication application,
			Activity activity, Context context) {
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
		mView = inflater.inflate(R.layout.fragment_receive_order, container,
				false);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mOrderList = (ListView) findViewById(R.id.lv_order_list);
		mStatu0 = (TextView) findViewById(R.id.tv_order_status_0);
		mStatu1 = (TextView) findViewById(R.id.tv_order_status_1);
		mStatu2 = (TextView) findViewById(R.id.tv_order_status_2);
		mStatu3 = (TextView) findViewById(R.id.tv_order_status_3);
		mStatu4 = (TextView) findViewById(R.id.tv_order_status_4);

		mNotInfo = (TextView) findViewById(R.id.tv_not_info);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mOrderList.setOnItemClickListener(this);
		mStatu0.setOnClickListener(this);
		mStatu1.setOnClickListener(this);
		mStatu2.setOnClickListener(this);
		mStatu3.setOnClickListener(this);
		mStatu4.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		setTitle(R.string.title_receive_order);
		settingIcon(R.drawable.ic_launcher, false, "");
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		mNotInfo.setVisibility(View.VISIBLE);
		mAdapter = new MyOrderAdapter(mList,getActivity());
		mOrderList.setAdapter(mAdapter);
	}

	public void setReceiveList(List<DxOrders> mLists) {
		this.mLists = mLists;
		getDxOrder(type, status);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取数据
	 */
	private void getOrdersList() {
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			Toast.makeText(getActivity(), R.string.network_error,
					Toast.LENGTH_SHORT).show();
			return;
		}
		showLoadingDialog("请稍后……");
		String urlString = UrlEngine.getOrderListUrl(userId);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismissLoadingDialog();
				if (statusCode == 0) {
					Toast.makeText(getActivity(),
							R.string.text_link_server_error, 0).show();
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					Toast.makeText(getActivity(), R.string.text_error_network,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						DxOrders dxOrders;
						if (response.length() != 0) {
							mLists.clear();
							for (int i = 0; i < response.length(); i++) {
								dxOrders = new DxOrders();
								dxOrders.initWithAttributes(response
										.getJSONObject(i));
								mLists.add(dxOrders);
							}
							getDxOrder(type, status);
							mAdapter.notifyDataSetChanged();
						} else {
							mNotInfo.setVisibility(View.VISIBLE);
							mOrderList.setVisibility(View.GONE);
						}

						dismissLoadingDialog();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	/**
	 * 获取orderList
	 * 
	 * @param type
	 * @param status
	 */
	protected void getDxOrder(String type, String status) {
		// TODO Auto-generated method stub
		DxOrders dxOrders;
		mList.clear();
		for (int j = 0; j < mLists.size(); j++) {
			dxOrders = mLists.get(j);
			if (dxOrders.getType().equals(type)) {
				for (int i = 0; i < dxOrders.getOrders().size(); i++) {
					DxStautsOrders dsOrder = dxOrders.getOrders().get(i);
					if (!TextUtils.isEmpty(status)
							&& dsOrder.getStatus().equals(status)) {
						mList.addAll(dsOrder.getList());
					} else if (TextUtils.isEmpty(status)) {
						mList.addAll(dsOrder.getList());
					}
				}
			}
		}
		if (mList.size() == 0) {
			mNotInfo.setVisibility(View.VISIBLE);
			mOrderList.setVisibility(View.GONE);
		} else {
			mNotInfo.setVisibility(View.GONE);
			mOrderList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DxOrder dxOrder = (DxOrder) parent.getAdapter().getItem(position);
		dxOrder.setType(type);
		dxOrder.setPosition(String.valueOf(position));
		Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
		intent.putExtra("dxOrder", dxOrder);
		getParentFragment().startActivityForResult(intent, 100);
	}
 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			getOrdersList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onClick(View v) {
		mStatu0.setTextColor(getResources().getColor(R.color.text_gray));
		mStatu1.setTextColor(getResources().getColor(R.color.text_gray));
		mStatu2.setTextColor(getResources().getColor(R.color.text_gray));
		mStatu3.setTextColor(getResources().getColor(R.color.text_gray));
		mStatu4.setTextColor(getResources().getColor(R.color.text_gray));
		switch (v.getId()) {
		case R.id.tv_order_status_0:
			mStatu0.setTextColor(getResources().getColor(R.color.orange_bg));
			status = "0";
			break;
		case R.id.tv_order_status_1:
			mStatu1.setTextColor(getResources().getColor(R.color.orange_bg));
			status = "1";
			break;
		case R.id.tv_order_status_2:
			mStatu2.setTextColor(getResources().getColor(R.color.orange_bg));
			status = "2";
			break;
		case R.id.tv_order_status_3:
			mStatu3.setTextColor(getResources().getColor(R.color.orange_bg));
			status = "3";
			break;
		case R.id.tv_order_status_4:
			mStatu4.setTextColor(getResources().getColor(R.color.orange_bg));
			status = "4";
			break;
		}
		getDxOrder(type, status);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		boolean b=mApplication.getSpUtil().getCommentFlag();
		if (b) {
			mApplication.getSpUtil().isCommentFlag(false);
			getOrdersList();
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		status=null;
	}
}
