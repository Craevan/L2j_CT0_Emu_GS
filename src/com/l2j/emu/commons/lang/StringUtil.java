package com.l2j.emu.commons.lang;

import com.l2j.emu.commons.logging.CLogger;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    public static final String DIGITS = "0123456789";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LETTERS = LOWER_CASE_LETTERS + UPPER_CASE_LETTERS;
    public static final String LETTERS_AND_DIGITS = LETTERS + DIGITS;

    private static final CLogger LOGGER = new CLogger(StringUtil.class.getName());

    /**
     * Проверяет каждую строку, переданную в качестве параметра.
     * Если хотя бы одна из них пуста или равна null, возвращается true.
     *
     * @param strings : Строка для проверки.
     * @return true, если хотя бы одна строка пуста или равна null.
     */
    public static boolean isEmpty(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty())
                return true;
        }
        return false;
    }

    /**
     * Добавляет объекты к существующему StringBuilder.
     *
     * @param sb      : StringBuilder к которому выполняется присоединение.
     * @param content : параметр для присоединения.
     */
    public static void append(StringBuilder sb, Object... content) {
        for (Object obj : content)
            sb.append((obj == null) ? null : obj.toString());
    }

    /**
     * @param text : Строка для проверки.
     * @return true, если строка содержит только цифры. Иначе false.
     */
    public static boolean isDigit(String text) {
        if (text == null) {
            return false;
        }

        return text.matches("[0-9]+");
    }

    /**
     * @param text : строка для проверки.
     * @return true, если строка содержит только цифры или буквы. Иначе false.
     */
    public static boolean isAlphaNumeric(String text) {
        if (text == null) {
            return false;
        }

        for (char chars : text.toCharArray()) {
            if (!Character.isLetterOrDigit(chars)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param value : число для форматирования.
     * @return число отформатированное с разделителем ",".
     */
    public static String formatNumber(long value) {
        return NumberFormat.getInstance(Locale.ENGLISH).format(value);
    }

    /**
     * @param string : начальное слово для перемешивания.
     * @return анаграмма заданной строки.
     */
    public static String scrambleString(String string) {
        final List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);

        final StringBuilder sb = new StringBuilder(string.length());
        for (String c : letters) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * Проверьте, совпадает ли заданный текст с шаблоном regex.
     *
     * @param text  : текст для проверки.
     * @param regex : regex pattern по которому будет выполняться проверка.
     * @return true если совпадает.
     */
    public static boolean isValidString(String text, String regex) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) { // case of illegal pattern
            pattern = Pattern.compile(".*");
        }

        Matcher regexp = pattern.matcher(text);

        return regexp.matches();
    }

    /**
     * @param text : строка для форматирования.
     */
    public static void printSection(String text) {
        final StringBuilder sb = new StringBuilder(80);
        sb.append("-".repeat(Math.max(0, (73 - text.length()))));

        StringUtil.append(sb, "=[ ", text, " ]");

        LOGGER.info(sb.toString());
    }

    /**
     * Форматирование времени, заданного в секундах, в "h m s". Формат строки.
     *
     * @param time : время в секундах.
     * @return строка в формате "h m s".
     */
    public static String getTimeStamp(int time) {
        final int hours = time / 3600;
        time %= 3600;
        final int minutes = time / 60;
        time %= 60;

        String result = "";
        if (hours > 0) {
            result += hours + "h";
        }
        if (minutes > 0) {
            result += " " + minutes + "m";
        }
        if (time > 0 || result.length() == 0) {
            result += " " + time + "s";
        }

        return result;
    }

    /**
     * Форматирует {@link String} удаляя ее расширение ("castles.xml" > "castles")
     *
     * @param fileName : Строка для редактирования, которая является именем файла.
     * @return усеченная слева строка до первого встретившегося "."
     */
    public static String getNameWithoutExtension(String fileName) {
        final int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }

        return fileName;
    }

    /**
     * Обрезает {@link String}, заданную в качестве параметра, до количества символов,
     * заданно в качестве второго параметра.
     *
     * @param s        : {@link String} для обрезания.
     * @param maxWidth : максимальная длина.
     * @return обрезанную {@link String}.
     */
    public static String trim(String s, int maxWidth) {
        return (s.length() > maxWidth) ? s.substring(0, maxWidth) : s;
    }

    /**
     * Обрезает {@link String}, заданную в качестве параметра, до количества символов,
     * заданно в качестве второго параметра.
     * Или возвращает {@link String} defaultValue, если {@link String} равен null или пуст
     *
     * @param s            : {@link String} для обрезания.
     * @param maxWidth     : максимальная длина.
     * @param defaultValue : {@link String} по умолчанию для возвращаемого значения, если {@link String} null или пустая.
     * @return The {@link String} trimmed to the good format.
     */
    public static String trim(String s, int maxWidth, String defaultValue) {
        if (s == null || s.isEmpty()) {
            return defaultValue;
        }
        return trim(s, maxWidth);
    }
}
