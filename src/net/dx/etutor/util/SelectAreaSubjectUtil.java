package net.dx.etutor.util;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import net.dx.etutor.popupwindow.SelectPopupWindow;
import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SelectAreaSubjectUtil {

	/**
	 * 家教类别
	 */
	private static String category;// 家教类别
	/**
	 * 科目
	 */
	private static String subject;// 科目
	/**
	 * 省
	 */
	private static String province;// 省
	/**
	 * 城市
	 */
	private static String city;// 城市
	/**
	 * 县区
	 */
	private static String region;// 县区
	/**
	 * 是否可以试听
	 */
	private static String listeningTest;// 是否可以试听
	/**
	 * 小班模式是否可以试听
	 */
	private static String classListenInfo;// 小班模式是否可以试听
	/**
	 * 教龄
	 */
	private static String teachingAge;// 教龄
	/**
	 * 授课方式
	 */
	private static String lectureType = "不限";// 授课方式
	/**
	 * 教学模式
	 */
	private static String teachingModel;// 教学模式
	/**
	 * 距离
	 */
	private static String distance;// 距离
	/**
	 * 评价(好、一般、差)
	 */
	private static String evaluate;// 评价
	/**
	 * 学校类型(幼儿园、小学、初中、高中、大学、其他)
	 */
	private static String schoolType;// 学校类型
	/**
	 * 学校类别(公办，民办)
	 */
	private static String schoolCategory;// 学校类别
	/**
	 * 地址类别
	 */
	private static int addressType;// 地址类别
	private static int pPostion;
	private static int cPostion;
	private static int sPostion;
	private static int indexLectureType;// 授课方式索引
	private static int indexSchoolType;// 学校类型索引
	private static int indexDistance;// 距离索引
	private static int indexCategory;// 学校类别索引
	public static TextView mProvince;
	public static TextView mCity;
	public static TextView mRegion;
	public static TextView mEducationalType;
	public static TextView mSubject;
	public static Activity activitys;
	/**
	 * 机构分类
	 */
	private static TextView mClassify;
	private static String classify;

	/**
	 * 处理科目和省市区选择的onClick事件
	 * 
	 * @param activity
	 * @param v
	 * @param flag
	 *            0、找需求 1、找老师 2、找机构 3、找学校 4、老师课程设置
	 */
	public static void onClick(Activity activity, View v, int flag) {
		// TODO Auto-generated method stub
		activitys = activity;
		if (flag != 0) {
			mProvince = (TextView) activity.findViewById(R.id.tvbtn_province);
			mCity = (TextView) activity.findViewById(R.id.tvbtn_city);
			mRegion = (TextView) activity.findViewById(R.id.tvbtn_region);

			province = mProvince.getText().toString().trim();
			city = mCity.getText().toString().trim();
			region = mRegion.getText().toString().trim();
			EtutorApplication.getInstance().getSpUtil().setProvince(province);
			EtutorApplication.getInstance().getSpUtil().setCity(city);
			EtutorApplication.getInstance().getSpUtil().setRegion(region);
		}
		if (flag == 2) {
			mClassify = (TextView) activity
					.findViewById(R.id.tvbtn_agency_classify);
			classify = mClassify.getText().toString().trim();
			EtutorApplication.getInstance().getSpUtil().setClassify(classify);
		}
		if (flag == 0 || flag == 1) {

			mEducationalType = (TextView) activity
					.findViewById(R.id.tvbtn_education_type);
			mSubject = (TextView) activity.findViewById(R.id.tvbtn_subject);
			category = mEducationalType.getText().toString().trim();
			EtutorApplication.getInstance().getSpUtil().setCategory(category);
			subject = mSubject.getText().toString().trim();
			EtutorApplication.getInstance().getSpUtil().setSubject(subject);
		}
		switch (v.getId()) {
		case R.id.tvbtn_province:
			addressType = 5;
			SelectPopupWindow provincePopupWindow = new SelectPopupWindow(
					activity, Constants.NEED_PROVINCE);
			provincePopupWindow
					.setOnWheelClickListener(new OnWheelClickListener() {

						@Override
						public void wheelOnClick(String value, int type) {
							// TODO Auto-generated method stub
							switch (type) {
							case Constants.NEED_PROVINCE:
								province = value;
								EtutorApplication.getInstance().getSpUtil()
										.setProvince(value);
								mProvince.setText(value);
								break;
							case Constants.NEED_CITY:
								city = value;
								EtutorApplication.getInstance().getSpUtil()
										.setCity(value);
								mCity.setText(value);
								break;
							case Constants.NEED_REGION:
								region = value;
								EtutorApplication.getInstance().getSpUtil()
										.setRegion(value);
								mRegion.setText(value);
								break;
							}
							// setArea(mArea);
						}
					});
			provincePopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tvbtn_city:
			if (province == null || province.equals("请选择")) {
				Toast.makeText(activity, "请先选择前面的选项!", 0).show();
				return;
			}
			addressType = 6;

			SelectPopupWindow cityPopupWindow = new SelectPopupWindow(activity,
					Constants.NEED_CITY);
			cityPopupWindow.setOnWheelClickListener(new OnWheelClickListener() {

				@Override
				public void wheelOnClick(String value, int type) {
					// TODO Auto-generated method stub
					switch (type) {
					case Constants.NEED_CITY:
						city = value;
						EtutorApplication.getInstance().getSpUtil()
								.setCity(value);
						mCity.setText(value);
						break;
					case Constants.NEED_REGION:
						region = value;
						EtutorApplication.getInstance().getSpUtil()
								.setRegion(value);
						mRegion.setText(value);
						break;
					}
					// setArea(mArea);
				}
			});
			cityPopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tvbtn_region:
			if (city == null || city.equals("请选择")) {
				Toast.makeText(activity, "请先选择前面的选项!", 0).show();
				return;
			}
			addressType = 7;
			SelectPopupWindow regionPopupWindow = new SelectPopupWindow(
					activity, Constants.NEED_REGION);
			regionPopupWindow
					.setOnWheelClickListener(new OnWheelClickListener() {

						@Override
						public void wheelOnClick(String value, int type) {
							// TODO Auto-generated method stub
							switch (type) {
							case Constants.NEED_REGION:
								region = value;
								EtutorApplication.getInstance().getSpUtil()
										.setRegion(value);
								mRegion.setText(value);
								break;
							}
							// setArea(mArea);
						}

					});
			regionPopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tvbtn_education_type:
			addressType = 8;
			SelectPopupWindow categoryPopupWindow = new SelectPopupWindow(
					activity, Constants.NEED_TEACHERCATEGORY);
			categoryPopupWindow
					.setOnWheelClickListener(new OnWheelClickListener() {

						@Override
						public void wheelOnClick(String value, int type) {
							// TODO Auto-generated method stub
							switch (type) {
							case Constants.NEED_TEACHERCATEGORY:
								category = value;
								EtutorApplication.getInstance().getSpUtil()
										.setCategory(value);
								mEducationalType.setText(value);
								break;
							case Constants.NEED_SUBJECT:
								subject = value;
								EtutorApplication.getInstance().getSpUtil()
										.setSubject(value);
								mSubject.setText(value);
								break;
							}
						}
					});
			categoryPopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tvbtn_subject:
			if (category == null || category.equals("请选择")) {
				Toast.makeText(activity, "请先选择前面的选项!", 0).show();
				return;
			}
			addressType = 4;
			SelectPopupWindow subjectPopupWindow = new SelectPopupWindow(
					activity, Constants.NEED_SUBJECT);
			subjectPopupWindow
					.setOnWheelClickListener(new OnWheelClickListener() {

						@Override
						public void wheelOnClick(String value, int type) {
							// TODO Auto-generated method stub
							switch (type) {
							case Constants.NEED_SUBJECT:
								subject = value;
								EtutorApplication.getInstance().getSpUtil()
										.setSubject(value);
								mSubject.setText(value);
								break;
							}
						}
					});
			subjectPopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tvbtn_agency_classify:
			addressType = 9;
			SelectPopupWindow classityPopupWindow = new SelectPopupWindow(
					activity, Constants.AGENCY_CLASSITY);
			classityPopupWindow
					.setOnWheelClickListener(new OnWheelClickListener() {

						@Override
						public void wheelOnClick(String value, int type) {
							// TODO Auto-generated method stub
							switch (type) {
							case Constants.AGENCY_CLASSITY:
								classify = value;
								EtutorApplication.getInstance().getSpUtil()
										.setClassify(classify);
								mClassify.setText(value);
								break;
							}
						}
					});
			classityPopupWindow.showAtLocation(
					activity.findViewById(R.id.search_teacher), Gravity.BOTTOM,
					0, 0);
			break;

		default:
			break;
		}

	}

	/**
	 * 处理radiogroup监听事件
	 * 
	 * @param group
	 * @param checkedId
	 */
	public static void onCheckedChanged(Activity activity, RadioGroup group,
			int checkedId) {
		// TODO Auto-generated method stub

		switch (group.getId()) {
		case R.id.rg_listening_choose:
			switch (checkedId) {
			case R.id.rb_listening_y:
				listeningTest = "可以";
				break;
			case R.id.rb_listening_n:
				listeningTest = "不可以";
				break;

			default:
				break;
			}

			break;

		case R.id.rg_lecture_type:
			switch (checkedId) {
			case R.id.rb_type_1:
				lectureType = "不限";
				indexLectureType = 0;
				break;
			case R.id.rb_type_2:
				lectureType = "家教上门";
				indexLectureType = 1;
				break;
			case R.id.rb_type_3:
				lectureType = "学生上门";
				indexLectureType = 2;
				break;
			case R.id.rb_type_4:
				lectureType = "附近公共场所";
				indexLectureType = 3;
				break;

			default:
				break;
			}
			break;
		case R.id.rg_teaching_model:
			switch (checkedId) {
			case R.id.rb_model_1:
				teachingModel = "一对一模式";
				break;
			case R.id.rb_model_2:
				teachingModel = "小班模式";
				break;

			default:
				break;
			}
			break;
		case R.id.rg_distance:
			switch (checkedId) {
			case R.id.rb_distance_no:
				distance = null;// 不限
				indexDistance = 0;
				break;
			case R.id.rb_distance_1000:
				distance = "1000";
				indexDistance = 1;
				break;
			case R.id.rb_distance_2000:
				distance = "2000";
				indexDistance = 2;
				break;
			case R.id.rb_distance_3000:
				distance = "3000";
				indexDistance = 3;
				break;
			case R.id.rb_distance_5000:
				distance = "5000";
				indexDistance = 4;
				break;

			default:
				break;
			}
			break;
		case R.id.rg_evaluate:
			switch (checkedId) {
			case R.id.rb_evaluate_1:
				evaluate = "1";
				break;
			case R.id.rb_evaluate_2:

				evaluate = "0";
				break;
			case R.id.rb_evaluate_3:
				evaluate = "-1";

				break;

			default:
				break;
			}
			break;
		case R.id.rg_school_type:
			switch (checkedId) {
			case R.id.rb_unlimited_type:
				schoolType = null;
				indexSchoolType = 0;
				break;
			case R.id.rb_kindergarten:
				schoolType = "幼儿园";
				indexSchoolType = 1;
				break;
			case R.id.rb_primary_school:

				schoolType = "小学";
				indexSchoolType = 2;
				break;
			case R.id.rb_middle_school:
				schoolType = "初中";
				indexSchoolType = 3;
				break;
			case R.id.rb_highmiddle_school:
				schoolType = "高中";
				indexSchoolType = 4;
				break;
			case R.id.rb_university:
				schoolType = "大学";
				indexSchoolType = 5;
				break;
			case R.id.rb_other:
				schoolType = "其他";
				indexSchoolType = 6;
				break;

			default:
				break;
			}
			break;
		case R.id.rg_school_category:
			switch (checkedId) {
			case R.id.rb_unlimited_category:
				schoolCategory = null;
				indexCategory = 0;
				break;
			case R.id.rb_public_school:
				schoolCategory = "公办";
				indexCategory = 1;
				break;
			case R.id.rb_private_school:
				schoolCategory = "民办";
				indexCategory = 2;
				break;
			case R.id.rb_other_school:
				schoolCategory = "其他";
				indexCategory = 3;
				break;

			default:
				break;
			}

			break;
		default:
			break;
		}

	}

	public static void setArea(TextView mArea) {
		if (null != mArea) {
			if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)
					&& !TextUtils.isEmpty(region)) {
				if (province.equals(city)) {
					mArea.setText(city + region);
				} else {
					mArea.setText(province + city + region);
				}
			}

		}
	}

	private static MyDatabase database = EtutorApplication.getInstance()
			.getMyDatabase();
	private static List<String> pList = database.getProvinces();
	private static List<String> sList = database.getSubject();
	private static List<String> cList;

	// private static List<TeacherCategoryModel> tList = SubjectSelectUtil
	// .analysisSubjectXMLBackList();
	private static int tPostion;

	public static int gettPostion() {
		for (int i = 0; i < sList.size(); i++) {
			if (sList.get(i).equals(category)) {
				tPostion = i;
			}
		}
		return tPostion;
	}

	public static int getpPostion() {
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(i).equals(province)) {
				pPostion = i;
			}
		}
		return pPostion;
	}

	public static int getcPostion() {
		getpPostion();
		cList = database.getCities(database.getId(pList.get(pPostion), false));
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).equals(city)) {
				cPostion = i;
			}
		}
		return cPostion;
	}

	// public static int getsPostion() {
	// for (int i = 0; i < tList.size(); i++) {
	// if (tList.get(i).gettCategory().equals(category)) {
	// sPostion = i;
	// }
	// }
	// return sPostion;
	// }

	/**
	 * @return 学校类型
	 */
	public static String getSchoolType() {
		return schoolType;
	}

	/**
	 * @return 学校类型索引
	 */
	public static int getSchoolTypeIndex() {
		return indexSchoolType;
	}

	/**
	 * @return 学校类别：公办、民办 、其他
	 */
	public static String getSchoolCategoty() {
		return schoolCategory;
	}

	/**
	 * @return 学校类别索引
	 */
	public static int getSchoolCategotyIndex() {
		return indexCategory;
	}

	/**
	 * @return 评价:1好、0中、-1差
	 */
	public static String getEvaluate() {
		return evaluate;
	}

	public static String getTeachingAge() {
		return teachingAge;
	}

	/**
	 * @return 授课方式
	 * 
	 */
	public static String getLectureType() {
		return lectureType;
	}

	/**
	 * @return 授课方式索引
	 */
	public static int getLectureTypeIndex() {
		return indexLectureType;
	}

	/**
	 * @return 一对一模式是否可以试听
	 */
	public static String getListeningTest() {
		return listeningTest;
	}

	/**
	 * @return 小班模式是否可以试听
	 */
	public static String getClassListenInfo() {
		return classListenInfo;
	}

	/**
	 * @return 教学模式：一对一，小班
	 */
	public static String getTeachingModel() {
		return teachingModel;
	}

	/**
	 * @return 距离索引
	 */
	public static int getDistanceIndex() {
		return indexDistance;
	}

	/**
	 * @return 省
	 */
	public static String getProvince() {
		return province;
	}

	/**
	 * @return 市
	 */
	public static String getCity() {
		return city;
	}

	/**
	 * @return 地区
	 */
	public static String getRegion() {
		return region;
	}

	public static String getCategory() {
		return category;
	}

	public static String getSubject() {
		return subject;
	}

	public static int getAddressType() {
		return addressType;
	}

	public static void clear() {
		province = null;
		city = null;
		region = null;
		category = null;
		subject = null;
		distance = null;
		lectureType = "不限";
		teachingModel = null;
		evaluate = null;
		schoolType = null;
		schoolCategory = null;
		EtutorApplication.getInstance().getSpUtil().clearArea();
	}

}
