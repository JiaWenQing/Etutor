package net.dx.etutor.db;

import java.util.ArrayList;
import java.util.Locale;

import net.dx.etutor.model.DxStatistic;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class StatisticDao implements StatisticTable {
	private static final String TAG = StatisticDao.class.getSimpleName();

	private SQLiteDatabase mDb;

	public StatisticDao(Context context) {
		mDb = StatisticDatabase.getInstance(context).getSQLiteDatabase();
		if (mDb == null) {
			Log.e(TAG, "init SQLiteDatabase failed");
		}
	}

	public long addDxStatistic(DxStatistic dxStatistic) {

		long id = -1;
		if (null == dxStatistic) {
			Log.w(TAG, "Add dxStatistic is null!");
			return id;
		}

		ContentValues values = new ContentValues();
		values.put(StatisticTable.START_TIME, dxStatistic.getStartTime());
		values.put(StatisticTable.END_TIME, dxStatistic.getEndTime());
		values.put(StatisticTable.UPLOAD_FLAG, dxStatistic.getUploadFlag());
		id = mDb.insert(StatisticTable.TABLE_NAME, null, values);
		return id;
	}

	public int delete(int id) {

		int delCount = 0;

		if (id < 0) {
			Log.w(TAG, "query mDxStatistic,id = " + id);
			return delCount;
		}

		String whereClause = String.format("%s=?", StatisticTable.ID);
		String[] whereArgs = new String[] { String.valueOf(id) };
		try {
			delCount = mDb.delete(StatisticTable.TABLE_NAME, whereClause,
					whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "delete mDxStatistic table error! id =" + delCount);
		}
		return delCount;
	}

	public int deleteAll() {
		int delCount = 0;
		try {
			delCount = mDb.delete(StatisticTable.TABLE_NAME, null, null);
		} catch (Exception e) {
			Log.e(TAG, "delete mDxStatistic table error! id =" + delCount);
		}
		return delCount;
	}
	
	public int deleteUploadAll(String[] ids) {
		int delCount = 0;
		try {
			delCount = mDb.delete(StatisticTable.TABLE_NAME, null, ids);
		} catch (Exception e) {
			Log.e(TAG, "delete mDxStatistic table error! id =" + delCount);
		}
		return delCount;
	}

	public DxStatistic query(int id) {

		if (id < 0) {
			Log.w(TAG, "query mDxStatistic,id = " + id);
			return null;
		}

		DxStatistic mDxStatistic = null;

		String selection = String.format(Locale.US, "%s='%s'",
				StatisticTable.ID, id);

		Cursor cursor = null;
		try {
			cursor = mDb.query(StatisticTable.TABLE_NAME, null, selection,
					null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {

				int statisticId = cursor.getInt(StatisticTable.INDEX_ID);
				String startTime = cursor
						.getString(StatisticTable.INDEX_START_TIME);
				String endTime = cursor
						.getString(StatisticTable.INDEX_END_TIME);
				int uploadFlag = cursor
						.getInt(StatisticTable.INDEX_UPLOAD_FLAG);

				mDxStatistic = new DxStatistic();
				mDxStatistic.setStatisticId(statisticId);
				mDxStatistic.setStartTime(startTime);
				mDxStatistic.setEndTime(endTime);
				mDxStatistic.setUploadFlag(uploadFlag);
			}
		} catch (SQLiteException e) {
			Log.e(TAG, "---Query failed!" + e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return mDxStatistic;
	}

	public ArrayList<DxStatistic> queryUploadDataAll() {

		ArrayList<DxStatistic> mDxStatisticList = new ArrayList<DxStatistic>();

		String order = StatisticTable.ID + " ASC";
		String selection = "uploadFlag=?";
		String[] selectionArgs = { "1" };
		Cursor cursor = null;
		try {
			cursor = mDb.query(StatisticTable.TABLE_NAME, null, selection,
					selectionArgs, null, null, order);
			if (cursor != null && cursor.moveToFirst()) {

				do {
					int id = cursor.getInt(StatisticTable.INDEX_ID);
					int statisticId = cursor.getInt(StatisticTable.INDEX_ID);
					String startTime = cursor
							.getString(StatisticTable.INDEX_START_TIME);
					String endTime = cursor
							.getString(StatisticTable.INDEX_END_TIME);
					int uploadFlag = cursor
							.getInt(StatisticTable.INDEX_UPLOAD_FLAG);

					DxStatistic mDxStatistic = new DxStatistic();
					mDxStatistic.setStatisticId(statisticId);
					mDxStatistic.setStartTime(startTime);
					mDxStatistic.setEndTime(endTime);
					mDxStatistic.setUploadFlag(uploadFlag);
					mDxStatisticList.add(mDxStatistic);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e(TAG, "Query all mDxStatistic failed!" + e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return mDxStatisticList;
	}

	public int updateFlagAll() {
		int updateCount = 0;
		ContentValues values = new ContentValues();
		values.put(StatisticTable.UPLOAD_FLAG, 1);
		try {
			updateCount = mDb.update(StatisticTable.TABLE_NAME, values, null,
					null);
		} catch (Exception e) {
			Log.e(TAG, "update mDxStatistic table error! count = "
					+ updateCount);
		}
		return updateCount;
	}

	public int update(int id, DxStatistic dxStatistic) {
		int updateCount = 0;

		if (id < 0 || (dxStatistic == null)) {
			Log.w(TAG, "update mDxStatistic,id = " + id);
			return updateCount;
		}

		String whereClause = String.format("%s=?", StatisticTable.ID);
		String[] whereArgs = new String[] { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(StatisticTable.END_TIME, dxStatistic.getEndTime());
		values.put(StatisticTable.UPLOAD_FLAG, dxStatistic.getUploadFlag());
		try {
			updateCount = mDb.update(StatisticTable.TABLE_NAME, values,
					whereClause, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "update mDxStatistic table error! count = "
					+ updateCount);
		}
		return updateCount;
	}

}
