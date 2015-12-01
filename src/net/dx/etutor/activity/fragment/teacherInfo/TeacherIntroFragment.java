package net.dx.etutor.activity.fragment.teacherInfo;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.util.DateUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class TeacherIntroFragment extends BaseFragment {

	private TextView mEducation;
	private TextView mAge;
	private TextView mSchool;
	private TextView mGraguteTime;
	private TextView mMajor;
	private TextView mSex;
	private DxTeacherinfo dxTeacherinfo = new DxTeacherinfo();

	public TeacherIntroFragment() {
		super();
	}

	public TeacherIntroFragment(EtutorApplication application,
			Activity activity, Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_personal_introduce,
				container, false);

		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	protected void initViews() {
		setTitle("");
		settingIcon(0, false, "");
		// TODO Auto-generated method stub
		mEducation = (TextView) findViewById(R.id.teacher_education_degree);
		mSex = (TextView) findViewById(R.id.teacher_sex);
		mAge = (TextView) findViewById(R.id.teacher_age);
		mSchool = (TextView) findViewById(R.id.teacher_graduate_school);
		mMajor = (TextView) findViewById(R.id.teacher_graduate_major);
		mGraguteTime = (TextView) findViewById(R.id.teacher_graduate_time);

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dxTeacherinfo = (DxTeacherinfo) getActivity().getIntent()
				.getSerializableExtra("dxTeacherinfo");

		setData();
	}

	public void setData() {

		if (!TextUtils.isEmpty(dxTeacherinfo.getDxUsers().getSex())) {
			mSex.setText("性别:" + dxTeacherinfo.getDxUsers().getSex());
		} else {
			mSex.setText("性别:保密");
		}
		if (!TextUtils.isEmpty(dxTeacherinfo.getEducation().toString())) {
			mEducation.setText("学历:" + dxTeacherinfo.getEducation().toString());
		} else {
			mEducation.setText("学历:");
		}
		if (!TextUtils.isEmpty(dxTeacherinfo.getTeacherBirthday())) {
			int age = DateUtil.getOffectAge(dxTeacherinfo.getTeacherBirthday());
			if (age == 0) {
				mAge.setText("年龄:保密");
			} else {
				mAge.setText("年龄:"
						+ DateUtil.getOffectAge(dxTeacherinfo
								.getTeacherBirthday()));
			}
		} else {
			mAge.setText("年龄:保密");
		}
		if (!TextUtils.isEmpty(dxTeacherinfo.getGraduateTime())) {
			mGraguteTime.setText("毕业时间:" + dxTeacherinfo.getGraduateTime());
		} else {
			mGraguteTime.setText("毕业时间:");
		}
		if (!TextUtils.isEmpty(dxTeacherinfo.getSchool())) {
			mSchool.setText("毕业学校:" + dxTeacherinfo.getSchool());
		} else {
			mSchool.setText("毕业学校:");
		}
		if (!TextUtils.isEmpty(dxTeacherinfo.getMajor())) {
			mMajor.setText("所学专业:" + dxTeacherinfo.getMajor());
		} else {
			mMajor.setText("所学专业:");
		}
	}
}
