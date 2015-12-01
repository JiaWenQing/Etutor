package net.dx.etutor.activity.fragment.banner;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.dx.etutor.R;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.util.ImageLoadOptionsUtil;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
@SuppressLint("ValidFragment")
public class ImageFragment extends Fragment {
	private String url;
	private List<String> bannerList = new ArrayList<String>();
	private int index;
	private int[] mImageRes = new int[] { R.drawable.banner1,
			R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, };

	public ImageFragment(int index, List<String> bannerList) {
		this.index = index;
		this.bannerList = bannerList;
	}

	public ImageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_image, container,
				false);
		ImageView mImageView = (ImageView) layout.findViewById(R.id.imageView1);
		if (!TextUtils.isEmpty(bannerList.get(index))) {
			ImageLoader.getInstance().displayImage(
					DataParam.REMOTE_SERVE + bannerList.get(index), mImageView,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mImageView.setImageResource(mImageRes[index]);
		}
		return layout;

	}
}
