package net.dx.etutor.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 栏目对应ID
	 * */
	public Integer id;
	/**
	 * 栏目对应NAME
	 * */
	public String name;
	/**
	 * 栏目在整体中的排序顺序 rank
	 * */
	public Integer orderId;
	/**
	 * 栏目是否选中
	 * */
	public Integer selected;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public Channel() {
	}

	public Channel(Integer id, String name, Integer orderId, Integer selected) {
		super();
		this.id = id;
		this.name = name;
		this.orderId = orderId;
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "Channel [id=" + id + ", name=" + name + ", orderId=" + orderId
				+ ", selected=" + selected + "]";
	}

	public void initWithAttributes(JSONObject json) {
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			name = json.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			selected = json.getInt("selected");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
