package net.dx.etutor.activity.adapter;

import java.util.List;

import net.dx.etutor.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

public class LocationPersonalAdapter extends BaseAdapter {
	
	List<PoiInfo> mList;
	private Context mContext;
	
	
	public LocationPersonalAdapter(Context mContext,List<PoiInfo> mList) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.mList=mList;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_personal_location, null);
			viewHolder = new ViewHolder();
			viewHolder.mName = (TextView) convertView
					.findViewById(R.id.tv_location_name);
			viewHolder.mAddress = (TextView) convertView
					.findViewById(R.id.tv_location_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PoiInfo info=(PoiInfo) getItem(position);
		viewHolder.mName.setText(info.name);
		viewHolder.mAddress.setText(info.address);
		return convertView;
	}
	class ViewHolder {
		private TextView mName;
		private TextView mAddress;
	}
	public void setPoiList(List<PoiInfo> mList) {
		// TODO Auto-generated method stub
		this.mList=mList;
		notifyDataSetChanged();
	}

}
