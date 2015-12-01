package net.dx.etutor.popupwindow;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BasePopupWindow;
import net.dx.etutor.activity.interfaces.OnWheelClickListener;
import net.dx.etutor.util.Constants;
import net.dx.etutor.view.wheelview.ArrayWheelAdapter;
import net.dx.etutor.view.wheelview.NumericWheelAdapter;
import net.dx.etutor.view.wheelview.OnWheelChangedListener;
import net.dx.etutor.view.wheelview.OnWheelScrollListener;
import net.dx.etutor.view.wheelview.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CoursePopupWindow extends BasePopupWindow implements
		android.view.View.OnClickListener, OnWheelChangedListener,
		OnWheelScrollListener {

	private TextView mBack;
	private TextView mOk;
	private Context mContext;
	private String mValue;
	private OnWheelClickListener mOnWheelClickListener;
	private WheelView wheelView;
	private String arrys[];
	private int mType;
	private String[][] items;

	public CoursePopupWindow(Context context, int type) {
		super(LayoutInflater.from(context).inflate(
				R.layout.wheel_dialog_layout, null), LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mType = type;
		setAnimationStyle(R.style.wheel_popwin_anim_style);
	}

	@Override
	public void initViews() {
		
		items = new String[][] {
				new String[] { "智力开发", "少儿英语", "学前教育", "幼儿陪玩", "幼儿托管", "幼儿园" },
				new String[] { "小学语文", "小学数学", "小学英语", "小学奥数", "小学全科", "小学陪读",
						"小学托管", "小学接送", "小学英文版教材", "小学作文" },
				new String[] { "初中文综", "初中理综", "初中语文", "初中英语", "初中数学", "初中奥数",
						"初中全科", "初中化学", "初中物理", "初中生物", "初中历史", "初中政治", "初中地理",
						"文科英文", "理科英文" },
				new String[] { "高中文综", "高中理综", "高中语文", "高中英语", "高中数学", "高考辅导",
						"高中全科", "高中物理", "高中化学", "高中生物", "高中政治", "高中地理", "高中历史" },
				new String[] { "少儿英语", "小学英语", "初中英语", "高中英语", "英语口语", "英语听力",
						"英语语法", "英语写作", "英语阅读", "托福", "雅思", "日语", "韩语", "德语",
						"法语" },
				new String[] { "钢琴", "提琴", "吉他", "二胡", "笛子", "琵琶", "葫芦丝",
						"打击乐", "古琴", "古筝", "架子鼓", "通俗唱法", "名族唱法", "美声唱法",
						"原生态唱法" },
				new String[] { "素描", "中国画", "雕塑", "陶艺", "水彩画", "沙画", "国画", "油画" },
				new String[] { "艺考", "肚皮舞", "钢管舞", "街舞", "拉丁舞", "爵士舞", "毛笔",
						"硬笔", "IT方面", "篮球", "羽毛球", "瑜伽", "武术", "棋类", "摄影", "其他" } };
		mBack = (TextView) findViewById(R.id.wheel_back);
		mOk = (TextView) findViewById(R.id.wheel_ok);
		wheelView = (WheelView) findViewById(R.id.wheel_view);
		wheelView.setVisibleItems(5);
		wheelView.setAdapter(new ArrayWheelAdapter<String>(arrys));
	}

	@Override
	public void initEvents() {
		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);
		wheelView.addChangingListener(this);
		wheelView.addScrollingListener(this);
	}

	@Override
	public void init() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_back:
			this.dismiss();
			break;
		case R.id.wheel_ok:
			switch (mType) {
			case Constants.NEED_SEX:
				mOnWheelClickListener.wheelOnClick(mValue, Constants.NEED_SEX);
				break;
			case Constants.NEED_TEACH:
				mOnWheelClickListener
						.wheelOnClick(mValue, Constants.NEED_TEACH);
				break;
			case Constants.NEED_LECTURE:
				mOnWheelClickListener.wheelOnClick(mValue,
						Constants.NEED_LECTURE);
				break;
			}
			this.dismiss();
		}
	}

	public void setOnWheelClickListener(OnWheelClickListener l) {
		this.mOnWheelClickListener = l;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		mValue = arrys[wheelView.getCurrentItem()];
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		mValue = arrys[wheelView.getCurrentItem()];
	}

}
