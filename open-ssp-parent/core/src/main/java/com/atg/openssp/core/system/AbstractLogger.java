package com.atg.openssp.core.system;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.Marker;

import com.atg.service.LogFacade;

/**
 * @author André Schmer
 *
 */
class AbstractLogger implements Logger {

	@Override
	public void info(final String msg) {
		LogFacade.logInfo(msg);
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

	}

	@Override
	public void trace(final String format, final Object arg) {

	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void trace(final String format, final Object... arguments) {

	}

	@Override
	public void trace(final String msg, final Throwable t) {

	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void trace(final Marker marker, final String msg) {

	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {

	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void trace(final Marker marker, final String format, final Object... argArray) {

	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {

	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void debug(final String msg) {

	}

	@Override
	public void debug(final String format, final Object arg) {

	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void debug(final String format, final Object... arguments) {

	}

	@Override
	public void debug(final String msg, final Throwable t) {

	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void debug(final Marker marker, final String msg) {

	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {

	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void debug(final Marker marker, final String format, final Object... arguments) {

	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {

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

	}

	@Override
	public void warn(final String format, final Object arg) {

	}

	@Override
	public void warn(final String format, final Object... arguments) {

	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void warn(final String msg, final Throwable t) {

	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void warn(final Marker marker, final String msg) {

	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {

	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void warn(final Marker marker, final String format, final Object... arguments) {

	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {

	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void error(final String msg) {

	}

	@Override
	public void error(final String format, final Object arg) {

	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void error(final String format, final Object... arguments) {

	}

	@Override
	public void error(final String msg, final Throwable t) {

	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void error(final Marker marker, final String msg) {

	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {

	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void error(final Marker marker, final String format, final Object... arguments) {

	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {

	}

}
