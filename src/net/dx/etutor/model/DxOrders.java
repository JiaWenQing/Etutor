package net.dx.etutor.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DxOrders implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7286453657673912799L;
	private String type;
	private List<DxStautsOrders> orders = new ArrayList<DxStautsOrders>();

	public void initWithAttributes(JSONObject json) {
		try {
			type = json.getString("type");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			JSONArray jsonArray = (JSONArray) json.getJSONArray("orders");
			for (int i = 0; i <= jsonArray.length(); i++) {
				DxStautsOrders dxStautsOrders = new DxStautsOrders();
				dxStautsOrders.initWithAttributes((JSONObject) jsonArray
						.getJSONObject(i));
				orders.add(dxStautsOrders);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DxStautsOrders> getOrders() {
		return orders;
	}

	public void setOrders(List<DxStautsOrders> orders) {
		this.orders = orders;
	}

}
