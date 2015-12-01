package net.dx.etutor.activity.home;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.search.SearchSchoolActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxCertificate;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.EmojiFilter;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.UploadUtil;
import net.dx.etutor.util.UploadUtil.OnUploadProcessListener;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class MyUploadPreviewActivity extends BaseActivity implements
		OnClickListener, OnUploadProcessListener {
	
	public static String TAG = "MyUploadPreviewActivity";
	private ImageView mImageView;
	private Button mCancel;
	private Button mUpload;
	private String mPath;
	private EditText mSummary;
	private DxCertificate dxCertificate = new DxCertificate();
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
	
	@Override
	public void initViews() {
		setContentView(R.layout.activity_my_upload_preview);
		setTitle(R.string.text_my_upload);
		mCancel = (Button) findViewById(R.id.btn_cancel);
		mUpload = (Button) findViewById(R.id.btn_upload);
		mImageView = (ImageView) findViewById(R.id.iv_my_upload);
		mSummary = (EditText) findViewById(R.id.et_picture_description);
		mPath = getIntent().getStringExtra("path");
		dxCertificate = (DxCertificate) getIntent().getSerializableExtra(
				"dxCertificate");
		if (TextUtils.isEmpty(mPath)&&!TextUtils.isEmpty(dxCertificate.getUserId())) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + dxCertificate.getCertificateUrl(),
					mImageView, ImageLoadOptionsUtil.getOptions());
			mSummary.setText(dxCertificate.getSummary().toString());
		} else if(!TextUtils.isEmpty(mPath)){
			try {
				mImageView.setImageBitmap(PictureUtil.revitionImageSize(mPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initEvents() {
		mCancel.setOnClickListener(this);
		mUpload.setOnClickListener(this);
		
		EmojiFilter.checkEmoji(MyUploadPreviewActivity.this, mSummary,
				null, 100);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upload:
			
			String msg=mSummary.getText().toString();
			msg = EmojiFilter.filterEmoji(msg);
			if (TextUtils.isEmpty(msg)) {
				msg="";
			} 
			showLoadingDialog("请稍等……");
			if (TextUtils.isEmpty(mPath)) {
				String urlString = UrlEngine.updateCertificate(
						dxCertificate.getId().toString(), msg);
				
				HttpUtil.post(urlString, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (statusCode == Constants.STAT_200) {
							setResult(RESULT_OK);
							finish();
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dismissLoadingDialog();
						if (statusCode==0) {
							showShortToast(R.string.text_link_server_error);
						}
						if (statusCode == Constants.STAT_401
								|| statusCode == Constants.STAT_403
								|| statusCode == Constants.STAT_404) {
							showShortToast(R.string.text_error_network);
						}
					}
				});
			} else {
				File file = new File(mPath);
				Map<String, String> param = new HashMap<>();
				param.put("type", "jpg");
				param.put("summary", msg);
				UploadUtil uploadUtil = UploadUtil.getInstance();
				uploadUtil.setOnUploadProcessListener(this);
				uploadUtil.uploadFile(file, "img", UrlEngine.uploadPic(),
						param);
			}
			break;
		case R.id.btn_cancel:
			this.finish();
			break;
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
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
				dismissLoadingDialog();
				setResult(RESULT_OK);
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

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
