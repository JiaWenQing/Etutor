package net.dx.etutor.popupwindow;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BasePopupWindow;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.SelectAreaSubjectUtil;
import net.dx.etutor.view.wheelview.ArrayWheelAdapter;
import net.dx.etutor.view.wheelview.OnWheelChangedListener;
import net.dx.etutor.view.wheelview.OnWheelScrollListener;
import net.dx.etutor.view.wheelview.WheelView;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 省市区选择器，科目选择器，机构分类选择器
 * 
 * @author jwq
 * 
 */
public class SelectPopupWindow extends BasePopupWindow implements
		android.view.View.OnClickListener, OnWheelChangedListener,
		OnWheelScrollListener {

	private Context mContext;
	private MyDatabase database;
	private TextView mBack;
	private TextView mOk;
	private String mCategory;
	private String mSubject;
	private String mValue;
	private String mProvince;
	private String mCity;
	private String mRegion;
	private String classity;
	private OnWheelClickListener mOnWheelClickListener;
	private WheelView wheelView;
	private String arrys[];
	private static String classitys[] = { "全部", "英语培训", "IT培训", "学历培训",
			"MBA培训", "留学", "职业资格", "小语种培训", "健身塑形", "会计考试", "成人教育", "艺术培训",
			"早教中心", "驾校", "室内设计", "外贸采购", "教师资格", "数码摄影", "其他" };
	private int mType;
	private int pPostion;
	private int cPostion;
	private int sPostion;
	private static List<String> pList; // 地址列表
	private static List<String> rList; // 地址列表
	private static List<String> cList; // 地址列表
	private static List<String> tList; // 科目列表
	private static List<String> sList; // 课程列表
	private boolean isSubject = true;

	/**
	 * 省市区选择器，科目选择器
	 * 
	 * @param context
	 * @param type
	 */
	public SelectPopupWindow(Context context, int type) {
		super(LayoutInflater.from(context).inflate(
				R.layout.wheel_dialog_layout, null), LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mType = type;
		mContext = context;
		setAnimationStyle(R.style.wheel_popwin_anim_style);

	}

	@Override
	public void init() {

	}

	@Override
	public void initViews() {
		mType = SelectAreaSubjectUtil.getAddressType();
		// pList = AreaSelectUtil.analysisAddressXMLBackList();
		database = EtutorApplication.getInstance().getMyDatabase();
		pList = database.getProvinces();
		tList = database.getSubject();

		mProvince = EtutorApplication.getInstance().getSpUtil().getProvince();
		mCity = EtutorApplication.getInstance().getSpUtil().getCity();
		mRegion = EtutorApplication.getInstance().getSpUtil().getRegion();
		classity = EtutorApplication.getInstance().getSpUtil().getClassify();

		mCategory = EtutorApplication.getInstance().getSpUtil().getCategory();
		mSubject = EtutorApplication.getInstance().getSpUtil().getSubject();

		pPostion = SelectAreaSubjectUtil.getpPostion();
		if (pPostion != 0) {
			cPostion = SelectAreaSubjectUtil.getcPostion();
		}

		sPostion = SelectAreaSubjectUtil.gettPostion();
		mBack = (TextView) findViewById(R.id.wheel_back);
		mOk = (TextView) findViewById(R.id.wheel_ok);
		wheelView = (WheelView) findViewById(R.id.wheel_view);
		wheelView.setVisibleItems(5);
		int type = 0;
		switch (mType) {
		case Constants.NEED_PROVINCE:
			type = 1;
			getProvince(pList);
			break;
		case Constants.NEED_CITY:
			type = 2;
			cList = database.getCities(database.getId(mProvince, false));
			getCity(cList);
			break;
		case Constants.NEED_REGION:
			type = 3;
			if (mCity.equals("北京市") || mCity.equals("天津市")
					|| mCity.equals("上海市") || mCity.equals("重庆市")) {
				rList = database.getRegions(database.getId(mCity, true));
			} else {
				rList = database.getRegions(database.getId(mCity, false));
			}
			getRegion(rList);
			break;
		case Constants.NEED_TEACHERCATEGORY:
			type = 4;
			getCategory(tList);
			break;
		case Constants.NEED_SUBJECT:
			type = 5;
			sList = database.getSubjectItem(sPostion + "");
			sList.remove(0);
			getSubject(sList);
			break;
		case Constants.AGENCY_CLASSITY:
			type = 6;
			getClassity();
			break;

		default:
			break;
		}
		wheelView.setAdapter(new ArrayWheelAdapter<String>(arrys), type);
	}

	@Override
	public void initEvents() {
		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);
		wheelView.addChangingListener(this);
		wheelView.addScrollingListener(this);
	}

	@Override
	public void onClick(View v) {
		// pList = AreaSelectUtil.analysisAddressXMLBackList();
		database = EtutorApplication.getInstance().getMyDatabase();
		pList = database.getProvinces();
		tList = database.getSubject();

		switch (v.getId()) {
		case R.id.wheel_back:
			this.dismiss();
			break;
		case R.id.wheel_ok:
			switch (mType) {
			case Constants.NEED_PROVINCE:
				getProvince(pList);
				if (mValue == null) {
					mValue = arrys[0];
				}
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_PROVINCE);
				mOnWheelClickListener.wheelOnClick(arrys[0],
						Constants.NEED_CITY);
				mOnWheelClickListener.wheelOnClick(arrys[0],
						Constants.NEED_REGION);
				break;
			case Constants.NEED_CITY:
				cList = database.getCities(database.getId(pList.get(pPostion),
						false));
				getCity(cList);
				if (mValue == null) {
					mValue = arrys[1];
				}
				mOnWheelClickListener.wheelOnClick(mValue, Constants.NEED_CITY);
				mOnWheelClickListener.wheelOnClick(arrys[0],
						Constants.NEED_REGION);
				break;
			case Constants.NEED_REGION:
				if (mValue == null) {
					mValue = arrys[0];
				}
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_REGION);
				break;
			case Constants.NEED_TEACHERCATEGORY:
				getCategory(tList);
				if (mValue == null) {
					mValue = arrys[0];
				}
				mCategory = mValue;
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_TEACHERCATEGORY);
				if (mValue.equals(arrys[0])) {
					mOnWheelClickListener.wheelOnClick(mValue,
							Constants.NEED_SUBJECT);
				} else {
					List<String> mList = database.getSubjectItem(sPostion + "");
					mOnWheelClickListener.wheelOnClick(mList.get(1),
							Constants.NEED_SUBJECT);
				}

				break;
			case Constants.NEED_SUBJECT:
				sList = database.getSubjectItem(sPostion + "");
				getSubject(sList);
				if (mValue == null) {
					mValue = arrys[0];
				}
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_SUBJECT);
				break;
			case Constants.AGENCY_CLASSITY:
				getClassity();
				if (mValue == null) {
					mValue = arrys[0];
				}
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.AGENCY_CLASSITY);
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
		mValue = arrys[wheelView.getCurrentItem()];
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		mValue = arrys[wheelView.getCurrentItem()];
	}

	public void getProvince(List<String> pList) {
		if (TextUtils.isEmpty(mValue)) {
			if (TextUtils.isEmpty(mProvince)) {
				mProvince = EtutorApplication.getInstance().getSpUtil()
						.getLocationCity();
				mValue = mProvince;
			} else {
				mValue = mProvince;
			}
		}
		arrys = new String[pList.size()];
		for (int i = 0; i < pList.size(); i++) {
			arrys[i] = pList.get(i);
			if (arrys[i].equals(mValue)) {
				pPostion = i;
			}
		}
	}

	public void getCity(List<String> cList) {
		if (TextUtils.isEmpty(mValue)) {
			if (TextUtils.isEmpty(mCity)) {
				mCity = EtutorApplication.getInstance().getSpUtil().getCity();
				mValue = mCity;
			} else {
				mValue = mCity;
			}
		}
		arrys = new String[cList.size()];
		for (int i = 0; i < cList.size(); i++) {
			arrys[i] = cList.get(i);
			if (arrys[i].equals(mValue)) {
				cPostion = i;
			}
		}
	}

	public void getRegion(List<String> rList) {
		if (TextUtils.isEmpty(mValue)) {
			if (TextUtils.isEmpty(mRegion)) {
				mRegion = EtutorApplication.getInstance().getSpUtil()
						.getRegion();
				mValue = mRegion;
			} else {
				mValue = mRegion;
			}
		}
		arrys = new String[rList.size()];
		for (int i = 0; i < rList.size(); i++) {
			arrys[i] = rList.get(i);
		}
	}

	public void getCategory(List<String> mList) {
		if (TextUtils.isEmpty(mValue)) {
			if (TextUtils.isEmpty(mCategory)) {
				mCategory = EtutorApplication.getInstance().getSpUtil()
						.getCategory();
				mValue = mCategory;
			} else {
				mValue = mCategory;
			}
		}
		arrys = new String[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			arrys[i] = mList.get(i);
			if (arrys[i].equals(mValue)) {
				sPostion = i;
			}
		}
	}

	public void getSubject(List<String> sList) {
		if (TextUtils.isEmpty(mValue)) {
			if (TextUtils.isEmpty(mSubject)) {
				mSubject = EtutorApplication.getInstance().getSpUtil()
						.getSubject();
				mValue = mSubject;
			} else {
				mValue = mSubject;
			}
		}
		arrys = new String[sList.size()];
		for (int i = 0; i < sList.size(); i++) {
			arrys[i] = sList.get(i);
		}
	}

	/**
	 * 机构分类
	 */
	private void getClassity() {
		// TODO Auto-generated method stub
		arrys = new String[classitys.length];
		for (int i = 0; i < classitys.length; i++) {
			arrys[i] = classitys[i];
		}
	}

}
