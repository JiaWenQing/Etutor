package net.dx.etutor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class StatisticDataUtil {
	private SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;

	public StatisticDataUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences("statistic_data",
				Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void clearSharePerference() {

	}

	/**
	 * 统计标记
	 * 
	 * @param b
	 */
	public void setFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.STATISTIC_FLAG, b);
		mEditor.commit();
	}

	/**
	 * 统计标记
	 * 
	 * @return
	 */
	public boolean getFlag() {
		return mSharedPreferences.getBoolean(Constants.STATISTIC_FLAG, false);
	}

	
	

	public void setVersionNumber(String versionNumber) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.STATISTIC_VERSION_NUMBER, versionNumber);
		mEditor.commit();
	}

	/**
	 * 默认值为当前版本
	 * 
	 * @return
	 */
	public String getVersionNumber() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.STATISTIC_VERSION_NUMBER,
				"1.2.0");
	}

	public void setFirstUseTime(long time) {
		mEditor.putLong(Constants.STATISTIC_FIRST_USETIME, time);
		mEditor.commit();
	}

	public long getFirstUseTime() {
		long time = System.currentTimeMillis();
		return mSharedPreferences.getLong(Constants.STATISTIC_FIRST_USETIME,
				time);
	}


	/**
	 * 重新统计
	 * 
	 * @param b
	 */
	public void setStatisticRestartFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.STATISTIC_RESTART_FLAG, b);
		mEditor.commit();
	}

	public boolean getStatisticRestartFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.STATISTIC_RESTART_FLAG,
				false);
	}


	public void setstartTime(String startTime) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.STATISTIC_START_TIME, startTime);
		mEditor.commit();
	}

	public void setendTime(String endTime) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.STATISTIC_END_TIME, endTime);
		mEditor.commit();
	}

	public String getstartTime() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.STATISTIC_START_TIME,
				null);
	}

	public String getendTime() {
		// TODO Auto-generated method stub
		return mSharedPreferences
				.getString(Constants.STATISTIC_END_TIME, null);
	}

	public void setEnterTime(long time) {
		mEditor.putLong(Constants.STATISTIC_ENTER_TIME, time);
		mEditor.commit();
	}

	public long getEnterTime() {
		long time = System.currentTimeMillis();
		return mSharedPreferences.getLong(Constants.STATISTIC_ENTER_TIME, time);
	}

	public void setStatisticId(long id) {
		// TODO Auto-generated method stub
		mEditor.putInt(Constants.STATISTIC_ID, (int)id);
		mEditor.commit();
	}

	public int getStatisticId() {
		return mSharedPreferences.getInt(Constants.STATISTIC_ID, -1);
	}

}
