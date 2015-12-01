package net.dx.etutor.activity.academy;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.umeng.analytics.MobclickAgent;

public class LocationActivity extends BaseActivity implements
		OnGetGeoCoderResultListener {

	public static String TAG = "LocationActivity";
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	BaiduMap mBaiduMap = null;
	MapView mMapView = null;

	private RelativeLayout layout = null;
	private TextView mInfo;
	private ImageView mMark;
	private View viewCache;
	LatLng latLng;
	double latitude;
	double longitude;

	String name = null;
	String address;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_geocoder);
		name = getIntent().getStringExtra("name");
		if (TextUtils.isEmpty(name)) {
			name = "位置";
		}
		address = getIntent().getStringExtra("address");
		setTitle(name);
		latitude = getIntent().getDoubleExtra("latitude", 31.239294);
		longitude = getIntent().getDoubleExtra("longitude", 121.396739);
		latLng = new LatLng(latitude, longitude);
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		// mapOptions = new BaiduMapOptions();
		// mapOptions.scaleControlEnabled(false); // 隐藏比例尺控件
		// mapOptions.zoomControlsEnabled(false);// 隐藏缩放按钮
		// mMapView = new MapView(this, mapOptions);// 获取地图控件引用
		mMapView.removeViewAt(1);
		int childCount = mMapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				zoom = child;
				break;
			}
		}
		zoom.setVisibility(View.GONE);

		// setContentView(mMapView);
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, 65);
		// View view = LayoutInflater.from(this).inflate(R.layout.main_head_bar,
		// null);// 自己的xml
		// this.addContentView(view, params);

		// 生成一个TextView用户在地图中显示InfoWindow
		// location = new TextView(getApplicationContext());
		// location.setBackgroundResource(R.drawable.icon_mark);
		// location.setPadding(30, 20, 30, 50);
		// location.setText(address);
		mBaiduMap = mMapView.getMap();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();

		viewCache = LayoutInflater.from(this)
				.inflate(R.layout.pop_layout, null);
		mInfo = (TextView) viewCache.findViewById(R.id.tv_mark_info);
		mMark = (ImageView) viewCache.findViewById(R.id.imv_mark);
		layout = (RelativeLayout) viewCache.findViewById(R.id.mark_layout);

		mInfo.setText(address);
	}

	// TextView location = null;
	int i = 0;
	LatLng llInfo;

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		// mSearch.geocode(new GeoCodeOption().city("").address(address));
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				// TODO Auto-generated method stub
				if (i % 2 != 0) {
					marker.setIcon(BitmapDescriptorFactory.fromView(layout));
				} else {
					marker.setIcon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_mark_bg));
				}
				i++;
				return true;

			}
		});

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
		}
		mBaiduMap.clear();
		MarkerOptions marker = new MarkerOptions();
		marker.position(result.getLocation());
		mBaiduMap.addOverlay(marker.icon(BitmapDescriptorFactory
				.fromView(layout)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		latitude = result.getLocation().latitude;
		longitude = result.getLocation().longitude;
		latLng = result.getLocation();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
		}
		mBaiduMap.clear();
		MarkerOptions marker = new MarkerOptions();
		marker.position(result.getLocation());
		mBaiduMap.addOverlay(marker.icon(BitmapDescriptorFactory
				.fromView(layout)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		// mBaiduMap.addOverlay(new
		// MarkerOptions().position(result.getLocation())
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_mark)));
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
		// .getLocation()));

	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

}