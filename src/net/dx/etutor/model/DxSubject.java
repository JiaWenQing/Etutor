package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxSubject entity. @author MyEclipse Persistence Tools
 */
public class DxSubject implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 776735687856212136L;
	private Integer subjectId;
	private String subjectName;
	private String createTime;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			createTime = json.getString("createTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			subjectName = json.getString("subjectName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			subjectId = json.getInt("subjectId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxSubject() {
	}

	/** full constructor */
	public DxSubject(String subjectName, String createTime) {
		this.subjectName = subjectName;
		this.createTime = createTime;
	}

	// Property accessors
	public Integer getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}