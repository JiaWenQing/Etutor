package net.dx.etutor.receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.db.StatisticDao;
import net.dx.etutor.model.DxStatistic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.NetWorkHelperUtil;
import net.dx.etutor.util.SharePreferenceUtil;
import net.dx.etutor.util.StatisticDataUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 统计基本数据
 * 
 * @author app
 * 
 */
public class StatisticReceiver extends BroadcastReceiver {

	public static final String ACTION = "net.dx.etutor.receiver.StatisticReceiver";
	protected StatisticDataUtil mStatisticDataUtil;
	public int[] ids;
	public StatisticDao dao;
	public ArrayList<DxStatistic> arrayList;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ACTION.equals(intent.getAction())) {
			EtutorApplication.getInstance().getSdUtil()
					.setFirstUseTime(System.currentTimeMillis());
			String time = DateUtil.getStringByFormat(
					System.currentTimeMillis(), DateUtil.dateFormatYMDHMS);
			uploadStatisticInfo(context);
		}
	}

	private void uploadStatisticInfo(Context context) {
		// TODO Auto-generated method stub
		Map<String, Object> map = getStatisticInfo();
		if (NetWorkHelperUtil.checkNetState(context)) {
			if ((boolean) map.get("flag")
					&& !TextUtils.isEmpty((String) map.get("jstr"))) {
				String url = UrlEngine.uploadStatisticInfo(map);
				mStatisticDataUtil = EtutorApplication.getInstance()
						.getSdUtil();
				HttpUtil.post(url, new JsonHttpResponseHandler() {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (statusCode == Constants.STAT_200) {
							if (response.toString().equals(Constants.NEW_USER)) {
								mStatisticDataUtil
										.setVersionNumber(EtutorApplication
												.getInstance()
												.getVersionNumber());
								for (int i = 0; i < ids.length; i++) {
									dao.delete(ids[i]);
								}
							}
						}
					}
				});
			}
		}

	}

	private Map<String, Object> getStatisticInfo() {
		// TODO Auto-generated method stub
		dao = EtutorApplication.getInstance().getStatisticDatabase();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = true;
		String jstr = "";
		arrayList = new ArrayList<DxStatistic>();
		arrayList = dao.queryUploadDataAll();
		if (arrayList.size() == 0) {
			flag = false;
		} else {
			ids = new int[arrayList.size()];
			for (int i = 0; i < arrayList.size(); i++) {
				ids[i] = arrayList.get(i).getStatisticId();
				jstr = jstr + arrayList.get(i).toString() + ",";
			}
			jstr = "[" + jstr.substring(0, jstr.length() - 1) + "]";
		}
		map.put("jstr", jstr);
		map.put("flag", flag);
		return map;
	}
}
