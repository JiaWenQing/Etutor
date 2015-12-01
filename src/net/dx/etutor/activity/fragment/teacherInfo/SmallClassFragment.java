package net.dx.etutor.activity.fragment.teacherInfo;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.dialog.LoadingDialog;
import net.dx.etutor.model.DxTeacherclass;
import net.dx.etutor.model.DxTeacherinfo;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SmallClassFragment extends BaseFragment {

	private TextView mNotClass;
	private TextView mTital;
	private TextView mCourseIntro;
	private TextView mCourseName;
	private TextView mCoursePrice;
	private TextView mListenInfo;
	private TextView mStudnetCount;
	private TextView mStartTime;
	private RelativeLayout mLayout;

	private String title = "";
	private String intro = "";
	private String course = "";
	private String oprice;
	private String count = "";
	private String time = "";
	private String listenInfo = "不可以";

	private String province;
	private String city;
	private String region;
	private String place;

	private DxTeacherclass dxTeacherclass;
	private TextView mArea;

	public SmallClassFragment() {
		super();
	}

	public SmallClassFragment(EtutorApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}


	protected void setData() {
		// TODO Auto-generated method stub
		title = dxTeacherclass.getTitle();
		intro = dxTeacherclass.getIntroduce();
		course = dxTeacherclass.getSubjectItemId();

		count = dxTeacherclass.getClassSize();

		if (dxTeacherclass.getPrice() != null) {
			oprice = dxTeacherclass.getPrice();
		} else {
			oprice = "0";
		}

		time = dxTeacherclass.getBeginTime();
		listenInfo = dxTeacherclass.getListenTest();

		if (TextUtils.isEmpty(title)) {
			mNotClass.setVisibility(View.VISIBLE);
			mLayout.setVisibility(View.GONE);
		} else {
			mNotClass.setVisibility(View.GONE);
			mLayout.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(count)) {
				count = "不限";
			}
			if (oprice.equals("0")) {
				mCoursePrice.setText("面议");
			} else {
				mCoursePrice.setText(oprice + "元/小时");
			}
			if (TextUtils.isEmpty(time)) {
				time = "随时开课";
			}

			if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city)
					|| TextUtils.isEmpty(region)) {
				place = "";
			} else {
				if (province.equals(city)) {
					place = province + region;
				} else {
					place = province + city + region;
				}
			}
			if (TextUtils.isEmpty(intro)) {
				intro = "无";
			}

			mArea.setText(place);
			mTital.setText(title);
			mCourseIntro.setText(intro);
			mCourseName.setText(course);
			mStudnetCount.setText(count);
			mStartTime.setText(time);
			mListenInfo.setText(listenInfo);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_small_class, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		setTitle("");
		settingIcon(0, false, "");
		// TODO Auto-generated method stub
		mNotClass = (TextView) findViewById(R.id.tv_no_class);
		mLayout = (RelativeLayout) findViewById(R.id.layout_class);
		mTital = (TextView) findViewById(R.id.tv_class_title);
		mCourseIntro = (TextView) findViewById(R.id.tv_course_intro);
		mCourseName = (TextView) findViewById(R.id.tv_class_subjects);
		mListenInfo = (TextView) findViewById(R.id.tv_listening_info);
		mCoursePrice = (TextView) findViewById(R.id.tv_preferential_price);
		mStudnetCount = (TextView) findViewById(R.id.tv_student_number);
		mStartTime = (TextView) findViewById(R.id.tv_start_time);
		mArea = (TextView) findViewById(R.id.tv_class_area);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		DxTeacherinfo dxTeacherinfo = (DxTeacherinfo) getActivity().getIntent()
				.getSerializableExtra("dxTeacherinfo");
		dxTeacherclass = dxTeacherinfo.getDxTeacherclass();
		province = dxTeacherinfo.getProvince();
		city = dxTeacherinfo.getCity();
		region = dxTeacherinfo.getRegion();
		dxTeacherclass = dxTeacherclass != null ? dxTeacherclass
				: new DxTeacherclass();
		setData();
	}
}
