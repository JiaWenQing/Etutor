package net.dx.etutor.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseFragment;
import net.dx.etutor.activity.forum.MyPostsActivity;
import net.dx.etutor.activity.home.CollectActivity;
import net.dx.etutor.activity.teacher.TeacherListActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.Channel;
import net.dx.etutor.model.DxCollect;
import net.dx.etutor.model.DxForumTopic;
import net.dx.etutor.model.DxTeacherList;
import net.dx.etutor.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class CardSortListAdapter extends BaseAdapter {

	/**
	 * 用户选定
	 */
	private List<Channel> userChannelList = new ArrayList<Channel>();

	private List<Map<Integer, Object>> mCardList = new ArrayList<Map<Integer, Object>>();
	private List<Map<Integer, Object>> mList = new ArrayList<Map<Integer, Object>>();
	private Context mContext;
	private LayoutInflater inflater;

	private MyCollectAdapter collectAdapter;
	private TeacherAdapter teacherAdapter;
	private MyPostsAdapter postsAdapter;
	private List<Integer> cardType;

	private PopupWindow popupWindow;
	private TextView mTop;// 置顶
	private TextView mDelete;// 删除

	private String userId;

	public CardSortListAdapter(Context mContext,
			List<Map<Integer, Object>> mCardList) {
		// TODO Auto-generated constructor stub
		this.mCardList = mCardList;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		initPopWindow();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCardList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
		int type = cardType!=null?cardType.get(pos):0;
		CardsViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_card_list, null);
			viewHolder = new CardsViewHolder();
			viewHolder.mTitleName = (TextView) convertView
					.findViewById(R.id.tv_card_title);
			viewHolder.mEnter = (TextView) convertView
					.findViewById(R.id.tv_card_more);
			viewHolder.mListViewCards = (ListView) convertView
					.findViewById(R.id.lv_cards_list);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CardsViewHolder) convertView.getTag();
		}
		switch (type) {
		case 1:
			collectAdapter = new MyCollectAdapter(mContext,
					(List<DxCollect>) mCardList.get(pos).get(type));
			viewHolder.mListViewCards.setAdapter(collectAdapter);
			break;
		case 2:
		case 4:
		case 16:
			teacherAdapter = new TeacherAdapter(mContext,
					(List<DxTeacherList>) mCardList.get(pos).get(type));
			viewHolder.mListViewCards.setAdapter(teacherAdapter);
			break;
		case 8:
			postsAdapter = new MyPostsAdapter((FragmentActivity) mContext,
					(List<DxForumTopic>) mCardList.get(pos).get(type));
			viewHolder.mListViewCards.setAdapter(postsAdapter);
			break;
		}
		viewHolder.mTitleName.setOnClickListener(new popAction(pos));
		if (type == 2) {
			viewHolder.mTitleName.setText("新入住教师");
		} else if (type == 4) {
			viewHolder.mTitleName.setText("推荐教师");
		} else if (type == 8) {
			viewHolder.mTitleName.setText("我的帖子");
		} else if (type == 16) {
			viewHolder.mTitleName.setText("专职教师");
		} else {
			viewHolder.mTitleName.setText("我的收藏");
		}
		viewHolder.mEnter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				// TODO Auto-generated method stub
				switch (cardType.get(pos)) {
				case 1:
					intent = new Intent(mContext, CollectActivity.class);
					mContext.startActivity(intent);
					break;
				case 2:
					intent = new Intent(mContext, TeacherListActivity.class);
					intent.putExtra("listType", 0);
					mContext.startActivity(intent);
					break;
				case 4:
					intent = new Intent(mContext, TeacherListActivity.class);
					intent.putExtra("listType", 1);
					mContext.startActivity(intent);
					break;
				case 8:
					intent = new Intent(mContext, MyPostsActivity.class);
					mContext.startActivity(intent);
					break;
				case 16:
					intent = new Intent(mContext, TeacherListActivity.class);
					intent.putExtra("listType", 2);
					mContext.startActivity(intent);
					break;
				}
			}
		});

		return convertView;
	}

	class CardsViewHolder {
		private ListView mListViewCards;
		private TextView mEnter;
		private TextView mTitleName;
	}

	public void setCardType(List<Integer> cardType){
		this.cardType=cardType;
		notifyDataSetChanged();
	}
	/**
	 * 每个ITEM中more按钮对应的点击动作
	 * */
	public class popAction implements OnClickListener {
		int position;

		public popAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {

			int[] arrayOfInt = new int[2];
			// 获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			showPop(v, x, y, position);
		}
	}

	/**
	 * 初始化弹出的pop
	 * */
	private void initPopWindow() {
		View popView = inflater.inflate(R.layout.pop_cards_menu, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// 设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		mTop = (TextView) popView.findViewById(R.id.tv_card_top);
		mDelete = (TextView) popView.findViewById(R.id.tv_card_delete);

	}

	/**
	 * 显示popWindow
	 * */
	public void showPop(final View parent, int x, int y, final int postion) {

		// 设置popwindow显示位置
		popupWindow.showAtLocation(parent, 0, x + parent.getWidth() + 30, y);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				UiUtil.setIcon(mContext, R.drawable.edit_nomal,
						(TextView) parent);
			}
		});
		popupWindow.update();
		if (popupWindow.isShowing()) {
			UiUtil.setIcon(mContext, R.drawable.edit_pressed, (TextView) parent);
		}
		// 置顶逻辑
		mTop.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
				setCardTop(cardType.get(postion), postion);
			}
		});
		// 删除逻辑
		mDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				setCardDelete(cardType.get(postion), postion);
				popupWindow.dismiss();
			}
		});
	}

	public void setCardTop(int type, int pos) {
		// TODO Auto-generated method stub
		getUserChannelList();
		List<Channel> list = new ArrayList<Channel>();
		for (int i = 0; i < userChannelList.size(); i++) {
			if (type == userChannelList.get(i).getId()) {
				list.add(userChannelList.get(i));
			}
		}
		for (int i = 0; i < userChannelList.size(); i++) {
			if (type != userChannelList.get(i).getId()) {
				list.add(userChannelList.get(i));
			}
		}
		setUserChannelList(list);
		int i=cardType.get(pos);
		cardType.remove(pos);
		cardType.add(0, i);
		Map<Integer, Object> map = mCardList.get(pos);
		
		mCardList.remove(pos);
		mCardList.add(0, map);
		mCardList.addAll(mList);
		notifyDataSetChanged();
	}

	public void setCardDelete(int type, int pos) {
		// TODO Auto-generated method stub
		mCardList.remove(pos);
		getUserChannelList();
		List<Channel> list = new ArrayList<Channel>();
		for (int i = 0; i < userChannelList.size(); i++) {
			if (type != userChannelList.get(i).getId()) {
				list.add(userChannelList.get(i));
			}
		}
		for (int i = 0; i < userChannelList.size(); i++) {
			if (type == userChannelList.get(i).getId()) {
				userChannelList.get(i).setSelected(0);
				list.add(userChannelList.get(i));
			}
		}
		setUserChannelList(list);
		cardType.remove(pos);
		notifyDataSetChanged();
	}
	
	private void getUserChannelList() {
		String cardSort = EtutorApplication.getInstance().getSpUtil()
				.getCardSort();
		userId = EtutorApplication.getInstance().getSpUtil().getUserId();
		try {
			userChannelList.clear();
			JSONArray jsonArray = new JSONArray(cardSort);
			for (int i = 0; i < jsonArray.length(); i++) {
				Channel channel = new Channel();
				channel.initWithAttributes(jsonArray.getJSONObject(i));
				userChannelList.add(channel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUserChannelList(List<Channel> userChannelList) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < userChannelList.size(); i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("id", userChannelList.get(i).getId());
				object.put("name", userChannelList.get(i).getName());
				object.put("selected", userChannelList.get(i).getSelected());
				jsonArray.put(object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String endCard = jsonArray.toString();
		EtutorApplication.getInstance().getSpUtil().setCardSort(endCard);
	}

}
