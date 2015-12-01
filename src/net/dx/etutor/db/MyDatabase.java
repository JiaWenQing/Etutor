package net.dx.etutor.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dx.etutor.model.DxStatistic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "city";
	private static final int DATABASE_VERSION = 2;

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * @param name
	 * @param flag
	 *            是否是直辖市
	 * @return
	 */
	public String getId(String name, boolean flag) {
		String id = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = { "id" };
		String sqlTables = "gb2260";
		String sqlSelection = String.format(Locale.US, "%s='%s'", "name", name);
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, null, null, null,
				null);
		if (cursor != null && cursor.moveToFirst()) {
			if (flag) {
				do {
					id = cursor.getString(0);
				} while (cursor.moveToNext());
			} else {
				id = cursor.getString(0);
			}
		}
		db.close();
		return id;

	}

	/**
	 * 查询科目
	 * 
	 * @return
	 */
	public List<String> getSubject() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String[] sqlSelect = { "id", "name" };
		String sqlTables = "subject";
		String sqlSelection = null;
		String sArgs[] = {};
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, sArgs, null,
				null, null);
		List<String> list = new ArrayList<String>();
		try {
			list.add("请选择");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String name = cursor.getString(1);
					list.add(name);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 查询科目子项
	 * 
	 * @param subjectId
	 * @return
	 */
	public List<String> getSubjectItem(String subjectId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String[] sqlSelect = { "id", "itemName" };
		String sqlTables = "subjectItem";
		String sqlSelection = "subjectId=?";
		String[] sArgs = { subjectId };
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, sArgs, null,
				null, null);
		List<String> list = new ArrayList<String>();
		try {
			list.add("请选择");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String name = cursor.getString(1);
					list.add(name);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	public List<String> getProvinces() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String[] sqlSelect = { "id", "name" };
		String sqlTables = "gb2260";
		String sqlSelection = " id%10000=0";
		String sArgs[] = {};
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, sArgs, null,
				null, null);
		List<String> list = new ArrayList<String>();
		try {
			list.add("请选择");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String id = cursor.getString(0);
					String name = cursor.getString(1);
					list.add(name);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return list;

	}

	public List<String> getCities(String provinceId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sqlSelection = " id % 100 = 0 and id-?<10000 and id-?>0 ";
		String[] sqlSelect = { "id", "name" };
		String[] sArgs = { provinceId, provinceId };
		String sqlTables = "gb2260";
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, sArgs, null,
				null, null);
		List<String> list = new ArrayList<String>();
		try {
			list.add("请选择");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String id = cursor.getString(0);
					String name = cursor.getString(1);
					list.add(name);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	public List<String> getRegions(String cityId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String s = cityId.substring(0, 4);
		String sqlSelection = " id like " + "'" + s + "%'"
				+ " and id-?<10000 and id-?>0 and id%100 != 0";
		String[] sqlSelect = { "id", "name" };
		String[] sArgs = { cityId, cityId };
		String sqlTables = "gb2260";
		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlSelection, sArgs, null,
				null, null);
		List<String> list = new ArrayList<String>();
		try {
			list.add("请选择");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String name = cursor.getString(1);
					list.add(name);
				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;

	}

}