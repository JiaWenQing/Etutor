package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.home.CollectActivity;
import net.dx.etutor.activity.student.StudentIntroActivity;
import net.dx.etutor.activity.teacher.TeacherIntroActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxCollect;
import net.dx.etutor.model.DxComment;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 收藏listview的adapter
 * 
 * @author app
 * 
 */
public class MyCollectAdapter extends BaseAdapter {
	
	/**
	 * ListView 中视图的种类个数 必须准确指定这个值， 并覆盖超类的getViewTypeCount()和getItemViewType（）方法
	 * 否则不能正常加载不同的View
	 */
	private final int MEX_ITEM_TYPE = 2;
	private LayoutInflater inflater;
	private List<DxCollect> mList = new ArrayList<DxCollect>();
	private Context mContext;

	public MyCollectAdapter(Context mContext, List<DxCollect> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type = super.getItemViewType(position);
		if (null != (DxCollect) getItem(position)) {
			type = ((DxCollect) getItem(position)).getType();
		}
		return type;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return MEX_ITEM_TYPE;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CollectViewHolder viewHolder = null;
		final DxCollect collect = (DxCollect) getItem(position);
		if (convertView == null) {
			switch (collect.getType()) {
			case 0:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_collect_student, null);
				viewHolder = new CollectViewHolder();
				viewHolder.mCollectStudent = (LinearLayout) convertView
						.findViewById(R.id.student_layout_collect);
				viewHolder.mStudentAvatar = (ImageView) convertView
						.findViewById(R.id.imv_collect_child);
				viewHolder.mNeedTitle = (TextView) convertView
						.findViewById(R.id.title_tv_child);
				viewHolder.mStudentIdentify = (ImageView) convertView
						.findViewById(R.id.identify_imv_student);
				viewHolder.mStudentVerify = (ImageView) convertView
						.findViewById(R.id.imv_student_verify);
				viewHolder.mStudentSubjects = (TextView) convertView
						.findViewById(R.id.tv_subject);
				viewHolder.mStudentFees = (TextView) convertView
						.findViewById(R.id.tv_student_class_fees);
				viewHolder.mStudentTimes = (TextView) convertView
						.findViewById(R.id.tv_lecture_count);
				viewHolder.mSRatingBar = (RatingBar) convertView
						.findViewById(R.id.ratingbar_student);
				convertView.setTag(viewHolder);
				viewHolder.mStudentIdentify.setVisibility(View.GONE);
				break;
			default:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_collect_teacher, null);
				viewHolder = new CollectViewHolder();
				viewHolder.mCollectTeacher = (LinearLayout) convertView
						.findViewById(R.id.teacher_layout_collect);
				viewHolder.mTeacherAvatar = (ImageView) convertView
						.findViewById(R.id.imv_teacher_avatar);
				viewHolder.mTeacherIdentify = (ImageView) convertView
						.findViewById(R.id.imv_teacher_identify);
				viewHolder.mTeacherVerify = (ImageView) convertView
						.findViewById(R.id.imv_teacher_verify);
				viewHolder.mTeacherListen = (ImageView) convertView
						.findViewById(R.id.imv_listen);
				viewHolder.mTeacherName = (TextView) convertView
						.findViewById(R.id.tv_teacher_name);
				viewHolder.mTeacherSubjects = (TextView) convertView
						.findViewById(R.id.tv_subjects);
				viewHolder.mTeacherArea = (TextView) convertView
						.findViewById(R.id.tv_teaching_area);
				viewHolder.mTeacherFees = (TextView) convertView
						.findViewById(R.id.tv_teacher_class_fees);
				viewHolder.mTeachingAge = (TextView) convertView
						.findViewById(R.id.tv_teaching_age);
				viewHolder.mTRatingBar = (RatingBar) convertView
						.findViewById(R.id.ratingbar_teacher);
				convertView.setTag(viewHolder);
				viewHolder.mTeacherIdentify.setVisibility(View.GONE);
				
				viewHolder.mTeacherVerify.setVisibility(View.GONE);
				viewHolder.mTeacherListen.setVisibility(View.GONE);
				break;
			}
		} else {
			viewHolder = (CollectViewHolder) convertView.getTag();
		}
		String imageUri = null;

		// 0学生，1老师
		final DxNeed dxNeed = collect.getNeed();
		if (collect.getType() == 0) {
			// viewHolder.mCollectTeacher.setVisibility(View.GONE);
			// viewHolder.mCollectStudent.setVisibility(View.VISIBLE);
			imageUri = dxNeed.getDxUser().getAvatarUrl();
			if (!TextUtils.isEmpty(imageUri)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + imageUri,
						viewHolder.mStudentAvatar,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mStudentAvatar.setImageResource(R.drawable.avatar);
			}

			if (!TextUtils.isEmpty(dxNeed.getNeedTitle())) {
				viewHolder.mNeedTitle.setText(dxNeed.getNeedTitle());
			} else {
				viewHolder.mNeedTitle.setText("无标题");
			}
			viewHolder.mStudentSubjects.setText(dxNeed.getSubjectItemId());
			if (dxNeed.getTradeNumber() == null || dxNeed.getTradeNumber() == 0) {
				viewHolder.mStudentTimes.setText("面议");
			} else {
				viewHolder.mStudentTimes.setText(dxNeed.getTradeNumber()
						+ "次/周  ");
			}
			double price = dxNeed.getPrice();
			if (price == 0) {
				viewHolder.mStudentFees.setText("面议");
			} else {
				viewHolder.mStudentFees.setText(price + "元/小时");
			}
			

			viewHolder.mStudentIdentify.setVisibility(View.GONE);
			viewHolder.mStudentVerify.setVisibility(View.GONE);
			int identify = dxNeed.getDxUser().getIdentify();
			for (int i = 0; i < Constants.LABEL_NUMBER_STUDENT; i++) {
				if ((identify & (1 << i)) != 0) {
					switch (i) {
					case 2:
						viewHolder.mStudentIdentify.setVisibility(View.VISIBLE);
						break;
					case 1:
						viewHolder.mStudentVerify.setVisibility(View.VISIBLE);
						break;
					}
				}
			}

			if (dxNeed.getDxUser().getRank() != null) {
				float rating = dxNeed.getDxUser().getRank();
				viewHolder.mSRatingBar.setRating(rating);
			} else {
				viewHolder.mSRatingBar.setRating(0);
			}
			viewHolder.mCollectStudent
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									StudentIntroActivity.class);
							intent.putExtra("dxNeed", dxNeed);
							intent.putExtra("type", "Collect");
							mContext.startActivity(intent);
						}
					});
		} else {
			final DxTeacherList dxTeacherList = collect.getDxTeacherList();
			// viewHolder.mCollectTeacher.setVisibility(View.VISIBLE);
			// viewHolder.mCollectStudent.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(dxTeacherList.getAvatarUrl())) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + dxTeacherList.getAvatarUrl(),
						viewHolder.mTeacherAvatar,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mTeacherAvatar.setImageResource(R.drawable.avatar);
			}

			String s = "";
			String subjectItem = dxTeacherList.getSubject();
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
			if (!TextUtils.isEmpty(s)) {
				s = s.substring(0, s.length() - 1);
			} else {
				s = "无";
			}
			viewHolder.mTeacherSubjects.setText(s);

			String area;
			String province = dxTeacherList.getProvince().toString().trim();
			String city = dxTeacherList.getCity().toString().trim();
			String region = dxTeacherList.getRegion().toString().trim();
			if (province.equals(city)) {
				area = province + region;
			} else {
				area = province + city;
			}
			viewHolder.mTeacherArea.setText(area);

			if (dxTeacherList.getCoachTime() != null) {
				viewHolder.mTeachingAge.setText(StrUtil
						.setCoachTime(dxTeacherList.getCoachTime()));
			} else {
				viewHolder.mTeachingAge.setText("不足1年");
			}
			if (dxTeacherList.getPrice() != null) {
				String price = dxTeacherList.getPrice();
				if (price.equals("0")) {
					viewHolder.mTeacherFees.setText("面议");
				} else {
					viewHolder.mTeacherFees.setText(price + "元/小时");
				}
			} else {
				viewHolder.mTeacherFees.setText("面议");
			}
			if (!TextUtils.isEmpty(dxTeacherList.getUserName())) {
				viewHolder.mTeacherName.setText(dxTeacherList.getUserName());
			} else {
				viewHolder.mTeacherName.setText("无");
			}

			viewHolder.mTeacherIdentify.setVisibility(View.GONE);
			viewHolder.mTeacherVerify.setVisibility(View.GONE);
			viewHolder.mTeacherListen.setVisibility(View.GONE);
			int identify = dxTeacherList.getIdentify();
			for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
				if ((identify & (1 << i)) != 0) {
					switch (i) {
					case 2:
						viewHolder.mTeacherIdentify.setVisibility(View.VISIBLE);
						break;
					case 1:
						viewHolder.mTeacherVerify.setVisibility(View.VISIBLE);
						break;
					case 0:
						viewHolder.mTeacherListen.setVisibility(View.VISIBLE);
						break;
					}
				}
			}

			if (dxTeacherList.getRank() != null) {
				float rating = dxTeacherList.getRank();
				viewHolder.mTRatingBar.setRating(rating);
			} else {
				viewHolder.mTRatingBar.setRating(0);
			}
			viewHolder.mCollectTeacher
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String url = UrlEngine
									.getTeacherDetail(dxTeacherList.getId());
							HttpUtil.post(url, new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									if (statusCode == Constants.STAT_200) {
										try {
											DxTeacherinfo dxTeacherinfo = new DxTeacherinfo();
											dxTeacherinfo
													.initWithAttributes(response);
											Intent intent = new Intent(
													mContext,
													TeacherIntroActivity.class);
											intent.putExtra("dxTeacherinfo",
													dxTeacherinfo);
											intent.putExtra("dxTeacher",
													dxTeacherList);
											mContext.startActivity(intent);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, Throwable throwable,
										JSONObject errorResponse) {
									if (statusCode == 0) {
										Toast.makeText(
												mContext,
												R.string.text_link_server_error,
												1).show();
									}
									if (statusCode == Constants.STAT_401
											|| statusCode == Constants.STAT_403
											|| statusCode == Constants.STAT_404) {
										Toast.makeText(mContext,
												R.string.text_error_network, 1)
												.show();
									}
								}
							});
						}
					});
		}

		return convertView;
	}

	class CollectViewHolder {
		public TextView mTeacherArea;
		private LinearLayout mCollectStudent;
		private LinearLayout mCollectTeacher;

		private ImageView mStudentAvatar;
		private TextView mNeedTitle;
		private ImageView mStudentIdentify;
		private ImageView mStudentVerify;
		private RatingBar mSRatingBar;
		private TextView mStudentSubjects;
		private TextView mStudentTimes;
		private TextView mStudentFees;

		private ImageView mTeacherAvatar;
		private ImageView mTeacherIdentify;
		private ImageView mTeacherVerify;
		private ImageView mTeacherListen;
		private RatingBar mTRatingBar;
		private TextView mTeacherName;
		private TextView mTeacherSubjects;
		private TextView mTeachingAge;
		private TextView mTeacherFees;

	}
}
