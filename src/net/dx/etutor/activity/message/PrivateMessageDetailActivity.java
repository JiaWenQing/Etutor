package net.dx.etutor.activity.message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.dx.etutor.R;
import net.dx.etutor.activity.ImageShowActivity;
import net.dx.etutor.activity.MainFragmentActivity;
import net.dx.etutor.activity.adapter.MessageDetailAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ChoicePicDialog;
import net.dx.etutor.dialog.ChoicePicDialog.OnChoicePicClickListener;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.FileUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.RecordMangerUtil;
import net.dx.etutor.util.RecordMangerUtil.SoundAmplitudeListen;
import net.dx.etutor.util.UpdateHeadIconUtil;
import net.dx.etutor.util.UploadUtil;
import net.dx.etutor.util.UploadUtil.OnUploadProcessListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.Mode;
import net.dx.etutor.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PrivateMessageDetailActivity extends BaseActivity implements
		OnRefreshListener<ListView>, OnClickListener, OnChoicePicClickListener,
		OnUploadProcessListener {

	public static String TAG = "PrivateMessageDetailActivity";
	/** 按住说话按钮 */
	private Button mBtnVoice;
	/** 用于完成录音 */
	// private MediaRecorder mRecorder = null;
	/** 选择照片按钮 */
	private Button mBtnPic;
	/** 选择文字左按钮 */
	private Button mBtnWord1;
	// /** 选择文字右按钮 */
	// private Button mBtnWord2;
	/** 录音按钮 */
	private Button mSendVoice;
	MediaPlayer player = new MediaPlayer();
	/** 用于完成录音 */
	private RecordMangerUtil mRecordManger = new RecordMangerUtil();
	private LinearLayout mLayoutRecord;
	private Chronometer mTime;
	private ImageView mIvRecord;
	private PullToRefreshListView mListView;
	private List<DxPrivatemsg> mList = new ArrayList<DxPrivatemsg>();
	private List<DxPrivatemsg> mPullList = new ArrayList<DxPrivatemsg>();
	private List<DxPrivatemsg> mCopyList = new ArrayList<DxPrivatemsg>();
	private MessageDetailAdapter mAdapter;
	private Button mSendMessage;
	private EditText mMessageInfo;
	private String userId;
	private String toUserId;
	private DxPrivatemsg dxPrivatemsg = new DxPrivatemsg();
	private LayoutInflater mInflater;
	private int start = 0;
	private int pageSize = 10;
	private int surplusPage = 0;
	private int index;
	private String imagePath;
	/**
	 * 类型 type=Advisory表示咨询
	 */
	private String type;
	private Map<String, Object> mMap;
	private long countTime = 60;
	protected File fileRecord;
	protected File filePic;

	private AnimationDrawable animationDrawable;
	protected View recordView;
	protected Timer timer;
	protected boolean messageflag;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_message_detail);
		setTitle(R.string.text_private_message_detail);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		initData();

		mLayoutRecord = (LinearLayout) findViewById(R.id.layout_dialog_sound);
		mTime = (Chronometer) findViewById(R.id.tv_time);
		mIvRecord = (ImageView) findViewById(R.id.imv_sound_progress);

		mListView = (PullToRefreshListView) findViewById(R.id.lv_message_list);
		mMessageInfo = (EditText) findViewById(R.id.et_message_info);

		mSendMessage = (Button) findViewById(R.id.btn_send);
		mSendVoice = (Button) findViewById(R.id.btn_send_voice);
		mBtnVoice = (Button) findViewById(R.id.btn_voice);
		mBtnPic = (Button) findViewById(R.id.btn_picture);
		mBtnWord1 = (Button) findViewById(R.id.btn_send_word1);

		mMessageInfo.setVisibility(View.VISIBLE);
		mBtnVoice.setVisibility(View.VISIBLE);
		mBtnPic.setVisibility(View.VISIBLE);
		mSendMessage.setVisibility(View.GONE);
		mSendVoice.setVisibility(View.GONE);
		mBtnWord1.setVisibility(View.GONE);
		mTime.setBase(60000);
		mRecordManger.setSoundAmplitudeListen(onSoundAmplitudeListen);// 设置振幅监听器
		mInflater = LayoutInflater.from(PrivateMessageDetailActivity.this);
		mAdapter = new MessageDetailAdapter(this, mList, imagePath);
		mListView.setAdapter(mAdapter);
		getMessageList(mMap);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mMap = new HashMap<String, Object>();
		mMap.put("start", start);
		mMap.put("pageSize", pageSize);

		type = getIntent().getStringExtra("type");
		if (type.equals("PrivateList")) {
			dxPrivatemsg = (DxPrivatemsg) getIntent().getSerializableExtra(
					"dxPrivatemsg");
			index = getIntent().getIntExtra("index", 0);
			imagePath = dxPrivatemsg.getDxUser().getAvatarUrl();
			mMap.put("senderId", dxPrivatemsg.getSenderId());
			mMap.put("receiverId", dxPrivatemsg.getReceiverId());
			mApplication.getSpUtil().setNewMsgContent(null);
			mApplication.getSpUtil().setNewMsgTime(null);
			updateMsgCount();
		} else if (type.equals("Advisory")) {
			mMap.put("senderId", userId);
			mMap.put("receiverId", -1);
		} else if (type.equals("Intro")) {
			dxPrivatemsg = (DxPrivatemsg) getIntent().getSerializableExtra(
					"dxPrivatemsg");
			imagePath = dxPrivatemsg.getDxUser().getAvatarUrl();
			mMap.put("senderId", dxPrivatemsg.getSenderId());
			mMap.put("receiverId", dxPrivatemsg.getReceiverId());
		} else if (type.equals("orderDetail")) {
			toUserId = getIntent().getStringExtra("toUserId");
			imagePath = getIntent().getStringExtra("imagePath");
			mMap.put("senderId", userId);
			mMap.put("receiverId", toUserId);
		}
	}

	/**
	 * 更新MsgCount
	 */
	private void updateMsgCount() {
		// TODO Auto-generated method stub
		int[] msgCount;
		String statu = mApplication.getSpUtil().getMsgCount();
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
			boolean b = false;
			statu = "";
			for (int i = 0; i < msgCount.length; i++) {
				if (i == index) {
					msgCount[i] = 0;
				}
				if (msgCount[i] == 1) {
					b = true;
				}
				statu = statu + msgCount[i];
			}
			mApplication.getSpUtil().setMsgCount(statu);
			mApplication.getSpUtil().setMsgStatu(b);
		} else {
			mApplication.getSpUtil().setMsgStatu(false);
		}
	}

	@Override
	public void initEvents() {

		mBtnVoice.setOnClickListener(this);
		mBtnPic.setOnClickListener(this);
		mSendMessage.setOnClickListener(this);
		mBtnWord1.setOnClickListener(this);
		mSendVoice.setOnClickListener(null);
		mSendVoice.setOnTouchListener(new OnTouchListener() {

			private long starttime;
			private long endtime;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					starttime = System.currentTimeMillis();
					startRecord();
					break;
				case MotionEvent.ACTION_MOVE:
					if (inRageOfView(v, event)) {
						mSendVoice.setText(R.string.text_voice_up);
					} else {
						mSendVoice.setText(R.string.text_voice_cancel);
					}
					break;
				case MotionEvent.ACTION_UP:
					try {
						endtime = System.currentTimeMillis();
						if ((endtime - starttime) < 300) {
							Thread.sleep(200);
						}
						if (!TimeOverFlag) {
							TimeOverFlag = false;
							if (inRageOfView(v, event)) {
								stopRecord(true);
							} else {
								stopRecord(false);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		mMessageInfo.addTextChangedListener(new TextWatcher() {

			private String str;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				str = s.toString();
				if (str.length() == 0) {
					mBtnPic.setVisibility(View.VISIBLE);
					mSendMessage.setVisibility(View.GONE);
				} else {
					mBtnPic.setVisibility(View.GONE);
					mSendMessage.setVisibility(View.VISIBLE);
				}
				mMessageInfo.invalidate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				str = s.toString();
				if (str.length() == 0) {
					mBtnPic.setVisibility(View.VISIBLE);
					mSendMessage.setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (animationDrawable != null) {
					animationDrawable.stop();
					if (messageflag) {
						recordView
								.setBackgroundResource(R.drawable.anim_voice_send);
					} else {
						recordView
								.setBackgroundResource(R.drawable.anim_voice_receive);
					}
				}
				DxPrivatemsg privatemsg = (DxPrivatemsg) parent.getAdapter()
						.getItem(position);
				messageflag = userId.equals(privatemsg.getSenderId());
				if (!TextUtils.isEmpty(privatemsg.getRecord())) {
					String path = privatemsg.getRecord().toString();
					player.stop();
					player.release();
					player = new MediaPlayer();
					getRecordFile(path);

					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					timer = new Timer();
					if (messageflag) {
						recordView = view.findViewById(R.id.tv_send_voice);
						animationDrawable = (AnimationDrawable) view
								.findViewById(R.id.tv_send_voice)
								.getBackground();

					} else {
						recordView = view.findViewById(R.id.tv_receive_voice);
						animationDrawable = (AnimationDrawable) view
								.findViewById(R.id.tv_receive_voice)
								.getBackground();
					}
					animationDrawable.start();
					int time = Integer.parseInt(path.substring(
							path.indexOf("=") + 1, path.length())) * 1000;
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							animationDrawable.stop();
							if (messageflag) {
								mHandler.sendEmptyMessage(10003);
							} else {
								mHandler.sendEmptyMessage(10002);
							}
						}
					}, time);

				}
				if (!TextUtils.isEmpty(privatemsg.getPicture())) {
					String url = DataParam.REMOTE_SERVE
							+ privatemsg.getPicture().toString().trim();
					initImageView(url);
				}

			}
		});
	}

	/**
	 * 播放语音
	 */
	public void getRecordFile(String path) {
		String url = DataParam.REMOTE_SERVE + path;
		try {
			player.setDataSource(url);
			player.prepare();
			player.start();

		} catch (IllegalArgumentException e) {

			e.printStackTrace();

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	/**
	 * 判断点击位置是否在指定View上
	 */
	private boolean inRageOfView(View v, MotionEvent ev) {
		int[] loc = new int[2];
		v.getLocationOnScreen(loc);
		int x = loc[0];
		int y = loc[1];
		if (ev.getRawX() > x && ev.getRawY() > y
				&& ev.getRawX() < (x + v.getWidth() * 1.5)
				&& ev.getRawY() < (y + v.getHeight() * 1.5)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 停止录音
	 */
	protected void stopRecord(boolean isSave) {
		// TODO Auto-generated method stub
		mSendVoice.setText(R.string.text_voice_down);
		fileRecord = mRecordManger.stopRecord();
		mTime.stop();
		mLayoutRecord.setVisibility(View.GONE);
		if (isSave) {
			if ((60 - countTime) < 1) {
				showShortToast("录音时间太短");
			} else {
				onSendVoice();
			}
		} else {
			showShortToast("取消发送");
		}
		countTime = 60;
	}

	/**
	 * 开始录音
	 */
	protected void startRecord() {
		// TODO Auto-generated method stub
		try {
			mRecordManger.startRecordCreateFile();
			mTime.start();
			initTimer(60);
			mSendVoice.setText(R.string.text_voice_up);
			mLayoutRecord.setVisibility(View.VISIBLE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 时间是否用完
	 */
	protected boolean TimeOverFlag = false;

	/**
	 * 显示大图
	 * 
	 * @param url
	 */
	private void initImageView(String url) {
		Intent intent = new Intent(this, ImageShowActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	/**
	 * 初始化计时器，计时器是通过widget.Chronometer来实现的
	 * 
	 * @param total
	 *            一共多少秒
	 */
	private void initTimer(long total) {
		countTime = total;
		mTime.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if (countTime <= 0) {
					TimeOverFlag = true;
					showShortToast("录音时间到");
					mTime.stop();
					// 录音停止
					fileRecord = mRecordManger.stopRecord();
					mLayoutRecord.setVisibility(View.GONE);
					mSendVoice.setText(R.string.text_voice_down);
					onSendVoice();
					return;
				}
				countTime--;
				refreshTimeLeft(countTime);
			}
		});
	}

	private void refreshTimeLeft(long time) {
		this.mTime.setText(time + "");
		// TODO 格式化字符串
	}

	/** 回调振幅，根据振幅设置图片 */
	private SoundAmplitudeListen onSoundAmplitudeListen = new SoundAmplitudeListen() {

		@Override
		public void amplitude(int amplitude, int db, int value) {
			if (value >= 4) {
				value = 4;
			}
			mIvRecord.setBackgroundResource(R.drawable.mic_1 + value);// 显示震幅图片

		}
	};
	int count = 1;
	private boolean isNewMsg = false;
	private int NewMsgType = 0;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (player.isPlaying()) {
			player.stop();
			player.release();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			onSend();
			break;
		case R.id.btn_picture:
			mBtnWord1.setVisibility(View.GONE);
			mBtnVoice.setVisibility(View.VISIBLE);
			mBtnPic.setVisibility(View.VISIBLE);

			mSendVoice.setVisibility(View.GONE);
			mMessageInfo.setVisibility(View.VISIBLE);
			ChoicePicDialog dialog = new ChoicePicDialog(this,
					getScreenHeight());
			dialog.setOnChoicePicClickListener(this, 3);
			dialog.show();
			break;
		case R.id.btn_voice:
			mBtnVoice.setVisibility(View.GONE);
			mBtnWord1.setVisibility(View.VISIBLE);
			mMessageInfo.setVisibility(View.GONE);
			mSendVoice.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_send_word1:
			mBtnWord1.setVisibility(View.GONE);
			mBtnVoice.setVisibility(View.VISIBLE);
			mMessageInfo.setVisibility(View.VISIBLE);
			mSendVoice.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 发送照片
	 */
	private void onSendPicture() {
		// TODO Auto-generated method stub
		filePic = new File(filePath);
		showLoadingDialog("请稍后…");
		Map<String, String> param = new HashMap<>();
		param.put("senderId", userId);
		if (!TextUtils.isEmpty(type) && type.equals("Advisory")) {
			param.put("receiverId", "-1");
		} else if (!TextUtils.isEmpty(type) && type.equals("orderDetail")) {
			param.put("receiverId", toUserId);
		} else {
			if (dxPrivatemsg.getReceiverId().equals(userId)) {
				param.put("receiverId", dxPrivatemsg.getSenderId());
			} else {
				param.put("receiverId", dxPrivatemsg.getReceiverId());
			}
		}
		if (mList.size() > 0) {
			param.put("id", mList.get(0).getId().toString());
		} else {
			param.put("id", "-1");
		}
		param.put("type", "jpg");
		param.put("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.put("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);
		uploadUtil
				.uploadFile(filePic, "voice", UrlEngine.uploadRecord(), param);
		isNewMsg = true;
		NewMsgType = 3;
	}

	private static String filePath;

	@Override
	public void choiceOnClick(View v, int flag) {
		// TODO Auto-generated method stub
		UpdateHeadIconUtil.choiceOnClick(PrivateMessageDetailActivity.this, v,
				3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != Activity.RESULT_OK) {// result is not correct
			return;
		} else {
			Intent intent;
			switch (requestCode) {
			case Constants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改
				filePath = UpdateHeadIconUtil.getFilePath();
				filePic = new File(filePath);
				Bitmap bitmap;
				bitmap = PictureUtil.getimage(filePath, filePic.length());
				PictureUtil.saveBitmap(Constants.PICTURE_PATH,
						filePic.getName(), bitmap, true);
				showLoadingDialog("请稍后……");
				onSendPicture();
				break;
			case Constants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改
				Uri uri = null;
				if (data != null) {
					if (resultCode == Activity.RESULT_OK) {
						if (!Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							showShortToast("SD卡不可用");
						} else {
							uri = data.getData();
							String img_path;
							if (!TextUtils.isEmpty(uri.getAuthority())) {    
						        Cursor cursor = getContentResolver().query(uri,  
						                new String[] { MediaStore.Images.Media.DATA },null, null, null);    
						        if (null == cursor) { 
						        	Toast.makeText(this, "图片没找到", 0).show();
						            return;   
						        }    
						        cursor.moveToFirst();    
						        img_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));  
						        cursor.close();
						    } else {    
						    	img_path = uri.getPath();    
						    }
							filePic = new File(img_path);
							if (filePic.length() < 102400) {
								filePath = Constants.PICTURE_PATH
										+ filePic.getName();
								FileUtil.copyFile(img_path, filePath);
								onSendPicture();
							} else {
								Bitmap bitmap1 = PictureUtil.getimage(img_path,
										filePic.length());
								PictureUtil.saveBitmap(Constants.PICTURE_PATH,
										filePic.getName(), bitmap1, true);
								filePath = Constants.PICTURE_PATH
										+ filePic.getName();
								showLoadingDialog("请稍后……");
								onSendPicture();
							}
						}

					} else {
						showShortToast("照片获取失败");
					}
				}

				break;
			}
		}
	}

	/**
	 * 发送语音
	 */
	private void onSendVoice() {
		// TODO Auto-generated method stub
		int time;
		time = (int) (60 - countTime);
		Map<String, String> param = new HashMap<>();
		showLoadingDialog("请稍后…");
		param.put("senderId", userId);
		if (!TextUtils.isEmpty(type) && type.equals("Advisory")) {
			param.put("receiverId", "-1");
		} else if (!TextUtils.isEmpty(type) && type.equals("orderDetail")) {
			param.put("receiverId", toUserId);
		} else {
			if (dxPrivatemsg.getReceiverId().equals(userId)) {
				param.put("receiverId", dxPrivatemsg.getSenderId());
			} else {
				param.put("receiverId", dxPrivatemsg.getReceiverId());
			}
		}
		if (mList.size() > 0) {
			param.put("id", mList.get(0).getId().toString());
		} else {
			param.put("id", "-1");
		}
		param.put("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.put("type", "aac");
		param.put("time", time + "");
		param.put("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);
		uploadUtil.uploadFile(fileRecord, "voice", UrlEngine.uploadRecord(),
				param);
		isNewMsg = true;
		NewMsgType = 2;
	}

	private void onSend() {
		// TODO Auto-generated method stub
		String message = mMessageInfo.getText().toString().trim();
		if (TextUtils.isEmpty(message)) {
			showLongToast("消息不能为空！");
			return;
		}
		mMessageInfo.setText("");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("senderId", userId);
		if (mList.isEmpty()) {
			map.put("msgId", 0);
		} else {
			map.put("msgId", mList.get(0).getId());
		}
		map.put("message", message);

		if (!TextUtils.isEmpty(type) && type.equals("Advisory")) {
			map.put("receiverId", -1);
		} else if (!TextUtils.isEmpty(type) && type.equals("orderDetail")) {
			map.put("receiverId", toUserId);
		} else {
			if (dxPrivatemsg.getReceiverId().equals(userId)) {
				map.put("receiverId", dxPrivatemsg.getSenderId());
			} else {
				map.put("receiverId", dxPrivatemsg.getReceiverId());
			}
		}
		sendMessage(map);
	}

	public void sendMessage(Map<String, Object> map) {
		showLoadingDialog("请稍后……");
		String urlString = UrlEngine.sendMessage(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						if (response.toString().equals(Constants.TOKEN_ILLEGAL)
								|| response.toString().equals(
										Constants.TOKEN_TIMEOUT)) {
							dismissLoadingDialog();
							showLongToast(R.string.token_error);
							mApplication.getSpUtil().clearSharePerference();
							Intent intent = new Intent(
									PrivateMessageDetailActivity.this,
									LoginActivity.class);
							intent.putExtra("targetId", 0);
							startActivityByPendingTransition(intent);
							PrivateMessageDetailActivity.this.finish();
						} else {
							mPullList.clear();
							mCopyList.clear();
							if (type.equals("PrivateList")) {
								updateMsgCount();
							}
							for (int i = 0; i < response.length(); i++) {
								DxPrivatemsg dxMessage = new DxPrivatemsg();
								dxMessage
										.initWithAttributes((JSONObject) response
												.get(i));
								mPullList.add(dxMessage);
							}
							dismissLoadingDialog();
							if (!mList.isEmpty()) {
								mCopyList.addAll(mList);
								mList.clear();
								mList.addAll(mPullList);
								mList.addAll(mCopyList);
								mAdapter.notifyDataSetChanged();
								mListView.getRefreshableView().setSelection(
										mList.size() - 1);// 定位到最后一条
							} else {
								mList.addAll(mPullList);
								mListView.getRefreshableView().setSelection(
										mList.size() - 1);// 定位到最后一条
							}
							isNewMsg = true;
							NewMsgType = 1;
						}
					} catch (Exception e) {
						// TODO: handle exception
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

	@Override
	public void defaultFinish() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mList.size() != 0) {
			switch (NewMsgType) {
			case 1:
				mApplication.getSpUtil().setNewMsgContent(
						mList.get(0).getMessage());
				break;
			case 2:
				mApplication.getSpUtil().setNewMsgContent(
						mList.get(0).getRecord());
				break;
			case 3:
				mApplication.getSpUtil().setNewMsgContent(
						mList.get(0).getPicture());
				break;
			default:
				mApplication.getSpUtil().setNewMsgContent(null);
				break;
			}
			mApplication.getSpUtil()
					.setNewMsgTime(mList.get(0).getCreateTime());
		}
		if (type.equals("PrivateList")) {
			if (isNewMsg) {
				isNewMsg = false;
				Intent intent = new Intent(PrivateMessageDetailActivity.this,
						MainFragmentActivity.class);
				intent.putExtra("NewMsgType", NewMsgType);
				setResult(RESULT_OK, intent);
			}
		}
		super.onBackPressed();
	}

	public void getMessageList(Map<String, Object> map) {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		showLoadingDialog("请稍后...");
		String urlString = UrlEngine.getMessageDetail(map);
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == Constants.STAT_200) {
					try {
						surplusPage = response.length() % pageSize;
						if (surplusPage != 0 || response.length() == 0) {
							mListView.onRefreshComplete();
							dismissLoadingDialog();
						}

						for (int i = 0; i < response.length(); i++) {
							DxPrivatemsg dxMessage = new DxPrivatemsg();
							dxMessage
							.initWithAttributes((JSONObject) response
									.get(i));
							mList.add(dxMessage);
						}
						mAdapter.notifyDataSetChanged();
						if (null != mList) {
							mListView.getRefreshableView().setSelection(
									mList.size() - 1);// 定位到最后一条
						}
						mListView.onRefreshComplete();
						dismissLoadingDialog();
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
					mListView.setVisibility(View.GONE);
				}
				if (statusCode == Constants.STAT_401
						|| statusCode == Constants.STAT_403
						|| statusCode == Constants.STAT_404) {
					showShortToast(R.string.text_error_network);
				}
			}
		});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				try {
					JSONArray response = (JSONArray) msg.obj;
					mPullList.clear();
					mCopyList.clear();
					if (type.equals("PrivateList")) {
						updateMsgCount();
					}
					for (int i = 0; i < response.length(); i++) {
						DxPrivatemsg dxMessage = new DxPrivatemsg();
						dxMessage.initWithAttributes((JSONObject) response
								.get(i));
						mPullList.add(dxMessage);
					}
					if (!mList.isEmpty()) {
						mCopyList.addAll(mList);
						mList.clear();
						mList.addAll(mPullList);
						mList.addAll(mCopyList);
						mAdapter.notifyDataSetChanged();
						mListView.getRefreshableView().setSelection(
								mList.size() - 1);// 定位到最后一条
					} else {
						mList.addAll(mPullList);
						mAdapter.notifyDataSetChanged();
						mListView.getRefreshableView().setSelection(
								mList.size() - 1);// 定位到最后一条
					}
					dismissLoadingDialog();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 10002:
				recordView.setBackgroundResource(R.drawable.anim_voice_receive);
				break;
			case 10003:
				recordView.setBackgroundResource(R.drawable.anim_voice_send);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onUploadDone(int responseCode, String message) {
		// TODO Auto-generated method stub
		String str = message.substring(message.indexOf("：") + 1,
				message.length());
		if (responseCode == 1) {
			JSONArray response;
			try {
				response = new JSONArray(str);
				Message msg = Message.obtain();
				msg.what = 1;
				msg.arg1 = responseCode;
				msg.obj = response;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initUpload(int fileSize) {
		// TODO Auto-generated method stub
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
			mMap.put("start", mList.size());
			getMessageList(mMap);
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
			getMessageList(mMap);
			// 定位到最后一条
			mListView.getRefreshableView().setSelection(mList.size() - 1);
		}

	}
}
