package com.atg.openssp.common.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Arrays;

/**
 * @author André Schmer
 *
 */
class AbstractLogger implements Logger {

	private static final Logger log = LoggerFactory.getLogger(AbstractLogger.class);

	@Override
	public void info(final String msg) {
		log.info(msg);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final Marker marker, final String format, final Object... argArray) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void debug(final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final Marker marker, final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void info(final String format, final Object arg) {
		System.out.println("logger1 called " + format + " " + (String) arg);
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		System.out.println("logger3 called " + format);
	}

	@Override
	public void info(final String format, final Object... arguments) {
		System.out.println("logger2 called " + format + " " + Arrays.toString(arguments));
	}

	@Override
	public void info(final String msg, final Throwable t) {
		System.out.println("logger4 called ");

	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void info(final Marker marker, final String msg) {
		System.out.println("logger5 called ");
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		System.out.println("logger6 called ");

	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		System.out.println("marker:" + marker.getName() + " format:" + format + " ");

	}

	@Override
	public void info(final Marker marker, final String format, final Object... arguments) {
		System.out.println("logger8 called ");

	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		System.out.println("logger9 called ");

	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public void warn(final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final Marker marker, final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void error(final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void error(final Marker marker, final String msg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final Marker marker, final String format, final Object... arguments) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		throw new UnsupportedOperationException("not yet implemented.");
	}

}
