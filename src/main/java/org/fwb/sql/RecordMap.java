package org.fwb.sql;

import org.fwb.sql.RecordFunction.StringRecordFunction;
import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * bridging jdbc ResultSet with collections Map&lt;String, T>
 * 
 * N.B. this class does not implement Map; it contains only factory methods.
 */
public class RecordMap {
	/** @deprecated static utilities only */
	@Deprecated
	private RecordMap() { }
	
	public static <T> Map<String, T> toMap(ResultSet rs) throws SQLException {
		return Maps.toMap(new HeaderList(rs), new RecordFunction<T>(rs));
	}
	public static Map<String, String> toMapStrings(ResultSet rs) throws SQLException {
		return Maps.toMap(new HeaderList(rs), new StringRecordFunction(rs));
	}
	
	/**
	 * @deprecated this does not gracefully handle duplicate field-names
	 * @see #toMap(ResultSet)
	 */
	public static <T> Map<String, T> asMap(ResultSet rs) throws SQLException {
		return Maps.asMap(new HeaderList.HeaderSet(rs), new RecordFunction<T>(rs));
	}
	
	/**
	 * @deprecated this does not gracefully handle duplicate field-names
	 * @see #toMapStrings(ResultSet)
	 */
	public static Map<String, String> asMapStrings(ResultSet rs) throws SQLException {
		return Maps.asMap(new HeaderList.HeaderSet(rs), new StringRecordFunction(rs));
	}
}
