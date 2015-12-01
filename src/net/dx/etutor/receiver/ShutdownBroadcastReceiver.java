package net.dx.etutor.receiver;

import net.dx.etutor.app.EtutorApplication;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 统计基本数据
 * 
 * @author app
 * 
 */
public class ShutdownBroadcastReceiver extends BroadcastReceiver {


	private static final String TAG = "BootCompletedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Shut down this system, ShutdownBroadcastReceiver onReceive()");
		if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			Log.i(TAG, "ShutdownBroadcastReceiver onReceive(), Do thing!"); 
			EtutorApplication.getInstance().getSdUtil()
			.setStatisticRestartFlag(true);
		}
	}
}
