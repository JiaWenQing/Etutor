package net.dx.etutor.activity.forum;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ChoicePicDialog;
import net.dx.etutor.dialog.ChoicePicDialog.OnChoicePicClickListener;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.FileUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.UpdateHeadIconUtil;
import net.dx.etutor.util.UploadUtil;
import net.dx.etutor.util.UploadUtil.OnUploadProcessListener;
import net.dx.etutor.view.MyUploadGridView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 发帖、回复
 * 
 * @author jwq
 * 
 */
public class SendPostsActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnChoicePicClickListener,
		OnUploadProcessListener {
	private MyUploadGridView mGridView;
	private GridAdapter mAdapter;
	private File filePic;
	private static String filePath;
	private EditText mTopicTitle;
	private EditText mTopicContent;
	private ImageView mLine;
	private DxForumTopic mDxForumTopic;
	private List<String> mList = new ArrayList<String>();
	// 去上传文件
	protected static final int TO_UPLOAD_FILE = 1;
	// 上传文件响应
	protected static final int UPLOAD_FILE_DONE = 2;
	// 选择文件
	public static final int TO_SELECT_PHOTO = 3;
	// 上传初始化
	private static final int UPLOAD_INIT_PROCESS = 4;
	// 上传中
	private static final int UPLOAD_IN_PROCESS = 5;
	private int loginStatu = 0;
	private int index;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_send_posts);
		index=getIntent().getIntExtra("index", 0);
		mDxForumTopic = (DxForumTopic) getIntent().getExtras().get("topic");
		setTitle(mDxForumTopic.getTitle());
		showIcon(0, "发送");
		mGridView = (MyUploadGridView) findViewById(R.id.my_upload_gridview);
		mLine = (ImageView) findViewById(R.id.split_line);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mTopicTitle = (EditText) findViewById(R.id.et_topic_title);
		mTopicContent = (EditText) findViewById(R.id.et_topic_content);
		mAdapter = new GridAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setVisibility(View.VISIBLE);
		// 0:发帖 1:1级回复 2:2级回复
		switch (mDxForumTopic.getReplyType()) {
		case 0:
			createDir();
			getPicList();
			break;
		case 1:
			mLine.setVisibility(View.GONE);
			mTopicTitle.setVisibility(View.GONE);
			createDir();
			getPicList();
			break;
		case 2:
			mLine.setVisibility(View.GONE);
			mTopicTitle.setVisibility(View.GONE);
			mGridView.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	@Override
	public void iconClick() {
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		if (loginStatu != 0) {
			switch (mDxForumTopic.getReplyType()) {
			// 发帖
			case 0:
				sendTopic();
				break;
			// 一级回复
			case 1:
				firstReplyTopic();
				break;
			// 二级回复
			case 2:
				secondReplyTopic();
				break;
			default:
				break;
			}
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		}

	}

	/**
	 * 发帖方法
	 */
	public void sendTopic() {
		if (TextUtils.isEmpty(mTopicTitle.getText())) {
			showShortToast("标题不能为空！");
			return;
		}
		if (TextUtils.isEmpty(mTopicContent.getText())) {
			showShortToast("内容不能为空！");
			return;
		}

		String imageUrl = "";
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i) != null && !"".equals(mList.get(i))) {
				imageUrl += "<img  src='upload/"
						+ DateUtil.getCurrentDate("yyyyMM/")
						+ mList.get(i).substring(
								mList.get(i).lastIndexOf("/") + 1) + "' onerror='javascript:this.src=\"upload/error.png\";'/>";
			}
		}
		send(imageUrl);
	}

	/**
	 * 一级回复
	 */
	public void firstReplyTopic() {
		if (TextUtils.isEmpty(mTopicContent.getText())) {
			showShortToast("内容不能为空！");
			return;
		}
		String imageUrl = "";
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i) != null && !"".equals(mList.get(i))) {
				imageUrl += "<img  src='upload/"
						+ DateUtil.getCurrentDate("yyyyMM/")
						+ mList.get(i).substring(
								mList.get(i).lastIndexOf("/") + 1) + "' onerror='javascript:this.src=\"upload/error.png\";'/>";
			}
		}
		reply(imageUrl, "first");
	}

	/**
	 * 一级回复
	 */
	public void secondReplyTopic() {
		if (TextUtils.isEmpty(mTopicContent.getText())) {
			showShortToast("内容不能为空！");
			return;
		}
		reply("", "second");
	}

	/**
	 * 回复方法，不分一二级
	 * 
	 * @param imageUrl
	 */
	private void reply(String imageUrl, String type) {
		showLoadingDialog("请稍后……");
		String urlString = "";
		String content = mTopicContent.getText().toString().trim();
		if ("first".equals(type)) {
			urlString = UrlEngine.insertReply(content + "<br/>" + imageUrl,
					mDxForumTopic.getId().toString(), mDxForumTopic
							.getReplyId().toString());
		} else {
			urlString = UrlEngine.insertReply(content + "<br/>", mDxForumTopic
					.getId().toString(), mDxForumTopic.getReplyId().toString());
		}
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dismissLoadingDialog();
					showShortToast("成功");
					setResult(RESULT_OK);
					PictureUtil.deleteDir(Constants.MyTopicDir);
					if (mDxForumTopic.getReplyType() != 0) {
						Intent intent = new Intent();
						intent.putExtra("index", index);
						setResult(RESULT_OK, intent);
					}
					finish();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void send(String imageUrl) {
		showLoadingDialog("请稍后……");
		String urlString = UrlEngine.insertTopic(mTopicTitle.getText()
				.toString(), mTopicContent.getText().toString() + "<br/>"
				+ imageUrl, mDxForumTopic.getBoardId().toString());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					dismissLoadingDialog();
//					showShortToast("发帖成功");
					mApplication.getSpUtil().isCardListFlushFlag(true);
					setResult(RESULT_OK);
					PictureUtil.deleteDir(Constants.MyTopicDir);
					finish();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void initEvents() {
		mGridView.setOnItemClickListener(this);
		EmojiFilter.checkEmoji(SendPostsActivity.this, mTopicTitle, null, 15);
		EmojiFilter
				.checkEmoji(SendPostsActivity.this, mTopicContent, null, 100);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == mList.size() - 1) {
			ChoicePicDialog picDialog = new ChoicePicDialog(this,
					getScreenHeight());
			picDialog.setOnChoicePicClickListener(this, 2);
			picDialog.show();
		} else {
			try {
				if (mList.get(position) != null
						&& !"".equals(mList.get(position))) {
					PictureUtil.delPicFile(mList.get(position));
					getPicList();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createDir() {
		File dir = new File(Constants.MyTopicDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public void getPicList() {
		mList.clear();
		File file = new File(Constants.MyTopicDir);
		File[] f = file.listFiles();
		if (null != f) {
			for (int i = 0; i < f.length; i++) {
				mList.add(f[i].getPath());
			}
			mList.add("");
			mAdapter.notifyDataSetChanged();
		}
	}

	public class GridAdapter extends BaseAdapter {

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

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SendPostsActivity.this)
						.inflate(R.layout.item_topic_pic, null);
				holder = new ViewHolder();
				holder.mImage = (ImageView) convertView
						.findViewById(R.id.item_grid_image);

				holder.mDelete = (ImageView) convertView
						.findViewById(R.id.item_upload_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position >= 3) {
				holder.mDelete.setVisibility(View.GONE);
				holder.mImage.setVisibility(View.GONE);
			} else {
				if (position == getCount() - 1) {
					holder.mDelete.setVisibility(View.INVISIBLE);
				} else {
					holder.mDelete.setVisibility(View.VISIBLE);
					holder.mDelete.setImageAlpha(110);
				}
			}
			if (position == getCount()) {
				holder.mDelete.setVisibility(View.GONE);
				holder.mImage.setBackgroundResource(R.drawable.upload_icon);
				holder.mImage.setImageResource(R.drawable.upload_icon);
			} else {
				ImageLoader.getInstance().displayImage(
						"file:///" + getItem(position), holder.mImage,
						ImageLoadOptionsUtil.getOptions());
			}
			Drawable d = holder.mImage.getBackground();
			if (null == d) {
				holder.mImage.setBackgroundResource(R.drawable.upload_icon);
			}
			return convertView;
		}
	}

	private class ViewHolder {
		private ImageView mImage;
		private ImageView mDelete;
	}

	@Override
	public void choiceOnClick(View v, int flag) {
		UpdateHeadIconUtil.choiceOnClick(SendPostsActivity.this, v, 4);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {// result is not correct
			return;
		} else {
			switch (requestCode) {
			case Constants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改:
				filePath = UpdateHeadIconUtil.getFilePath();
				filePic = new File(filePath);
				Bitmap bitmap;
				bitmap = PictureUtil.getimage(filePath, filePic.length());
				PictureUtil.saveBitmap(Constants.MyTopicDir, filePic.getName(),
						bitmap, true);
				onSendPicture();
				break;
			case Constants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改;
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
								filePath = Constants.MyTopicDir
										+ filePic.getName();
								FileUtil.copyFile(img_path, filePath);
								onSendPicture();
							} else {
								Bitmap bitmap1 = PictureUtil.getimage(img_path,
										filePic.length());
								PictureUtil.saveBitmap(Constants.MyTopicDir,
										filePic.getName(), bitmap1, true);
								filePath = Constants.MyTopicDir
										+ filePic.getName();
								onSendPicture();
							}
						}

					} else {
						showShortToast("照片获取失败");
					}
				}
				break;
			case 1000:
				iconClick();
				break;
			}
			if (requestCode != 1000) {
				getPicList();
			}
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		try {
			JSONObject response = new JSONObject(message);
			Message msg = Message.obtain();
			msg.what = 2;
			msg.arg1 = responseCode;
			msg.obj = response;
			handler.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUploadProcess(int uploadSize) {

	}

	@Override
	public void initUpload(int fileSize) {

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				break;
			case UPLOAD_INIT_PROCESS:
				break;
			case UPLOAD_IN_PROCESS:
				break;
			case UPLOAD_FILE_DONE:
				/*
				 * Toast.makeText(SendPostsActivity.this, "sdsaf",
				 * Toast.LENGTH_SHORT).show();
				 */
				JSONObject response = (JSONObject) msg.obj;
				try {
					String imageUrl = response.getString("imageUrl");
					String imageName = imageUrl.substring(imageUrl
							.lastIndexOf("/") + 1);
					PictureUtil.fileRename(filePath, Constants.MyTopicDir
							+ imageName);
					getPicList();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onBackPressed() {
		PictureUtil.deleteDir(Constants.MyTopicDir);
		super.onBackPressed();
	}

	@Override
	public void defaultFinish() {
		onBackPressed();
	}

	private void onSendPicture() {
		// TODO Auto-generated method stub
		filePic = new File(filePath);
		Map<String, String> param = new HashMap<>();
		param.put("type", "png");
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);
		uploadUtil.uploadFile(filePic, "img", UrlEngine.uploadToppic(), param);
	}
}
