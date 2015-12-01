package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxPrivatemsg entity. @author MyEclipse Persistence Tools
 */
public class DxPrivatemsg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6015543675139527126L;
	// Fields

	private Integer id;
	private String senderId;
	private String receiverId;
	private String message;
	private String createTime;
	private String status;
	private DxUsers dxUser;
	private String dialogueId;
	private Integer newMsg;
	private String record;// 语音
	private String picture;// 图片
//	private String time;//语音时常


	// json获取转换
	public void initWithAttributes(JSONObject json) {
		
		try {
			dialogueId = json.getString("dialogueId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			record = json.getString("record");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			picture = json.getString("picture");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject jsonObject = (JSONObject) json.getJSONObject("user");
			DxUsers user = new DxUsers();
			user.initWithAttributes(jsonObject);
			this.dxUser = user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
//			long time=json.getJSONObject("createTime").getLong("time");
//			createTime = DateUtil.getStringByFormat(time, DateUtil.dateFormatYMDHMS);

			createTime = json.getString("createTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			message = json.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			senderId = json.getString("senderId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			receiverId = json.getString("receiverId");
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
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newMsg = json.getInt("newMsg");
			if (newMsg==null) {
				newMsg=0;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxPrivatemsg() {
	}

	/** minimal constructor */
	public DxPrivatemsg(String receiverId) {
		this.receiverId = receiverId;
	}

	/** full constructor */
	public DxPrivatemsg(String receiverId, String senderId, String message,
			String createTime) {
		this.receiverId = receiverId;
		this.senderId = senderId;
		this.message = message;
		this.createTime = createTime;
	}

	
	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DxUsers getDxUser() {
		return dxUser;
	}

	public void setDxUser(DxUsers dxUser) {
		this.dxUser = dxUser;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDialogueId() {
		return dialogueId;
	}

	public void setDialogueId(String dialogueId) {
		this.dialogueId = dialogueId;
	}

	public Integer getNewMsg() {
		return newMsg;
	}

	public void setNewMsg(Integer newMsg) {
		this.newMsg = newMsg;
	}
	
	

}