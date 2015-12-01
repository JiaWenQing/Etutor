package net.dx.etutor.model;

public class DxCollect implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7305948620419117706L;

	/**
	 * 0学生，1老师
	 */
	private int type;
	private DxNeed need = new DxNeed();
	private DxTeacherList dxTeacherList = new DxTeacherList();

	public DxNeed getNeed() {
		return need;
	}

	public void setNeed(DxNeed need) {
		this.need = need;
	}

	public DxTeacherList getDxTeacherList() {
		return dxTeacherList;
	}

	public void setDxTeacherList(DxTeacherList dxTeacherList) {
		this.dxTeacherList = dxTeacherList;
	}

	public int getType() {
		return type;
	}

	/**
	 * 0学生，1老师
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

}
