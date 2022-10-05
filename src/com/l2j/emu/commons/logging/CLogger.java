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

    /**
     * Форматирует сообщение, разрешив использовать '{}' в качестве параметра. Избегайте конкатенации строк.
     *
     * @param message : Строка для форматирования
     * @param args    : аргументы для передачи
     * @return отформатированная строка
     */
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

    /**
     * Логирует объект с уровнем FINE
     *
     * @param message : объект для логирования
     */
    public void debug(Object message) {
        log0(Level.FINE, null, message, null);
    }

    /**
     * Логирует объект с уровнем FINE
     *
     * @param message : объект для логирования
     * @param args    : аргументы для форматирования сообщения
     */
    public void debug(Object message, Object... args) {
        log0(Level.FINE, null, message, null, args);
    }

    /**
     * Логирует объект с уровнем FINE
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     */
    public void debug(Object message, Throwable exception) {
        log0(Level.FINE, null, message, exception);
    }

    /**
     * Логирует объект с уровнем FINE
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     * @param args      : аргументы для форматирования сообщения
     */
    public void debug(Object message, Throwable exception, Object... args) {
        log0(Level.FINE, null, message, exception, args);
    }

    /**
     * Логирует объект с уровнем INFO.
     *
     * @param message : объект для логирования
     */
    public void info(Object message) {
        log0(Level.INFO, null, message, null);
    }

    /**
     * Логирует объект с уровнем INFO.
     *
     * @param message : объект для логирования
     * @param args    : аргументы для форматирования сообщения
     */
    public void info(Object message, Object... args) {
        log0(Level.INFO, null, message, null, args);
    }

    /**
     * Логирует объект с уровнем INFO.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     */
    public void info(Object message, Throwable exception) {
        log0(Level.INFO, null, message, exception);
    }

    /**
     * Логирует объект с уровнем INFO.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     * @param args      : аргументы для форматирования сообщения
     */
    public void info(Object message, Throwable exception, Object... args) {
        log0(Level.INFO, null, message, exception, args);
    }

    /**
     * Логирует объект с уровнем WARNING.
     *
     * @param message : объект для логирования
     */
    public void warn(Object message) {
        log0(Level.WARNING, null, message, null);
    }

    /**
     * Логирует объект с уровнем WARNING.
     *
     * @param message : объект для логирования
     * @param args    : аргументы для форматирования сообщения
     */
    public void warn(Object message, Object... args) {
        log0(Level.WARNING, null, message, null, args);
    }

    /**
     * Логирует объект с уровнем WARNING.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     */
    public void warn(Object message, Throwable exception) {
        log0(Level.WARNING, null, message, exception);
    }

    /**
     * Логирует объект с уровнем WARNING.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     * @param args      : аргументы для форматирования сообщения
     */
    public void warn(Object message, Throwable exception, Object... args) {
        log0(Level.WARNING, null, message, exception, args);
    }

    /**
     * Логирует объект с уровнем SEVERE.
     *
     * @param message : объект для логирования
     */
    public void error(Object message) {
        log0(Level.SEVERE, null, message, null);
    }

    /**
     * Логирует объект с уровнем SEVERE.
     *
     * @param message : объект для логирования
     * @param args    : аргументы для форматирования сообщения
     */
    public void error(Object message, Object... args) {
        log0(Level.SEVERE, null, message, null, args);
    }

    /**
     * Логирует объект с уровнем SEVERE.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     */
    public void error(Object message, Throwable exception) {
        log0(Level.SEVERE, null, message, exception);
    }

    /**
     * Логирует объект с уровнем SEVERE.
     *
     * @param message   : объект для логирования
     * @param exception : логирует перехваченное исключение
     * @param args      : аргументы для форматирования сообщения
     */
    public void error(Object message, Throwable exception, Object... args) {
        log0(Level.SEVERE, null, message, exception, args);
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

    private void log0(Level level, StackTraceElement caller, Object message, Throwable exception, Object... args) {
        if (!logger.isLoggable(level)) {
            return;
        }
        if (caller == null) {
            StackTraceElement newCaller = new Throwable().getStackTrace()[2];
            logger.logp(level, newCaller.getClassName(), newCaller.getMethodName(), format(String.valueOf(message), args), exception);
        } else {
            logger.logp(level, caller.getClassName(), caller.getMethodName(), format(String.valueOf(message), args), exception);
        }
    }
}
