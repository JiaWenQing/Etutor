package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxSystemmsg entity. @author MyEclipse Persistence Tools
 */
/**
 * @author app
 *
 */
public class DxSystemmsg implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -954026460186746828L;
	private Integer id;
	private String systemMessage;
	private String sysTime;
	private String title;
	private String createTime;
	private int type;
	private int userId;
	private String url;

	// json获取转换
	public void initWithAttributes(JSONObject json) {

		try {
			type = json.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			url = json.getString("url");
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
			title = json.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sysTime = json.getString("sysTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			systemMessage = json.getString("systemMessage");
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
			userId = json.getInt("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxSystemmsg() {
	}

	/** minimal constructor */
	public DxSystemmsg(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	/** full constructor */
	public DxSystemmsg(String systemMessage, String sysTime, String title,
			String createTime) {
		this.systemMessage = systemMessage;
		this.sysTime = sysTime;
		this.title = title;
		this.createTime = createTime;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSystemMessage() {
		return this.systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public String getSysTime() {
		return this.sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}