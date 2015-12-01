package net.dx.etutor.activity.fragment.teacherInfo;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.DxTeachercourse;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.util.StrUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TeacherAbilityFragment extends BaseFragment {
	
	private TextView mSubject;
	private TextView mListen;
	private TextView mLectureType;
	private TextView mTeachingAge;
	private TextView mArea;
	private TextView mFees;
	private TextView mIntro;
	private String subject;

	private DxTeachercourse dxTeachercourse = new DxTeachercourse();
	private DxTeacherinfo dxTeacherinfo = new DxTeacherinfo();

	private CheckBox mCheckBox;
	private int[] data = new int[21];
	private int courseTable = 7;
	private TextView mNotClass;
	private RelativeLayout mLayout;

	public TeacherAbilityFragment() {
		super();
	}

	public TeacherAbilityFragment(EtutorApplication application,
			Activity activity, Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_teacher_ablitity, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		setTitle("");
		settingIcon(0, false, "");
		mNotClass = (TextView) findViewById(R.id.tv_no_course);
		mLayout = (RelativeLayout) findViewById(R.id.layout_course);
		mSubject = (TextView) findViewById(R.id.tv_lecture_subjects);
		mListen = (TextView) findViewById(R.id.tv_listening_test);
		mLectureType = (TextView) findViewById(R.id.tv_lecture_type);
		mTeachingAge = (TextView) findViewById(R.id.tv_teaching_age);
		mArea = (TextView) findViewById(R.id.tv_lecture_area);
		mFees = (TextView) findViewById(R.id.tv_class_fees);
		mIntro = (TextView) findViewById(R.id.tv_self_assessment);

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
		dxTeachercourse = dxTeacherinfo.getDxTeachercourse();
		dxTeachercourse = dxTeachercourse != null ? dxTeachercourse
				: new DxTeachercourse();

		for (int i = 0; i < 21; i++) {
			data[i] = R.id.rb_1_1 + i;
		}

		if (dxTeachercourse.getSubtime() != null) {
			courseTable = dxTeachercourse.getSubtime();
			for (int i = 0; i < 21; i++) {
				mCheckBox = (CheckBox) findViewById(data[i]);
				if ((courseTable & (1 << i)) != 0) {
					mCheckBox.setChecked(true);
				} else {
					mCheckBox.setChecked(false);
				}
				mCheckBox.setClickable(false);
			}
		}
		setData();
	}

	public void setData() {
		String s = "";
		String subjectItem = dxTeachercourse.getSubjectItemId1();
		if (TextUtils.isEmpty(subjectItem)) {
			subjectItem = ",";
		}
		String[] str = subjectItem.split(",");
		if (str.length != 0) {
			for (String string : str) {
				if (!TextUtils.isEmpty(string)) {
					s = s + string + ",";
				}
			}
		}
		if (TextUtils.isEmpty(s)) {
			subject = "";
		} else {
			subject = s.substring(0, s.length() - 1);
		}
		if (TextUtils.isEmpty(subject)) {
			mNotClass.setVisibility(View.VISIBLE);
			mLayout.setVisibility(View.GONE);
		} else {
			mNotClass.setVisibility(View.GONE);
			mLayout.setVisibility(View.VISIBLE);
			mSubject.setText(subject);
			mListen.setText(dxTeachercourse.getListenTest());
			String type = dxTeachercourse.getLectureType();
			if (TextUtils.isEmpty(type)) {
				type = "不限";
			}
			mLectureType.setText(type);
			if (dxTeacherinfo.getCoachTime() != null) {
				mTeachingAge.setText(StrUtil.setCoachTime(dxTeacherinfo
						.getCoachTime()));
			}
			String region;
			if (TextUtils.isEmpty(dxTeacherinfo.getProvince())
					|| TextUtils.isEmpty(dxTeacherinfo.getCity())
					|| TextUtils.isEmpty(dxTeacherinfo.getRegion())) {
				region = "";
			} else {
				if (dxTeacherinfo.getProvince().equals(dxTeacherinfo.getCity())) {
					region = dxTeacherinfo.getProvince()
							+ dxTeacherinfo.getRegion();
				} else {
					region = dxTeacherinfo.getProvince()
							+ dxTeacherinfo.getCity()
							+ dxTeacherinfo.getRegion();
				}
			}
			mArea.setText(region);
			if (dxTeachercourse.getPrice() != null) {
				int price = dxTeachercourse.getPrice();
				if (price == 0) {
					mFees.setText("面议");
				} else {
					mFees.setText(price + "元/小时");
				}
			} else {
				mFees.setText("面议");
			}
			if (!TextUtils.isEmpty(dxTeachercourse.getIntroduce())) {
				mIntro.setText(dxTeachercourse.getIntroduce());
			} else {
				mIntro.setText("无");
			}
		}
	}

}
