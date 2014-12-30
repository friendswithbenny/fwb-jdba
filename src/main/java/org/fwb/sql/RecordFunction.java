package org.fwb.sql;

import com.google.common.base.Function;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * bridging java.sql.ResultSet with com.google.common.base.Function
 */
public class RecordFunction<T> implements Function<String, T> {
	public final ResultSet RS;
	
	public RecordFunction(ResultSet rs) {
		this.RS = rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T apply(String input) throws ClassCastException, RuntimeException {
		try {
			return (T) RS.getObject((String) input);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* STATIC */
	/**
	 * using jdbc's getString(_) method to fetch Strings from the db
	 * (rather than getObject(_).toString() for instance).
	 */
	public static class StringRecordFunction extends RecordFunction<String> {
		public StringRecordFunction(ResultSet rs) {
			super(rs);
		}
		
		@Override
		public String apply(String input) throws RuntimeException {
			try {
				return RS.getString(input);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}