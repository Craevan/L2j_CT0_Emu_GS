package com.l2j.emu.commons.logging;

import com.l2j.emu.commons.lang.StringReplacer;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class CLogger {

    private final Logger logger;

    public CLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    private static String format(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        final StringReplacer stringReplacer = new StringReplacer(message);
        stringReplacer.replaceAll(args);
        return stringReplacer.toString();
    }

    public void log(LogRecord record) {
        logger.log(record);
    }

    private void log0(Level level, StackTraceElement caller, Object message, Throwable exception) {
        if (!logger.isLoggable(level)) {
            return;
        }
        if (caller == null) {
             StackTraceElement newCaller = new Throwable().getStackTrace()[2];
            logger.logp(level, newCaller.getClassName(), newCaller.getMethodName(), format(String.valueOf(message)), exception);
        } else {
            logger.logp(level, caller.getClassName(), caller.getMethodName(), format(String.valueOf(message)), exception);
        }
    }
}
