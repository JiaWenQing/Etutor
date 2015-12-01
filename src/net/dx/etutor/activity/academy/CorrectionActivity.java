package net.dx.etutor.activity.academy;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class CorrectionActivity extends BaseActivity implements OnClickListener {

	public static String TAG = "CorrectionActivity";
	private static EditText mEtAddress;
	private static EditText mEtPhone;
	private static EditText mEtWeb;
	private static EditText mEtMore;
	private static RadioButton mRbPublic;
	private static RadioButton mRbPrivate;
	private static RadioButton mRbOther;
	private LinearLayout mLLProperty;
	private Button mSubmit;
	private static int id;
	private static String address;
	private static String phone;
	private static String web;
	private static String property;
	private static String more;
	private static String userId;
	/**
	 * 纠错类型：0、学校 1、机构
	 */
	private static int type = 0;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_correction);
		setTitle(R.string.title_correction);
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		type = getIntent().getIntExtra("type", 0);
		id = getIntent().getIntExtra("id", -1);
		address=getIntent().getStringExtra("address");
		phone=getIntent().getStringExtra("phoneNumber");
		web=getIntent().getStringExtra("webSite");
		property=getIntent().getStringExtra("property");
		mLLProperty=(LinearLayout) findViewById(R.id.layout_school_property);
		mEtAddress = (EditText) findViewById(R.id.et_correction_address);
		mEtPhone = (EditText) findViewById(R.id.et_correction_phone);
		mEtWeb = (EditText) findViewById(R.id.et_correction_uri);
		mEtMore = (EditText) findViewById(R.id.et_correction_more);
		mRbPublic=(RadioButton) findViewById(R.id.rb_choose_public);
		mRbPrivate=(RadioButton) findViewById(R.id.rb_choose_private);
		mRbOther=(RadioButton) findViewById(R.id.rb_choose_other);
		mSubmit = (Button) findViewById(R.id.btn_submit);
		mEtAddress.setFocusable(true);   
		mEtAddress.setFocusableInTouchMode(true);   
		mEtAddress.requestFocus();  
	}

	@Override
	public void initEvents() {
		mSubmit.setOnClickListener(this);
		if (TextUtils.isEmpty(address)) {
			mEtAddress.setHint(R.string.text_perfection_adress);
		}else {
			mEtAddress.setText(address);
		}
		if (TextUtils.isEmpty(phone)) {
			mEtPhone.setHint(R.string.text_perfection_phone);
		}else {
			mEtPhone.setText(phone);
		}
		if (TextUtils.isEmpty(web)) {
			mEtWeb.setHint(R.string.text_perfection_web);
		}else {
			mEtWeb.setText(web);
		}
		if (type != 0) {
			mLLProperty.setVisibility(View.GONE);
		}else {
			mLLProperty.setVisibility(View.VISIBLE);
			switch (property) {
			case "公办":
				mRbPublic.setChecked(true);
				break;
			case "民办":
				mRbPrivate.setChecked(true);
				break;
			case "其他":
				mRbOther.setChecked(true);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			onSubmit();
			break;

		default:
			break;
		}
	}

	/**
	 * 提交纠错
	 */
	private void onSubmit() {
		// TODO Auto-generated method stub
		if (!NetWorkHelperUtil.checkNetState(this)) {
			showShortToast(R.string.network_error);
			return;
		}
		if (checkData()) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", id);
			map.put("userId", userId);
			map.put("type",type);
			map.put("address", address);
			map.put("phone", phone);
			map.put("web", web);
			map.put("property", property);
			map.put("more", more);
			String url=UrlEngine.correctionInfo(map);
			showLoadingDialog("请稍等…");
			HttpUtil.post(url, new JsonHttpResponseHandler(){

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
					if (statusCode==Constants.STAT_200) {
						dismissLoadingDialog();
						showLongToast("非常感谢您的帮助,我们会尽快核实！");
						CorrectionActivity.this.finish();
					}
				}
				
			});
		}
	}

	private boolean checkData() {
		boolean flag = false;
		address = mEtAddress.getText().toString().trim();
		phone = mEtPhone.getText().toString().trim();
		web = mEtWeb.getText().toString().trim();
		if (mRbPublic.isChecked()) {
			property = mRbPublic.getText().toString().trim();
		}else if(mRbPrivate.isChecked()) {
			property=mRbPrivate.getText().toString().trim();
		}else if(mRbOther.isChecked()){
			property=mRbOther.getText().toString().trim();
		}else {
			property="";
		}
		more = mEtMore.getText().toString().trim();
		if (type == 0) {
			if (TextUtils.isEmpty(address) && TextUtils.isEmpty(phone)
					&& TextUtils.isEmpty(web) && TextUtils.isEmpty(property)
					&& TextUtils.isEmpty(more)) {
				showShortToast("请输入你想纠正的内容");
				flag = false;
			} else {
				flag = true;
			}
		} else {
			if (TextUtils.isEmpty(address) && TextUtils.isEmpty(phone)
					&& TextUtils.isEmpty(web) && TextUtils.isEmpty(more)) {
				showShortToast("请输入你想纠正的内容");
				flag = false;
			} else {
				property="";
				flag = true;
			}
		}
		return flag;
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
