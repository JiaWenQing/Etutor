package net.dx.etutor.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DxStautsOrders {

	private String status;
	private List<DxOrder> list = new ArrayList<DxOrder>();

	public void initWithAttributes(JSONObject json) {
		try {
			status = json.getString("status");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			JSONArray jsonArray = (JSONArray) json.getJSONArray("orders");
			for (int i = 0; i <= jsonArray.length(); i++) {
				DxOrder dxOrder = new DxOrder();
				dxOrder.initWithAttributes((JSONObject) jsonArray
						.getJSONObject(i));
				dxOrder.setStatus(status);
				list.add(dxOrder);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DxOrder> getList() {
		return list;
	}

	public void setList(List<DxOrder> list) {
		this.list = list;
	}

}
