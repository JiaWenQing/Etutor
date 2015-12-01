package net.dx.etutor.db;

public interface CustomEventTable{

	public static final String TABLE_NAME="event";
	public static final String ID="id";
	public static final String TITLE="name";
	public static final String COUNT="count";
	
	public static final int INDEX_ID=0;
	public static final int INDEX_TITLE=1;
	public static final int INDEX_CONTENT=2;
	
	public static final String SQL_TABLE_DROP=
			String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
	
	public static final String SQL_TABLE_CREATE=new StringBuffer()
	.append("CREATE TABLE IF NOT EXISTS ")
	.append(TABLE_NAME).append(" (")
	.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	.append(TITLE).append(" TEXT,")
	.append(COUNT).append(" INTEGER,")
	.append(");").toString();
	
	
	
}
