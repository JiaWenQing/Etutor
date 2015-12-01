package net.dx.etutor.view.imageview;

import net.dx.etutor.activity.ImageShowActivity;
import net.dx.etutor.data.DataParam;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ClickImageView extends ImageView implements OnClickListener {

	private Context mContext;

	public ClickImageView(Context context) {
		super(context);
		mContext = context;
		this.setOnClickListener(this);
	}

	public ClickImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String url = (String) v.getTag();
		if (!TextUtils.isEmpty(url)) {
			Intent intent = new Intent(mContext, ImageShowActivity.class);
			intent.putExtra("url", DataParam.REMOTE_SERVE + (String) v.getTag());
			mContext.startActivity(intent);
		}
	}
}
