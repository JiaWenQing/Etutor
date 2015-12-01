package net.dx.etutor.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DxOrderList implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2682487203127419042L;
	private List<DxOrders> orders = new ArrayList<DxOrders>();

	public void initWithAttributes(JSONObject json) {

		try {
			DxOrders dxOrders = new DxOrders();
			dxOrders.initWithAttributes(json);
			orders.add(dxOrders);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public DxOrderList() {
		// TODO Auto-generated constructor stub
	}
	public List<DxOrders> getOrders() {
		return orders;
	}

	public void setOrders(List<DxOrders> orders) {
		this.orders = orders;
	}

}
