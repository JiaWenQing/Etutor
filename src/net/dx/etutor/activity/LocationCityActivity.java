package net.dx.etutor.activity;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.db.MyDatabase;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class LocationCityActivity extends BaseActivity implements
		OnItemClickListener {
	public static String TAG = "LocationCityActivity";
	private ListView mListView;
	private TextView mTitle;
	private TextView mBack;
	// private List<String> mList;
	private LocationAdapter mAdapter;
	private List<String> pList;
	private String mCity = EtutorApplication.getInstance().getSpUtil()
			.getLocationCity();
	private int p;

	@Override
	public void initViews() {
		setContentView(R.layout.activity_location_city);
		setTitle(R.string.title_location);
		mCity = EtutorApplication.getInstance().getSpUtil().getLocationCity();
		// mLocationCity = (TextView) findViewById(R.id.tv_location_city);
		mListView = (ListView) findViewById(R.id.lv_location_city);
		mTitle = (TextView) findViewById(R.id.main_head_bar_title);
		mBack = (TextView) findViewById(R.id.main_head_bar_back);
		mTitle.setText(R.string.title_location);
		initData();
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		mListView.setOnItemClickListener(this);
	}

	private static MyDatabase database = EtutorApplication.getInstance()
			.getMyDatabase();

	private void initData() {
		// TODO Auto-generated method stub
		pList = database.getProvinces();
		pList.remove(0);
		mAdapter = new LocationAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setSelection(p);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LocationCityActivity.this,
				MainFragmentActivity.class);
		intent.putExtra("city", mApplication.getSpUtil().getLocationCity());
		setResult(RESULT_OK, intent);
		finish();
	}

	class LocationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pList.size();
		}

		@Override
		public String getItem(int position) {
			return pList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.item_location_city, null);
				viewHolder = new ViewHolder();
				viewHolder.mCityName = (TextView) convertView
						.findViewById(R.id.location_list_item_content);
				viewHolder.mLayout = (LinearLayout) convertView
						.findViewById(R.id.location_layout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (mCity.equals(pList.get(position))) {
				viewHolder.mCityName.setTextColor(getResources().getColor(
						R.color.color_location_selected));
			} else {
				viewHolder.mCityName.setTextColor(getResources().getColor(
						R.color.text_login_color));
			}
			viewHolder.mCityName.setText(pList.get(position).toString());
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView mCityName;
		private LinearLayout mLayout;

	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mCity = pList.get(position);
		mAdapter.notifyDataSetChanged();
		Intent intent = new Intent(LocationCityActivity.this,
				MainFragmentActivity.class);
		intent.putExtra("city", pList.get(position));
		setResult(RESULT_OK, intent);
		finish();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

}
