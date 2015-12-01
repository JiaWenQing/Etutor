package net.dx.etutor.activity.order;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.order.alipay.PayResult;
import net.dx.etutor.activity.order.alipay.SignUtils;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.Md5Util;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.Util;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class OrderCreateActivity extends BaseActivity implements
		OnClickListener {
	public static String TAG = "OrderCreateActivity";
	private DxNeed mDxNeed = new DxNeed();
	private TextView mSubject;
	private TextView mFirstLectureTime;
	private Button mSubmit;
	private long minTime;
	// 商户PID
	public static final String PARTNER = "2088021129496154";
	// 商户收款账号
	public static final String SELLER = "2088021129496154";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKNAvO5w2a8Y0KoO    1+m/YLgOSv0V/LwYNvxc7gf/s3GbzzexgmldS9qMcUmtYjws8GwGgvxQkn4Yrevm    c/CEOLEG8ZffkXaxZmRiJirmIsCLAoCotxKoISeezLvTWJzKGGSJ0BGoFBcQGP9W    3W1d4K4EAl+GgR6Tjj6Qj1yp4pizAgMBAAECgYBqMQQ0fce7tAKXfAxKiihZ0UJK    GnFJ0N6Djddv8I5DCjziV18jNoLO6KgQau4JgpnpStsNUUPZLiiD2GX2P6u3wUGl    VmuDnFiuS7EvFNey5yWJDH8W9857UtFTvmJSU2qjEzPae49UvPZbEMcUroMq4eb0    6rBSLZNC55Rh5krrIQJBANUjaxahmHLbNShg58ihFnCKnUeNgY1thhs4ZNZehxcu    TB4fddRx0ian4tWa40lyxcA1rRfmGgyKBXKJEsvlmbkCQQDEFSoVCpEgeugZKmiQ    M0x0LsrOZTmtALHBkqSZwl1XjuLU4fkXIguSphCPUrh0P6KZOt/w+FtQioH0b+X/    TMvLAkBIq4fSsoww9Q6d0tSUcmAdRRW1FMlMOECWPVccRCSZQb7QMnfRVHGLnohX    vjSqkUhdba+zy0+sYM7Uq2nOPV15AkAxOS7rQU/VW4VQa/j72iTjNavrzDWCXJ5C    /dEcWFKeXFxNHL0vNXi6Q738fMOZSzBCTylQkMg7m8mr1zthr6GpAkApERLMBiaB    3fciqtn6K3ryUeoHj6dsUeGFVrZ4wlkrEOoK2r6i9PT3rYavDsVwg10aTSJJfTMU    V69UiBpHvsc/";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	// private static final int SDK_CHECK_FLAG = 2;
	/**
	 * 预约接受者
	 */
	private TextView mTVReceiveName;
	/**
	 * 预约创建者
	 */
	private TextView mTVCreateName;

	private LinearLayout mLayoutPayWay;
	private EditText mClassCount;// 课时数
	private TextView mClassPay;// 课时费
	private TextView mPayTotal;// 总费用

	private RadioButton mWeiXin;
	private RadioButton mAlipay;
	/**
	 * 1 专职教师 0 非专职教师
	 */
	private int fullTime;
	/**
	 * 课时数
	 */
	private String courseNumber;
	/**
	 * 课时费
	 */
	private double coursePrice;
	/**
	 * 总价
	 */
	private double totalPrice;
	/**
	 * 商家商品号 微信以W 开头， 支付宝以Z开头
	 */
	private String accountNumber;

	private String firstCourseTime;

	private Map<String, Object> map;
	public String prepayId;

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_create_order);
		setTitle(R.string.title_order_detail);
		req = new PayReq();
		msgApi.registerApp(Constants.APP_ID_WEIXIN);
		mSubmit = (Button) findViewById(R.id.btn_submit_order_detail);
		fullTime = getIntent().getIntExtra("fullTime", 0);
		mDxNeed = (DxNeed) getIntent().getSerializableExtra("dxNeed");
		mTVReceiveName = (TextView) findViewById(R.id.tv_receive_name);
		mTVCreateName = (TextView) findViewById(R.id.tv_create_name);
		mFirstLectureTime = (TextView) findViewById(R.id.tv_first_lecture_time);
		mLayoutPayWay = (LinearLayout) findViewById(R.id.layout_pay_way);
		mClassCount = (EditText) findViewById(R.id.et_class_hours);
		mClassPay = (TextView) findViewById(R.id.tv_class_fees);
		mPayTotal = (TextView) findViewById(R.id.tv_total_fees);
		mWeiXin = (RadioButton) findViewById(R.id.rb_pay_wx);
		mAlipay = (RadioButton) findViewById(R.id.rb_pay_alipay);
		initData();
	}

	@Override
	public void initEvents() {
		mSubmit.setOnClickListener(this);
		mFirstLectureTime.setOnClickListener(this);
		mClassCount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					int hours = Integer.parseInt(s.toString());
					mPayTotal.setText(new DecimalFormat("#0.00").format(hours
							* coursePrice));
				} else {
					mPayTotal.setText("0.00");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initData() {
		if (fullTime != 0) {
			mLayoutPayWay.setVisibility(View.VISIBLE);
		} else {
			mLayoutPayWay.setVisibility(View.GONE);
		}
		coursePrice = mDxNeed.getPrice();
		mClassPay.setText(coursePrice + "");
		mPayTotal.setText("0.00");
		minTime = System.currentTimeMillis();
		mTVReceiveName.setText(mDxNeed.getDxUser().getUserName());
		mTVCreateName.setText(EtutorApplication.getInstance().getSpUtil()
				.getUserName());
		mFirstLectureTime.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit_order_detail:
			checkedData();
			break;
		case R.id.tv_first_lecture_time:
			DatePickerDialog cDialog = new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							String date = DateUtil.getBirthday(year,
									monthOfYear, dayOfMonth);
							mFirstLectureTime.setText(date);
						}
					}, 0, 0, 0);
			DatePicker datePicker = cDialog.getDatePicker();
			datePicker.setMinDate(minTime);
			cDialog.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 检查提交数据
	 */
	private void checkedData() {
		// TODO Auto-generated method stub
		firstCourseTime = mFirstLectureTime.getText().toString();
		if (TextUtils.isEmpty(firstCourseTime)) {
			showShortToast("请选择授课时间");
			return;
		}
		courseNumber = mClassCount.getText().toString();
		if (TextUtils.isEmpty(courseNumber)) {
			showShortToast("请填写课时数");
			return;
		}

		if (fullTime == 1) {
			totalPrice = Double.parseDouble(mPayTotal.getText().toString());
			String time = DateUtil.getStringByFormat(
					System.currentTimeMillis(), DateUtil.dateFormatYMDHMSS);
			if (mWeiXin.isChecked()) {
				accountNumber = "W" + time;
				onWeixin();
			} else {
				accountNumber = "Z" + time;
				onAlipay();
			}
		} else {
			submit();
		}
	}

	/**
	 * 支付宝支付
	 */
	private void onAlipay() {
		// 订单
		String orderInfo = getOrderInfo("预约老师", "该测试商品的详细描述", totalPrice + "");// total

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(OrderCreateActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 微信支付
	 */
	private void onWeixin() {
		// TODO Auto-generated method stub
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(OrderCreateActivity.this, "提示",
					"正在获取预支付订单...");
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			System.out
					.println("prepay_id\n" + result.get("prepay_id") + "\n\n");

			req.appId = Constants.APP_ID_WEIXIN;
			req.partnerId = Constants.MCH_ID_WEIXIN;
			req.prepayId = result.get("prepay_id");
			req.packageValue = "Sign=WXPay";
			req.nonceStr = genNonceStr();
			req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", req.appId));
			signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
			signParams.add(new BasicNameValuePair("package", req.packageValue));
			signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
			signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
			signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
			req.sign = genPackageSign(signParams);

			msgApi.registerApp(Constants.APP_ID_WEIXIN);
			msgApi.sendReq(req);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();
			System.out.println(entity);
			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			System.out.println("content==" + content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	String content;

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			System.out.println("e.toString()==" + e.toString());
		}
		return null;

	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			String total_fee = (int) (totalPrice * 100) + "";
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid",
					Constants.APP_ID_WEIXIN));
			packageParams.add(new BasicNameValuePair("body", "fees"));
			packageParams.add(new BasicNameValuePair("mch_id",
					Constants.MCH_ID_WEIXIN));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://121.40.35.3/test"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					accountNumber));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", total_fee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			System.out.println("e.getMessage()==" + e.getMessage());
			return null;
		}

	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		System.out.println("sb.toString()==" + sb.toString());
		return sb.toString();
	}

	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY_WEIXIN);

		String packageSign = Md5Util.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return packageSign;
	}

	private String genNonceStr() {
		Random random = new Random();
		return Md5Util.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	public void submit() {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		double a = Double.parseDouble(mClassCount.getText().toString().trim());
		double b = Double.parseDouble(mClassPay.getText().toString().trim());
		map = new HashMap<String, Object>();
		map.put("orderId", DateUtil.getOrderId());
		map.put("fromUserId", mApplication.getSpUtil().getUserId());
		map.put("toUserId", mDxNeed.getDxUser().getUserId());
		map.put("firstCourseTime", firstCourseTime);
		map.put("needId", mDxNeed.getNeedId());
		map.put("courseNumber", mClassCount.getText().toString().trim());
		map.put("coursePrice", mClassPay.getText().toString().trim());
		map.put("totalPrice", totalPrice);// hours * coursePrice
		map.put("accountNumber", accountNumber);
		map.put("fullTime", fullTime);
		showLoadingDialog("请稍后...");
		String urlString = UrlEngine.insertOrder(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dismissLoadingDialog();
					showLongToast("预约成功!");
					OrderCreateActivity.this.finish();
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
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
		if (fullTime == 1 && mWeiXin.isChecked() && mApplication.isWeiXinPay()) {
			mApplication.setWeiXinPay(false);
			submit();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// Toast.makeText(OrderCreateActivity.this, "支付成功",
					// Toast.LENGTH_SHORT).show();
					submit();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderCreateActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(OrderCreateActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"// 改成服务器通知的Url(还没改)
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		String key = "Z"
				+ DateUtil.getStringByFormat(new Date(),
						DateUtil.dateFormatYMDHMSS);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
