package net.dx.etutor.activity.fragment.banner;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import net.dx.etutor.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class BannerFragment extends Fragment {

	private Handler mHandler = new Handler();
	private boolean mIsDragging = false;
	private static final int BANNER_COUNT = 100000;
	private static final long AUTO_SCROOL = 5000;
	private int[] mImageViewID = new int[] { R.id.imageView1, R.id.imageView2,
			R.id.imageView3, R.id.imageView4, };
	private ArrayList<ImageView> ListImageView = new ArrayList<ImageView>();
	private List<String> bannerList = new ArrayList<String>();
	private ViewPager mViewPager;
	private Runnable mRun;

	public BannerFragment() {
	}

	public BannerFragment(List<String> bannerList) {
		this.bannerList = bannerList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_banner, container,
				false);
		// 初始化Itdi
		initImgItdi(layout);
		initViewPager(layout);

		return layout;
	}

	// 只让autoScrollBanner（）执行一次
	private Boolean b = true;

	@Override
	public void onStart() {
		super.onStart();
		if (b) {
			autoScrollBanner();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 移除线程
		mHandler.removeCallbacks(mRun);
	}

	// 自动滚动BANNER
	private void autoScrollBanner() {
		b = false;
		// Toast.makeText(getActivity(), "次数", Toast.LENGTH_SHORT).show();
		mRun = new Runnable() {

			public void run() {
				// 判断用户是否在拖动ViewPager
				if (!mIsDragging) {
					int currentItem = mViewPager.getCurrentItem();
					mViewPager.setCurrentItem(++currentItem);
				}
				mHandler.postDelayed(this, AUTO_SCROOL);
			}
		};
		mHandler.postDelayed(mRun, AUTO_SCROOL);
	}

	private void initViewPager(View layout) {
		mViewPager = (ViewPager) layout.findViewById(R.id.vp_banner);
		mViewPager.setOnPageChangeListener(new PageChangeListenerImpl());
		FragmentManager fm = getChildFragmentManager();
		BannerAdapter bannerAdapter = new BannerAdapter(fm);
		mViewPager.setAdapter(bannerAdapter);
		mViewPager.setCurrentItem(BANNER_COUNT / 2);
	}

	private void initImgItdi(View layout) {
		for (int i = 0; i < mImageViewID.length; i++) {
			ImageView imageView = (ImageView) layout
					.findViewById(mImageViewID[i]);
			if (i == 0) {
				imageView.setImageResource(R.drawable.point_focused);
			} else {
				imageView.setImageResource(R.drawable.point_unfocused);
			}
			ListImageView.add(imageView);
		}
	}

	class BannerAdapter extends FragmentPagerAdapter {

		public BannerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ImageFragment(position % 4, bannerList);
			return fragment;
		}

		@Override
		public int getCount() {
			return BANNER_COUNT;
		}

	}

	class PageChangeListenerImpl implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (ViewPager.SCROLL_STATE_DRAGGING == arg0) {
				mIsDragging = true;
			} else {
				mIsDragging = false;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mImageViewID.length; i++) {
				ImageView imageView = ListImageView.get(i);
				if ((position % mImageViewID.length) == i) {
					imageView.setImageResource(R.drawable.point_focused);
				} else {
					imageView.setImageResource(R.drawable.point_unfocused);

				}
			}
		}

	}

}
