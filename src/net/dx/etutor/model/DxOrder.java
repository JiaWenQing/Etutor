package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxOrder entity. @author MyEclipse Persistence Tools
 */
public class DxOrder implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4002869626431437460L;
	private Integer id;
	private String orderId;
	private String fromUserId;
	private String toUserId;
	private String needId;
	private String firstCourseTime;
	private String status;
	private String type;// 接受 发送类型
	private String position;

	private DxUsers user = new DxUsers();

	// json获取转换
	public void initWithAttributes(JSONObject json) {

		try {
			JSONObject jsonObject = (JSONObject) json.getJSONObject("user");
			DxUsers dxUsers = new DxUsers();
			dxUsers.initWithAttributes(jsonObject);
			this.user = dxUsers;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			status = json.getString("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			firstCourseTime = json.getString("firstCourseTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			toUserId = json.getString("toUserId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			needId = json.getString("needId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fromUserId = json.getString("fromUserId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			orderId = json.getString("orderId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			type = json.getString("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxOrder() {
	}

	/** full constructor */
	public DxOrder(String orderId, String fromUserId, String toUserId,
			String firstCourseTime, String status) {
		this.orderId = orderId;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.firstCourseTime = firstCourseTime;
		this.status = status;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFromUserId() {
		return this.fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return this.toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getNeedId() {
		return needId;
	}

	public void setNeedId(String needId) {
		this.needId = needId;
	}

	public String getFirstCourseTime() {
		return this.firstCourseTime;
	}

	public void setFirstCourseTime(String firstCourseTime) {
		this.firstCourseTime = firstCourseTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DxUsers getUser() {
		return user;
	}

	public void setUser(DxUsers user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}