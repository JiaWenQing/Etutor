package net.dx.etutor.view;

import net.dx.etutor.model.DxNeed;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

public class ExpandableListViewCompat extends ExpandableListView {
	private static final String TAG = "ListViewCompat";

	private SlideView mFocusedItemView;

	public ExpandableListViewCompat(Context context) {
		super(context);
	}

	public ExpandableListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandableListViewCompat(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 还原指定位置的view显示
	 * 
	 * @param position
	 */
	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 还原指定位置的view显示
	 * 
	 * @param SlideView
	 */
	public void shrinkListItem(SlideView view) {
		if (view != null) {
			view.shrink();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			int x = (int) event.getX();
			int y = (int) event.getY();
			int position = pointToPosition(x, y);
			if (position != INVALID_POSITION) {
				DxNeed data = (DxNeed) getItemAtPosition(position);
				mFocusedItemView = data.slideView;
			}
		}
		default:
			break;
		}

		if (mFocusedItemView != null) {
			mFocusedItemView.onRequireTouchEvent(event);
		}

		return super.onTouchEvent(event);
	}

}
