package net.dx.etutor.util;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author michaeljin
 * 
 */
@SuppressLint("DefaultLocale")
public class UiUtil {

	/**
	 * 弹出键盘时间(毫秒).
	 */
	public static final int KEYBOARD_WAIT_TIME = 200;

	/**
	 * 根据手机的分辨率从dp的单位转成为px（像素）.
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从px（像素）的单位转成为dp.
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据字体大小计算字符串显示大�?
	 * 
	 * @param textSize
	 *            字体大小
	 * @param text
	 *            字符�? * @return 字符串相视大�?
	 */
	public static Rect getTextBounds(float textSize, String text) {
		final Rect rect = new Rect();
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		paint.getTextBounds(text, 0, text.length(), rect);

		return rect;
	}

	/**
	 * 根据字体大小计算字符串显示大�?
	 * 
	 * @param textSize
	 *            字体大小
	 * @param text
	 *            字符�? * @return 字符串相视大�?
	 */
	public static Rect getTextBounds(Typeface typeface, float textSize,
			String text) {
		final Rect rect = new Rect();
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		paint.setTextSize(textSize);
		paint.getTextBounds(text, 0, text.length(), rect);

		return rect;
	}

	/**
	 * 根据字体大小计算字符串显示大�?
	 * 
	 * @param textSize
	 *            字体大小
	 * @param text
	 *            字符�? * @return 字符串相视大�?
	 */
	public static Rect getTextBounds(Typeface typeface, float textSize,
			String text, int start, int end) {
		final Rect rect = new Rect();
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		paint.setTextSize(textSize);
		paint.getTextBounds(text, start, end, rect);

		return rect;
	}

	/**
	 * 获取屏幕宽度.
	 * 
	 * @param mContext
	 *            上下�? * @return 屏幕宽度
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context mContext) {
		int width;
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Service.WINDOW_SERVICE);
		if (VERSION.SDK_INT <= 12) {
			width = windowManager.getDefaultDisplay().getWidth();
		} else {
			Point point = new Point();
			windowManager.getDefaultDisplay().getSize(point);
			width = point.x;
		}

		return width;
	}

	/**
	 * 获取屏幕宽度.
	 * 
	 * @param mContext
	 *            上下�? * @return 屏幕高度
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		int height;
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Service.WINDOW_SERVICE);
		if (VERSION.SDK_INT <= 12) {
			height = windowManager.getDefaultDisplay().getHeight();
		} else {
			windowManager.getDefaultDisplay().getRealMetrics(dm);
			height = dm.heightPixels;
		}

		return height;
	}

	/**
	 * 保留两位小数.
	 * 
	 * @param f
	 *            f
	 * @return f
	 */
	public static float getTwoDecimal(float f) {
		return (float) (Math.round(f * 100)) / 100;
	}

	public static String getSubString(String string) {
		if (!TextUtils.isEmpty(string)) {
			if (string.length() <= 6) {
				return string;
			} else {
				return string.substring(string.length() - 6);
			}
		}
		return "";
	}

	public static String getFormatStringOfFloat(float f) {
		DecimalFormat a = new DecimalFormat("##.##");
		String intString = a.format(f);
		return intString;
	}

	public static void setSmallFilter(EditText mEditText) {
		InputFilter mSmallFilter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) {
				String value = arg0.toString();
				value = value.toLowerCase();
				return value;
			}
		};
		mEditText.setFilters(new InputFilter[] { mSmallFilter });
	}

	public static Dialog createAlertDialog(Context context, String msg,
			String okStr, String cancelStr,
			DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		builder.setNegativeButton(cancelStr, cancelListener);
		dialog = builder.create();

		return dialog;
	}

	public static Dialog createAlertDialog(Context context, int msg, int okStr,
			int cancelStr, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		builder.setNegativeButton(cancelStr, cancelListener);
		dialog = builder.create();

		return dialog;
	}

	public static Dialog createAlertDialog(Context context, int title, int msg,
			int okStr, int cancelStr,
			DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		builder.setNegativeButton(cancelStr, cancelListener);
		dialog = builder.create();

		return dialog;
	}

	// 方法重载
	public static Dialog createAlertDialog(Context context, int title, int msg,
			int okStr, int cancelStr, DialogInterface.OnClickListener okListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		builder.setNegativeButton(cancelStr, okListener);
		dialog = builder.create();
		return dialog;
	}

	public static Dialog createAlertDialog(Context context, int title, int msg,
			int okStr, DialogInterface.OnClickListener okListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		dialog = builder.create();

		return dialog;
	}

	public static Dialog createAlertDialog(Context context, int title,
			String msg, int okStr, DialogInterface.OnClickListener okListener) {
		Dialog dialog = null;

		Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(okStr, okListener);
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 隐藏键盘.
	 */
	public static void hiddenKeyBoard(Context context, View view) {
		view.clearFocus();
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 隐藏键盘.
	 */
	public static void hiddenKeyBoard(Activity activity) {
		View view = activity.getCurrentFocus();

		if (view != null) {
			view.clearFocus();

			((InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 隐藏键盘.
	 */
	public static void hiddenKeyBoard(Dialog dialog) {
		View view = dialog.getCurrentFocus();

		if (view != null) {
			view.clearFocus();

			((InputMethodManager) dialog.getContext().getSystemService(
					Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 显示键盘.
	 */
	public static void showKeyBoard(Context context, View view) {
		final Context fContext = context;
		final View fView = view;
		fView.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) fContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(fView, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}

		}, KEYBOARD_WAIT_TIME);
	}

	public static boolean isKeyBoardActive(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive(view); // isOpen若返回true，则表示输入法打�?
	}

	public static void showMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showMessage(Context context, int resId) {
		String message = context.getResources().getString(resId);
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void changeCautionNumber(Context context, TextView caution,
			ViewGroup.LayoutParams lp, int count) {
		if (count <= 0) {
			caution.setVisibility(View.GONE);
		} else {
			caution.setVisibility(View.VISIBLE);
			String sCount = count + "";
			if (sCount.length() == 1) {
				caution.setText(sCount);
				lp.width = UiUtil.dip2px(context, 20);
			} else if (sCount.length() == 2) {
				caution.setText(sCount);
				lp.width = UiUtil.dip2px(context, 30);
			} else {
				lp.width = UiUtil.dip2px(context, 30);
				caution.setText("99+");
			}
			caution.setLayoutParams(lp);
		}
	}

	public static void setIcon(Context context, int resId, TextView view) {
		Drawable drawable = context.getResources().getDrawable(resId);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		view.setCompoundDrawables(null, null, drawable, null);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 设置TextView的图标
	 */
	public static void setIcon(Activity activity, TextView view, int resId) {
		Drawable drawable = activity.getResources().getDrawable(resId);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		view.setCompoundDrawables(null, null, drawable, null);
	}
}
