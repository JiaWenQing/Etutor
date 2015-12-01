package net.dx.etutor.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.app.EtutorApplication;
import android.text.TextUtils;

public class DataParam {

	// public static String REMOTE_SERVE = "115.29.179.195:31837/AppServer/";
	 public static String REMOTE_SERVE_IP = "www.jiajiaook.com";
	 public static String REMOTE_SERVE =
	 "http://www.jiajiaook.com/AppServer/";
//	public static String REMOTE_SERVE_IP = "27.115.86.138";
//	public static String REMOTE_SERVE = "http://27.115.86.138:8080/AppServer/";
//	 public static String REMOTE_SERVE_IP = "10.0.6.232";
//	 public static String REMOTE_SERVE = "http://10.0.6.232:8080/AppServer/";
	// public static String REMOTE_SERVE_IP = "10.0.6.218";
	// public static String REMOTE_SERVE = "http://10.0.6.218:8080/AppServer/";
	private final static String CONNECTOR = "&";
	private final static String NET_PARAMS_FORMAT = "%s=%s";
	private final static String URL_FORMAT_PARAM = "%s%s.jspx?%s";
	private final static String URL_FORMAT_PARAM_EMPTY = "%s%s.jspx";
	private String mApi;
	private List<String> mParams = new ArrayList<String>();

	/*
	 * public static void setRemoteServerIP(String ip) { REMOTE_SERVE_IP = ip;
	 * REMOTE_SERVE = "http://" + REMOTE_SERVE_IP + ":318370/AppServer/"; }
	 */

	public void setApi(String api) {
		mApi = api;
		addParam("uuid", EtutorApplication.getInstance().getSpUtil().getUuid());
	}

	public void addParam(String key, Object value) {
		try {
			if (value != null) {
				value = URLEncoder.encode(value.toString(), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String netParam = String.format(NET_PARAMS_FORMAT, key, value);
		mParams.add(netParam);
	}

	public void addParam(String key, int value) {
		String netParam = String.format(NET_PARAMS_FORMAT, key, value + "");
		mParams.add(netParam);
	}

	public String getParams() {
		return TextUtils.join(CONNECTOR, mParams);
	}

	public String getUrlStr() {
		String urlString = "";
		String params = getParams();

		if (TextUtils.isEmpty(params)) {
			urlString = String.format(URL_FORMAT_PARAM_EMPTY, REMOTE_SERVE,
					mApi);
		} else {
			urlString = String.format(URL_FORMAT_PARAM, REMOTE_SERVE, mApi,
					params);
		}
		return urlString;
	}

}
