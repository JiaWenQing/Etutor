package net.dx.etutor.activity.forum;

import java.util.ArrayList;
import java.util.HashMap;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.FirstPostsReplyAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ShareChoiceDialog;
import net.dx.etutor.dialog.ShareChoiceDialog.OnShareChoiceClickListener;
import net.dx.etutor.model.DxForumReply;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

/**
 * 帖子详情
 * 
 * @author jwq
 * 
 */
public class PostsDetailActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener<ListView>,
		OnShareChoiceClickListener, Response {

	public static String TAG = "PostsDetailActivity";

	private FirstPostsReplyAdapter mAdapter;
	private PullToRefreshListView mListView;
	private ArrayList<DxForumTopic> mList = new ArrayList<DxForumTopic>();
	private ArrayList<DxForumTopic> mListRefresh = new ArrayList<DxForumTopic>();
	private Button mPostsShare;
	private RadioButton mPostsCollect;
	private RadioButton mPostsPraise;
	private Button mPostsRefresh;

	private DxForumTopic dxForumTopic;

	private String title;
	private String contentShare;

	private HashMap<String, Object> mMap;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;

	private Tencent mTencent;

	private IWeiboShareAPI mWeiboShareAPI;

	private String userId;
	private String postsIds;

	private String collectId;
	private int isBelaud;
	private int topicId;
	private int clicks;
	private int belauds;
	private int count;
	private int replyId;

	private int loginStatu;

	private int status = 1;

	protected int cardFlush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WEIXIN);
		wxApi.registerApp(Constants.APP_ID_WEIXIN);

		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();
		// 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
		// 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
		// 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
		// 失败返回 false，不调用上述回调
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		userId = mApplication.getSpUtil().getUserId();
		mTencent = Tencent.createInstance(Constants.APP_ID_QQ,
				PostsDetailActivity.this);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		dxForumTopic = (DxForumTopic) getIntent().getSerializableExtra(
				"dxForumTopic");
		topicId = dxForumTopic.getId();
		replyId = dxForumTopic.getReplyId();
		clicks = dxForumTopic.getClicks();
		title = dxForumTopic.getTitle();
		collectId = dxForumTopic.getCollectId();
		isBelaud = dxForumTopic.getIsBelaud();
		belauds = dxForumTopic.getBelauds();
		count = dxForumTopic.getCount();
		setContentView(R.layout.activity_posts_detail_list);
		showIcon(0, "回复");
		setTitle(title);
		mPostsShare = (Button) findViewById(R.id.btn_postsdetail_share);
		mPostsCollect = (RadioButton) findViewById(R.id.rb_postsdetail_collect);
		mPostsPraise = (RadioButton) findViewById(R.id.rb_postsdetail_praise);
		mPostsRefresh = (Button) findViewById(R.id.btn_postsdetail_refresh);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_posts_first_reply);
		mAdapter = new FirstPostsReplyAdapter(this, mList);
		mListView.setMode(Mode.BOTH);
		mListView.setAdapter(mAdapter);

		initStatus();
		mMap = new HashMap<String, Object>();
		mMap.put("userId", userId);
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);
		mMap.put("topicId", topicId);
		mMap.put("status", status);
		getPostsDetail(0);
		if (TextUtils.isEmpty(collectId)) {
			mPostsCollect.setChecked(false);
		} else {
			mPostsCollect.setChecked(true);
		}
		mMap.put("status", 0);
	}

	private void initStatus() {
		// TODO Auto-generated method stub
		postsIds = mApplication.getSpUtil().getPostsIds();
		if (!TextUtils.isEmpty(postsIds)) {
			String[] ids = postsIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(topicId + "")) {
					status = 0;
					break;
				}
			}
		}
		clicks += status;
		dxForumTopic.setClicks(clicks);
	}

	@Override
	public void iconClick() {
		Intent intent = new Intent(this, SendPostsActivity.class);
		dxForumTopic.setReplyType(1);
		intent.putExtra("topic", dxForumTopic);
		startActivityForResultByPendingTransition(intent, 102);
	}

	/**
	 * @param type
	 *            0:刷新 1:加载更多
	 */
	private void getPostsDetail(final int type) {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mListView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.getRelpyList(1, mMap);
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
						surplusPage = response.length() % pageSize;
						if (surplusPage != 0 || response.length() == 0) {
							mListView.onRefreshComplete();
							if (type == 1) {
								showShortToast(R.string.text_loading_last);
							}
						}
						mList.clear();
						if (type == 0) {
							mListRefresh.clear();
						}
						for (int i = 0; i < response.length(); i++) {
							DxForumReply dxForumReply = new DxForumReply();
							dxForumReply
									.initWithAttributes((JSONObject) response
											.get(i));
							dxForumReply.setTopicId(topicId + "");
							DxForumTopic topic = new DxForumTopic();
							topic.setId(topicId);
							topic.setDxForumReply(dxForumReply);
							mListRefresh.add(topic);
						}
						mList.addAll(mListRefresh);
						dxForumTopic.setDxForumReply(mList.get(0)
								.getDxForumReply());
						dxForumTopic.setCount(count);
						mList.add(0, dxForumTopic);
						mList.remove(1);
						mAdapter.notifyDataSetChanged();
						/*
						 * setListViewHeightBasedOnChildren(mListView
						 * .getRefreshableView());
						 */
						belauds = mList.get(0).getDxForumReply().getBelauds();
						isBelaud = mList.get(0).getDxForumReply().getIsBelaud();
						if (isBelaud == 1) {
							mPostsPraise.setChecked(true);
						} else {
							mPostsPraise.setChecked(false);
						}
						if (mList.size() == 0) {
							mListView.setVisibility(View.GONE);
						} else {
							mListView.setVisibility(View.VISIBLE);
						}

						if (!TextUtils.isEmpty(postsIds)) {
							String[] ids = postsIds.split(",");
							boolean b = true;
							for (int i = 0; i < ids.length; i++) {
								if (ids[i].equals(topicId + "")) {
									b = false;
									break;
								}
							}
							if (b) {
								postsIds = postsIds + "," + topicId;
							}
						} else {
							postsIds = topicId + "";
						}
						mApplication.getSpUtil().setPostsIds(postsIds);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(this);
		mPostsShare.setOnClickListener(this);
		mPostsCollect.setOnClickListener(this);
		mPostsPraise.setOnClickListener(this);
		mPostsRefresh.setOnClickListener(this);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		int totalHeight = 0;
		FirstPostsReplyAdapter listAdapter1;
		if (listView.getAdapter() instanceof HeaderViewListAdapter) {
			HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView
					.getAdapter();
			listAdapter1 = (FirstPostsReplyAdapter) listAdapter
					.getWrappedAdapter();
		} else {
			listAdapter1 = (FirstPostsReplyAdapter) listView.getAdapter();
		}
		if (listAdapter1 == null) {
			// pre-condition
			return;
		}
		// listAdapter.getCount()返回数据项的数目
		for (int i = 0, len = listAdapter1.getCount(); i < len; i++) {
			View listItem = listAdapter1.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params1 = listView.getLayoutParams();
		params1.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter1.getCount() - 1));
		listView.setLayoutParams(params1);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_postsdetail_share:
			ShareChoiceDialog shareChoiceDialog = new ShareChoiceDialog(
					PostsDetailActivity.this, getScreenHeight());
			shareChoiceDialog.setOnShareChoiceClickListener(this);
			shareChoiceDialog.show();
			break;
		case R.id.rb_postsdetail_collect:
			loginStatu = mApplication.getSpUtil().getLoginStatu();
			if (loginStatu == 0) {
				mPostsCollect.setChecked(false);
				Intent intent = new Intent(this, LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1001);
			} else {
				if (TextUtils.isEmpty(collectId)) {
					addCollect();
				} else {
					delCollect();
				}
			}
			break;
		case R.id.rb_postsdetail_praise:
			loginStatu = mApplication.getSpUtil().getLoginStatu();
			if (loginStatu == 0) {
				mPostsPraise.setChecked(false);
				Intent intent = new Intent(this, LoginActivity.class);
				startActivityForResultByPendingTransition(intent, 1002);
			} else {
				addPraise();
			}
			break;
		case R.id.btn_postsdetail_refresh:
			onRefresh(mListView);
			break;
		}
	}

	/**
	 * 点赞
	 */
	private void addPraise() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.belaudReply(userId, replyId);
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
					try {
						String status = response.getString("status");
						if (status.equals("1")) {
							isBelaud = 1;
							belauds++;
							mPostsPraise.setChecked(true);
						} else {
							isBelaud = 0;
							belauds--;
							mPostsPraise.setChecked(false);
						}
						mList.get(0).setBelauds(belauds);
						mAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});
	}

	/**
	 * 取消收藏
	 */
	private void delCollect() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.deleteCollect(String.valueOf(collectId));
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
					mPostsCollect.setChecked(false);
					collectId = null;
					showShortToast(R.string.text_collect_delete);
					int i = ++cardFlush % 2;
					if (i == 0) {
						mApplication.getSpUtil().setTopicFlag(false);
					} else {
						mApplication.getSpUtil().setTopicFlag(true);
					}
				}
			}
		});
	}

	/**
	 * 添加收藏
	 */
	public void addCollect() {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		String urlString = UrlEngine.insertCollect(userId, "-2",
				dxForumTopic.getId() + "");
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
					try {
						mPostsCollect.setChecked(true);
						collectId = response.getString("id");
						showShortToast(R.string.text_collect_success);
						int i = ++cardFlush % 2;
						if (i == 0) {
							mApplication.getSpUtil().setTopicFlag(false);
						} else {
							mApplication.getSpUtil().setTopicFlag(true);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});

	}

	@Override
	public void ShareOnClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_cancel:
			break;
		}
	}

	/*
	 * 0.微信好友 1、朋友圈 2、QQ好友 3、QQ空间 4、微博
	 */
	@Override
	public void ShareOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		String content = mList.get(0).getDxForumReply().getContent();
		int index=content.lastIndexOf("<br/>");
		index=index>=100?100:index;
		contentShare = content.substring(0, index);
		switch (position) {
		case 0:
			wechatShare(0);
			break;
		case 1:
			wechatShare(1);
			break;
		case 2:
			onClickShare();
			break;
		case 3:
			shareToQzone();
			break;
		case 4:
			shareToWeibo();
			break;
		}

	}

	private void onClickShare() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, contentShare);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.URL_SHARE
				+ topicId);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
				Constants.URL_SHARE_LOGO);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "易家教");
		mTencent.shareToQQ(PostsDetailActivity.this, params,
				new BaseUiListener());
	}

	private void shareToQzone() {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, contentShare);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.URL_SHARE
				+ topicId);
		ArrayList imageUrls = new ArrayList();
		imageUrls.add(Constants.URL_SHARE_LOGO);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		// params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
		// QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

		mTencent.shareToQzone(PostsDetailActivity.this, params,
				new BaseUiListener());

	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			JSONObject jsonObject = (JSONObject) response;
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
			showShortToast(R.string.text_share_ok);
		}

		@Override
		public void onError(UiError e) {
			showShortToast(R.string.text_share_fail);
		}

		@Override
		public void onCancel() {
			showShortToast(R.string.text_share_cancel);
		}

	}

	IWXAPI wxApi;

	// 实例化
	/**
	 * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
	 * 
	 * @param flag
	 *            (0:分享到微信好友，1：分享到微信朋友圈)
	 */
	private void wechatShare(int flag) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = Constants.URL_SHARE + topicId;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = contentShare;
		// 这里替换一张自己工程里的图片资源
		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.app_logo_share);
		msg.setThumbImage(thumb);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		wxApi.sendReq(req);
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()}
	 * < 10351 时，只支持分享单条消息，即 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
	 */
	private void shareToWeibo() {

		// 1. 初始化微博的分享消息
		// 用户可以分享文本、图片、网页、音乐、视频中的一种
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj();

		// 2. 初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = contentShare;

		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(BitmapFactory.decodeResource(getResources(),
				R.drawable.app_logo_share));
		mediaObject.actionUrl = Constants.URL_SHARE + topicId;
		mediaObject.defaultText = title;
		return mediaObject;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mTencent.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 101:
				int index = data.getIntExtra("index", 0);
				if (index != 0) {
					Intent intent = new Intent(PostsDetailActivity.this,
							SecondPostsListActivity.class);
					intent.putExtra("dxForumTopic", mList.get(index));
					startActivityByPendingTransition(intent);
				} else {
					onRefresh(mListView);
				}
				break;
			case 102:
				count++;
				start = 0;
				mList.clear();
				mAdapter.notifyDataSetChanged();
				mListView.setMode(Mode.BOTH);
				mMap.put("start", 0);
				getPostsDetail(0);
				break;
			case 1001:
				userId = mApplication.getSpUtil().getUserId();
				addCollect();
				break;
			case 1002:
				start = 0;
				userId = mApplication.getSpUtil().getUserId();
				mMap.put("userId", userId);
				mMap.put("start", start);
				getPostsDetail(0);
				break;
			}
		}
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			showShortToast(R.string.text_share_ok);
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			showShortToast(R.string.text_share_cancel);
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			showShortToast(R.string.text_share_fail);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("collectId", collectId);
		intent.putExtra("isBelaud", isBelaud);
		intent.putExtra("belauds", belauds);
		intent.putExtra("clicks", clicks);
		intent.putExtra("count", count);
		intent.putExtra("replyId", replyId);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void defaultFinish() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	protected void onRestart() {
		onRefresh(mListView);
		super.onRestart();
	}

	@Override
	public void onResume() {
		super.onResume();
		userId = mApplication.getSpUtil().getUserId();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
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
			getPostsDetail(0);
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
			getPostsDetail(1);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}
}
