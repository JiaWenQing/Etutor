package net.dx.etutor.widget;

import net.dx.etutor.util.UiUtil;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 扩展EditText.
 */
public class CustomEditText extends EditText {

	private Context mContext;
	private Drawable mRight;
	private Rect mBounds;
	private boolean mFocus = false;
	private boolean mTextChanged = false;

	private OnFocusChangeListener mOnFocusChangeListener = null;

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initEditText();
	}

	public CustomEditText(Context context) {
		super(context);
		mContext = context;
		initEditText();
	}

	// 初始化edittext 控件
	private void initEditText() {
		addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监�?
			public void afterTextChanged(Editable paramEditable) {
			}

			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
				if (CustomEditText.this.isFocused()) {
					CustomEditText.this.setEditTextDrawable();
					mTextChanged = true;
				}
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && getText().toString().length() > 0) {
					setCompoundDrawables(null, null, mRight, null);
					setSelection(getText().toString().length());
					mFocus = true;
				} else {
					setCompoundDrawables(null, null, null, null);
					mFocus = false;
				}

				if (hasFocus) {
					mTextChanged = false;
				} else {

					if (mTextChanged) {
						if (mOnFocusChangeListener != null) {
							mOnFocusChangeListener.onFocusChange(v, hasFocus);
						}
					}
					// UiUtils.hiddenKeyBoard(mContext, v);
				}
			}
		});
	}

	public void setFocusChangeListener(
			OnFocusChangeListener onFocusChangeListener) {
		mOnFocusChangeListener = onFocusChangeListener;
	}

	// 控制图片的显�?
	private void setEditTextDrawable() {
		if (getText().toString().length() == 0) {
			mFocus = false;
			setCompoundDrawables(null, null, null, null);
		} else {
			mFocus = true;
			setCompoundDrawables(null, null, this.mRight, null);
		}
	}

	protected void finalize() throws Throwable {
		super.finalize();
		this.mRight = null;
		this.mBounds = null;
	}

	// 添加触摸事件
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if ((this.mRight != null) && mFocus
				&& (paramMotionEvent.getAction() == MotionEvent.ACTION_UP)) {
			this.mBounds = this.mRight.getBounds();
			int i = (int) paramMotionEvent.getX();
			if (i > getWidth()
					- (UiUtil.dip2px(getContext(), 10) + this.mBounds.width())) {
				setText("");
				paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	// 设置显示的图片资�?
	public void setCompoundDrawables(Drawable paramDrawable1,
			Drawable paramDrawable2, Drawable paramDrawable3,
			Drawable paramDrawable4) {
		if (paramDrawable3 != null) {
			this.mRight = paramDrawable3;
		}
		if (!isFocused()) {
			super.setCompoundDrawables(null, null, null, null);
		} else {
			super.setCompoundDrawables(paramDrawable1, paramDrawable2,
					paramDrawable3, paramDrawable4);
		}
	}

}
