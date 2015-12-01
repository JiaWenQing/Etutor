package net.dx.etutor.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.fragment.order.ReceivedOrderFragment;
import net.dx.etutor.activity.fragment.order.SendedOrderFragment;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxOrders;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class OrderListFragment extends BaseFragment implements
		OnPageChangeListener, OnClickListener {

	public static String TAG = "OrderListFragment";
	private static List<Fragment> list = new ArrayList<Fragment>();
	public List<DxOrders> mLists = new ArrayList<DxOrders>();
	private ImageView mIssueOrder;
	private ImageView mReceiveOrder;
	private TextView mTitle;
	private TextView mNotLogin;
	private LinearLayout mNotLoginLayout;
	private MyAdapter adapter;
	private ViewPager mViewPager;
	private SendedOrderFragment issueFragment;
	private ReceivedOrderFragment receiveFragment;
	private String userId;
	private RelativeLayout mPager;
	private LinearLayout mFinishNetwork;
	EtutorApplication application;
	private int pos = 0;
	private int loginStatu;
	private int i=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_main_orderlist, null);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public OrderListFragment() {
		super();
	}

	public OrderListFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
		this.application = application;
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		if (i == 0) {
			setTitle(R.string.menu_create_order);
			i++;
		}else {
			if (pos==0) {
				setTitle(R.string.title_issue_order);
			}else {
				setTitle(R.string.title_receive_order);
			}
		}
		settingIcon(0, true, "");
		mFinishNetwork = (LinearLayout) findViewById(R.id.layout_finish_network);
		mNotLoginLayout = (LinearLayout) findViewById(R.id.layout_login);
		mNotLogin = (TextView) findViewById(R.id.tv_login);
		mPager = (RelativeLayout) findViewById(R.id.layout_pager);
		mTitle = (TextView) findViewById(R.id.main_head_bar_title);
		mReceiveOrder = (ImageView) findViewById(R.id.imv_order_receive);
		mIssueOrder = (ImageView) findViewById(R.id.imv_order_issue);
		mViewPager = (ViewPager) findViewById(R.id.view_order_pager);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mFinishNetwork.setOnClickListener(this);
		mNotLogin.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		list.clear();
		if (null == issueFragment) {
			issueFragment = new SendedOrderFragment(mApplication,
					getActivity(), getActivity());
		}
		if (null == receiveFragment) {
			receiveFragment = new ReceivedOrderFragment(mApplication,
					getActivity(), getActivity());
		}
		list.add(issueFragment);
		list.add(receiveFragment);

		if (null == adapter) {
			adapter = new MyAdapter(getChildFragmentManager());
		}
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(pos);
		mViewPager.setOffscreenPageLimit(0);
		
		
		boolean isFirst = mApplication.getSpUtil().getFirstEnterOrderFlag();
		if (loginStatu != 0) {
			if (mApplication.getSpUtil().getNewOrderFlag()) {
				mApplication.getSpUtil().isNewOrderFlag(false);
				getOrdersList();
			} else {
				if (isFirst) {
					mApplication.getSpUtil().isFirstEnterOrder(false);
					getOrdersList();
				}
			}
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mIssueOrder.setImageResource(R.drawable.order_page_nomal);
		mReceiveOrder.setImageResource(R.drawable.order_page_nomal);
		switch (arg0 % 2) {
		case 0:
			pos = 0;
			mTitle.setText(R.string.title_issue_order);
			mReceiveOrder.setImageResource(R.drawable.order_page_focused);
			break;
		case 1:
			pos = 1;
			mTitle.setText(R.string.title_receive_order);
			mIssueOrder.setImageResource(R.drawable.order_page_focused);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_finish_network:
			if (NetWorkHelperUtil.checkNetState(getActivity())) {
				mFinishNetwork.setVisibility(View.GONE);
			}
			if (loginStatu != 0) {
				getOrdersList();
			}
			break;
		case R.id.tv_login:
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 3000);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取数据
	 */
	private void getOrdersList() {
		if (!NetWorkHelperUtil.checkNetState(getActivity())) {
			Toast.makeText(getActivity(), R.string.network_error, 0).show();
			mFinishNetwork.setVisibility(View.VISIBLE);
			mPager.setVisibility(View.GONE);
			return;
		} else {
			mPager.setVisibility(View.VISIBLE);
			mFinishNetwork.setVisibility(View.GONE);
		}
		showLoadingDialog("请稍后……");
		userId = mApplication.getSpUtil().getUserId();
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
							0).show();
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
							initData();
							issueFragment.setSenderList(mLists);
							receiveFragment.setReceiveList(mLists);
						}
						dismissLoadingDialog();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 100:
				receiveFragment.onActivityResult(requestCode, resultCode, data);
				break;
			case 101:
				issueFragment.onActivityResult(requestCode, resultCode, data);
				break;
			case 3000:
				if (pos == 0) {
					mTitle.setText(R.string.title_issue_order);
				} else {
					mTitle.setText(R.string.title_receive_order);
				}
				getOrdersList();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu == 0) {
			mNotLoginLayout.setVisibility(View.VISIBLE);
			mPager.setVisibility(View.GONE);
		} else {
			mNotLoginLayout.setVisibility(View.GONE);
			mPager.setVisibility(View.VISIBLE);
		}
		if (pos == 0) {
			mTitle.setText(R.string.title_issue_order);
		} else {
			mTitle.setText(R.string.title_receive_order);
		}
		MobclickAgent.onPageStart(TAG); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		list.clear();
	}

}
