package net.dx.etutor.activity.adapter;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxForumBoard;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public  class InterestingAdapter extends BaseAdapter {
	private List<DxForumBoard> mList;
	private Context mContext;
	public InterestingAdapter(List<DxForumBoard> mList,Context mContext) {
		this.mList=mList;
		this.mContext=mContext;
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
					R.layout.item_board_forum, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.activity_forum_image);
			viewHolder.mName = (TextView) convertView
					.findViewById(R.id.forum_title);
			viewHolder.mDescription = (TextView) convertView
					.findViewById(R.id.forum_title_fabu);
			viewHolder.test_collect = (TextView) convertView
					.findViewById(R.id.forum_colltct);
			viewHolder.test_collect.setVisibility(View.GONE);
			viewHolder.test_fabu_number = (TextView) convertView
					.findViewById(R.id.forum_fabu_number);
			viewHolder.test_collect_number = (TextView) convertView
					.findViewById(R.id.forum_colltct_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		DxForumBoard board = (DxForumBoard) getItem(position);
		// 图片加载
		ImageLoader.getInstance().displayImage(
				DataParam.REMOTE_SERVE + board.getIconUrl(),
				viewHolder.mImageView, ImageLoadOptionsUtil.getOptions());
		viewHolder.mName.setText(board.getName().toString());
		viewHolder.mDescription.setText(board.getDescription());

		return convertView;
	}
	class ViewHolder {
		private ImageView mImageView;
		private TextView mName;
		private TextView mDescription;
		private TextView test_collect;
		private TextView test_fabu_number;
		private TextView test_collect_number;
	}
}
