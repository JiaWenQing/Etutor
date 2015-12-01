package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.model.DxOrder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public  class MyOrderAdapter extends BaseAdapter {
	private List<DxOrder> mList = new ArrayList<DxOrder>();
	private Context mContext;
	public MyOrderAdapter(List<DxOrder> mList,Context mContext) {
		this.mList=mList;
		this.mContext=mContext;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_order, null);
			viewHolder = new OrderViewHolder();
			viewHolder.mLectureTime = (TextView) convertView
					.findViewById(R.id.tv_lecture_time);
			viewHolder.mStatus1 = (TextView) convertView
					.findViewById(R.id.tv_status_1);
			viewHolder.mStatus2 = (TextView) convertView
					.findViewById(R.id.tv_status_2);
			viewHolder.mStatus3 = (TextView) convertView
					.findViewById(R.id.tv_status_3);
			viewHolder.mStatus4 = (TextView) convertView
					.findViewById(R.id.tv_status_4);
			viewHolder.mStatus5 = (TextView) convertView
					.findViewById(R.id.tv_status_5);
			viewHolder.mOrderNumber = (TextView) convertView
					.findViewById(R.id.tv_order_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OrderViewHolder) convertView.getTag();
		}
		DxOrder dxOrder = (DxOrder) getItem(position);

		viewHolder.mLectureTime.setText(dxOrder.getFirstCourseTime());
		viewHolder.mOrderNumber.setText(dxOrder.getOrderId());
		viewHolder.mStatus1.setVisibility(View.GONE);
		viewHolder.mStatus2.setVisibility(View.GONE);
		viewHolder.mStatus3.setVisibility(View.GONE);
		viewHolder.mStatus4.setVisibility(View.GONE);
		viewHolder.mStatus5.setVisibility(View.GONE);
		String status = dxOrder.getStatus();
		switch (status) {
		case "0":
			viewHolder.mStatus1.setVisibility(View.VISIBLE);
			break;
		case "1":
			viewHolder.mStatus2.setVisibility(View.VISIBLE);
			break;
		case "2":
			viewHolder.mStatus3.setVisibility(View.VISIBLE);
			break;
		case "3":
			viewHolder.mStatus4.setVisibility(View.VISIBLE);
			break;
		case "4":
			viewHolder.mStatus5.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		return convertView;

	}

	class OrderViewHolder {
		private TextView mLectureTime;
		private TextView mStatus1;
		private TextView mStatus2;
		private TextView mStatus3;
		private TextView mStatus4;
		private TextView mStatus5;
		private TextView mOrderNumber;
	}
}


