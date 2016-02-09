bridging java's relational API (jdbc) with other, namely algebraic APIs (collections, functions)

wraps instances of java.sql.ResultSet in sensible implementations of:

- java.util.List
- java.util.Map
- com.google.common.base.Function

also wraps instances of java.sql.ResultSetMetaData with implementation of List<String>

note: to be 100% precise, RecordList would implement List&lt;Object>
  and RecordMap would implement implement Map&lt;String, Object>.
however, they are generic for the sake of:
  string-serializer subclasses StringRecordList and StringRecordMap and
  consumers with knowledge of additional constraints on the types of a ResultSet's fields.
