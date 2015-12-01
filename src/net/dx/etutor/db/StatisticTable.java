package net.dx.etutor.db;

public interface StatisticTable{

	public static final String TABLE_NAME="statistic";
	public static final String ID="statisticId";
	
	public static final String START_TIME="startTime";
	public static final String END_TIME="endTime";
	public static final String UPLOAD_FLAG="uploadFlag";
	
	
	public static final int INDEX_ID=0;
	public static final int INDEX_START_TIME=1;
	public static final int INDEX_END_TIME=2;
	public static final int INDEX_UPLOAD_FLAG=3;
	
	public static final String SQL_TABLE_DROP=
			String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
	
	public static final String SQL_TABLE_CREATE=new StringBuffer()
	.append("CREATE TABLE IF NOT EXISTS ")
	.append(TABLE_NAME).append(" (")
	.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	.append(START_TIME).append(" TEXT,")
	.append(END_TIME).append(" TEXT,")
	.append(UPLOAD_FLAG).append(" INTEGER")
	.append(");").toString();
	
	
	
}
