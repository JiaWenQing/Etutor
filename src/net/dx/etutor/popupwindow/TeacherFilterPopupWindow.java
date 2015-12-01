package net.dx.etutor.popupwindow;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BasePopupWindow;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherFilterPopupWindow extends BasePopupWindow implements
		OnItemClickListener {

	private ListView mPopListView;
	private List<String> mList;
	private Context mContext;
	private FilterAdapter mAdapter;
	private FilterOnClickListener filterOnClickListener;
	private TextView mTextView;

	public TeacherFilterPopupWindow(Context context, TextView textView) {
		super(LayoutInflater.from(context).inflate(
				R.layout.view_teacher_filter_layout, null),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContext = context;
		mTextView = textView;
		setAnimationStyle(R.style.popwin_anim_style);
	}

	public TeacherFilterPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(
				R.layout.view_teacher_filter_layout, null), width, height);
		mContext = context;
		setAnimationStyle(R.style.popwin_anim_style);
	}

	@Override
	public void initViews() {
		mPopListView = (ListView) findViewById(R.id.pop_filter_list);
		initData();
		mAdapter = new FilterAdapter();
		mPopListView.setAdapter(mAdapter);
		mPopListView.setOnItemClickListener(this);
	}

	@Override
	public void initEvents() {

	}

	private void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<String>();
		mList.add("不限");
		mList.add("北京");
		mList.add("上海");
		mList.add("广州");
		mList.add("深圳");
		mList.add("重庆");
		mList.add("厦门");
		mList.add("苏州");
		mList.add("杭州");
	}

	@Override
	public void init() {
	}

	public interface FilterOnClickListener {
		void filterOnClick(String value);
	}

	public void setFilterOnClickListener(FilterOnClickListener l) {
		this.filterOnClickListener = l;
	}

	class FilterAdapter extends BaseAdapter {

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
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_teacher_filter, null);
				viewHolder = new ViewHolder();
				viewHolder.mFilterTitle = (TextView) convertView
						.findViewById(R.id.filter_item_title);
				convertView
						.setBackgroundResource(R.drawable.selector_search_teacher_filter);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mFilterTitle.setText(mList.get(position).toString());
			return convertView;
		}

	}

	private class ViewHolder {
		private TextView mFilterTitle;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.search_teacher_filter_arrow);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		filterOnClickListener.filterOnClick(mList.get(position));
		mTextView.setCompoundDrawables(null, null, drawable, null);
		this.dismiss();
	}
}
