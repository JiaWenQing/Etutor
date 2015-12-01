package net.dx.etutor.app;

import java.io.File;

import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.db.StatisticDao;
import net.dx.etutor.util.ExampleUtil;
import net.dx.etutor.util.Md5Util;
import net.dx.etutor.util.SharePreferenceUtil;
import net.dx.etutor.util.StatisticDataUtil;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class EtutorApplication extends Application {
	public static EtutorApplication mInstance;
	public String AppProvince;
	public String AppCity;
	public String AppRegion;
	public String Address;
	

	public static String filePath;
	public static String versionNumber = "2.1.1";
	public double latitude = 31.239004;// 纬度
	public double longitude = 121.481647;// 经度

	/**
	 * 定位SDK的核心类
	 */
	private static LocationClient mLocClient;
	private static GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private static LatLng mLatlng;
	/**
	 * 微信支付是否完成
	 */
	public boolean isWeiXinPay=false;//微信支付是否完成

	@Override
	public void onCreate() {
		SDKInitializer.initialize(this);
		mInstance = this;
		init();
	}

	private void init() {
		initImageLoader(getApplicationContext());
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);
		// 所谓保留最近的，意思是，如果有新的通知到达，之前列表里最老的那条会被移除。
		// 例如，设置为保留最近 5 条通知。假设已经有 5 条显示在通知栏，当第 6 条到达时，第 1 条将会被移除。
		JPushInterface.setLatestNotifactionNumber(this, 2);
		getMyDatabase();
		getStatisticDatabase();
	}

	public StatisticDao mStatisticDao;

	public StatisticDao getStatisticDatabase() {
		// TODO Auto-generated method stub
		if (mStatisticDao == null) {
			mStatisticDao = new StatisticDao(this);
		}
		return mStatisticDao;
	}

	/**
	 * 获取当前程序的版本号 .
	 */
	public String getVersionNumber() {
		versionNumber = ExampleUtil.GetVersion(getApplicationContext());
		return versionNumber;
	}

	/**
	 * @return
	 */
	public String getImei() {
		String device_id = null;
		device_id = ExampleUtil.getImei(getApplicationContext(), device_id);
		if (TextUtils.isEmpty(device_id)) {
			device_id = getDeviceInfo(getApplicationContext());
		}
		device_id = Md5Util.md52("jiajiaobao" + device_id);
		return device_id;
	}

	public String getDeviceInfo(Context context) {
		try {
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			return device_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"etutor/Cache");// 获取到缓存的目录地址
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	// 单例模式，才能及时返回数据
	SharePreferenceUtil mSpUtil;

	public SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null) {
			mSpUtil = new SharePreferenceUtil(this);
		}
		return mSpUtil;
	}

	// 单例模式，才能及时返回数据
	StatisticDataUtil mStatisticDataUtil;

	public StatisticDataUtil getSdUtil() {
		if (mStatisticDataUtil == null) {
			mStatisticDataUtil = new StatisticDataUtil(this);
		}
		return mStatisticDataUtil;
	}

	private boolean isFirstEnter=false;
	public MyDatabase database;

	public MyDatabase getMyDatabase() {
		if (database == null) {
			database = new MyDatabase(this);
		}
		return database;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param user
	 */
	public static EtutorApplication getInstance() {

		return mInstance;
	}

	/**
	 * 刷新位置
	 */
	public void reflushLocation() {
		isFirstEnter = getSpUtil().getFlag();
		mSearch = GeoCoder.newInstance();
		mLocClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(60000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null)
					return;
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				mLatlng = new LatLng(latitude, longitude);
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(mLatlng));
			}
		});

		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					AppProvince = "上海市";
					AppCity = "上海市";
					AppRegion = "请选择";
				} else {
					AppProvince = result.getAddressDetail().province;
					AppCity = result.getAddressDetail().city;
					AppRegion = result.getAddressDetail().district;
				}
				Address=result.getAddress();
				if (isFirstEnter) {
					getSpUtil().setLocationCity(AppProvince);
				}
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public String getAppProvince() {
		if (TextUtils.isEmpty(AppProvince)) {
			AppProvince = "请选择";
		}
		return AppProvince;
	}

	public void setAppProvince(String appProvince) {
		AppProvince = appProvince;
	}

	public String getAppCity() {
		if (TextUtils.isEmpty(AppCity)) {
			AppCity = "请选择";
		}
		return AppCity;
	}

	public void setAppCity(String appCity) {
		AppCity = appCity;
	}

	public String getAppRegion() {
		if (TextUtils.isEmpty(AppRegion)) {
			AppRegion = "请选择";
		}
		return AppRegion;
	}

	public void setAppRegion(String appRegion) {
		AppRegion = appRegion;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return Address;
	}

	public boolean isWeiXinPay() {
		return isWeiXinPay;
	}

	public void setWeiXinPay(boolean isWeiXinPay) {
		this.isWeiXinPay = isWeiXinPay;
	}

}
