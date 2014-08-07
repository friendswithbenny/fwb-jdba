package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * bridging java.sql.ResultSet with java.util.List
 *
 * note: it would be trivial to implement this class using HeaderList and RecordFunction, and Maps.asMap(_):
 *	 "Maps.asMap(new HeaderList(rs), new RecordFunction(rs))"
 *	 this explicit implementation uses ResultSet.getObject(int) to avoid
 *	 the possible performance hit associated with ResultSet.getObject(String)
 */
public class RecordList<T> extends AbstractList<T> implements RandomAccess {
	private final ResultSet rs;
	
	public RecordList(ResultSet rs) {
		this.rs = rs;
	}
	
	public ResultSet getResultSet() {
		return rs;
	}
	
	@Override
	public int size() {
		try {
			return rs.getMetaData().getColumnCount();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(int i) throws ClassCastException, RuntimeException {
		try {
			return (T) rs.getObject(i+1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* STATIC */
	/**
	 * using jdbc's getString(_) method to fetch Strings from the db
	 * (rather than getObject(_).toString() for instance).
	 */
	public static class StringRecordList extends RecordList<String> {
		public StringRecordList(ResultSet rs) {
			super(rs);
		}
		
		public String get(int i) throws RuntimeException {
			try {
				return getResultSet().getString(i+1);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}