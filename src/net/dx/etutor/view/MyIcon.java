package net.dx.etutor.view;

import net.dx.etutor.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;


public class MyIcon extends View {

	public static int w;
	public static int h;
	public static  Bitmap mBitmap;

	public MyIcon(Context context) {
		super(context);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_mark_blue);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		w = this.getWidth() / 2 - mBitmap.getWidth() / 2;
		h = this.getHeight() / 2 - mBitmap.getHeight() / 2;
		canvas.drawBitmap(mBitmap, w, h, null);
	}

}
