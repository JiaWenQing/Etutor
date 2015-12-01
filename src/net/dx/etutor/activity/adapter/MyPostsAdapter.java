package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.forum.PostsDetailActivity;
import net.dx.etutor.activity.fragment.posts.PostsCollectFragment;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.dialog.LoadingDialog;
import net.dx.etutor.dialog.LogoutDialog;
import net.dx.etutor.dialog.LogoutDialog.OnLogoutClickListener;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import net.dx.etutor.util.StrUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帖子中我的发布、我的收藏、最新贴、最热帖、精华帖的adapter
 * 
 * @author jwq
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class MyPostsAdapter extends BaseAdapter {

	private List<DxForumTopic> mList = new ArrayList<DxForumTopic>();
	private FragmentActivity activity;
	private Context mContext;
	protected int requestCode = 0;
	private LayoutInflater inflater;
	private boolean isShow;
	private boolean isDeteleCollectPosts = false;
	private LogoutDialog dialog;
	private LoadingDialog mLoadingDialog;
	private PostsCollectFragment collectFragment;

	@SuppressLint("NewApi")
	public MyPostsAdapter(FragmentActivity activity, List<DxForumTopic> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.activity = activity;
		isDeteleCollectPosts = false;
		inflater = LayoutInflater.from(activity);
	}

	@SuppressLint("NewApi")
	public MyPostsAdapter(FragmentActivity activity, List<DxForumTopic> mList,
			PostsCollectFragment collectFragment) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.activity = activity;
		this.collectFragment = collectFragment;
		isDeteleCollectPosts = true;
		inflater = LayoutInflater.from(activity);
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
		final int index = position;
		PostsViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_posts, null);
			viewHolder = new PostsViewHolder();
			viewHolder.mLayoutItem = (LinearLayout) convertView
					.findViewById(R.id.layout_item_posts);
			viewHolder.mPostsDelete = (TextView) convertView
					.findViewById(R.id.tv_delete);
			viewHolder.mPostsTop = (TextView) convertView
					.findViewById(R.id.tv_posts_top);
			viewHolder.mPostsTitle = (TextView) convertView
					.findViewById(R.id.tv_posts_title);
			viewHolder.mPostsPoint = (ImageView) convertView
					.findViewById(R.id.imv_posts_point);

			viewHolder.mLine = (ImageView) convertView
					.findViewById(R.id.split_line);
			viewHolder.mPosterAvatar = (ImageView) convertView
					.findViewById(R.id.imv_poster_avatar);
			viewHolder.mPosterName = (TextView) convertView
					.findViewById(R.id.tv_poster_name);
			viewHolder.mPostsTime = (TextView) convertView
					.findViewById(R.id.tv_send_posts_time);
			viewHolder.mImageLaout = (LinearLayout) convertView
					.findViewById(R.id.img_layout);
			viewHolder.mContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			for (int i = 0; i < 3; i++) {
				viewHolder.mImages[i] = (ImageView) convertView
						.findViewById(R.id.img_1_1 + i);
			}
			viewHolder.mPostsPraiseCount = (TextView) convertView
					.findViewById(R.id.tv_posts_praise_count);
			viewHolder.mPostsPVCount = (TextView) convertView
					.findViewById(R.id.tv_posts_pv_count);
			viewHolder.mPostsReplyCount = (TextView) convertView
					.findViewById(R.id.tv_posts_reply_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (PostsViewHolder) convertView.getTag();
		}
		final DxForumTopic dxForumTopic = mList.get(position);
		String title = dxForumTopic.getTitle();
		String name = dxForumTopic.getUserName();
		String content = dxForumTopic.getDescription().trim();
		String avatarUrl = dxForumTopic.getAvatarUrl();
		String time = dxForumTopic.getCreateTime();
		int pvCount = dxForumTopic.getClicks();
		int praiseCount = dxForumTopic.getBelauds();
		int replyCount = dxForumTopic.getCount();
		int topicType = dxForumTopic.getTopicType();

		if (topicType == 1) {
			viewHolder.mPostsTop.setVisibility(View.VISIBLE);
			viewHolder.mPostsTop.setText("顶");
			viewHolder.mPostsTop
					.setBackgroundResource(R.drawable.shape_corners_bg_blue);
		} else if (topicType == 2) {
			viewHolder.mPostsTop.setVisibility(View.VISIBLE);
			viewHolder.mPostsTop.setText("精");
			viewHolder.mPostsTop
					.setBackgroundResource(R.drawable.shape_corners_bg_red);
		} else {
			viewHolder.mPostsTop.setVisibility(View.GONE);
		}
		viewHolder.mPostsPoint.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(title)) {
			viewHolder.mPostsTitle.setText(title);
		}
		viewHolder.mImages[0].setVisibility(View.GONE);
		viewHolder.mImages[1].setVisibility(View.GONE);
		viewHolder.mImages[2].setVisibility(View.GONE);
		if (!TextUtils.isEmpty(content)) {
			List<String> list = StrUtil.getImgSrc(content);
			if (list.size() != 0) {
				viewHolder.mContent.setText(Html.fromHtml(content.substring(0,
						content.lastIndexOf("<br/>"))));
				viewHolder.mImageLaout.setVisibility(View.VISIBLE);
				for (int i = 0; i < list.size(); i++) {
					viewHolder.mImages[i].setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							DataParam.REMOTE_SERVE + list.get(i),
							viewHolder.mImages[i],
							ImageLoadOptionsUtil.getOptions());
				}
			} else {
				viewHolder.mImageLaout.setVisibility(View.GONE);
				viewHolder.mContent.setText(Html.fromHtml(content));
			}
		}
		if (position == getCount()) {
			viewHolder.mLine.setVisibility(View.GONE);
		} else {
			viewHolder.mLine.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(name)) {
			viewHolder.mPosterName.setText(name);
		}
		if (!TextUtils.isEmpty(avatarUrl)) {
			ImageLoader.getInstance()
					.displayImage(DataParam.REMOTE_SERVE + avatarUrl,
							viewHolder.mPosterAvatar,
							ImageLoadOptionsUtil.getOptions());
		} else {
			viewHolder.mPosterAvatar
					.setImageResource(R.drawable.default_avatar_small);
		}

		if (!TextUtils.isEmpty(time)) {
			viewHolder.mPostsTime.setText(time);
		}

		viewHolder.mPostsPraiseCount.setText(praiseCount + "");
		viewHolder.mPostsPVCount.setText(pvCount + "");
		viewHolder.mPostsReplyCount.setText(replyCount + "");

		if (isShow) {
			viewHolder.mPostsDelete.setVisibility(View.VISIBLE);
			viewHolder.mPostsDelete.setClickable(true);
		} else {
			viewHolder.mPostsDelete.setVisibility(View.GONE);
			viewHolder.mPostsDelete.setClickable(false);
		}
		viewHolder.mLayoutItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, PostsDetailActivity.class);
				intent.putExtra("dxForumTopic", dxForumTopic);
				activity.startActivityForResult(intent, 500);
			}
		});
		viewHolder.mPostsDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLoadingDialog = new LoadingDialog(activity, "");
				mLoadingDialog.show();
				dialog = new LogoutDialog(activity);
				dialog.setOnLogoutClickListener(new OnLogoutClickListener() {

					@Override
					public void logoutOnClick(View v) {
						// TODO Auto-generated method stub
						mLoadingDialog.dismiss();
						switch (v.getId()) {
						case R.id.btn_logout_confirm:
							int id = mList.get(index).getReplyId();
							String url;
							if (isDeteleCollectPosts) {
								url = UrlEngine.deleteCollect(mList.get(index)
										.getCollectId());
							} else {
								url = UrlEngine.deletePosts(id);
							}
							HttpUtil.post(url, new JsonHttpResponseHandler() {

								@Override
								public void onFailure(int statusCode,
										Header[] headers, Throwable throwable,
										JSONObject errorResponse) {
									super.onFailure(statusCode, headers,
											throwable, errorResponse);
								}

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									if (statusCode == Constants.STAT_200) {
										mList.remove(index);
										notifyDataSetChanged();
										if (!isDeteleCollectPosts) {
											EtutorApplication.getInstance()
													.getSpUtil()
													.isCardListFlushFlag(true);
										} else {
											EtutorApplication.getInstance()
													.getSpUtil()
													.setTopicFlag(true);
											collectFragment.onRefresh();
										}
									}
								}

							});
							break;
						case R.id.btn_logout_cancle:
							dialog.dismiss();
							break;
						default:
							break;
						}

					}
				}, "确定删除吗？", "");
				dialog.show();
			}
		});

		return convertView;
	}

	public void setIsShow(boolean isShow) {
		this.isShow = isShow;
		notifyDataSetChanged();
	}

	class PostsViewHolder {

		private LinearLayout mImageLaout;
		private LinearLayout mLayoutItem;
		private TextView mPostsDelete;

		private TextView mPostsTop;
		private TextView mPostsTitle;
		private ImageView mPostsPoint;

		private TextView mContent;
		private ImageView mImage0;
		private ImageView mImage1;
		private ImageView mImage2;
		private ImageView[] mImages = { mImage0, mImage1, mImage2 };

		// private WebView mPostsContent;

		private ImageView mPosterAvatar;
		private TextView mPosterName;
		private TextView mPostsTime;
		private TextView mPostsPraiseCount;
		private TextView mPostsPVCount;
		private TextView mPostsReplyCount;
		private ImageView mLine;

	}

}
