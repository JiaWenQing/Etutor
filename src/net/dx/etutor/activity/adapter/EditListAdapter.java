package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.model.Channel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 管理卡片的adapter
 * 
 * @author jwq
 * 
 */
public class EditListAdapter extends BaseAdapter {

	/**
	 * 用户选定
	 */
	private List<Channel> userChannelList = new ArrayList<Channel>();
	private Context mContext;

	private LayoutInflater mInflater;

	public EditListAdapter(Context context, List<Channel> userChannelList) {
		// TODO Auto-generated constructor stub
		this.userChannelList = userChannelList;
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public class ViewHolder {
		ImageButton cardMove;
		TextView cardStatus;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userChannelList.size();
	}

	@Override
	public Channel getItem(int position) {
		// TODO Auto-generated method stub
		return userChannelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
		final int type = getItem(position).getSelected();
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_card_contral, null);
			holder = new ViewHolder();
			holder.cardMove = (ImageButton) convertView
					.findViewById(R.id.tv_card_move);
			holder.cardStatus = (TextView) convertView
					.findViewById(R.id.tv_card_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Channel channel = getItem(position);
		String name = channel.getName();
		Integer statu = channel.getSelected();
		if (statu == 1) {
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.ok);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.cardStatus.setCompoundDrawables(drawable, null, null, null);
		} else {
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.empty);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.cardStatus.setCompoundDrawables(drawable, null, null, null);
		}
		holder.cardStatus.setText(name);
		holder.cardStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateData(pos, type);
			}
		});

		return convertView;
	}

	public void up(int position) {
		Channel channel = userChannelList.get(position);
		userChannelList.remove(position);
		userChannelList.add(0, channel);
		this.notifyDataSetChanged();
	}

	public void down(int position) {
		Channel channel = userChannelList.get(position);
		userChannelList.remove(position);
		userChannelList.add(channel);
		this.notifyDataSetChanged();
	}

	public void updateData(int pos, int type) {
		if (type == 1) {
			userChannelList.get(pos).setSelected(0);
			down(pos);
		} else {
			userChannelList.get(pos).setSelected(1);
			up(pos);
		}
	}

	public void remove(int index) {
		userChannelList.remove(index);
		this.notifyDataSetChanged();
	}

	public void insert(Channel channel, int index) {
		userChannelList.add(index, channel);
		this.notifyDataSetChanged();
	}

}
