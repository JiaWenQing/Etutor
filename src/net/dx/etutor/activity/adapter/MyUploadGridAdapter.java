package net.dx.etutor.activity.adapter;

import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.model.DxCertificate;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的上传的adapter
 * 
 * @author jwq
 * 
 */
public class MyUploadGridAdapter extends BaseAdapter {

	private List<DxCertificate> mList;
	private Context mContext;
	private LayoutInflater inflater;

	public MyUploadGridAdapter(Context mContext, List<DxCertificate> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * ListView Item设置
	 */
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_upload, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grid_image);
			holder.mSelected = (ImageView) convertView
					.findViewById(R.id.item_upload_selected);
			holder.mStatus = (TextView) convertView
					.findViewById(R.id.item_tv_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DxCertificate dxCertificate = (DxCertificate) getItem(position);
		if (position >= 9) {
			holder.image.setVisibility(View.GONE);
			holder.mStatus.setVisibility(View.INVISIBLE);
		} else {
			if (position == getCount() - 1) {
				holder.mStatus.setVisibility(View.INVISIBLE);
			} else {
				holder.mStatus.setVisibility(View.VISIBLE);
			}
		}
		if (position == getCount()) {
			holder.mSelected.setImageResource(-1);
			holder.image.setBackgroundResource(R.drawable.upload_icon);
			holder.image.setImageResource(R.drawable.upload_icon);
			holder.image.setImageAlpha(255);
			holder.mStatus.setVisibility(View.VISIBLE);
		} else {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + dxCertificate.getCertificateUrl(),
					holder.image, ImageLoadOptionsUtil.getOptions());

			if (mList.get(position).isSelected()) {
				holder.mSelected.setImageResource(R.drawable.upload_selected);
				holder.image.setImageAlpha(100);
			} else {
				holder.mSelected.setImageResource(-1);
				holder.image.setImageAlpha(255);
			}
			String status = dxCertificate.getStatus();
			if (TextUtils.isEmpty(status)) {

			} else {

				switch (status) {
				case "-1":
					holder.mStatus.setText("未通过");
					holder.mStatus.setTextColor(Color.GRAY);
					break;
				case "1":
					holder.mStatus.setText("已通过");
					holder.mStatus.setTextColor(Color.BLACK);
					break;
				case "0":
					holder.mStatus.setText("待审核");
					holder.mStatus.setTextColor(Color.BLACK);
					break;
				}
			}
		}

		Drawable d = holder.image.getBackground();
		if (null == d) {
			holder.image.setBackgroundResource(R.drawable.upload_icon);
		}

		return convertView;
	}

	public class ViewHolder {
		private ImageView image;
		private ImageView mSelected;
		private TextView mStatus;
	}

}
