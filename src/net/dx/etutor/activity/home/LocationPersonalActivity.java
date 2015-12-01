package net.dx.etutor.activity.home;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.LocationPersonalAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.model.LocationBean;
import net.dx.etutor.util.BaiduMapUtilByRacer;
import net.dx.etutor.util.BaiduMapUtilByRacer.GeoCodePoiListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("ClickableViewAccessibility")
public class LocationPersonalActivity extends BaseActivity implements
		OnItemClickListener, OnGetGeoCoderResultListener {

	public static String TAG = "LocationPersonalActivity";
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private Marker mMarker = null;
	private MapView mMapView;
	private BaiduMap mBaiduMap = null;
	private ListView mListView;

	private List<PoiInfo> mList = new ArrayList<PoiInfo>();
	private LocationPersonalAdapter mAdapter;

	private LatLng mLatlng;
	public double latitude = 31.239004;// 纬度
	public double longitude = 121.481647;// 经度
	private ImageView mLocationHome;
	private TextView mLocationAddress;
	
	private RelativeLayout layout = null;
	private TextView mInfo;
	private ImageView mMark;
	private View viewCache;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_personal_location);
		setTitle("地点选择");

		latitude = mApplication.latitude;
		longitude = mApplication.longitude;
		mLatlng = new LatLng(latitude, longitude);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mLocationHome = (ImageView) findViewById(R.id.imv_location_home);
		mLocationAddress = (TextView) findViewById(R.id.tv_mark_info);
		mListView = (ListView) findViewById(R.id.lv_address);

		mLocationAddress.setVisibility(View.GONE);

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
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
		mSearch = GeoCoder.newInstance();
		mAdapter = new LocationPersonalAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mMarker = BaiduMapUtilByRacer.showMarkerByResource(latitude, longitude,
				R.drawable.icon_mark_blue, mBaiduMap, 0, true);
		
		viewCache = LayoutInflater.from(this)
				.inflate(R.layout.pop_layout, null);
		mInfo = (TextView) viewCache.findViewById(R.id.tv_mark_info);
		mMark = (ImageView) viewCache.findViewById(R.id.imv_mark);
		layout = (RelativeLayout) viewCache.findViewById(R.id.mark_layout);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mLatlng));
		mListView.setOnItemClickListener(this);
		mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
		mLocationHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMarker != null) {
					mMarker.remove();
				} else {
					mBaiduMap.clear();
				}
				mMarker = BaiduMapUtilByRacer.showMarkerByResource(mApplication.latitude,
						mApplication.longitude, R.drawable.icon_mark_blue, mBaiduMap, 0,
						true);
			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				InfoWindow mInfoWindow = null;
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				// 设置详细信息布局为可见
				layout.setVisibility(View.VISIBLE);
				return true;
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener()
		{

			@Override
			public boolean onMapPoiClick(MapPoi arg0)
			{
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0)
			{
				layout.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();

			}
		});

	}

	OnMapStatusChangeListener mapStatusChangeListener = new OnMapStatusChangeListener() {
		/**
		 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
		 * 
		 * @param status
		 *            地图状态改变开始时的地图状态
		 */
		public void onMapStatusChangeStart(MapStatus status) {
		}

		/**s
		 * 地图状态变化中
		 * 
		 * @param status
		 *            当前地图状态
		 */
		public void onMapStatusChange(MapStatus status) {
		}

		/**
		 * 地图状态改变结束
		 * 
		 * @param status
		 *            地图状态改变结束后的地图状态
		 */
		public void onMapStatusChangeFinish(MapStatus status) {
			LatLng ptCenter = new LatLng(status.target.latitude,
					status.target.longitude);
			// 反Geo搜索
			reverseGeoCode(ptCenter, true);
		}
	};
	protected LocationBean mLocationBean;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PersonalSettingActivity.class);
		intent.putExtra("address", mList.get(position).address);
		intent.putExtra("latitude", mList.get(position).location.latitude);
		intent.putExtra("longitude", mList.get(position).location.longitude);
		setResult(RESULT_OK, intent);
		finish();
	}

	protected void reverseGeoCode(LatLng ptCenter, final boolean isShowTextView) {
		// TODO Auto-generated method stub
		BaiduMapUtilByRacer.getPoisByGeoCode(ptCenter.latitude,
				ptCenter.longitude, new GeoCodePoiListener() {

					@Override
					public void onGetSucceed(LocationBean locationBean,
							List<PoiInfo> poiList) {
						mLocationBean = (LocationBean) locationBean.clone();
						// mBaiduMap.setMapStatus(MapStatusUpdateFactory
						// .newLatLng(new LatLng(locationBean
						// .getLatitude(), locationBean
						// .getLongitude())));
						if (mList == null) {
							mList = new ArrayList<PoiInfo>();
						}
						mList.clear();
						if (poiList != null) {
							mList.addAll(poiList);
						} else {
							showShortToast("抱歉，未能找到结果");
						}
						mAdapter.setPoiList(mList);
						if (mList.size() > 0) {
							mLocationAddress.setVisibility(View.VISIBLE);
							mLocationAddress.setText(mList.get(0).name);
						} else {
							mLocationAddress.setVisibility(View.GONE);
						}
					}

					@Override
					public void onGetFailed() {
						showShortToast("抱歉，未能找到结果");
					}
				});
	}

	@Override
	protected void onDestroy() {
		mMarker = null;
		mSearch.destroy();
		mLocationBean = null;
		if (mBaiduMap != null) {
			mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
			mBaiduMap = null;
		}
		if (mMapView != null) {
			mMapView.destroyDrawingCache();
			mMapView.onDestroy();
			mMapView = null;
		}
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

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			showShortToast("抱歉，未能找到结果");
		} else {
			mList = result.getPoiList();
			mAdapter.setPoiList(mList);
			if (mList.size() > 0) {
				mLocationAddress.setVisibility(View.VISIBLE);
				mLocationAddress.setText(mList.get(0).name);
			} else {
				mLocationAddress.setVisibility(View.GONE);
			
			}
		}
	}

}
