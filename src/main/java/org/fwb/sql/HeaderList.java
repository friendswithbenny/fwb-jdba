package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Set;

/**
 * bridging jdbc ResultSetMetaData with collections List&lt;String>
 * 
 * TODO warning, this breaks the contract of {@link Set} if there are duplicate column titles.
 */
public class HeaderList extends AbstractList<String> implements Set<String> {
	final ResultSetMetaData RSMD;
	
	public HeaderList(ResultSet rs) throws SQLException {
		this(rs.getMetaData());
	}
	
	public HeaderList(ResultSetMetaData rsmd) {
		RSMD = rsmd;
	}
	
	public ResultSetMetaData getMetaData() {
		return RSMD;
	}
	
	@Override
	public int size() {
		try {
			return RSMD.getColumnCount();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String get(int i) {
		try {
			return RSMD.getColumnLabel(i+1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}