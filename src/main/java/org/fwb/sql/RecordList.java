package org.fwb.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * bridging jdbc {@link ResultSet} with collections {@link List},
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
	
	/**
	 * this implements Iterator by storing a copy of the current record to peek at the next.
	 * this stores the record as a List of its values, and does not support LOB fields.
	 */
	public static class RecordListIterator extends ForwardingList<Object> implements Iterator<Void> {
		final RecordList<?> NEXT;
		private ImmutableList<Object> current = null;
		private boolean hasNext;
		
		RecordListIterator(ResultSet rs) {
			NEXT = new RecordList<Object>(rs);
			peek();
		}
		
		/** advance the state of NEXT.RS by 1, and register it as hasNext */
		private void peek() {
			try {
				hasNext = NEXT.RS.next();
			} catch (SQLException e) {
				throw new RuntimeException(e); // TODO
			}
		}
		
		@Override
		protected List<Object> delegate() {
			if (null == current) {
				try {
					boolean isBeforeFirst = NEXT.RS.isBeforeFirst();
					throw new IllegalStateException("next has never been called: " + isBeforeFirst);
				} catch (SQLException e) {
					throw new IllegalStateException("next has never been called (and isBeforeFirst failed)", e);
				}
			}
			
			return current;
		}
		
		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Void next() {
			if (! hasNext())
				throw new NoSuchElementException();
			
			current = ImmutableList.copyOf(NEXT);
			peek();
			return null;
		}

		@Override
		public void remove() {
			// TODO there must be a jdbc way to issue a delete-record (roundabout though it may be)?
			throw new UnsupportedOperationException();
		}
	}
}