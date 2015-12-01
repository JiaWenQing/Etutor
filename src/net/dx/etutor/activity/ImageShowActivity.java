package net.dx.etutor.activity;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.HttpUtil;
import net.dx.etutor.view.imageview.TouchImageView;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class ImageShowActivity extends BaseActivity {
	public static String TAG = "ImageShowActivity";

	// private View mLoaction;
	// private ImageView image;
	private String url;
	private LinearLayout ll_viewArea;
	private LinearLayout.LayoutParams parm;
	private ViewArea viewArea;
	private static int w;
	private static int h;
	private int imgDisplayW;
	private int imgDisplayH;
	private int imgW;
	private int imgH;

	@Override
	public void initViews() {
		url = getIntent().getStringExtra("url");
		setContentView(R.layout.image_dialog);
		setTitle(R.string.text_look_picture);
		// image = (ImageView) findViewById(R.id.image);
		// getUserAvatar();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ll_viewArea = (LinearLayout) findViewById(R.id.ll_viewArea);
		parm = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		viewArea = new ViewArea(ImageShowActivity.this); // 自定义布局控件，用来初始化并存放自定义imageView
		ll_viewArea.addView(viewArea, parm);

	}

	@Override
	public void initEvents() {

	}

	// 用来存放和初始化自定义imageView。
	class ViewArea extends FrameLayout {
		public ViewArea(Context context) {
			super(context);
			imgDisplayW = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth();
			imgDisplayH = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getHeight();
			TouchImageView touchView = new TouchImageView(context, imgDisplayW,
					imgDisplayH);// 这句就是我们的自定义ImageView
			getUserAvatar(touchView, context);
		}
	}

	public void getUserAvatar(final TouchImageView touchView, Context context) {
		HttpUtil.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == Constants.STAT_200) {
					float f = 1.0f;
					if (responseBody.length < 100000) {
						f = 3.5f;
					} else if (responseBody.length < 150000) {
						f = 2.5f;
					} else if (responseBody.length < 200000) {
						f = 1.0f;
					} else {
						f = 0.5f;
					}
					Matrix matrix = new Matrix();
					matrix.postScale(f, f);
					BitmapFactory factory = new BitmapFactory();
					@SuppressWarnings("static-access")
					Bitmap bitmap = factory.decodeByteArray(responseBody, 0,
							responseBody.length);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0,
							bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					touchView.setImageBitmap(bitmap);// 给自定义imageView设置要显示的图片
					imgW = bitmap.getWidth();
					imgH = bitmap.getHeight();
					// showLongToast("宽度:" + imgW + "；高度：" + imgH);
					// 图片第一次加载进来，判断图片大小从而确定第一次图片的显示方式。
					int layout_w = imgW > imgDisplayW ? imgDisplayW : imgW;
					int layout_h = imgH > imgDisplayH ? imgDisplayH : imgH;

					// 下面的代码是判断图片初始显示样式的，将宽大于高的图片按照宽缩小的比例把高压缩，前提必须是宽度超出了屏幕大小，相反，如果高大于宽，我将图片按照高缩小的比例把宽压缩，前提必须是高度超出了屏幕大小
					if (imgW >= imgH) {
						if (layout_w == imgDisplayW) {
							layout_h = (int) (imgH * ((float) imgDisplayW / imgW));
						}
					} else {
						if (layout_h == imgDisplayH) {
							layout_w = (int) (imgW * ((float) imgDisplayH / imgH));
						}
					}
					// 这里需要注意的是，采用FreamLayout或者LinearLayout的好处是，如果压缩后的图片扔有一个边大于屏幕，那么只显示在屏幕内的部分，可以通过移动后看见外部（不会裁剪掉图片），如果采用RelativeLayout布局，图片会始终完整显示在屏幕内部，不会有超出屏幕的现象。如果图片不是完全占满屏幕，那么在屏幕上没有图片的地方拖动，图片也会移动，这样的体验不太好，建议用FreamLayout或者LinearLayout。
					touchView.setLayoutParams(new FrameLayout.LayoutParams(
							layout_w, layout_h));// 这是自定义imageView的大小，也就是触摸范围
					viewArea.addView(touchView);

				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

}
