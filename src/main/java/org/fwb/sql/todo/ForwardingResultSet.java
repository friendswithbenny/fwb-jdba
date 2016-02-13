package org.fwb.sql.todo;

import java.sql.ResultSet;

import com.google.common.collect.ForwardingObject;

/** @deprecated TODO override all ResultSet methods to forward to delegate */
abstract class ForwardingResultSet extends ForwardingObject implements ResultSet {
	@Override
	protected abstract ResultSet delegate();
	
	abstract // TODO remove abstract when parent class is fixed
	public static class FixedForwardingResultSet extends ForwardingResultSet {
		final ResultSet DELEGATE;
		public FixedForwardingResultSet(ResultSet rs) {
			DELEGATE = rs;
		}
		@Override
		protected ResultSet delegate() {
			return DELEGATE;
		}
	}
}
