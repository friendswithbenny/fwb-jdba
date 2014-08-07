package org.fwb.sql;

/**
 * bridging RDBMS-relational java API: jdbc / javax.sql
 * with
 * standard algebra APIs: java.util.Collection / com.google.common.base.Function
 * 
 * note: to be 100% precise, RecordList would implement List&lt;Object>
 *   and RecordMap would implement implement Map&lt;String, Object>
 * however, it is generic for the sake of:
 *   string-serializer subclasses StringRecordList and StringRecordMap and
 *   consumers with knowledge of additional constraints on the types of a ResultSet's fields.
 * 
 * @author friendswithbenny@gmail.com
 */