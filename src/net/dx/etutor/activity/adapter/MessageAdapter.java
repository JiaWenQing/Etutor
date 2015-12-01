package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageAdapter extends BaseAdapter {
	private String imagePath;
	private int[] msgCount;
	public DxPrivatemsg dxPrivatemsg;
	private List<DxPrivatemsg> mList;
	private Context mContext;
	public MessageAdapter( List<DxPrivatemsg> mList,DxPrivatemsg dxPrivatemsg,Context mContext) {
		this.mList=mList;
		this.dxPrivatemsg=dxPrivatemsg;
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

	boolean FlagMsg, FlagVoice, FlagPic;
	int type = 0;
	private String statu = "";

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_user_message, null);
			viewHolder = new ViewHolder();
			viewHolder.mHeadImageView = (RoundHeadImageView) convertView
					.findViewById(R.id.imv_user_avatar);
			viewHolder.mMessageContent = (TextView) convertView
					.findViewById(R.id.tv_user_message_info);
			viewHolder.mIVMessage = (ImageView) convertView
					.findViewById(R.id.imv_message_info);
			viewHolder.mMessageTitle = (TextView) convertView
					.findViewById(R.id.tv_user_name);
			viewHolder.mMessageCount = (TextView) convertView
					.findViewById(R.id.tv_message_count);
			viewHolder.mMessageTime = (TextView) convertView
					.findViewById(R.id.tv_message_time);
			viewHolder.mRatingBar = (RatingBar) convertView
					.findViewById(R.id.ratingbar);
			viewHolder.mMesageLayout = (LinearLayout) convertView
					.findViewById(R.id.private_message_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		dxPrivatemsg = (DxPrivatemsg) getItem(position);

		FlagMsg = TextUtils.isEmpty(dxPrivatemsg.getMessage());
		FlagVoice = TextUtils.isEmpty(dxPrivatemsg.getRecord());
		FlagPic = TextUtils.isEmpty(dxPrivatemsg.getPicture());
		if (!FlagMsg && FlagVoice && FlagPic) {
			type = 1;
		} else if (FlagMsg && !FlagVoice && FlagPic) {
			type = 2;
		} else if (FlagMsg && FlagVoice && !FlagPic) {
			type = 3;
		} else {
			type = 0;
		}
		if (position == 0) {
			viewHolder.mHeadImageView
					.setImageResource(R.drawable.manager_avatr);
			viewHolder.mMessageTitle.setTextColor(Color.RED);
		} else {
			imagePath = dxPrivatemsg.getDxUser().getAvatarUrl();
			if (!TextUtils.isEmpty(imagePath)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + imagePath,
						viewHolder.mHeadImageView,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mHeadImageView
						.setImageResource(R.drawable.default_avatar);
			}
			viewHolder.mMessageTitle.setTextColor(Color.BLACK);

		}
		String createTime = dxPrivatemsg.getCreateTime();
		//获得未读的条数
		statu=EtutorApplication.getInstance().getSpUtil().getMsgCount();
		if (!TextUtils.isEmpty(statu)) {
			msgCount = new int[statu.length()];
			char[] c = statu.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '1') {
					msgCount[i] = 1;
				} else {
					msgCount[i] = 0;
				}
			}
			}
		if (msgCount[position] == 0) {
			viewHolder.mMessageCount.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.mMessageCount.setVisibility(View.VISIBLE);
		}

		float rating = dxPrivatemsg.getDxUser().getRank();

		viewHolder.mRatingBar.setRating(rating);

		viewHolder.mMessageTitle.setText(dxPrivatemsg.getDxUser()
				.getUserName());
		viewHolder.mMessageTime.setText(createTime);
		viewHolder.mIVMessage.setVisibility(View.GONE);
		viewHolder.mMessageContent.setVisibility(View.GONE);
		switch (type) {
		case 1:
			viewHolder.mMessageContent.setVisibility(View.VISIBLE);
			viewHolder.mMessageContent.setText(dxPrivatemsg.getMessage()
					.toString());
			break;
		case 2:
			viewHolder.mIVMessage.setVisibility(View.VISIBLE);
			viewHolder.mIVMessage
					.setBackgroundResource(R.drawable.icon_voice_blue);
			break;
		case 3:
			viewHolder.mIVMessage.setVisibility(View.VISIBLE);
			viewHolder.mIVMessage
					.setBackgroundResource(R.drawable.icon_pic);
			break;
		default:
			viewHolder.mMessageContent.setVisibility(View.VISIBLE);
			viewHolder.mMessageContent.setText(dxPrivatemsg.getMessage()
					.toString());
			break;
		}
		return convertView;
	}

	class ViewHolder {
		private RoundHeadImageView mHeadImageView;
		private TextView mMessageCount;
		private ImageView mIVMessage;
		private TextView mMessageTitle;
		private TextView mMessageTime;
		private RatingBar mRatingBar;
		private TextView mMessageContent;
		private LinearLayout mMesageLayout;
	}
}
	
