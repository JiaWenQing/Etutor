package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.teacher.TeacherIntroActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.model.DxTeacherinfo;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;

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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 教师的adapter
 * 
 * @author jwq
 * 
 */
public class TeacherAdapter extends BaseAdapter {

	private List<DxTeacherList> mList = new ArrayList<DxTeacherList>();
	private Context mContext;
	private LayoutInflater inflater;

	public TeacherAdapter(Context mContext, List<DxTeacherList> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_collect_teacher, null);
			viewHolder = new ViewHolder();
			viewHolder.mTeacherLayout = (LinearLayout) convertView
					.findViewById(R.id.teacher_layout_collect);
			viewHolder.mRoundImageView = (RoundHeadImageView) convertView
					.findViewById(R.id.imv_teacher_avatar);
			viewHolder.mTeacherName = (TextView) convertView
					.findViewById(R.id.tv_teacher_name);
			viewHolder.mTeachAge = (TextView) convertView
					.findViewById(R.id.tv_teaching_age);
			viewHolder.mSubject = (TextView) convertView
					.findViewById(R.id.tv_subjects);
			viewHolder.mArea = (TextView) convertView
					.findViewById(R.id.tv_teaching_area);
			viewHolder.mTeacherFullTime = (ImageView) convertView
					.findViewById(R.id.imv_teacher_fulltime);
			viewHolder.mTeacherIdentify = (ImageView) convertView
					.findViewById(R.id.imv_teacher_identify);
			viewHolder.mTeacherVerify = (ImageView) convertView
					.findViewById(R.id.imv_teacher_verify);
			viewHolder.mTeacherListen = (ImageView) convertView
					.findViewById(R.id.imv_listen);
			viewHolder.mPrice = (TextView) convertView
					.findViewById(R.id.tv_teacher_class_fees);
			viewHolder.mRating = (RatingBar) convertView
					.findViewById(R.id.ratingbar_teacher);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final DxTeacherList dxTeacher = (DxTeacherList) mList.get(position);
		if (!TextUtils.isEmpty(dxTeacher.getAvatarUrl())) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + dxTeacher.getAvatarUrl(),
					viewHolder.mRoundImageView,
					ImageLoadOptionsUtil.getOptions());
		}else {
			viewHolder.mRoundImageView.setImageResource(R.drawable.avatar);
		}
		if (dxTeacher.getCoachTime() != null) {
			viewHolder.mTeachAge.setText(StrUtil.setCoachTime(dxTeacher
					.getCoachTime()));
		}
		if (!TextUtils.isEmpty(dxTeacher.getUserName())) {
			String name = dxTeacher.getUserName().replace((char) 12288, ' ').trim();
			viewHolder.mTeacherName.setText(name);
		}else {
			viewHolder.mTeacherName.setText("");
		}
		String s = "";
		String subjectItem = dxTeacher.getSubject().trim();
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
		}
		viewHolder.mSubject.setText(s);

		String area;
		String province = dxTeacher.getProvince().toString().trim();
		String city = dxTeacher.getCity().toString().trim();
		String region = dxTeacher.getRegion().toString().trim();
		if (province.equals(city)) {
			area = province + region;
		} else {
			area = province + city;
		}
		viewHolder.mArea.setText(area);

		String price = "0";
		if (dxTeacher.getPrice() != null) {
			price = dxTeacher.getPrice();
		} else {
			price = "0";
		}

		if (price.equals("0")) {
			viewHolder.mPrice.setText("面议");
		} else {
			viewHolder.mPrice.setText(price + "元/小时");
		}

		viewHolder.mTeacherFullTime.setVisibility(View.GONE);
		viewHolder.mTeacherIdentify.setVisibility(View.GONE);
		viewHolder.mTeacherVerify.setVisibility(View.GONE);
		viewHolder.mTeacherListen.setVisibility(View.GONE);
		int identify = dxTeacher.getIdentify();
		for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
			if ((identify & (1 << i)) != 0) {
				switch (i) {
				case 3:
					viewHolder.mTeacherFullTime.setVisibility(View.VISIBLE);
					break;
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

		float rating = dxTeacher.getRank();
		viewHolder.mRating.setRating(rating);

		viewHolder.mTeacherLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = UrlEngine.getTeacherDetail(dxTeacher.getId());
				HttpUtil.post(url, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (statusCode == Constants.STAT_200) {
							try {
								DxTeacherinfo dxTeacherinfo = new DxTeacherinfo();
								dxTeacherinfo.initWithAttributes(response);
								Intent intent = new Intent(mContext,
										TeacherIntroActivity.class);
								intent.putExtra("dxTeacherinfo", dxTeacherinfo);
								intent.putExtra("dxTeacher", dxTeacher);
								intent.putExtra("index", index);
								mContext.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
					}
				});
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView mArea;
		private LinearLayout mTeacherLayout;
		private RoundHeadImageView mRoundImageView;
		private TextView mTeacherName;
		public RatingBar mRating;
		public ImageView mTeacherFullTime;
		private ImageView mTeacherIdentify;
		private ImageView mTeacherVerify;
		private ImageView mTeacherListen;
		private TextView mTeachAge;
		private TextView mSubject;
		private TextView mPrice;
	}
}
