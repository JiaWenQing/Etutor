package net.dx.etutor.dialog;

import net.dx.etutor.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareChoiceDialog extends Dialog implements
		android.view.View.OnClickListener, OnItemClickListener {

	private OnShareChoiceClickListener mOnShareChoiceClickListener;
	private Context context;
	private Button mCancel;
	private GridView mGridView;
	private MyShareAdapter mAdapter;

	public interface OnShareChoiceClickListener {
		void ShareOnClick(View v);

		void ShareOnItemClick(AdapterView<?> parent, View view, int position, long id);
	}

	public void setOnShareChoiceClickListener(OnShareChoiceClickListener l) {
		this.mOnShareChoiceClickListener = l;
	}

	public ShareChoiceDialog(Context context, int y) {
		super(context, R.style.transparentFrameWindowStyle);
		this.context = context;
		setContentView(
				LayoutInflater.from(context).inflate(R.layout.dialog_share,
						null), new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		mCancel = (Button) findViewById(R.id.share_cancel);
		mGridView = (GridView) findViewById(R.id.gv_share);
		mAdapter=new MyShareAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mCancel.setOnClickListener(this);
		Window window = getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = y;
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		onWindowAttributesChanged(wl);
		// 设置点击外围解散
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		if (mOnShareChoiceClickListener != null) {
			mOnShareChoiceClickListener.ShareOnClick(v);
			if (isShowing()) {
				this.dismiss();
			}
		}
	}

	class MyShareAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_share_list, null);
				holder.img = (ImageView) convertView.findViewById(R.id.imv_share_icon);
				holder.title = (TextView) convertView.findViewById(R.id.tv_share_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setImageResource(R.drawable.icon_share_1_weixin_friend+position);
			holder.title.setText(R.string.text_share_1_weixin_friend+position);
			return convertView;
		}

		class ViewHolder {
			public ImageView img;
			public TextView title;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (mOnShareChoiceClickListener != null) {
			mOnShareChoiceClickListener.ShareOnItemClick(parent, view, position, id);
			if (isShowing()) {
				this.dismiss();
			}
		}
	}
}
