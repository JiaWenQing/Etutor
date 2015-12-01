package net.dx.etutor.popupwindow;

import java.util.Iterator;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.base.BasePopupWindow;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.GlobalData;
import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.util.Constants;
import net.dx.etutor.view.wheelview.ArrayWheelAdapter;
import net.dx.etutor.view.wheelview.OnWheelChangedListener;
import net.dx.etutor.view.wheelview.OnWheelScrollListener;
import net.dx.etutor.view.wheelview.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SubjectPopupWindow extends BasePopupWindow implements
		android.view.View.OnClickListener, OnWheelChangedListener,
		OnWheelScrollListener {
	private MyDatabase database;
	private static final int mScreenHeight=BaseActivity.getScreenHeight();
	private TextView mBack;
	private TextView mOk;
	private String subjects[];
	private String courses[];
	private WheelView mSubject;
	private WheelView mItem;
	private String items[][];
	private OnWheelClickListener mOnWheelClickListener;
	private String mValue;
	private int mType;
	private Iterator<String> iterator;
	private static List<String> tList; //科目列表
	private static List<String> sList; //课程列表
	
	public SubjectPopupWindow(Context context, int height,int type) {
		super(LayoutInflater.from(context).inflate(
				R.layout.wheel_dialog_subject_layout, null),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mType=type;
		setAnimationStyle(R.style.wheel_popwin_anim_style);
	}

	@Override
	public void initViews() {
//		tList = SubjectSelectUtil.analysisSubjectXMLBackList();
		database=EtutorApplication.getInstance().getMyDatabase();
		tList=database.getSubject();
		getCategory(tList);
		getItems(subjects);

		mBack = (TextView) findViewById(R.id.wheel_back);
		mOk = (TextView) findViewById(R.id.wheel_ok);
		mSubject = (WheelView) findViewById(R.id.wheel_view_subject);
		mItem = (WheelView) findViewById(R.id.wheel_view_item);
		mSubject.setVisibleItems(5);
		mItem.setVisibleItems(5);
		mSubject.setCurrentItem(0);
		mSubject.setAdapter(new ArrayWheelAdapter<String>(subjects));
		mItem.setAdapter(new ArrayWheelAdapter<String>(items[0]));
	}

	@Override
	public void initEvents() {
		mSubject.addChangingListener(this);
		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);
		mItem.addScrollingListener(this);
	}

	public void setOnWheelClickListener(OnWheelClickListener l) {
		this.mOnWheelClickListener = l;
	}

	@Override
	public void init() {

	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		GlobalData.instance().subjectItme = items[mSubject.getCurrentItem()][mItem
				.getCurrentItem()];
		mValue = items[mSubject.getCurrentItem()][mItem.getCurrentItem()];
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		mItem.setAdapter(new ArrayWheelAdapter<String>(items[newValue]));
		mItem.setCurrentItem(0);
		mValue = items[mSubject.getCurrentItem()][mItem.getCurrentItem()];
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_back:
			this.dismiss();
			break;
		case R.id.wheel_ok:
			if (mValue == null) {
				mValue = items[0][0];
			}
			switch (mType) {
			case Constants.NEED_SUBJECT:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_SUBJECT);
				break;
			case Constants.NEED_SUBJECT1:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_SUBJECT1);
				break;
			case Constants.NEED_SUBJECT2:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_SUBJECT2);
				break;
			case Constants.NEED_SUBJECT3:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_SUBJECT3);
				break;
			}
			this.dismiss();
		}
	}

	
	
	

	/**
	 * 获取Items数据
	 * 
	 * @param subjects
	 */
	public void getItems(String[] subjects) {
		items = new String[subjects.length][];
		for (int i = 0; i < subjects.length; i++) {
			sList=database.getSubjectItem(i+1+"");
			int m= sList.size();
			courses=new String[m];
			for (int k = 0; k < m; k++) {
				if (k==m-1) {
					courses[k]="请选择";
				}else {
					courses[k]=sList.get(k+1);
				}
			}
			items[i] = courses;
		}
	}
	/**
	 * 获取Subject数据
	 * 
	 * @param tList
	 */
	public void getCategory(List<String> tList) {
		tList.remove(0);
		subjects = new String[11];
		for (int i = 0; i < subjects.length; i++) {
			subjects[i]=tList.get(i);
		}
	}

}
