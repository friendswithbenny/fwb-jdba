package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * bridging java.sql.ResultSet with java.util.List,
 * this delegates to {@link ResultSet#getObject(int)}.
 */
public class RecordList<T> extends AbstractList<T> implements RandomAccess {
	final ResultSet RS;
	
	public RecordList(ResultSet rs) {
		RS = rs;
	}
	
	public ResultSet getResultSet() {
		return RS;
	}
	
	@Override
	public int size() {
		try {
			return getResultSet().getMetaData().getColumnCount();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(int i) throws ClassCastException, RuntimeException {
		try {
			return (T) getResultSet().getObject(i+1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * this alternative approach uses HeaderList, RecordFunction, and Lists.transform.
	 * the distinction is a possible performance hit associated with {@link ResultSet#getObject(String)}
	 * rather than the nominally higher-performance {@link ResultSet#getObject(int)}
	 */
	public static <T> List<T> recordFunctionList(ResultSet rs) throws SQLException {
		return Lists.transform(new HeaderList(rs), new RecordFunction<T>(rs));
	}
	
	/**
	 * uses jdbc's getString(_) method to fetch Strings from the db.
	 */
	public static class StringRecordList extends RecordList<String> {
		public StringRecordList(ResultSet rs) {
			super(rs);
		}
		
		@Override
		public String get(int i) throws RuntimeException {
			try {
				return getResultSet().getString(i+1);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * calls {@link Object#toString()}, but unlike Guava's version, accepts null (returning null).
		 * @see com.google.common.base.Functions#toStringFunction()
		 */
		static Function<Object, String> TO_STRING = new Function<Object, String>() {
			@Override
			public String apply(Object input) {
				return null == input ? null : input.toString();
			}
		};
		/**
		 * this alternative approach
		 * uses {@link ResultSet#getObject(int)}.{@link Object#toString()}
		 * instead of {@link ResultSet#getString(int)}
		 */
		public static List<String> toStringRecordList(ResultSet rs) {
			return Lists.transform(new RecordList<Object>(rs), TO_STRING);
		}
	}
}