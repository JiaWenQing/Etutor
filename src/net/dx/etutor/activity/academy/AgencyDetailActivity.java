package net.dx.etutor.activity.academy;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxAgencyinfo;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class AgencyDetailActivity extends BaseActivity implements
		OnClickListener {
	public static String TAG = "AgencyDetailActivity";
	private DxAgencyinfo agency;
	private ImageView mAgencyIcon;
	private TextView mAgencyName;
	private TextView mAgencyAddress;
	private TextView mAgencyFeature;
	private TextView mAgencyIntro;
	private TextView mAgencyPhone;
	private TextView mAgencyWeb;

	private String agencyAddress;
	private String name;
	private String introduce;
	private String imageUrl;
	private String webSite;
	private String phoneNumber;
	private String feature;
	private String classity;

	private double latitude;
	private double longitude;
	private int loginStatu;
	private TextView mAgencyClassity;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_agency_detail);
		setIcon(R.drawable.selector_button_correction);
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		agency = (DxAgencyinfo) getIntent().getSerializableExtra("agency");
		setTitle(agency.getName().toString());
		mAgencyIcon = (ImageView) findViewById(R.id.imv_agency_icon);
		mAgencyName = (TextView) findViewById(R.id.tv_agency_name);
		mAgencyFeature = (TextView) findViewById(R.id.tv_agency_feature);
		mAgencyIntro = (TextView) findViewById(R.id.tv_agency_intro);

		mAgencyAddress = (TextView) findViewById(R.id.tv_agency_location);
		mAgencyPhone = (TextView) findViewById(R.id.tv_agency_phone);
		mAgencyWeb = (TextView) findViewById(R.id.tv_agency_web_site);
		mAgencyClassity = (TextView) findViewById(R.id.tv_agency_classity);
		mAgencyIcon.setMaxHeight(BaseActivity.getScreenWidth() * 3 / 4);
		mAgencyIcon.setMinimumHeight(BaseActivity.getScreenWidth() * 3 / 4);
		initData();
	}

	private void initData() {
		imageUrl = agency.getImageUrl();
		name = agency.getName();
		agencyAddress = agency.getAddress();
		phoneNumber = agency.getPhoneNumber();
		webSite = agency.getWebSite();
		introduce = agency.getIntroduce();
		feature = agency.getFeature();
		classity = agency.getTypes();
		latitude = agency.getLatitude();
		longitude = agency.getLongitude();
		if (!TextUtils.isEmpty(imageUrl)) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + imageUrl, mAgencyIcon,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mAgencyIcon.setImageResource(R.drawable.icon_home_agency);
		}
		if (TextUtils.isEmpty(name)) {
			name = "暂无";
		}
		if (TextUtils.isEmpty(agencyAddress)) {
			agencyAddress = "暂无";
		}
		if (TextUtils.isEmpty(phoneNumber)) {
			phoneNumber = "";
		}
		if (TextUtils.isEmpty(webSite)) {
			webSite = "";
		}
		if (TextUtils.isEmpty(introduce)) {
			introduce = "无";
		}
		if (TextUtils.isEmpty(feature)) {
			feature = "无";
		}
		if (TextUtils.isEmpty(classity)) {
			classity = "";
		}
		mAgencyName.setText(name);
		mAgencyPhone.setText(phoneNumber);
		mAgencyWeb.setText(webSite);
		mAgencyAddress.setText(agencyAddress);
		mAgencyFeature.setText(feature);
		mAgencyIntro.setText(introduce);
		mAgencyClassity.setText(classity);
	}

	@Override
	public void initEvents() {
		mAgencyAddress.setOnClickListener(this);
		mAgencyName.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_agency_location:
			if (latitude != 0 && longitude != 0) {
				onAgencyLocation();
			}
			break;
		case R.id.tv_agency_name:
			iconClick();
			break;
		default:
			break;
		}
	}

	private void onAgencyLocation() {
		// TODO Auto-generated method stub 121.396739,31.239294
		Intent intent = new Intent(this, LocationActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("address", agencyAddress);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		this.startActivity(intent);
	}

	@Override
	public void iconClick() {
		// TODO Auto-generated method stub
		super.iconClick();
		Intent intent;
		if (loginStatu == 0) {
			intent = new Intent(AgencyDetailActivity.this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		} else {
			intent = new Intent(AgencyDetailActivity.this,
					CorrectionActivity.class);
			intent.putExtra("type", 1);
			intent.putExtra("id", agency.getId());
			intent.putExtra("address", agencyAddress);
			intent.putExtra("webSite", webSite);
			intent.putExtra("phoneNumber", phoneNumber);
			intent.putExtra("property", "");
			startActivityByPendingTransition(intent);
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			// 发帖成功，在这里刷新数据
			switch (requestCode) {
			case 1000:
				loginStatu = mApplication.getSpUtil().getLoginStatu();
				if (loginStatu != 0) {
					Intent intent = new Intent(AgencyDetailActivity.this,
							CorrectionActivity.class);
					intent.putExtra("type", 1);
					intent.putExtra("id", agency.getId());
					intent.putExtra("address", agencyAddress);
					intent.putExtra("webSite", webSite);
					intent.putExtra("phoneNumber", phoneNumber);
					intent.putExtra("property", "");
					startActivityByPendingTransition(intent);
				}
				break;
			}
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
