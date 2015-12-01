package net.dx.etutor.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import net.dx.etutor.activity.home.PersonalSettingActivity;
import net.dx.etutor.activity.order.OrderDetailActivity;
import net.dx.etutor.activity.register.LoginActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.DxOrder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class OrderReceiver extends BroadcastReceiver {

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			String str = printBundle(bundle);
			// 打开自定义的Activity
			int loginStatu = EtutorApplication.getInstance().getSpUtil()
					.getLoginStatu();
			Intent i;
			DxOrder dxOrder = new DxOrder();
			try {
				EtutorApplication.getInstance().getSpUtil()
						.isNewOrderFlag(true);
				JSONObject json = new JSONObject(str);
				dxOrder.initWithAttributes(json);
				if (loginStatu == 0) {
					i = new Intent(context, LoginActivity.class);
					i.putExtra("targetId", 11);
				} else {
					EtutorApplication.getInstance().getSpUtil()
							.setLoginStatu(3);
					i = new Intent(context, OrderDetailActivity.class);
				}
				i.putExtra("dxOrder", dxOrder);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);

				context.startActivity(i);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
		} else {

		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				sb.append(bundle.getString(key));
			}
			/*
			 * else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
			 * sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			 * } else if(){ sb.append("\nkey:" + key + ", value:" +
			 * bundle.getString(key)); }else { sb.append("\nkey:" + key +
			 * ", value:" + bundle.getString(key)); }
			 */
		}
		return sb.toString();

	}

	/*
	 * // send msg to MainActivity private void processCustomMessage(Context
	 * context, Bundle bundle) { if (MainActivity.isForeground) { String message
	 * = bundle.getString(JPushInterface.EXTRA_MESSAGE); String extras =
	 * bundle.getString(JPushInterface.EXTRA_EXTRA); Intent msgIntent = new
	 * Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	 * msgIntent.putExtra(MainActivity.KEY_MESSAGE, message); if
	 * (!ExampleUtil.isEmpty(extras)) { try { JSONObject extraJson = new
	 * JSONObject(extras); if (null != extraJson && extraJson.length() > 0) {
	 * msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras); } } catch
	 * (JSONException e) {
	 * 
	 * }
	 * 
	 * } context.sendBroadcast(msgIntent); } }
	 */
}
