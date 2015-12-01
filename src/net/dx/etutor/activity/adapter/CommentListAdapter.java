package net.dx.etutor.activity.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxComment;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 评论listview的adapter
 * 
 * @author jwq
 * 
 */
public class CommentListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<DxComment> mList;
	private Context mContext;
	private boolean flag;

	public CommentListAdapter(Context mContext, List<DxComment> mList,
			boolean flag) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		this.flag = flag;
		inflater = LayoutInflater.from(mContext);
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
		CommentViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_comment, null);
			viewHolder = new CommentViewHolder();
			viewHolder.mAvatar = (ImageView) convertView
					.findViewById(R.id.imv_avatar);
			viewHolder.mCommentType = (ImageView) convertView
					.findViewById(R.id.imv_comment_type);
			viewHolder.mFulltime = (ImageView) convertView
					.findViewById(R.id.imv_teacher_fulltime);
			viewHolder.mIdentify = (ImageView) convertView
					.findViewById(R.id.imv_identity);
			viewHolder.mVerify = (ImageView) convertView
					.findViewById(R.id.imv_verify);
			viewHolder.mListen = (ImageView) convertView
					.findViewById(R.id.imv_listen);
			viewHolder.mName = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.mCommentTime = (TextView) convertView
					.findViewById(R.id.tv_comment_time);
			viewHolder.mContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			viewHolder.mTRatingBar = (RatingBar) convertView
					.findViewById(R.id.ratingbar_teacher);
			viewHolder.mSRatingBar = (RatingBar) convertView
					.findViewById(R.id.ratingbar_student);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CommentViewHolder) convertView.getTag();
		}
		DxComment c = (DxComment) getItem(position);
		viewHolder.mFulltime.setVisibility(View.GONE);
		viewHolder.mIdentify.setVisibility(View.GONE);
		viewHolder.mVerify.setVisibility(View.GONE);
		viewHolder.mListen.setVisibility(View.GONE);
		if (c.getDxUsers() != null) {
			if (c.getDxUsers().getIdentify() != null) {
				int identify = c.getDxUsers().getIdentify();
				for (int i = 0; i < Constants.LABEL_NUMBER_TEACHER; i++) {
					if ((identify & (1 << i)) != 0) {
						switch (i) {
						case 3:
							viewHolder.mFulltime.setVisibility(View.VISIBLE);
							break;
						case 2:
							viewHolder.mIdentify.setVisibility(View.VISIBLE);
							break;
						case 1:
							viewHolder.mVerify.setVisibility(View.VISIBLE);
							break;
						case 0:
							viewHolder.mListen.setVisibility(View.VISIBLE);
							break;
						}
					}
				}
			}
		}
		if (c.getDxUsers() != null) {
			String imageUri = c.getDxUsers().getAvatarUrl();
			if (!TextUtils.isEmpty(imageUri)) {
				ImageLoader.getInstance().displayImage(
						DataParam.REMOTE_SERVE + imageUri, viewHolder.mAvatar,
						ImageLoadOptionsUtil.getOptions());
			} else {
				viewHolder.mAvatar.setImageResource(R.drawable.default_avatar);
			}

			String type = c.getDxUsers().getUserType();
			if (!TextUtils.isEmpty(type)) {
				float rating = c.getDxUsers().getRank();
				if (type.equals("0")) {
					viewHolder.mSRatingBar.setVisibility(View.VISIBLE);
					viewHolder.mSRatingBar.setRating(rating);
					viewHolder.mTRatingBar.setVisibility(View.GONE);
				} else {
					viewHolder.mTRatingBar.setVisibility(View.VISIBLE);
					viewHolder.mTRatingBar.setRating(rating);
					viewHolder.mSRatingBar.setVisibility(View.GONE);
				}
			}
			if (!TextUtils.isEmpty(c.getDxUsers().getUserName())) {
				viewHolder.mName.setText(c.getDxUsers().getUserName());
			} else {
				viewHolder.mName.setText("");
			}
		}
		if (!TextUtils.isEmpty(c.getCommentTime())) {
			viewHolder.mCommentTime.setText(c.getCommentTime());
		} else {
			viewHolder.mCommentTime.setText(new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date()));
		}
		if (!TextUtils.isEmpty(c.getContent())) {
			if (c.getContent().length() > 10) {
				viewHolder.mContent.setText(c.getContent().substring(0, 10)
						+ "...");
			} else {
				viewHolder.mContent.setText(c.getContent());
			}
		} else {
			viewHolder.mContent.setText("无");
		}
		if (flag) {
			viewHolder.mCommentType.setVisibility(View.VISIBLE);
		} else {
			viewHolder.mCommentType.setVisibility(View.GONE);
		}
		if (c.getStarLevel() != null) {
			String CommentType = c.getStarLevel();
			switch (CommentType) {
			case "1":
				viewHolder.mCommentType
						.setImageResource(R.drawable.face_normal_good);
				break;
			case "0":
				viewHolder.mCommentType
						.setImageResource(R.drawable.face_normal_ordinary);
				break;
			case "-1":
				viewHolder.mCommentType
						.setImageResource(R.drawable.face_normal_bad);
				break;
			default:
				viewHolder.mCommentType.setVisibility(View.GONE);
				break;
			}
		} else {
			viewHolder.mCommentType.setVisibility(View.GONE);
		}
		return convertView;
	}

	class CommentViewHolder {
		public ImageView mFulltime;
		public RatingBar mSRatingBar;
		public RatingBar mTRatingBar;
		private ImageView mAvatar;
		private ImageView mCommentType;
		private ImageView mIdentify;
		private ImageView mVerify;
		private ImageView mListen;
		private TextView mName;
		private TextView mCommentTime;
		private TextView mContent;
	}

}
