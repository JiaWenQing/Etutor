package net.dx.etutor.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.dx.etutor.R;
import net.dx.etutor.activity.adapter.EditListAdapter;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.Channel;
import net.dx.etutor.view.listview.DragListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CardContralActivity extends BaseActivity {

	/**
	 * 用户选定
	 */
	private List<Channel> userChannelList = new ArrayList<Channel>();
	private List<Channel> startChannelList = new ArrayList<Channel>();

	private DragListView mList;

	private EditListAdapter adapter;

	public OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {

			} else {

			}
			adapter.notifyDataSetChanged();
		}
	};

	private String startCard;

	private String endCard;

	private boolean isChange;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_card_list);
		setTitle(R.string.title_card_contral);
		showIcon(0, "确定");
		initCardData();
		mList = (DragListView) findViewById(R.id.userGridView);
		adapter = new EditListAdapter(this, userChannelList);
		mList.setAdapter(adapter);

	}

	private void initCardData() {
		// TODO Auto-generated method stub
		startCard = EtutorApplication.getInstance().getSpUtil().getCardSort();
		try {
			JSONArray jsonArray = new JSONArray(startCard);
			for (int i = 0; i < jsonArray.length(); i++) {
				Channel channel = new Channel();
				channel.initWithAttributes(jsonArray.getJSONObject(i));
				userChannelList.add(channel);
				startChannelList.add(channel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void iconClick() {
		// TODO Auto-generated method stub
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
		endCard = jsonArray.toString();
		mApplication.getSpUtil().setCardSort(endCard);
		if (!startCard.equals(endCard)) {
			isChange = true;
		}
		Intent intent = new Intent(CardContralActivity.this,
				MainFragmentActivity.class);
		intent.putExtra("isChange", isChange);
		setResult(RESULT_OK, intent);
		finish();
	}
}
