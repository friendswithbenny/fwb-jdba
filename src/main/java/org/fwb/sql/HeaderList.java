package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Set;


/**
 * bridging jdbc ResultSetMetaData with collections List&lt;String>
 */
public class HeaderList extends AbstractList<String> {
	final ResultSetMetaData RSMD;
	
	public HeaderList(ResultSet rs) {
		this(getMetaData(rs));
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
			return RSMD.getColumnLabel(1 + i);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ResultSetMetaData getMetaData(ResultSet rs) {
		try {
			return rs.getMetaData();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @deprecated this breaks the contract of {@link Set} if given non-unique field names.
	 */
	public static class HeaderSet extends HeaderList implements Set<String> {
		public HeaderSet(ResultSet rs) {
			this(getMetaData(rs));
		}
		public HeaderSet(ResultSetMetaData rsmd) {
			super(rsmd);
			
			// TODO requires fwb-collection-utils
//			SetView.checkUnique(this);
		}
	}
	
	/** TODO it's just lazy to extend HeaderList; semantically it's incorrect */
	public static class HeaderTypeList extends HeaderList {
		public HeaderTypeList(ResultSetMetaData rsmd) {
			super(rsmd);
		}
		
		@Override
		public String get(int i) {
			try {
				return RSMD.getColumnTypeName(1 + i);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}