package net.dx.etutor.activity.adapter;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxPrivatemsg;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.view.imageview.RoundHeadImageView;
import net.dx.etutor.view.imageview.RoundImageView;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageDetailAdapter extends BaseAdapter {

	/**
	 * ListView 中视图的种类个数 必须准确指定这个值， 并覆盖超类的getViewTypeCount()和getItemViewType（）方法
	 * 否则不能正常加载不同的View
	 */
	private final int MEX_ITEM_TYPE = 2;
	private String userId;
	private String imagePath;
	private List<DxPrivatemsg> mList;
	private Context mContext;

	public MessageDetailAdapter(Context mContext, List<DxPrivatemsg> mList,
			String imagePath) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		this.imagePath = imagePath;
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
	}

//	@Override
//	public int getItemViewType(int position) {
//		// TODO Auto-generated method stub
//		int type = super.getItemViewType(position);
//		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
//		if (userId.equals(((DxPrivatemsg) getItem(position)).getSenderId())) {
//			type = 1;
//		} else {
//			type = 0;
//		}
//		return type;
//	}
//
//	@Override
//	public int getViewTypeCount() {
//		// TODO Auto-generated method stub
//		return MEX_ITEM_TYPE;
//	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(mList.size() - position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean FlagMsg, FlagVoice, FlagPic, flag;
		int type = 0;
		DxPrivatemsg dxPrivatemsg;
		MessageViewHolder viewHolder = null;
		dxPrivatemsg = (DxPrivatemsg) getItem(position);
		flag = userId.equals(dxPrivatemsg.getSenderId());
		FlagMsg = TextUtils.isEmpty(dxPrivatemsg.getMessage());
		FlagVoice = TextUtils.isEmpty(dxPrivatemsg.getRecord());
		FlagPic = TextUtils.isEmpty(dxPrivatemsg.getPicture());
		if (!FlagMsg && FlagVoice && FlagPic) {
			type = 1;
		} else if (FlagMsg && !FlagVoice && FlagPic) {
			type = 2;
		} else if (FlagMsg && FlagVoice && !FlagPic) {
			type = 3;
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_private_message,
					null);
			viewHolder = new MessageViewHolder();
			viewHolder.mReceiveLayout = (RelativeLayout) convertView
					.findViewById(R.id.layout_receive_message);
			viewHolder.mLayoutReceiveWord = (LinearLayout) convertView
					.findViewById(R.id.layout_receive_words);
			viewHolder.mLayoutReceivePic = (LinearLayout) convertView
					.findViewById(R.id.layout_receive_picture);
			viewHolder.mLayoutReceiveVoice = (LinearLayout) convertView
					.findViewById(R.id.layout_receive_voice);
			viewHolder.mTVReceiveVoiceLength = (TextView) convertView
					.findViewById(R.id.tv_receive_voice_length);

			viewHolder.mSendLayout = (RelativeLayout) convertView
					.findViewById(R.id.layout_send_message);
			viewHolder.mLayoutSendWord = (LinearLayout) convertView
					.findViewById(R.id.layout_send_words);
			viewHolder.mLayoutSendPic = (LinearLayout) convertView
					.findViewById(R.id.layout_send_picture);
			viewHolder.mLayoutSendVoice = (LinearLayout) convertView
					.findViewById(R.id.layout_send_voice);
			viewHolder.mTVSendVoiceLength = (TextView) convertView
					.findViewById(R.id.tv_send_voice_length);

			viewHolder.mSendImageView = (RoundImageView) convertView
					.findViewById(R.id.imv_send_avatar);
			viewHolder.mSendTimes = (TextView) convertView
					.findViewById(R.id.tv_send_time);
			viewHolder.mSendMessage = (TextView) convertView
					.findViewById(R.id.tv_send_message_info);
			viewHolder.mTVSendVoiceTime = (TextView) convertView
					.findViewById(R.id.tv_send_voice_time);
			viewHolder.mRHImvSend = (RoundHeadImageView) convertView
					.findViewById(R.id.imv_send_picture);

			viewHolder.mReceiveImageView = (RoundImageView) convertView
					.findViewById(R.id.imv_receive_avatar);
			viewHolder.mReceiveTimes = (TextView) convertView
					.findViewById(R.id.tv_receive_time);
			viewHolder.mReceiveMessage = (TextView) convertView
					.findViewById(R.id.tv_receive_message_info);
			viewHolder.mTVReceiveVoiceTime = (TextView) convertView
					.findViewById(R.id.tv_receive_voice_time);
			viewHolder.mRHImvReceive = (RoundHeadImageView) convertView
					.findViewById(R.id.imv_receive_picture);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MessageViewHolder) convertView.getTag();
		}
		if (flag) {
			viewHolder.mReceiveLayout.setVisibility(View.GONE);
			viewHolder.mSendLayout.setVisibility(View.VISIBLE);
			viewHolder.mSendLayout
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			String avatarUrl = EtutorApplication.getInstance().getSpUtil().getUserAvatar();
			if (!TextUtils.isEmpty(avatarUrl)) {
				ImageLoader.getInstance().displayImage(avatarUrl,
						viewHolder.mSendImageView,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mSendImageView
						.setImageResource(R.drawable.default_avatar);
			}
			viewHolder.mLayoutSendWord.setVisibility(View.GONE);
			viewHolder.mLayoutSendVoice.setVisibility(View.GONE);
			viewHolder.mLayoutSendPic.setVisibility(View.GONE);
			switch (type) {
			case 2:
				String time = dxPrivatemsg
						.getRecord()
						.substring(
								dxPrivatemsg.getRecord().indexOf("=") + 1,
								dxPrivatemsg.getRecord().length())
						.toString();
				int width = 50 + BaseActivity.getScreenWidth()
						* Integer.parseInt(time) / 110;
				viewHolder.mLayoutSendVoice.setVisibility(View.VISIBLE);
				viewHolder.mTVSendVoiceTime.setText(time);
				viewHolder.mTVSendVoiceLength.setWidth(width);
				break;
			case 3:
				viewHolder.mLayoutSendPic.setVisibility(View.VISIBLE);
				String imageUrl = dxPrivatemsg.getPicture().toString();
				if (!TextUtils.isEmpty(imageUrl)) {
					ImageLoader.getInstance().displayImage(
							DataParam.REMOTE_SERVE + imageUrl,
							viewHolder.mRHImvSend,
							ImageLoadOptionsUtil.getOptions());
				} else {
					viewHolder.mRHImvSend
							.setImageResource(R.drawable.app_logo);
				}
				break;

			default:
				viewHolder.mLayoutSendWord.setVisibility(View.VISIBLE);
				viewHolder.mSendMessage.setText(dxPrivatemsg.getMessage());
				viewHolder.mSendTimes.setText(dxPrivatemsg.getCreateTime());
				break;
			}
		} else {
			viewHolder.mSendLayout.setVisibility(View.GONE);
			viewHolder.mReceiveLayout.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(imagePath)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + imagePath,
						viewHolder.mReceiveImageView,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mReceiveImageView
						.setImageResource(R.drawable.default_avatar);
			}
			viewHolder.mLayoutReceiveWord.setVisibility(View.GONE);
			viewHolder.mLayoutReceiveVoice.setVisibility(View.GONE);
			viewHolder.mLayoutReceivePic.setVisibility(View.GONE);
			switch (type) {
			case 2:
				String time = dxPrivatemsg
						.getRecord()
						.substring(
								dxPrivatemsg.getRecord().indexOf("=") + 1,
								dxPrivatemsg.getRecord().length())
						.toString();
				int width = 50 + BaseActivity.getScreenWidth()
						* Integer.parseInt(time) / 110;
				viewHolder.mLayoutReceiveVoice.setVisibility(View.VISIBLE);
				viewHolder.mTVReceiveVoiceTime.setText(time);
				viewHolder.mTVReceiveVoiceLength.setMinWidth(width);
				break;
			case 3:
				viewHolder.mLayoutReceivePic.setVisibility(View.VISIBLE);

				String imageUrl = dxPrivatemsg.getPicture().toString();
				if (!TextUtils.isEmpty(imageUrl)) {
					ImageLoader.getInstance().displayImage(
							DataParam.REMOTE_SERVE + imageUrl,
							viewHolder.mRHImvReceive,
							ImageLoadOptionsUtil.getOptions());
				} else {
					viewHolder.mRHImvReceive
							.setImageResource(R.drawable.app_logo);
				}
				break;

			default:
				viewHolder.mLayoutReceiveWord.setVisibility(View.VISIBLE);
				viewHolder.mReceiveMessage.setText(dxPrivatemsg
						.getMessage());
				viewHolder.mReceiveTimes.setText(dxPrivatemsg
						.getCreateTime());
				break;
			}

		}

		return convertView;
	}
	class MessageViewHolder {
		private RelativeLayout mReceiveLayout;
		private RelativeLayout mSendLayout;
		private LinearLayout mLayoutReceiveWord;
		private LinearLayout mLayoutSendWord;
		private LinearLayout mLayoutReceiveVoice;
		private TextView mTVReceiveVoiceLength;
		private LinearLayout mLayoutSendVoice;
		private TextView mTVSendVoiceLength;
		private LinearLayout mLayoutReceivePic;
		private LinearLayout mLayoutSendPic;

		private RoundImageView mReceiveImageView;
		private RoundImageView mSendImageView;
		private RoundHeadImageView mRHImvReceive;
		private RoundHeadImageView mRHImvSend;

		private TextView mTVSendVoiceTime;
		private TextView mTVReceiveVoiceTime;
		private TextView mSendMessage;
		private TextView mSendTimes;
		private TextView mReceiveMessage;
		private TextView mReceiveTimes;
	}

}
