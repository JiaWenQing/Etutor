package net.dx.etutor.model;

import net.dx.etutor.app.EtutorApplication;

public class DxStatistic implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6023014635970224653L;

	private Integer statisticId;
	private String startTime;
	private String endTime;
	private Integer uploadFlag;
	private String time;

	public Integer getStatisticId() {
		return statisticId;
	}

	public void setStatisticId(Integer statisticId) {
		this.statisticId = statisticId;
	}


	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * false=0，true=1
	 * 
	 * @return
	 */
	public Integer getUploadFlag() {
		return uploadFlag;
	}

	/**
	 * false=0，true=1
	 * 
	 * @param uploadFlag
	 */
	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{\"startTime\":\""+startTime+"\",\"endTime\":\""+endTime+"\"}";
	}
	
	

}
