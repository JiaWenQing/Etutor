package net.dx.etutor.model;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * DxCertificate entity. @author MyEclipse Persistence Tools
 */
public class DxCertificate implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455423432302330637L;
	private Integer id;
	private String userId;
	private String certificateUrl;
	private String certificateUpdateTime;
	private boolean selected = false;
	private String path;
	private String summary;
	private String status;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			summary = json.getString("summary");
			if (TextUtils.isEmpty(summary)) {
				summary="无";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			id = json.getInt("id");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			userId = json.getString("userId");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			certificateUrl = json.getString("certificateUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			certificateUpdateTime = json.getString("certificateUpdateTime");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			status = json.getString("checkStatus");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxCertificate() {
	}


	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCertificateUrl() {
		return this.certificateUrl;
	}

	public void setCertificateUrl(String certificateUrl) {
		this.certificateUrl = certificateUrl;
	}

	public String getCertificateUpdateTime() {
		return this.certificateUpdateTime;
	}

	public void setCertificateUpdateTime(String certificateUpdateTime) {
		this.certificateUpdateTime = certificateUpdateTime;
	}


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}