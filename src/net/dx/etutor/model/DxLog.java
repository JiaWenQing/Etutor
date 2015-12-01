package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxLog entity. @author MyEclipse Persistence Tools
 */
public class DxLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1429143572503661364L;
	private Integer logId;
	private String operateTable;
	private String operateType;
	private String operateTime;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			operateTime = json.getString("operateTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			operateType = json.getString("operateType");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			operateTable = json.getString("operateTable");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			logId = json.getInt("logId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxLog() {
	}

	/** full constructor */
	public DxLog(String operateTable, String operateType, String operateTime) {
		this.operateTable = operateTable;
		this.operateType = operateType;
		this.operateTime = operateTime;
	}

	// Property accessors
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getOperateTable() {
		return this.operateTable;
	}

	public void setOperateTable(String operateTable) {
		this.operateTable = operateTable;
	}

	public String getOperateType() {
		return this.operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

}