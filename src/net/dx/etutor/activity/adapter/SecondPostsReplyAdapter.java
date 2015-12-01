package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.forum.InformPostsActivity;
import net.dx.etutor.activity.forum.PostsDetailActivity;
import net.dx.etutor.activity.forum.SecondPostsListActivity;
import net.dx.etutor.activity.forum.SendPostsActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumReply;
import net.dx.etutor.model.DxForumReplySecond;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.DateUtil;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.UiUtil;
import net.dx.etutor.view.imageview.ClickImageView;
import net.dx.etutor.view.imageview.RoundImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 楼中楼、2级回复的adapter
 * 
 * @author jwq
 * 
 */
public class SecondPostsReplyAdapter extends BaseAdapter {

	/**
	 * ListView 中视图的种类个数 必须准确指定这个值， 并覆盖超类的getViewTypeCount()和getItemViewType（）方法
	 * 否则不能正常加载不同的View
	 */
	private final int MEX_ITEM_TYPE = 2;
	private DxForumReply dxForumReply;
	private List<DxForumReplySecond> mList = new ArrayList<DxForumReplySecond>();
	private Context mContext;
	private LayoutInflater inflater;
	private int flag = 0;

	private PopupWindow popupWindow;
	/**
	 * 回复
	 */
	private RadioButton mRbReply;
	/**
	 * 举报
	 */
	private RadioButton mRbInform;

	public SecondPostsReplyAdapter(Context mContext, DxForumReply dxForumReply,
			List<DxForumReplySecond> mList,int flag) {
		// TODO Auto-generated constructor stub
		this.dxForumReply = dxForumReply;
		this.mContext = mContext;
		this.mList = mList;
		this.flag = flag;
		inflater = LayoutInflater.from(mContext);
		initPopWindow();
	}

	/**
	 * 初始化弹出的pop
	 * */
	private void initPopWindow() {
		View popView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_reply_menu, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// 设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		mRbReply = (RadioButton) popView.findViewById(R.id.rb_reply);
		mRbInform = (RadioButton) popView.findViewById(R.id.rb_inform);

	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type = super.getItemViewType(position);
		if (position == 0) {
			type = 0;
		} else {
			type = 1;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return MEX_ITEM_TYPE;
	}

	@Override
	public int getCount() {
		return mList.size() + 1;
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
		final int index = position;
		ReplyViewHolder viewHolder;
		int type = position == 0 ? 0 : 1;
		if (convertView == null) {
			switch (type) {
			case 0:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_posts_relpy_list, null);
				viewHolder = new ReplyViewHolder();
				viewHolder.mPosterAvatar = (RoundImageView) convertView
						.findViewById(R.id.imv_poster_avatar);
				viewHolder.mPosterName = (TextView) convertView
						.findViewById(R.id.tv_poster_name);
				viewHolder.mIsPoster = (TextView) convertView
						.findViewById(R.id.tv_is_poster);
				viewHolder.mPostsMenu = (TextView) convertView
						.findViewById(R.id.tv_posts_menu);
				viewHolder.mIsBelaud = (RadioButton) convertView
						.findViewById(R.id.rb_isbelaud);
				viewHolder.mFloor = (TextView) convertView
						.findViewById(R.id.tv_floor);
				viewHolder.mPostsTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				viewHolder.mContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				viewHolder.mReplyMore = (TextView) convertView
						.findViewById(R.id.tv_look_more_reply);
				for (int i = 0; i < 3; i++) {
					viewHolder.mImages[i] = (ClickImageView) convertView
							.findViewById(R.id.img_1_1 + i);
				}
				convertView.setTag(viewHolder);
				break;

			default:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_my_posts_simple, null);
				viewHolder = new ReplyViewHolder();
				viewHolder.mSendPosterName = (TextView) convertView
						.findViewById(R.id.tv_posts_send_name);
				viewHolder.mIsPoster = (TextView) convertView
						.findViewById(R.id.tv_is_poster);
				viewHolder.mReceivePosterName = (TextView) convertView
						.findViewById(R.id.tv_posts_receive_name);
				viewHolder.mPostsContent = (TextView) convertView
						.findViewById(R.id.tv_posts_content);
				viewHolder.mPostsTime = (TextView) convertView
						.findViewById(R.id.tv_reply_time);
				viewHolder.mInform = (TextView) convertView
						.findViewById(R.id.tv_inform);

				convertView.setTag(viewHolder);
				break;
			}

		} else {
			viewHolder = (ReplyViewHolder) convertView.getTag();
		}
		if (type == 0) {
			if (flag==0) {
				viewHolder.mIsBelaud.setVisibility(View.VISIBLE);
			}else {
				viewHolder.mIsBelaud.setVisibility(View.GONE);
			}
			String name = dxForumReply.getUserName();
			String content = dxForumReply.getContent();
			String avatarUrl = dxForumReply.getAvatarUrl();
			String time = dxForumReply.getCreateTime();
			String floor = dxForumReply.getReplyIndex();
			int belauds = dxForumReply.getBelauds();
			int isBelaud = dxForumReply.getIsBelaud();
			viewHolder.mIsBelaud.setText(belauds + "");
			if (isBelaud == 0) {
				viewHolder.mIsBelaud.setChecked(false);
			} else {
				viewHolder.mIsBelaud.setChecked(true);
			}
			int po = dxForumReply.getOp();
			if (po == 0) {
				viewHolder.mIsPoster.setVisibility(View.GONE);
			} else {
				viewHolder.mIsPoster.setVisibility(View.VISIBLE);
			}

			if (!TextUtils.isEmpty(name)) {
				viewHolder.mPosterName.setText(name);
			}
			if (!TextUtils.isEmpty(avatarUrl)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + avatarUrl,
						viewHolder.mPosterAvatar,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mPosterAvatar.setImageResource(R.drawable.avatar);
			}
			if (!TextUtils.isEmpty(floor)) {
				viewHolder.mFloor.setText("第 " + floor + " 楼");
			}
			if (!TextUtils.isEmpty(time)) {
				viewHolder.mPostsTime.setText(time);
			}
			if (!TextUtils.isEmpty(content)) {
				List<String> list = StrUtil.getImgSrc(content);
				if (list.size() != 0) {
					viewHolder.mContent.setText(Html.fromHtml(content
							.substring(0, content.lastIndexOf("<br/>"))));
					for (int i = 0; i < list.size(); i++) {
						viewHolder.mImages[i].setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								DataParam.REMOTE_SERVE + list.get(i),
								viewHolder.mImages[i],
								ImageLoadOptionsUtil.getOptions());
						viewHolder.mImages[i].setTag(list.get(i));
					}
				} else {
					viewHolder.mContent.setText(Html.fromHtml(content));
				}
			}
			viewHolder.mPostsMenu.setOnClickListener(new popAction(position,
					dxForumReply));
			viewHolder.mIsBelaud.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addPraise();
				}
			});
		} else {
			final DxForumReplySecond reply = mList.get(position - 1);
			String sendName = reply.getUserName();
			String receiveName = reply.getReplyUserName();
			String content = reply.getContent();
			String time = DateUtil.getReplyPostsTime(reply.getCreateTime());
			content = content.split("<br/>")[0];
			int po = reply.getOp();
			if (po == 0) {
				viewHolder.mIsPoster.setVisibility(View.GONE);
			} else {
				viewHolder.mIsPoster.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(sendName)) {
				viewHolder.mSendPosterName.setText(sendName);
			}
			if (!TextUtils.isEmpty(receiveName)) {
				viewHolder.mReceivePosterName.setText("回复 " + receiveName
						+ " :");
			}
			if (!TextUtils.isEmpty(content)) {
				viewHolder.mPostsContent.setText(content);
			}
			if (!TextUtils.isEmpty(time)) {
				viewHolder.mPostsTime.setText(time);
			}

			viewHolder.mSendPosterName
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onReply(index);
						}
					});
			viewHolder.mInform.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onInform(index);
				}
			});
		}
		return convertView;
	}

	class ReplyViewHolder {
		public TextView mReplyMore;
		public TextView mContent;
		public TextView mFloor;
		public RadioButton mIsBelaud;
		public TextView mPostsMenu;
		public TextView mPosterName;
		public RoundImageView mPosterAvatar;
		private TextView mIsPoster;
		private TextView mSendPosterName;
		private TextView mReceivePosterName;
		private TextView mPostsContent;
		private TextView mInform;
		private TextView mPostsTime;

		private ClickImageView mImage0;
		private ClickImageView mImage1;
		private ClickImageView mImage2;
		private ImageView[] mImages = { mImage0, mImage1, mImage2 };

	}

	class popAction implements OnClickListener {
		DxForumReply dxForumReply;
		int position;

		public popAction(int position, DxForumReply dxForumReply) {
			this.position = position;
			this.dxForumReply = dxForumReply;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.tv_posts_send_name) {
				mRbInform.setVisibility(View.GONE);
			} else {
				mRbInform.setVisibility(View.VISIBLE);
			}
			int[] arrayOfInt = new int[2];
			// 获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			showPop(v, x, y, position, dxForumReply);
		}
	}

	/**
	 * 显示popWindow
	 * */
	public void showPop(final View parent, int x, int y, final int postion,
			final DxForumReply dxForumReply) {

		// 设置popwindow显示位置
		popupWindow
				.showAtLocation(parent, 0, x - parent.getWidth() * 4, y - 15);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				UiUtil.setIcon(mContext,
						R.drawable.icon_posts_more_menu_normal,
						(TextView) parent);
			}
		});
		popupWindow.update();
		if (popupWindow.isShowing()) {
			UiUtil.setIcon(mContext, R.drawable.icon_posts_more_menu_pressed,
					(TextView) parent);
		}
		mRbReply.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
				onReply(postion);

			}
		});
		mRbInform.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
				onInform(postion);
			}
		});
	}

	/**
	 * 举报
	 * 
	 * @param postion
	 */
	protected void onInform(int postion) {

		Intent intent = new Intent(mContext, InformPostsActivity.class);
		DxForumTopic dxForumTopic = new DxForumTopic();
		switch (postion) {
		case 0:
			dxForumTopic.setDxForumReply(dxForumReply);
			dxForumTopic.setReplyType(3);
			break;
		default:
			DxForumReply reply = new DxForumReply();
			ArrayList<DxForumReplySecond> dxForumReplySeconds = new ArrayList<DxForumReplySecond>();
			dxForumReplySeconds.add(mList.get(postion - 1));
			reply.setDxForumReplySeconds(dxForumReplySeconds);
			dxForumTopic.setDxForumReply(reply);
			dxForumTopic.setReplyType(4);
			break;
		}
		intent.putExtra("dxForumTopic", dxForumTopic);
		((Activity) mContext).startActivity(intent);
	}

	/**
	 * 回复
	 * 
	 * @param postion
	 */
	protected void onReply(int postion) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, SendPostsActivity.class);
		DxForumTopic dxForumTopic = new DxForumTopic();
		dxForumTopic.setReplyType(2);
		if (postion != 0) {
			dxForumTopic.setTitle("回复" + mList.get(postion).getUserName());
			dxForumTopic
					.setId(Integer.valueOf(mList.get(postion).getTopicId()));
			dxForumTopic
					.setReplyId(Integer.valueOf(mList.get(postion).getId()));
		} else {
			dxForumTopic.setTitle("回复" + dxForumReply.getUserName());
			dxForumTopic.setId(Integer.valueOf(dxForumReply.getTopicId()));
			dxForumTopic.setReplyId(Integer.valueOf(dxForumReply.getId()));
		}
		intent.putExtra("topic", dxForumTopic);
		((Activity) mContext).startActivityForResult(intent, 101);
	}

	/**
	 * 点赞
	 */
	private void addPraise() {
		// TODO Auto-generated method stub
		String userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		String urlString = UrlEngine.belaudReply(userId, dxForumReply.getId());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						String status = response.getString("status");
						int isBelaud;
						int belauds=dxForumReply.getBelauds();
						if (status.equals("1")) {
							isBelaud=1;
							belauds++;
						} else {
							isBelaud=0;
							belauds--;
						}
						if (belauds<0) {
							belauds=0;
						}
						dxForumReply.setIsBelaud(isBelaud);
						dxForumReply.setBelauds(belauds);
						notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});
	}

}
