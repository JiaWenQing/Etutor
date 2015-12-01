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

public class BindChoiceDialog extends Dialog implements
		android.view.View.OnClickListener {

	private OnBindChoiceClickListener mOnBindChoiceClickListener;
	private Button mCancel;
	private Button mBindQQ;
	private Button mBindSina;
	public interface OnBindChoiceClickListener {
		void choiceOnClick(View v);
	}

	public void setOnBindChoiceClickListener(OnBindChoiceClickListener l) {
		this.mOnBindChoiceClickListener = l;
	}

	public BindChoiceDialog(Context context, int y) {
		super(context, R.style.transparentFrameWindowStyle);
		setContentView(
				LayoutInflater.from(context).inflate(
						R.layout.dialog_thirdparty_binding, null),
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		mCancel = (Button) findViewById(R.id.binding_cancel);
		mBindQQ = (Button) findViewById(R.id.binding_qq);
		mBindSina = (Button) findViewById(R.id.binding_sina);
		mCancel.setOnClickListener(this);
		mBindQQ.setOnClickListener(this);
		mBindSina.setOnClickListener(this);
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
		if (mOnBindChoiceClickListener != null) {
			mOnBindChoiceClickListener.choiceOnClick(v);
			if (isShowing()) {
				this.dismiss();
			}
		}
	}
}
