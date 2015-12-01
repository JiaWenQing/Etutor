package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.ImageShowActivity;
import net.dx.etutor.activity.forum.InformPostsActivity;
import net.dx.etutor.activity.forum.SecondPostsListActivity;
import net.dx.etutor.activity.forum.SendPostsActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxForumReply;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;
import net.dx.etutor.util.UiUtil;
import net.dx.etutor.view.imageview.ClickImageView;
import net.dx.etutor.view.imageview.RoundHeadImageView;
import net.dx.etutor.view.imageview.RoundImageView;
import net.dx.etutor.view.pulltorefresh.PullToRefreshListView;

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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帖子详情的adapter
 * 
 * @author jwq
 * 
 */
public class FirstPostsReplyAdapter extends BaseAdapter {

	/**
	 * ListView 中视图的种类个数 必须准确指定这个值， 并覆盖超类的getViewTypeCount()和getItemViewType（）方法
	 * 否则不能正常加载不同的View
	 */
	private final int MEX_ITEM_TYPE = 2;
	private List<DxForumTopic> mList = new ArrayList<DxForumTopic>();
	private Context mContext;
	private SecondPostsReplyAdapter secondPostsReplyAdapter;
	private PopupWindow popupWindow;
	/**
	 * 回复
	 */
	private RadioButton mRbReply;
	/**
	 * 举报
	 */
	private RadioButton mRbInform;
	private String userId;

	public FirstPostsReplyAdapter(Context mContext, List<DxForumTopic> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
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
		if (null != mList.get(position)) {
			if (position == 0) {
				type = 0;
			} else {
				type = 1;
			}
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

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		PostsViewHolder viewHolder = null;
		int type = position == 0 ? 0 : 1;
		if (convertView == null) {
			switch (type) {
			case 0:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_first_posts, null);
				viewHolder = new PostsViewHolder();
				viewHolder.mPosterAvatar = (RoundHeadImageView) convertView
						.findViewById(R.id.imv_poster_avatar);
				viewHolder.mPosterName = (TextView) convertView
						.findViewById(R.id.tv_poster_name);
				viewHolder.mIdentify = (TextView) convertView
						.findViewById(R.id.tv_identify);
				viewHolder.mVerify = (TextView) convertView
						.findViewById(R.id.tv_verify);
				viewHolder.mRating = (RatingBar) convertView
						.findViewById(R.id.ratingbar_teacher);
				viewHolder.mPostsTime = (TextView) convertView
						.findViewById(R.id.tv_posts_time);
				viewHolder.mPostsPVCount = (TextView) convertView
						.findViewById(R.id.tv_posts_pv_count);
				viewHolder.mPostsReplyCount = (TextView) convertView
						.findViewById(R.id.tv_posts_reply_count);
				viewHolder.mPostsTitle = (TextView) convertView
						.findViewById(R.id.tv_posts_title);
				viewHolder.mInform = (TextView) convertView
						.findViewById(R.id.tv_inform);
				viewHolder.mImageLaout = (LinearLayout) convertView
						.findViewById(R.id.img_layout);
				viewHolder.mContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				for (int i = 0; i < 3; i++) {
					viewHolder.mImages[i] = (ClickImageView) convertView
							.findViewById(R.id.img_1_1 + i);
				}
				viewHolder.mPostsPraiseCount = (TextView) convertView
						.findViewById(R.id.tv_praise_users);
				convertView.setTag(viewHolder);
				break;
			default:

				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_list, null);
				viewHolder = new PostsViewHolder();
				viewHolder.mListView = (ListView) convertView
						.findViewById(R.id.lv_public_listview);
				viewHolder.mPostsMore = (TextView) convertView
						.findViewById(R.id.tv_look_more_reply);

				convertView.setTag(viewHolder);
				break;
			}
		} else {
			viewHolder = (PostsViewHolder) convertView.getTag();
		}
		if (type == 0) {
			viewHolder.mImages[0].setVisibility(View.GONE);
			viewHolder.mImages[1].setVisibility(View.GONE);
			viewHolder.mImages[2].setVisibility(View.GONE);
			final DxForumTopic dxForumTopic = mList.get(0);
			String title = dxForumTopic.getTitle();
			String name = dxForumTopic.getUserName();
			String content = dxForumTopic.getDxForumReply().getContent();
			String avatarUrl = dxForumTopic.getAvatarUrl();
			String time = dxForumTopic.getCreateTime();
			int pvCount = dxForumTopic.getClicks();
			int praiseCount = dxForumTopic.getBelauds();
			int replyCount = dxForumTopic.getCount();
			int identify = dxForumTopic.getIdentify();
			int rating = dxForumTopic.getRank();
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
			viewHolder.mRating.setRating(rating);
			viewHolder.mIdentify.setVisibility(View.GONE);
			viewHolder.mVerify.setVisibility(View.GONE);
			for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
				if ((identify & (1 << i)) != 0) {
					switch (i) {
					case 2:
						viewHolder.mIdentify.setVisibility(View.VISIBLE);
						break;
					case 1:
						viewHolder.mVerify.setVisibility(View.VISIBLE);
						break;
					}
				}
			}
			if (!TextUtils.isEmpty(time)) {
				viewHolder.mPostsTime.setText(time);
			}
			viewHolder.mPostsPraiseCount.setText(praiseCount + "人赞过");
			viewHolder.mPostsPVCount.setText(pvCount + "");
			viewHolder.mPostsReplyCount.setText(replyCount + "");

			if (!TextUtils.isEmpty(title)) {
				viewHolder.mPostsTitle.setText(title);
			}
			if (!TextUtils.isEmpty(content)) {
				List<String> list = StrUtil.getImgSrc(content);
				if (list.size() != 0) {
					viewHolder.mContent.setText(Html.fromHtml(content
							.substring(0, content.lastIndexOf("<br/>"))));
					viewHolder.mImageLaout.setVisibility(View.VISIBLE);
					for (int i = 0; i < list.size(); i++) {
						viewHolder.mImages[i].setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								DataParam.REMOTE_SERVE + list.get(i),
								viewHolder.mImages[i],
								ImageLoadOptionsUtil.getOptions());
						viewHolder.mImages[i].setTag(list.get(i));
					}
				} else {
					viewHolder.mImageLaout.setVisibility(View.GONE);
					viewHolder.mContent.setText(Html.fromHtml(content));
				}
			}
			viewHolder.mInform.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onInform(5, index);
				}
			});
			// clickImages(viewHolder.mImages);
		} else {
			final DxForumReply dxForumReply = mList.get(position)
					.getDxForumReply();

			secondPostsReplyAdapter = new SecondPostsReplyAdapter(mContext,
					dxForumReply, dxForumReply.getDxForumReplySeconds(),0);
			viewHolder.mListView.setAdapter(secondPostsReplyAdapter);
			setListViewHeightBasedOnChildren(viewHolder.mListView);
			
			if (dxForumReply.getCount() > 2) {
				viewHolder.mPostsMore.setVisibility(View.VISIBLE);
				viewHolder.mPostsMore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mContext,
								SecondPostsListActivity.class);
						intent.putExtra("dxForumTopic", mList.get(index));
						mContext.startActivity(intent);
					}
				});
			} else {
				viewHolder.mPostsMore.setVisibility(View.GONE);
			}

		}
		// clickImages(viewHolder.mImages);
		return convertView;
	}

	class PostsViewHolder {

		public RoundImageView mReplyAvatar;
		public TextView mFloor;
		public TextView mPostsMenu;
		public TextView mIsPoster;
		public RadioButton mIsBelaud;
		private RoundHeadImageView mPosterAvatar;
		private TextView mPosterName;
		private TextView mPostsTime;
		private LinearLayout mImageLaout;
		public RatingBar mRating;
		private TextView mIdentify;
		private TextView mVerify;
		private TextView mPostsTitle;
		private TextView mInform;
		private TextView mPostsPraiseCount;
		private TextView mPostsPVCount;
		private TextView mPostsReplyCount;

		private ListView mListView;
		private TextView mPostsMore;

		private TextView mContent;
		private ClickImageView mImage0;
		private ClickImageView mImage1;
		private ClickImageView mImage2;
		private ImageView[] mImages = { mImage0, mImage1, mImage2 };


	}

	/**
	 * 点赞
	 */
	private void addPraise(final int index) {
		// TODO Auto-generated method stub
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		String urlString = UrlEngine.belaudReply(userId, mList.get(index)
				.getDxForumReply().getId());
		HttpUtil.post(urlString, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == Constants.STAT_200) {
					try {
						String status = response.getString("status");
						if (status.equals("1")) {
							mList.get(index).getDxForumReply().setIsBelaud(1);
							mList.get(index)
									.getDxForumReply()
									.setBelauds(
											mList.get(index).getDxForumReply()
													.getBelauds() + 1);
						} else {
							mList.get(index).getDxForumReply().setIsBelaud(0);
							mList.get(index)
									.getDxForumReply()
									.setBelauds(
											mList.get(index).getDxForumReply()
													.getBelauds() - 1);
						}
						notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	class popAction implements OnClickListener {
		DxForumReply dxForumReply;
		int pos;

		public popAction(int pos, DxForumReply dxForumReply) {
			this.pos = pos;
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
			showPop(v, x, y, pos, dxForumReply);
		}
	}

	/**
	 * 显示popWindow
	 * */
	public void showPop(final View parent, int x, int y, final int pos,
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
				if (pos != -1) {
					onReply(pos, dxForumReply, pos);
				}

			}
		});
		mRbInform.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
				if (pos != -1) {
					onInform(3, pos);
				}
			}
		});
	}

	/**
	 * @param type
	 *            3:举报1级回复 5:举报帖子
	 * @param postion
	 */
	protected void onInform(int type, int postion) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, InformPostsActivity.class);
		DxForumTopic dxForumTopic = mList.get(postion);
		switch (type) {
		case 5:
			dxForumTopic.setReplyType(5);
			break;
		case 3:
			dxForumTopic.setReplyType(3);
			break;
		default:
			dxForumTopic.setReplyType(4);
			if (type == 2) {
				dxForumTopic.getDxForumReply().getDxForumReplySeconds()
						.remove(0);
			}
			break;
		}

		intent.putExtra("dxForumTopic", dxForumTopic);
		((Activity) mContext).startActivity(intent);

	}

	/**
	 * 回复
	 * 
	 * @param postion
	 * @param dxForumReply
	 */
	protected void onReply(int postion, DxForumReply dxForumReply, int index) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, SendPostsActivity.class);
		DxForumTopic dxForumTopic = new DxForumTopic();
		dxForumTopic.setReplyType(2);
		if (postion > 0) {
			dxForumTopic.setTitle("回复" + dxForumReply.getUserName());
			dxForumTopic.setId(Integer.valueOf(dxForumReply.getTopicId()));
			dxForumTopic.setReplyId(Integer.valueOf(dxForumReply.getId()));
		} else {
			dxForumTopic.setTitle("回复"
					+ dxForumReply.getDxForumReplySeconds().get(postion + 4)
							.getUserName());
			dxForumTopic.setId(Integer.valueOf(dxForumReply.getTopicId()));
			dxForumTopic.setReplyId(Integer.valueOf(dxForumReply
					.getDxForumReplySeconds().get(postion + 4).getId()));
		}
		intent.putExtra("topic", dxForumTopic);
		intent.putExtra("index", index);
		((Activity) mContext).startActivityForResult(intent, 101);
	}

	public void clickImages(ImageView[] mImages) {
		for (int i = 0; i < mImages.length; i++) {
			mImages[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = (String) v.getTag();
					if (!TextUtils.isEmpty(url)) {
						Intent intent = new Intent(mContext,
								ImageShowActivity.class);
						intent.putExtra("url", DataParam.REMOTE_SERVE
								+ (String) v.getTag());
						mContext.startActivity(intent);
					}
				}
			});
		}

	}
}
