package net.dx.etutor.util;

import net.dx.etutor.R;
import net.dx.etutor.app.EtutorApplication;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 排序工具类
 * 
 * @author jwq
 * 
 */
public class SortableUtil {

	public static LinearLayout mFilter, mFilterSeniority, mFilterAuthenticate,
			mFilterAppraise, mFilterNew;
	public static TextView mTextPrivate, mTextSeniority, mTextAuthenticate,
			mTextAppraise, mTextNew;
	public static Drawable[] drawables = new Drawable[3];
	public static int i = 1, j, l, m;
	public static String sortType = "createTime desc";

	public static void getDrawables() {
		for (int i = 0; i < 3; i++) {
			drawables[i] = EtutorApplication
					.getInstance()
					.getResources()
					.getDrawable(
							R.drawable.search_teacher_filter_arrow_down + i);
		}
	}

	public static void onClickOfSort(Activity activity, View v, int type) {
		getDrawables();

		if (type != 0) {
			mFilterSeniority = (LinearLayout) activity
					.findViewById(R.id.filter_seniority);
			mTextSeniority = (TextView) activity
					.findViewById(R.id.text_filter_seniority);
			mTextSeniority.setCompoundDrawables(null, null, drawables[2], null);
		}
		mFilterAuthenticate = (LinearLayout) activity
				.findViewById(R.id.filter_authenticate);
		mFilterAppraise = (LinearLayout) activity
				.findViewById(R.id.filter_appraise);
		mFilterNew = (LinearLayout) activity.findViewById(R.id.filter_new);
		// mTextPrivate = (TextView)
		// activity.findViewById(R.id.text_filter_price);
		mTextAuthenticate = (TextView) activity
				.findViewById(R.id.text_filter_authenticate);
		mTextNew = (TextView) activity.findViewById(R.id.text_filter_new);
		mTextAppraise = (TextView) activity
				.findViewById(R.id.text_filter_appraise);
		for (int i = 0; i < drawables.length; i++) {
			drawables[i].setBounds(0, 0, drawables[i].getMinimumWidth(),
					drawables[i].getMinimumHeight());
		}
		mTextAppraise.setCompoundDrawables(null, null, drawables[2], null);
		mTextAuthenticate.setCompoundDrawables(null, null, drawables[2], null);
		mTextNew.setCompoundDrawables(null, null, drawables[2], null);
		switch (v.getId()) {
		case R.id.filter_new:
			if (i == 0) {
				mTextNew.setCompoundDrawables(null, null, drawables[0], null);
				sortType = "createTime desc";
				i = 1;
				j = 0;
				l = 0;
				m = 0;
			} else {
				mTextNew.setCompoundDrawables(null, null, drawables[1], null);
				sortType = "createTime asc";
				i = 0;
			}

			break;
		case R.id.filter_seniority:
			if (j == 0) {
				mTextSeniority.setCompoundDrawables(null, null, drawables[0],
						null);
				sortType = "coachTime desc";
				i = 0;
				j = 1;
				l = 0;
				m = 0;
			} else {
				mTextSeniority.setCompoundDrawables(null, null, drawables[1],
						null);
				sortType = "coachTime asc";
				j = 0;
			}

			break;
		case R.id.filter_authenticate:

			if (l == 0) {
				mTextAuthenticate.setCompoundDrawables(null, null,
						drawables[1], null);
				sortType = "identify asc,id asc";
				i = 0;
				j = 0;
				l = 1;
				m = 0;
			} else {
				mTextAuthenticate.setCompoundDrawables(null, null,
						drawables[0], null);
				sortType = "identify desc,id desc";
				l = 0;
			}

			break;
		case R.id.filter_appraise:
			if (m == 0) {
				mTextAppraise.setCompoundDrawables(null, null, drawables[0],
						null);
				sortType = "rank desc";
				i = 0;
				j = 0;
				l = 0;
				m = 1;
			} else {
				mTextAppraise.setCompoundDrawables(null, null, drawables[1],
						null);
				sortType = "rank asc";
				m = 0;
			}

			break;

		}

	}

	public static String getSortType() {
		return sortType;
	}

	public static void cleanSortType() {
		sortType = null;
	}
}
