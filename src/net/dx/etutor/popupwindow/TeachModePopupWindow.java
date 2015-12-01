package net.dx.etutor.popupwindow;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BasePopupWindow;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.util.Constants;
import net.dx.etutor.view.wheelview.ArrayWheelAdapter;
import net.dx.etutor.view.wheelview.NumericWheelAdapter;
import net.dx.etutor.view.wheelview.OnWheelChangedListener;
import net.dx.etutor.view.wheelview.OnWheelScrollListener;
import net.dx.etutor.view.wheelview.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TeachModePopupWindow extends BasePopupWindow implements
		android.view.View.OnClickListener, OnWheelChangedListener,
		OnWheelScrollListener {

	private TextView mBack;
	private TextView mOk;
	private Context mContext;
	private String mValue;
	private OnWheelClickListener mOnWheelClickListener;
	private WheelView wheelView;
	private String arrys[];
	private int mType;

	public TeachModePopupWindow(Context context, int type) {
		super(LayoutInflater.from(context).inflate(
				R.layout.wheel_dialog_layout, null), LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mType = type;
		setAnimationStyle(R.style.wheel_popwin_anim_style);
	}

	@Override
	public void initViews() {
		arrys = new String[] { "不限", "一对一", "一对多" };
		mBack = (TextView) findViewById(R.id.wheel_back);
		mOk = (TextView) findViewById(R.id.wheel_ok);
		wheelView = (WheelView) findViewById(R.id.wheel_view);
		wheelView.setVisibleItems(5);
		wheelView.setAdapter(new ArrayWheelAdapter<String>(arrys));
	}

	@Override
	public void initEvents() {
		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);
		wheelView.addChangingListener(this);
		wheelView.addScrollingListener(this);
	}

	@Override
	public void init() {
	}

	@Override
	public void onClick(View v) {
		if (mValue==null) {
			mValue="不限"	;
		}
		switch (v.getId()) {
		case R.id.wheel_back:
			this.dismiss();
			break;
		case R.id.wheel_ok:
			switch (mType) {
			case Constants.NEED_SEX:
				mOnWheelClickListener.wheelOnClick(mValue, Constants.NEED_SEX);
				break;
			case Constants.NEED_TEACH:
				mOnWheelClickListener
						.wheelOnClick(mValue, Constants.NEED_TEACH);
				break;
			case Constants.NEED_LECTURE:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_LECTURE);
				break;
			}
			this.dismiss();
		}
	}

	public void setOnWheelClickListener(OnWheelClickListener l) {
		this.mOnWheelClickListener = l;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if ("".equals(mValue)) {
			mValue = "不限";
		} else {
			mValue = arrys[wheelView.getCurrentItem()];
		}
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		if ("".equals(mValue)) {
			mValue = "不限";
		} else {
			mValue = arrys[wheelView.getCurrentItem()];
		}
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		if ("".equals(mValue)) {
			mValue = "不限";
		} else {
			mValue = arrys[wheelView.getCurrentItem()];
		}
	}

}
