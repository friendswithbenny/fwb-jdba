package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Set;

/**
 * bridging java.sql.ResultSetMetaData with java.util.List&lt;String>
 */
public class HeaderList extends AbstractList<String> implements Set<String> {
	public final ResultSetMetaData RSMD;
	
	public HeaderList(ResultSet rs) throws SQLException {
		this(rs.getMetaData());
	}
	
	public HeaderList(ResultSetMetaData rsmd) {
		this.RSMD = rsmd;
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