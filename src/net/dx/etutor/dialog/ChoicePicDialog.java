package net.dx.etutor.dialog;

import net.dx.etutor.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class ChoicePicDialog extends Dialog implements
		android.view.View.OnClickListener {

	private OnChoicePicClickListener mOnChoicePicClickListener;
	private Button mCancel;
	private Button mGallery;
	private Button mCamera;
	private int flag=0;

	public interface OnChoicePicClickListener {
		void choiceOnClick(View v,int flag);
	}

	public void setOnChoicePicClickListener(OnChoicePicClickListener l,int flag) {
		this.mOnChoicePicClickListener = l;
		this.flag=flag;
	}

	public ChoicePicDialog(Context context, int y) {
		super(context, R.style.transparentFrameWindowStyle);
		setContentView(
				LayoutInflater.from(context).inflate(
						R.layout.dialog_photo_choose_layout, null),
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		mCancel = (Button) findViewById(R.id.photo_cancel);
		mGallery = (Button) findViewById(R.id.photo_gallery);
		mCamera = (Button) findViewById(R.id.photo_camera);
		mCancel.setOnClickListener(this);
		mGallery.setOnClickListener(this);
		mCamera.setOnClickListener(this);
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
		if (mOnChoicePicClickListener != null) {
			mOnChoicePicClickListener.choiceOnClick(v,flag);
			if (isShowing()) {
				this.dismiss();
			}
		}
	}
}
