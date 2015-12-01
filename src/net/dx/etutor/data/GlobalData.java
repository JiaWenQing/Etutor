package net.dx.etutor.data;

/**
 * 系统全局变量类，单一实例模式，用于任意地方交换数据.
 * 
 * 
 */
public class GlobalData {
	private static GlobalData instance = new GlobalData();

	public static GlobalData instance() {
		return instance;
	}

	public String uuid = "";
	/** 用户ID. */
	public String userId = "";
	public String subjectItme = "";
	
	/**
	 * 课表
	 */
	public int courseTable=0;

}
