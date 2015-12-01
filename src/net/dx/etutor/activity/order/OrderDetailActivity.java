package net.dx.etutor.activity.order;

import net.dx.etutor.R;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.comment.CommentWriteActivity;
import net.dx.etutor.activity.message.PrivateMessageDetailActivity;
import net.dx.etutor.activity.student.StudentIntroActivity;
import net.dx.etutor.activity.teacher.TeacherIntroActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.LogoutDialog;
import net.dx.etutor.dialog.LogoutDialog.OnLogoutClickListener;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxOrder;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class OrderDetailActivity extends BaseActivity implements
		OnClickListener, OnLogoutClickListener {

	public static String TAG = "OrderDetailActivity";
	/**
	 * 预约接受者
	 */
	private TextView mTVReceiveName;
	/**
	 * 预约创建者
	 */
	private TextView mTVCreateName;
	/**
	 * 首次预约时间
	 */
	private TextView mFirstCourseTime;
	/**
	 * 预约号
	 */
	private TextView mOrderNumber;
	/**
	 * 预约状态
	 */
	private TextView mOrderStatu;
	/**
	 * 预约快照
	 */
	private TextView mOrderInstantane;

	private LinearLayout mConfirmLayout;
	private LinearLayout mSuccessLayout;
	private LinearLayout mCommentLayout;
	private Button mAgree;
	private Button mClose;
	private Button mComment;
	private Button mSuccess;

	private DxOrder dxOrder;
	/**
	 * 预约状态 0待确认 1已确认 2待评价 3已评价 4已拒绝
	 */
	private String status;
	/**
	 * 1.发送 0.接收
	 */
	private String type = "1";
	private String userName;
	private String userId;
	private int loginStatu;
	private int userType;
	private String toUserId;
	private String name;
	private String needId;
	private String orderId;
	private int[] orderStatus = { R.string.text_order_status_1,
			R.string.text_order_status_2, R.string.text_order_status_3,
			R.string.text_order_status_4, R.string.text_order_status_5 };
	private LogoutDialog dialog;
	private String submitStatus;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_order_detail);
		setTitle(R.string.title_order_detail);
		userName = mApplication.getSpUtil().getUserName();
		userId = mApplication.getSpUtil().getUserId();
		userType = mApplication.getSpUtil().getUserType();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		dxOrder = (DxOrder) getIntent().getSerializableExtra("dxOrder");
		mTVReceiveName = (TextView) findViewById(R.id.tv_receive_name);
		mTVCreateName = (TextView) findViewById(R.id.tv_create_name);
		mFirstCourseTime = (TextView) findViewById(R.id.tv_first_course_time);
		mOrderNumber = (TextView) findViewById(R.id.tv_order_number);
		mOrderInstantane = (TextView) findViewById(R.id.tv_order_instantane);
		mOrderStatu = (TextView) findViewById(R.id.tv_order_statu);

		mComment = (Button) findViewById(R.id.btn_comment_order);
		mSuccess = (Button) findViewById(R.id.btn_success_order);
		mAgree = (Button) findViewById(R.id.btn_agree_order);
		mClose = (Button) findViewById(R.id.btn_close_order);

		mConfirmLayout = (LinearLayout) findViewById(R.id.layout_order_status_1);
		mSuccessLayout = (LinearLayout) findViewById(R.id.layout_order_status_2);
		mCommentLayout = (LinearLayout) findViewById(R.id.layout_order_status_3);
		checkData();

	}

	private void checkData() {
		// TODO Auto-generated method stub
		type = dxOrder.getType();
		orderId = dxOrder.getOrderId();
		if (loginStatu == 3) {
			getDetailOrder();
		} else {
			needId = dxOrder.getNeedId();
			toUserId = dxOrder.getUser().getUserId().toString();
			status = dxOrder.getStatus().toString();
			initData();
		}
	}

	private void initData() {
		// TODO Auto-generated method stub

		switch (type) {
		case "0":
			switch (status) {
			case "0":
				mConfirmLayout.setVisibility(View.VISIBLE);
				mCommentLayout.setVisibility(View.GONE);
				mSuccessLayout.setVisibility(View.GONE);
				break;
			case "1":
				// mSuccessLayout.setVisibility(View.VISIBLE);
				mCommentLayout.setVisibility(View.GONE);
				mConfirmLayout.setVisibility(View.GONE);
				break;
			case "2":
				mCommentLayout.setVisibility(View.VISIBLE);
				mConfirmLayout.setVisibility(View.GONE);
				mSuccessLayout.setVisibility(View.GONE);
				break;
			default:
				mSuccessLayout.setVisibility(View.GONE);
				mConfirmLayout.setVisibility(View.GONE);
				mCommentLayout.setVisibility(View.GONE);
				break;
			}
			break;
		case "1":
			switch (status) {
			case "0":
				mConfirmLayout.setVisibility(View.GONE);
				mCommentLayout.setVisibility(View.GONE);
				mSuccessLayout.setVisibility(View.GONE);
				break;
			case "1":
				// mSuccessLayout.setVisibility(View.VISIBLE);
				mConfirmLayout.setVisibility(View.GONE);
				mCommentLayout.setVisibility(View.GONE);
				break;
			case "2":
				mCommentLayout.setVisibility(View.VISIBLE);
				mConfirmLayout.setVisibility(View.GONE);
				mSuccessLayout.setVisibility(View.GONE);
				break;
			default:
				mSuccessLayout.setVisibility(View.GONE);
				mConfirmLayout.setVisibility(View.GONE);
				mCommentLayout.setVisibility(View.GONE);
				break;
			}
			break;

		default:
			break;
		}

		switch (Integer.parseInt(type)) {
		// 发送
		case 1:
			mTVReceiveName.setText(dxOrder.getUser().getUserName());// 订单接收人
			mTVCreateName.setText(userName);
			mTVReceiveName.setTextColor(Color.BLUE);
			mTVCreateName.setTextColor(Color.BLACK);
			mTVReceiveName.setClickable(true);
			mTVCreateName.setClickable(false);
			break;
		// 接收
		case 0:
			mTVReceiveName.setText(userName);
			mTVCreateName.setText(dxOrder.getUser().getUserName());
			mTVCreateName.setTextColor(Color.BLUE);
			mTVReceiveName.setTextColor(Color.BLACK);
			mTVReceiveName.setClickable(false);
			mTVCreateName.setClickable(true);
			break;
		default:
			break;
		}
		mOrderInstantane.setOnClickListener(this);
		if (!toUserId.equals("-1")) {
			if (!TextUtils.isEmpty(type) && type.equals("1")) {
				mTVReceiveName.setOnClickListener(this);
			}
			if (!TextUtils.isEmpty(type) && type.equals("0")) {
				mTVCreateName.setOnClickListener(this);
			}
		} else {
			mTVReceiveName.setTextColor(Color.BLACK);
			mTVCreateName.setTextColor(Color.BLACK);
		}
		mOrderNumber.setText(orderId);
		mFirstCourseTime.setText(dxOrder.getFirstCourseTime());
		mOrderStatu.setText(orderStatus[Integer.parseInt(status)]);
	}

	/**
	 * 通过通知进入时 访问服务器 获取订单详情
	 */
	private void getDetailOrder() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍后…");
		String url = UrlEngine.getDetailOrder(orderId, type);
		HttpUtil.post(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dxOrder.initWithAttributes(response);
					needId = dxOrder.getNeedId();
					toUserId = dxOrder.getUser().getUserId().toString();
					status = dxOrder.getStatus().toString();
					initData();
					dismissLoadingDialog();
				}
			}

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
		});
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSuccess.setOnClickListener(this);
		mComment.setOnClickListener(this);
		mAgree.setOnClickListener(this);
		mClose.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (loginStatu == 3) {
			Intent intent = new Intent(OrderDetailActivity.this,
					MainFragmentActivity.class);
			startActivityByPendingTransition(intent);
			finish();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void defaultFinish() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_order_instantane:
			String Type = dxOrder.getUser().getUserType();

			if (type.equals("0")) {
				Type = userType + "";
			}
			if (Type.equals("0")) {
				getNeedStudnetInfo(Type);
			} else {
				getTeacherInfo(userId, toUserId, Type);
			}
			break;
		case R.id.btn_comment_order:
			intent = new Intent(this, CommentWriteActivity.class);
			intent.putExtra("dxOrder", dxOrder);
			startActivityByPendingTransition(intent);
			finish();
			break;
		case R.id.btn_success_order:
			submitStatus = "2";
			showLoadingDialog("");
			dialog = new LogoutDialog(this);
			dialog.setOnLogoutClickListener(this, "确定预约已完成？", "");
			dialog.show();
			break;
		case R.id.btn_agree_order:
			submitStatus = "1";
			showLoadingDialog("");
			dialog = new LogoutDialog(this);
			dialog.setOnLogoutClickListener(this, "确定要同意吗？", "");
			dialog.show();
			break;
		case R.id.btn_close_order:
			submitStatus = "4";
			showLoadingDialog("");
			dialog = new LogoutDialog(this);
			dialog.setOnLogoutClickListener(this, "确定要拒绝吗？", "");
			dialog.show();
			break;
		case R.id.tv_receive_name:
			if (!type.equals("0")) {
				String type1 = dxOrder.getUser().getUserType();
				switch (type1) {
				case "0":
					getNeedStudnetInfo(null);
					break;
				case "1":
					getTeacherInfo(userId, toUserId, null);
					break;
				default:
					break;
				}
			}
			break;
		case R.id.tv_create_name:
			sendMessageToUser();
			break;

		default:
			break;
		}
	}

	private DxTeacherList dxTeacher;

	/**
	 * 查询老师详情
	 */
	private void getTeacherInfo(String userId, final String teacherId,
			final String types) {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍后...");
		String urlString;
		if (type.equals("0")) {
			urlString = UrlEngine.getTeacherInfo(teacherId, userId);
		} else {
			urlString = UrlEngine.getTeacherInfo(userId, teacherId);
		}
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.length() != 0) {
							dxTeacher = new DxTeacherList();
							dxTeacher.initWithAttributes(response
									.getJSONObject(0));
							getTeacherDetail(teacherId, types);
						} else {
							sendMessageToUser();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

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
		});

	}

	/**
	 * 查询老师详情
	 */
	protected void getTeacherDetail(String teacherId, final String type) {
		// TODO Auto-generated method stub
		String url;
		if (null != type) {
			url = UrlEngine.getInstantaneInfo(orderId);
		} else {
			url = UrlEngine.getTeacherDetail(teacherId);
		}
		HttpUtil.post(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					if (response.toString().equals(Constants.BINDING_FAIL)) {
						dismissLoadingDialog();
						showShortToast("暂无快照!");
					} else {
						try {
							DxTeacherinfo dxTeacherinfo = new DxTeacherinfo();
							dxTeacherinfo.initWithAttributes(response);
							Intent intent;
							intent = new Intent(OrderDetailActivity.this,
									TeacherIntroActivity.class);
							if (null != type) {
								intent.putExtra("type", "instantane");
							}
							intent.putExtra("dxTeacherinfo", dxTeacherinfo);
							intent.putExtra("dxTeacher", dxTeacher);
							startActivityByPendingTransition(intent);
							dismissLoadingDialog();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

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
		});
	}

	/**
	 * 查询学生需求详情
	 */
	private void getNeedStudnetInfo(final String type) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String url;
		if (null != type) {
			url = UrlEngine.getInstantaneInfo(orderId);
		} else {
			url = UrlEngine.needStudent(needId, userId);
		}
		showLoadingDialog("请稍后...");
		HttpUtil.post(url, new JsonHttpResponseHandler() {

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
					if (response.length() != 0) {
						if (response.toString().equals(Constants.BINDING_FAIL)) {
							dismissLoadingDialog();
							showShortToast("暂无快照!");
						} else {
							DxNeed dxNeed = new DxNeed();
							Intent intent;
							try {
								dxNeed.initWithAttributes(response
										.getJSONObject(0));
								intent = new Intent(OrderDetailActivity.this,
										StudentIntroActivity.class);
								if (null != type) {
									intent.putExtra("type", "instantane");
								} else {
									intent.putExtra("type", "orderDetail");
								}
								intent.putExtra("dxNeed", dxNeed);
								startActivityByPendingTransition(intent);
								dismissLoadingDialog();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						dismissLoadingDialog();
						sendMessageToUser();
					}
				}
			}

		});

	}

	protected void sendMessageToUser() {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		Intent intent = new Intent(OrderDetailActivity.this,
				PrivateMessageDetailActivity.class);
		intent.putExtra("imagePath", dxOrder.getUser().getAvatarUrl());
		intent.putExtra("toUserId", toUserId);
		intent.putExtra("type", "orderDetail");
		startActivityByPendingTransition(intent);
	}

	private void onSubmit(final String statu) {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍等……");
		String urlString = UrlEngine.updateOrder(orderId, statu);
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
					JSONObject response) {
				// TODO Auto-generated method stub
				if (statusCode == Constants.STAT_200) {
					showShortToast("更改成功！");
					dismissLoadingDialog();
					if (loginStatu == 3) {
						onBackPressed();
					} else {
						Intent intent = new Intent();
						intent.putExtra("status", statu);
						intent.putExtra("orderId", dxOrder.getOrderId());
						setResult(RESULT_OK, intent);
						OrderDetailActivity.this.finish();
					}
				}
			}

		});
	}

	@Override
	public void logoutOnClick(View v) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		switch (v.getId()) {
		case R.id.btn_logout_confirm:
			onSubmit(submitStatus);
			break;
		case R.id.btn_logout_cancle:
			dialog.dismiss();
			break;
		default:
			break;
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

}
