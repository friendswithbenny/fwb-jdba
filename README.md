bridging java's relational API (jdbc) with other, namely algebraic APIs (collections, functions)

wraps instances of java.sql.ResultSet in sensible implementations of:

- java.util.List
- java.util.Map
- com.google.common.base.Function

also wraps instances of java.sql.ResultSetMetaData with implementation of List<String>
