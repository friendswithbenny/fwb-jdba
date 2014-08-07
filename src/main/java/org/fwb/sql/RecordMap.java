package org.fwb.sql;

import org.fwb.sql.RecordFunction.StringRecordFunction;
import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * bridging java.sql.ResultSet with java.util.Map&lt;String, ...>
 * 
 * N.B. this class does not implement Map; it contains factory methods.
 */
public class RecordMap {
	public static <T> Map<String, T> of(ResultSet rs) throws SQLException {
		return Maps.asMap(new HeaderList(rs), new RecordFunction<T>(rs));
	}

	public static Map<String, String> ofStringValues(ResultSet rs) throws SQLException {
		return Maps.asMap(new HeaderList(rs), new StringRecordFunction(rs));
	}
}