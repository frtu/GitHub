package com.github.frtu.maven.plugin.logger;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.Logger;

import com.github.frtu.simple.logs.bridge.AbstractPlaceholderBridgeLogger;

/**
 * Allow to use slf4j placeholder inside Maven Mojo.
 * 
 * @author frtu
 */
public class MavenLogToSlf4jAdapter extends AbstractPlaceholderBridgeLogger implements Logger {
    private static final long serialVersionUID = -7058922562582723187L;

    private Log innerLogger;

    public MavenLogToSlf4jAdapter(Log log, String name) {
        super(name);
        this.innerLogger = log;
    }

    @Override
    public boolean isTraceEnabled() {
        return isDebugEnabled();
    }

    @Override
    public void trace(String msg) {
        debug(msg);
    }

    @Override
    public void trace(String msg, Throwable t) {
        innerLogger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return innerLogger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        innerLogger.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable t) {
        innerLogger.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return innerLogger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        innerLogger.info(msg);
    }

    @Override
    public void info(String msg, Throwable t) {
        innerLogger.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return innerLogger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        innerLogger.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable t) {
        innerLogger.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return innerLogger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        innerLogger.error(msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        innerLogger.error(msg, t);
    }
}
