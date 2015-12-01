package net.dx.etutor.activity.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.MyUploadGridAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.ChoicePicDialog;
import net.dx.etutor.dialog.ChoicePicDialog.OnChoicePicClickListener;
import net.dx.etutor.dialog.LogoutDialog;
import net.dx.etutor.dialog.LogoutDialog.OnLogoutClickListener;
import net.dx.etutor.model.DxCertificate;
import net.dx.etutor.util.AnimationUtil;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.PictureUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.TokenCheckUtil;
import net.dx.etutor.util.UpdateHeadIconUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class MyUploadActivity extends BaseActivity implements OnClickListener,
		OnChoicePicClickListener, OnItemClickListener, OnItemLongClickListener,
		OnLogoutClickListener {

	public static String TAG = "MyUploadActivity";
	private GridView mGridView;
	private MyUploadGridAdapter mAdapter;
	private static String filePath;
	private String path;
	private List<String> id = new ArrayList<String>();
	private boolean[] itemSelected;
	private List<DxCertificate> mList;
	private RelativeLayout mFinishNetwork;
	private int[] status = new int[9];
	public int count;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_my_upload);
		setTitle(R.string.text_my_upload);
		setIcon(R.drawable.main_head_bar_icon_recy_selector);
		mFinishNetwork = (RelativeLayout) findViewById(R.id.layout_finish_network);
		mGridView = (GridView) findViewById(R.id.my_upload_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mList = new ArrayList<DxCertificate>();
		mAdapter = new MyUploadGridAdapter(this,mList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		getCertificate();
	}

	@Override
	public void initEvents() {
		mFinishNetwork.setOnClickListener(this);
	}

	public void getCertificate() {
		if (!NetWorkHelperUtil.checkNetState(this)) {
			mFinishNetwork.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			showShortToast(R.string.network_error);
			return;
		} else {
			mFinishNetwork.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
		}
		showLoadingDialog("请稍后...");
		TokenCheckUtil.checkToken();
		String urlString = UrlEngine.getCertificate(EtutorApplication
				.getInstance().getSpUtil().getUserId());
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
						mList.clear();
						for (int i = 0; i < response.length(); i++) {
							DxCertificate dxCertificate = new DxCertificate();
							dxCertificate
									.initWithAttributes((JSONObject) response
											.get(i));
							mList.add(dxCertificate);
						}
						itemSelected = new boolean[mList.size()];
						DxCertificate dxCertificate = new DxCertificate();
						mList.add(dxCertificate);
						dismissLoadingDialog();
						mAdapter.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void iconClick() {

		for (int i = 0; i < mList.size() - 1; i++) {
			if (mList.get(i).isSelected()) {
				/*
				 * String fileName =
				 * mList.get(i).getCertificateUrl().split("/")[1];
				 * PictureUtil.delFile(fileName); mList.remove(i);
				 */
				id.add(mList.get(i).getId().toString());
				if (mList.get(i).getStatus().equals("1")) {
					count++;
				}
			}
		}
		if (id.size() == 0) {
			showShortToast("请选择要删除的图片");
		} else {
			showLoadingDialog("");
			LogoutDialog dialog = new LogoutDialog(this);
			dialog.setOnLogoutClickListener(this, "确定要删除吗？", "");
			dialog.show();
		}
		super.iconClick();
	}

	@Override
	public void choiceOnClick(View v, int flag) {
		UpdateHeadIconUtil.choiceOnClick(MyUploadActivity.this, v, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		UpdateHeadIconUtil.onActivityResultToHead(MyUploadActivity.this, null,
				requestCode, resultCode, data, null);
		switch (requestCode) {
		case Constants.REQUESTCODE_UPLOADAVATAR_PREVIEW:
			showLoadingDialog("请稍后……");
			getCertificate();
			break;
		}
	}

	public void savePic(Bitmap bitmap) {
		if (bitmap != null) {
			// 保存图片
			String filename = new SimpleDateFormat("yyMMddHHmmss")
					.format(new Date()) + ".jpg";
			path = Constants.MyCredentialsDir + filename;
			PictureUtil.saveBitmaph(Constants.MyCredentialsDir, filename,
					bitmap, true);
			if (bitmap != null && bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}

	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 如果点击的是最后一张图片，就调用dialog选取图片
		if (position == mList.size() - 1) {
			ChoicePicDialog picDialog = new ChoicePicDialog(this,
					getScreenHeight());
			picDialog.setOnChoicePicClickListener(this, 2);
			picDialog.show();
		} else {
			ImageView imageView = (ImageView) view
					.findViewById(R.id.item_upload_selected);
			ImageView mImageView = (ImageView) view
					.findViewById(R.id.item_grid_image);

			if (!itemSelected[position]) {
				AnimationUtil.addAnimation(imageView);
				imageView.setImageResource(R.drawable.upload_selected);
				mImageView.setImageAlpha(100);
				mImageView.setBackground(null);
				mList.get(position).setSelected(true);
				itemSelected[position] = true;
			} else {
				AnimationUtil.addAnimation(imageView);
				imageView.setImageResource(-1);
				mImageView.setImageAlpha(255);
				mList.get(position).setSelected(false);
				itemSelected[position] = false;
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (position == mList.size()) {
			ChoicePicDialog picDialog = new ChoicePicDialog(this,
					getScreenHeight());
			picDialog.setOnChoicePicClickListener(this, 2);
			picDialog.show();
		} else {
			DxCertificate dxCertificate = mList.get(position);
			Intent intent = new Intent(this, MyUploadPreviewActivity.class);
			intent.putExtra("dxCertificate", dxCertificate);
			startActivityForResultByPendingTransition(intent,
					Constants.REQUESTCODE_UPLOADAVATAR_PREVIEW);
		}
		// 在处理长按时，注意的细节是把onItemLongClick返回设置为true，
		// 否则长按是会执行setOnItemClickListener。
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_finish_network:
			getCertificate();
			break;

		default:
			break;
		}
	}

	@Override
	public void logoutOnClick(View v) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		switch (v.getId()) {
		case R.id.btn_logout_confirm:
			String urlString = UrlEngine.deleteCertificate(
					StrUtil.join(id, ","), count);
			count = 0;
			HttpUtil.post(urlString, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (statusCode == Constants.STAT_200) {
						getCertificate();
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
			break;
		case R.id.btn_logout_cancle:
			id.clear();
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
