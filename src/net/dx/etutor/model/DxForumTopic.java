package net.dx.etutor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 帖子
 * 
 * @author app
 * 
 */
/**
 * @author app
 * 
 */
public class DxForumTopic implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7752556446796404425L;
	private Integer id;
	private String title;
	private String userId;
	/**
	 * 0：一般帖子 1：顶 2：精华帖
	 */
	private Integer topicType;
	private String status;
	private String collectId;
	private int isBelaud;
	private String createTime;
	/**
	 * 认证 核实
	 */
	private Integer identify = 0;
	private Integer rank = 0;
	private Integer count;// 评论数
	private Integer clicks;// 浏览数
	private Integer belauds;// 点赞数
	private String userName;
	private String description;
	private String avatarUrl;
	private Integer boardId;// 版块ID
	private Integer replyId; // 回复Id;

	// 0:发帖 1：一级回复,2: 二级回复
	private Integer replyType;

	private DxForumReply dxForumReply = new DxForumReply();

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			replyId = json.getInt("replyId");
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
			description = json.getString("description");
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
			boardId = json.getInt("boardId");
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
			count = json.getInt("count");
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
			title = json.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			userId = json.getString("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			topicType = json.getInt("topicType");
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
			collectId = json.getString("collectId");
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
			clicks = json.getInt("clicks");
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
			identify = json.getInt("identify");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rank = json.getInt("rank");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONArray jsonArray = (JSONArray) json.getJSONArray("dxForumReply");
			dxForumReply.initWithAttributes((JSONObject) jsonArray
					.getJSONObject(0));
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getBelauds() {
		return belauds;
	}

	public void setBelauds(Integer belauds) {
		this.belauds = belauds;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}

	public Integer getIdentify() {
		return identify;
	}

	public void setIdentify(Integer identify) {
		this.identify = identify;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public DxForumReply getDxForumReply() {
		return dxForumReply;
	}

	public void setDxForumReply(DxForumReply dxForumReply) {
		this.dxForumReply = dxForumReply;
	}

	/**
	 * @return 0:发帖 1:1级回复 2:2级回复 3:举报1级回复 4:举报2级回复 5:举报帖子
	 */
	public Integer getReplyType() {
		return replyType;
	}

	/**
	 * @param replyType 0:发帖 1:1级回复 2:2级回复 3:举报1级回复 4:举报2级回复 5:举报帖子
	 */
	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}

	public Integer getReplyId() {
		return replyId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}

	public int getIsBelaud() {
		return isBelaud;
	}

	public void setIsBelaud(int isBelaud) {
		this.isBelaud = isBelaud;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "collectId=" + collectId + " isBelaud=" + isBelaud + " title="
				+ title + " topicId=" + id+" replyId="+replyId;
	}

}
