package com.github.frtu.simple.logs.bridge;

import org.slf4j.helpers.MarkerIgnoringBase;

public abstract class AbstractPlaceholderBridgeLogger extends MarkerIgnoringBase {
    private static final long serialVersionUID = 1L;
    
    protected String name;

    public AbstractPlaceholderBridgeLogger(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg });
            trace(msg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg1, arg2 });
            trace(msg);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, arguments);
            trace(msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg });
            debug(msg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg1, arg2 });
            debug(msg);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, arguments);
            debug(msg);
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (isInfoEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg });
            info(msg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (isInfoEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg1, arg2 });
            info(msg);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, arguments);
            info(msg);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (isWarnEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg });
            warn(msg);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (isWarnEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg1, arg2 });
            warn(msg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, arguments);
            warn(msg);
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (isErrorEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg });
            error(msg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (isErrorEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, new Object[] { arg1, arg2 });
            error(msg);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            String msg = MessagePlaceholderFormatter.format(format, arguments);
            error(msg);
        }
    }
}