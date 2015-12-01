package net.dx.etutor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatisticDatabase{

	private static final String DATABASE_NAME = "statistic.db";
	private static final int DATABASE_VERSION = 1;


	private SQLiteDatabase sDB;
	private DatabaseHelper dbHelper;
	private static StatisticDatabase myDB;
	private Context context;
	
	private StatisticDatabase(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		try {
			if (dbHelper==null) {
				dbHelper=new DatabaseHelper(context);
			}
			if (sDB==null) {
				sDB=dbHelper.getWritableDatabase();
			}
			if (sDB!=null&&sDB.isOpen()) {
				return;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("111", "GetWritableDatabase error.\n info=" + e.getMessage());
		}
		
	}
	public synchronized static StatisticDatabase getInstance(Context context) {
		if ( myDB== null) {
			myDB= new StatisticDatabase(context);
		}
		return myDB;
	}
	
	/**
	 * 获取DB对象。
	 * 
	 * @return
	 */
	public SQLiteDatabase getSQLiteDatabase() {
		return sDB;
	}

	/**
	 * 关闭数据库的操作
	 */
	public void close() {
		sDB.close();
		dbHelper.close();
	}

	@Override
	protected void finalize() throws Throwable {
		Log.i("111", "---DB close!");
		close();
		super.finalize();
	}

	/**
	 * 清除数据库中的数据 
	 */
	public void clearDataBase() {
		
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(StatisticTable.SQL_TABLE_CREATE);
		}
		
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("111", "DB Upgrade,oldversion=" + oldVersion + ",newVersion=" + newVersion);
		}
	}

}