package net.dx.etutor.activity.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public abstract class BasePopupWindow extends PopupWindow {

	protected View mContentView;

	public BasePopupWindow() {
		super();
	}

	public BasePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(Context context) {
		super(context);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(int width, int height) {
		super(width, height);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(View contentView) {
		super(contentView);
		initViews();
		initEvents();
		init();
	}

	public BasePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height, true);
		setBackgroundDrawable(new BitmapDrawable());
		mContentView = contentView;
		initViews();
		initEvents();
		init();
	}

	public abstract void initViews();

	public abstract void initEvents();

	public abstract void init();

	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	/**
	 * 显示在parent的上部并水平居中
	 * 
	 * @param parent
	 */
	public void showViewTopCenter(View parent) {
		showAtLocation(parent, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	/**
	 * 显示在parent的中心
	 * 
	 * @param parent
	 */
	public void showViewCenter(View parent) {
		showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	/**
	 * 显示在下方
	 * 
	 * @param parent
	 */
	public void showViewAsDropDown(View parent) {
		showAsDropDown(parent, 0, 1);
	}
}
