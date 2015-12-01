package net.dx.etutor.activity.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.academy.AgencyDetailActivity;
import net.dx.etutor.activity.academy.SchoolDetailActivity;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxAgencyinfo;
import net.dx.etutor.model.DxSchoolinfo;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 学校和机构的Adapter
 */
public class MyAcademyAdapter extends BaseAdapter {

	private List mList = new ArrayList();
	private Context mContext;
	private int mBaoJi;
	private DxSchoolinfo school;
	private DxAgencyinfo agencyinfo;

	public MyAcademyAdapter(List mList, Context mContext, int mBaoJi) {
		this.mList = mList;
		this.mContext = mContext;
		this.mBaoJi = mBaoJi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SchoolViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_agency, null);
			viewHolder = new SchoolViewHolder();
			viewHolder.mImageUrl = (ImageView) convertView
					.findViewById(R.id.iv_agency_pic);
			viewHolder.mName = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.mPhone = (TextView) convertView
					.findViewById(R.id.tv_telephone);
			viewHolder.mAddress = (TextView) convertView
					.findViewById(R.id.tv_address);
			viewHolder.mDistance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			viewHolder.mLayoutRoot = (RelativeLayout) convertView
					.findViewById(R.id.agency_layout_root);
			viewHolder.mImageUrl.setBackground(mContext.getResources()
					.getDrawable(R.drawable.home_school));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (SchoolViewHolder) convertView.getTag();
		}
		String imagePath = null;
		if (mBaoJi == 0) {
			school = (DxSchoolinfo) getItem(position);
			imagePath = school.getImageUrl();
		} else {
			agencyinfo = (DxAgencyinfo) getItem(position);
			imagePath = agencyinfo.getImageUrl();
		}
		if (!TextUtils.isEmpty(imagePath)) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + imagePath, viewHolder.mImageUrl,
					ImageLoadOptionsUtil.getOptions());
		} else {
			viewHolder.mImageUrl.setImageResource(R.drawable.home_school);
		}
		String distance;
		double d = 0;
		DecimalFormat df = null;
		if (mBaoJi == 0) {
			viewHolder.mName.setText(school.getName());
			viewHolder.mPhone.setText(school.getPhoneNumber());
			viewHolder.mAddress.setText(school.getAddress());
			df = new DecimalFormat("#.0");
			d = school.getDistance();
		} else {
			viewHolder.mName.setText(agencyinfo.getName());
			viewHolder.mPhone.setText(agencyinfo.getPhoneNumber());
			viewHolder.mAddress.setText(agencyinfo.getAddress());
			df = new DecimalFormat("#.0");
			d = agencyinfo.getDistance();
		}

		if (d < 1000) {
			distance = (int) d + "米";
		} else if (d > 500000) {
			distance = ">500km";
		} else {
			distance = df.format(d / 1000) + "km";
		}
		viewHolder.mDistance.setText(distance);
		viewHolder.mLayoutRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBaoJi == 0) {
					Intent intent = new Intent(mContext,
							SchoolDetailActivity.class);
					intent.putExtra("school", school);
					mContext.startActivity(intent);
				} else {
					Intent intent = new Intent(mContext,
							AgencyDetailActivity.class);
					intent.putExtra("agency", agencyinfo);
					mContext.startActivity(intent);
				}

			}

		});
		return convertView;
	}

	class SchoolViewHolder {
		private ImageView mImageUrl;
		private TextView mName;
		private TextView mAddress;
		public TextView mDistance;
		private TextView mPhone;
		private RelativeLayout mLayoutRoot;
	}
}