package net.dx.etutor.activity.academy;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxSchoolinfo;
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

public class SchoolDetailActivity extends BaseActivity implements
		OnClickListener {

	public static String TAG = "SchoolDetailActivity";
	private DxSchoolinfo school;
	private ImageView mSchoolIcon;
	private TextView mSchoolName;
	private TextView mSchoolAddress;
	private TextView mSchoolFeature;
	private TextView mSchoolIntro;
	private TextView mSchoolScope;
	private TextView mSchoolPhone;
	private TextView mSchoolWeb;
	private TextView mSchoolCategory;

	private String schoolAddress;
	private String name;
	private String introduce;
	private String imageUrl;
	private String type;
	private String category;
	private String phoneNumber;
	private String webSite;
	private String characteristic;// 特色
	private String range;// 范围

	private double latitude;
	private double longitude;
	private int loginStatu;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_school_detail);
		setIcon(R.drawable.selector_button_correction);
		loginStatu = mApplication.getSpUtil().getLoginStatu();
		school = (DxSchoolinfo) getIntent().getSerializableExtra("school");
		setTitle(school.getName().toString());
		mSchoolIcon = (ImageView) findViewById(R.id.imv_school_icon);
		mSchoolName = (TextView) findViewById(R.id.tv_school_name);
		mSchoolAddress = (TextView) findViewById(R.id.tv_school_location);
		mSchoolPhone = (TextView) findViewById(R.id.tv_school_phone);
		mSchoolWeb = (TextView) findViewById(R.id.tv_school_web_site);
		mSchoolCategory = (TextView) findViewById(R.id.tv_school_category);
		mSchoolFeature = (TextView) findViewById(R.id.tv_school_feature);
		mSchoolIntro = (TextView) findViewById(R.id.tv_school_intro);
		mSchoolScope = (TextView) findViewById(R.id.tv_school_scope);
		mSchoolIcon.setMaxHeight(BaseActivity.getScreenWidth() * 3 / 4);
		mSchoolIcon.setMinimumHeight(BaseActivity.getScreenWidth() * 3 / 4);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		imageUrl = school.getImageUrl();
		name = school.getName();
		schoolAddress = school.getAddress();
		phoneNumber = school.getPhoneNumber();
		webSite = school.getWebSite();
		type = school.getType();
		category = school.getCategory();
		characteristic = school.getCharacteristic();
		introduce = school.getIntroduce();
		range = school.getRange();
		latitude = school.getLatitude();
		longitude = school.getLongitude();
		if (!TextUtils.isEmpty(imageUrl)) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + imageUrl, mSchoolIcon,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mSchoolIcon.setImageResource(R.drawable.icon_home_school);
		}
		if (TextUtils.isEmpty(name)) {
			name = "暂无";
		}
		if (TextUtils.isEmpty(schoolAddress)) {
			schoolAddress = "暂无";
		}
		if (TextUtils.isEmpty(phoneNumber)) {
			phoneNumber = "";
		}
		if (TextUtils.isEmpty(webSite)) {
			webSite = "";
		}
		if (TextUtils.isEmpty(category)
				|| (!category.equals("公办") && !category.equals("民办"))) {
			category = "其他";
		}
		if (TextUtils.isEmpty(characteristic)) {
			characteristic = "无";
		}
		if (TextUtils.isEmpty(introduce)) {
			introduce = "无";
		}
		if (TextUtils.isEmpty(range)) {
			range = "无";
		}
		mSchoolName.setText(name);
		mSchoolPhone.setText(phoneNumber);
		mSchoolWeb.setText(webSite);
		mSchoolAddress.setText(schoolAddress);
		mSchoolCategory.setText(category.toString());
		mSchoolFeature.setText(characteristic);
		mSchoolIntro.setText(introduce);
		mSchoolScope.setText(range);

	}

	@Override
	public void initEvents() {
		mSchoolAddress.setOnClickListener(this);
		mSchoolName.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_school_location:
			if (!TextUtils.isEmpty(schoolAddress) && !schoolAddress.equals("无")) {
				onSchoolLocation();
			}
			break;
		case R.id.tv_school_name:
			iconClick();
			break;
		default:
			break;
		}
	}

	private void onSchoolLocation() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SchoolDetailActivity.this,
				LocationActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("address", schoolAddress);
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
			intent = new Intent(SchoolDetailActivity.this, LoginActivity.class);
			startActivityForResultByPendingTransition(intent, 1000);
		} else {
			intent = new Intent(SchoolDetailActivity.this,
					CorrectionActivity.class);
			intent.putExtra("type", 0);
			intent.putExtra("id", school.getId());
			intent.putExtra("address", schoolAddress);
			intent.putExtra("webSite", webSite);
			intent.putExtra("phoneNumber", phoneNumber);
			intent.putExtra("property", category);
			startActivityByPendingTransition(intent);
		}
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
					Intent intent = new Intent(SchoolDetailActivity.this,
							CorrectionActivity.class);
					intent.putExtra("type", 0);
					intent.putExtra("id", school.getId());
					intent.putExtra("address", schoolAddress);
					intent.putExtra("webSite", webSite);
					intent.putExtra("phoneNumber", phoneNumber);
					intent.putExtra("property", category);
					startActivityByPendingTransition(intent);
				}
				break;
			}
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		loginStatu = mApplication.getSpUtil().getLoginStatu();
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
