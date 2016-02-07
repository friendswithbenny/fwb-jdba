package org.fwb.sql;

/**
 * note: to be 100% precise, RecordList would implement List&lt;Object>
 *   and RecordMap would implement implement Map&lt;String, Object>
 * however, it is generic for the sake of:
 *   string-serializer subclasses StringRecordList and StringRecordMap and
 *   consumers with knowledge of additional constraints on the types of a ResultSet's fields.
 */