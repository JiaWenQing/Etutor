package net.dx.etutor.view;

import net.dx.etutor.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 由两部分组成：listview_item(内容) + 删除按钮view(holder) + [其他按钮view(另一个holder)]
 * 
 * @author
 * 
 */
public class SlideView extends LinearLayout implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "SlideView";
	private Context mContext;
	private LinearLayout mViewContent;// 内容为：listview_item
	private Scroller mScroller;
	private OnSlideListener mOnSlideListener;
	private int mHolderWidth = 80;// 删除按钮所在布局的宽度
	private int mLastX = 0;
	private int mLastY = 0;

	private static final int TAN = 2;
	public static final int ONE_HOLDER_WIDTH = 80;
	public static final int TWO_HOLDER_WIDTH = 160;

	public interface OnSlideListener {
		public static final int SLIDE_STATUS_OFF = 0;// 隐藏删除按钮状态
		public static final int SLIDE_STATUS_START_SCROLL = 1;// 开始滚动状态
		public static final int SLIDE_STATUS_ON = 2;// 显示删除按钮状态

		/**
		 * @param view
		 *            current SlideView
		 * @param status
		 *            SLIDE_STATUS_ON or SLIDE_STATUS_OFF
		 */
		public void onSlide(View view, int status);
	}

	public SlideView(Context context) {
		super(context);
		initView();
	}

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mContext = getContext();
		mScroller = new Scroller(mContext);
		setOrientation(LinearLayout.HORIZONTAL);
		View.inflate(mContext, R.layout.slide_view_merge, this);
		mViewContent = (LinearLayout) findViewById(R.id.view_content);
		mHolderWidth = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
						.getDisplayMetrics()));
	}

	//
	// public void setDeleteButtonText(CharSequence text) {
	// ((TextView) findViewById(R.id.shield_text)).setText(text);
	// }
	//
	// public void setTopButtonText(CharSequence text) {
	// ((TextView) findViewById(R.id.release_text)).setText(text);
	// }

	public void setHolderWidth(int width) {
		mHolderWidth = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, width, getResources()
						.getDisplayMetrics()));
	}

	public void setContentView(View view) {
		mViewContent.addView(view);
	}

	public void setOnSlideListener(OnSlideListener onSlideListener) {
		mOnSlideListener = onSlideListener;
	}

	/**
	 * 还原位置为(0,0)
	 */
	public void shrink() {
		if (getScrollX() != 0) {
			this.smoothScrollTo(0, 0);
		}
	}

	public void onRequireTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		// view的可见的左上角x坐标
		// [减去]
		// 手机屏幕的视图坐标（包括：屏幕可见部分和不可见部分）的左上角的x坐标的距离
		int scrollX = getScrollX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();// 停止上一次未完成的滚动动画
			}
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
				break;
			}

			int newScrollX = scrollX - deltaX;// scrollX:要手动更新，所以要取新的
			if (deltaX != 0) {
				if (newScrollX < 0) {// 手指向右滑动，滚动位置为0
					newScrollX = 0;
				} else if (newScrollX > mHolderWidth) {
					// 手指向左滑动，滚动位置不能超过删除按钮的的宽度，这样刚好能显示删除按钮
					newScrollX = mHolderWidth;
				}
				this.scrollTo(newScrollX, 0);// 移动中，不用smoothScrollTo，用这个方法，就可以了。
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			int newScrollX = 0;
			int tempWidth = mHolderWidth >= 240 ? mHolderWidth / 2
					: mHolderWidth;// 这里，使一个按钮和两个按钮的情况，滑动距离的边界值相同
			if (scrollX - tempWidth * 0.75 > 0) {
				newScrollX = mHolderWidth;
			}
			this.smoothScrollTo(newScrollX, 0);
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
								: OnSlideListener.SLIDE_STATUS_ON);
			}
			break;
		}
		default:
			break;
		}

		mLastX = x;
		mLastY = y;
	}

	private void smoothScrollTo(int destX, int destY) {
		// 缓慢滚动到指定位置
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);// 开始滚动
		invalidate();// 会调用computeScroll()
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {// mScroller滚动动画没有完成，返回true,反之false
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());// view的滚动
			postInvalidate();// 会调用computeScroll(),直到滚动动画完成
		}
	}

	/*
	 * TODO 上面：通过mScroller.startScroll()来控制view的滚动（view.scrollTo()）
	 */

	/**
	 * 在onTouchEvent()方法中： 1. MotionEvent.ACTION_DOWN：按下事件
	 * 》》停止上一个滚动动画，调用mOnSlideListener.onSlide(); <br/>
	 * 
	 * 2.MotionEvent.ACTION_MOVE：移动事件 》》取得新的移动间距，确定是水平移动
	 * 》》取得新的滚动位置(newScrollX),确定是向左或向右，达到一定的条件，调用 this.scrollTo(newScrollX,
	 * 0);使view随着手指的移动到指定的位置 (效果就是view平滑移动)<br/>
	 * 2.MotionEvent.ACTION_UP：抬起事件 如果scrollX没有达到一定的距离，还原位置
	 * 反之，this.smoothScrollTo(newScrollX, 0);平滑的滚动到新的位置（这里是显示右边的按钮） <br/>
	 * 也就是下面的情况：<br/>
	 * newScrollX == 0 ，this.smoothScrollTo(newScrollX, 0);会还原view的位置 状态为：
	 * OnSlideListener.SLIDE_STATUS_OFF <br/>
	 * newScrollX = mHolderWidth; this.smoothScrollTo(newScrollX,
	 * 0);平滑的滚动到新的位置（这里是显示右边的按钮）
	 */

}
