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

public class TeachAgePopupWindow extends BasePopupWindow implements
		android.view.View.OnClickListener, OnWheelChangedListener,
		OnWheelScrollListener {

	private TextView mBack;
	private TextView mOk;
	private String mValue;
	private OnWheelClickListener mOnWheelClickListener;
	private WheelView wheelView;
	private String arrys[];
	private int mType;

	public TeachAgePopupWindow(Context context, int type) {
		super(LayoutInflater.from(context).inflate(
				R.layout.wheel_dialog_layout, null), LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mType = type;
		setAnimationStyle(R.style.wheel_popwin_anim_style);
	}

	@Override
	public void initViews() {

		arrys = new String[] { "不足1年","1年", "2年", "3年", "4年", "5年", "6年", "7年", "8年",
				"9年", "10年", "10年以上" };
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
		if (mValue == null) {
			mValue = "不足1年";
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
			case Constants.NEED_AGE:
				mOnWheelClickListener.wheelOnClick(mValue, Constants.NEED_AGE);
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

	}

	@Override
	public void onScrollingStarted(WheelView wheel) {

	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		mValue = arrys[wheelView.getCurrentItem()];
	}

}
