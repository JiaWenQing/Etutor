package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxComment entity. @author MyEclipse Persistence Tools
 */
public class DxComment implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5184300612275809372L;
	private Integer commentId;
	private String starLevel;
	private String commentTime;
	private String content;
	private String fromUserId;
	private String toUserId;
	private String commentType;
	private String orderId;
	private String createTime;

	private DxUsers dxUsers;
	private String commentCount;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			commentCount = json.getString("commentCount");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			createTime = json.getString("createTime");
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
			commentType = json.getString("commentType");
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
			fromUserId = json.getString("fromUserId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			content = json.getString("content");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			commentTime = json.getString("commentTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			starLevel = json.getString("starLevel");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			commentId = json.getInt("commentId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject jsonObject = (JSONObject) json.getJSONObject("user");
			DxUsers user = new DxUsers();
			user.initWithAttributes(jsonObject);
			this.dxUsers = user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxComment() {
	}

	/** full constructor */
	public DxComment(String starLevel, String commentTime, String content,
			String fromUserId, String toUserId, String commentType,
			String orderId, String createTime) {
		this.starLevel = starLevel;
		this.commentTime = commentTime;
		this.content = content;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.commentType = commentType;
		this.orderId = orderId;
		this.createTime = createTime;
	}

	// Property accessors
	public Integer getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getStarLevel() {
		return this.starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}

	public String getCommentTime() {
		return this.commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getCommentType() {
		return this.commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public DxUsers getDxUsers() {
		return dxUsers;
	}

	public void setDxUsers(DxUsers dxUsers) {
		this.dxUsers = dxUsers;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	

}