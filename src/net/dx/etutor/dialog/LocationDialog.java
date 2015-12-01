package net.dx.etutor.dialog;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.base.BaseDialog;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class LocationDialog extends BaseDialog implements OnItemClickListener {

	private Context mContext;
	private ListView mListView;
	private LocationDialogCallBack mCallBack;
	private List<String> mList;
	private LocationAdapter mAdapter;
	private List<String> pList;
	private String mCity;

	/**
	 * 定位SDK的核心类
	 */
	private LocationClient mLocClient;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	LatLng mLatlng;

	boolean isFirstLoc = true;// 是否首次定位
	double latitude = 31.239004;// 纬度
	double longitude = 121.481647;// 经度

	public LocationDialog(Context context) {
		super(context);
		this.mContext = context;
		LinearLayout.LayoutParams LP_FF = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, BaseActivity.getScreenHeight() / 2);
		setDialogContentView(R.layout.dialog_location_layout, LP_FF);
		setDialogTitle(R.string.selected_city);
		mListView = (ListView) findViewById(R.id.location_dialog_list);

		mLocClient = new LocationClient(mContext);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
							.show();
					setDialogTitle("当前位置:定位失败");
				} else {
					mCity = result.getAddressDetail().province;
					EtutorApplication.getInstance().getSpUtil()
							.setLocationCity(mCity);
					setDialogTitle("当前位置:" + mCity);
				}
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub

			}
		});
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null)
					return;
				if (isFirstLoc) {
					isFirstLoc = false;
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					mLatlng = new LatLng(latitude, longitude);
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(mLatlng));
				}
			}
		});

		initData();
		mAdapter = new LocationAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

	}
	private static MyDatabase database=EtutorApplication.getInstance().getMyDatabase();
	private void initData() {
		// TODO Auto-generated method stub
		if (null == mCity) {
			mCity = EtutorApplication.getInstance().getSpUtil()
					.getLocationCity();
		}
		mList = new ArrayList<String>();
		pList = database.getProvinces();
		for (int i = 0; i < pList.size(); i++) {
			mList.add(pList.get(i).toString());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mCallBack.setBackValue(this, mAdapter.getItem(position));

	}

	public interface LocationDialogCallBack {
		void setBackValue(Dialog dialog, String value);
	}

	public void setLocationDialogCallBack(LocationDialogCallBack callBack) {
		this.mCallBack = callBack;
	}

	class LocationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public String getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.list_item_location_dialog, null);
				viewHolder = new ViewHolder();
				viewHolder.mCityName = (TextView) convertView
						.findViewById(R.id.location_list_item_content);
				convertView
						.setBackgroundResource(R.drawable.selector_search_teacher_filter);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (null != mCity && mCity.equals(mList.get(position))) {
				viewHolder.mCityName.setTextColor(Color.DKGRAY);
				viewHolder.mCityName.setTextSize(18);
			} else {
				viewHolder.mCityName.setTextColor(Color.BLACK);
			}
			viewHolder.mCityName.setText(mList.get(position).toString());
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView mCityName;
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}
