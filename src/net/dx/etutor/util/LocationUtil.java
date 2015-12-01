package net.dx.etutor.util;

import java.util.HashMap;
import java.util.Map;
import net.dx.etutor.app.EtutorApplication;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtil {

	private static Map<String, Double> map = new HashMap<String, Double>();
	private static double latitude = 31.239004;// 纬度
	private static double longitude = 121.481647;// 经度

	/**
	 * latitude longitude
	 * 
	 * @return
	 */
	public static Map<String, Double> getLocation() {
		LocationManager locationManager = (LocationManager) EtutorApplication
				.getInstance().getSystemService("location");
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {

				}

				// Provider被disable时触发此函数，比如GPS被关闭
				@Override
				public void onProviderDisabled(String provider) {

				}

				// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
						latitude = location.getLatitude(); // 经度
						longitude = location.getLongitude(); // 纬度
					}
				}
			};
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度
				map.put("latitude", latitude);
				map.put("longitude", longitude);
				return map;
			}
		}
		return map;

	}

}
