package net.dx.etutor.view.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角头像
 * 
 * @author app
 * 
 */
public class RoundHeadImageView extends ImageView {
	private Paint paint;

	public RoundHeadImageView(Context context) {
		this(context, null);
	}

	public RoundHeadImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundHeadImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
	}

	/**
	 * 绘制圆角矩形图片
	 * 
	 * @author caizhiming
	 */

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (null != drawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			Bitmap b = getRoundBitmap(bitmap, 10);
			final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
			final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
			paint.reset();
			canvas.drawBitmap(b, rectSrc, rectDest, paint);

		} else {
			super.onDraw(canvas);
		}
	}

	/**
	 * 获取圆角矩形图片方法
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            ,一般设置成14
	 * @return Bitmap
	 * @author caizhiming
	 */
	private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {

		/*
		 * Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		 * bitmap.getHeight(), Config.ARGB_8888); Canvas canvas = new
		 * Canvas(output);
		 * 
		 * final int color = 0xff424242;
		 * 
		 * final Rect rect = new Rect(0, 0, bitmap.getWidth(),
		 * bitmap.getHeight()); final RectF rectF = new RectF(rect);
		 * paint.setAntiAlias(true); canvas.drawARGB(0, 0, 0, 0);
		 * paint.setColor(color); paint.setAntiAlias(true);
		 * paint.setFilterBitmap(true); paint.setDither(true);
		 * canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		 * paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		 * canvas.drawBitmap(bitmap, rect, rect, paint); return output;
		 */

		int x = bitmap.getWidth();
		int y = bitmap.getHeight();
		float[] mOuter = new float[] { roundPx, roundPx, roundPx, roundPx,
				roundPx, roundPx, roundPx, roundPx };
		// 根据源文件新建一个darwable对象
		Drawable imageDrawable = new BitmapDrawable(bitmap);
		// 新建一个新的输出图片
		Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		// 新建一个矩形
		RectF outerRect = new RectF(0, 0, x, y);
		Path mPath = new Path();
		// 创建一个圆角矩形路径
		mPath.addRoundRect(outerRect, mOuter, Path.Direction.CW);

		// 画出一个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		canvas.drawPath(mPath, paint);
		// 设置绘图两层交汇时的模式
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		// 设置绘图区域（在那个区域内绘图）
		imageDrawable.setBounds(0, 0, x, y);
		// 将用于绘制图片的图层拷贝到单独图层并压入图层栈
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		// 将源图片绘制到这个拷贝的图片图层上
		imageDrawable.draw(canvas);
		// 将图片图层退出栈并将图像绘制到矩形的那一层
		canvas.restore();
		return output;

	}
}
