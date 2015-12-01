package net.dx.etutor.dialog;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserSexDialog extends BaseDialog implements OnItemClickListener {

	private Context mContext;
	private ListView mListView;
	private UserSexDialogCallBack mCallBack;
	private List<String> mList;
	private LocationAdapter mAdapter;

	public UserSexDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.dialog_location_layout);
		setDialogTitle(R.string.choice_sex);
		mListView = (ListView) findViewById(R.id.location_dialog_list);
		this.mContext = context;
		initData();
		mAdapter = new LocationAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<String>();
		mList.add("保密");
		mList.add("男");
		mList.add("女");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mCallBack.setBackValue(this, mAdapter.getItem(position));
	}

	public interface UserSexDialogCallBack {
		void setBackValue(Dialog dialog, String value);
	}

	public void setUserSexDialogCallBack(UserSexDialogCallBack callBack) {
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
