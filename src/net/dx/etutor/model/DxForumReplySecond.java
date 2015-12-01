package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 2级回复
 * 
 * @author app
 * 
 */
public class DxForumReplySecond implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4460992342817370921L;

	private Integer id;
	private String avatarUrl;
	private String userName;
	private String content;
	private String topicId;
	private String replyUserId;
	private String replyId;
	private String replyIndex;
	private String subReplyIndex;
	private String status;
	private Integer belauds;
	private String createTime;
	private String replyUserName;
	private int isBelaud;
	private int op;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			avatarUrl = json.getString("avatarUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			userName = json.getString("userName");
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
			topicId = json.getString("topicId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			replyUserId = json.getString("replyUserId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			replyId = json.getString("replyId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			replyIndex = json.getString("replyIndex");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			subReplyIndex = json.getString("subReplyIndex");
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
			replyUserName = json.getString("replyUserName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			belauds = json.getInt("belauds");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			isBelaud = json.getInt("isBelaud");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			op = json.getInt("op");
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
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getReplyIndex() {
		return replyIndex;
	}

	public void setReplyIndex(String replyIndex) {
		this.replyIndex = replyIndex;
	}

	public String getSubReplyIndex() {
		return subReplyIndex;
	}

	public void setSubReplyIndex(String subReplyIndex) {
		this.subReplyIndex = subReplyIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getBelauds() {
		return belauds;
	}

	public void setBelauds(Integer belauds) {
		this.belauds = belauds;
	}

	
	public int getIsBelaud() {
		return isBelaud;
	}

	public void setIsBelaud(int isBelaud) {
		this.isBelaud = isBelaud;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

}
