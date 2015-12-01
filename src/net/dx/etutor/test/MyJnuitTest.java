package net.dx.etutor.test;

import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.util.FileUtil;
import android.test.AndroidTestCase;
import android.util.Log;

public class MyJnuitTest extends AndroidTestCase {
	public static String TAG = "LocationCityActivity";

	public void testAdd() throws Exception {
		MyDatabase database = EtutorApplication.getInstance().getMyDatabase();
//		database.getAll();
//		FileUtil.writeFile(database.getAll());
	}
}
