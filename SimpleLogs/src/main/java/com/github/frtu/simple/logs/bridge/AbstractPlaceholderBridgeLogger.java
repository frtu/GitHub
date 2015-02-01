package com.github.frtu.simple.logs.bridge;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * Bridge abstract class that allow to resolve the sign {} by replacing it with the following arguments.
 * 
 * @author frtu
 */
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
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg });
            trace(formattingTuple.getMessage());
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg1, arg2 });
            trace(formattingTuple.getMessage());
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, arguments);
            trace(formattingTuple.getMessage());
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg });
            debug(formattingTuple.getMessage());
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg1, arg2 });
            debug(formattingTuple.getMessage());
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, arguments);
            debug(formattingTuple.getMessage());
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg });
            info(formattingTuple.getMessage());
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg1, arg2 });
            info(formattingTuple.getMessage());
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, arguments);
            info(formattingTuple.getMessage());
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg });
            warn(formattingTuple.getMessage());
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg1, arg2 });
            warn(formattingTuple.getMessage());
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, arguments);
            warn(formattingTuple.getMessage());
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg });
            error(formattingTuple.getMessage());
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, new Object[] { arg1, arg2 });
            error(formattingTuple.getMessage());
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(format, arguments);
            error(formattingTuple.getMessage());
        }
    }
}