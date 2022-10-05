package com.l2j.emu.commons.lang;

public final class StringReplacer {
    private static final String SEPARATOR = "{}";

    private final StringBuilder stringBuilder;

    public StringReplacer(String source) {
        this.stringBuilder = new StringBuilder(source);
    }

    /**
     * Заменяет все вхождения строки на другую строку
     *
     * @param pattern     : Заменяемая строка.
     * @param replacement : Новая строка.
     */
    public void replaceAll(String pattern, String replacement) {
        int position;
        while ((position = stringBuilder.indexOf(pattern)) != -1) {
            stringBuilder.replace(position, position + pattern.length(), replacement);
        }
    }

    /**
     * Заменяет все разделители '{}' на строковое представление других объектов. Важно отметить, что:
     * <ul>
     * <li>Если параметров недостаточно, то остаток не обрабатывается.</li>
     * <li>Если параметров слишком много, цикл обрывается, когда не находит, чем заменить.</li>
     * <li>Если объект - null, отправляется null</li>
     * </ul>
     *
     * @param args : Объекты для прохождения.
     */
    public void replaceAll(Object... args) {
        int index;
        int newIndex = 0;

        for (Object obj : args) {
            index = stringBuilder.indexOf(SEPARATOR, newIndex);
            if (index == -1) {
                break;
            }
            newIndex = index + 2;
            stringBuilder.replace(index, newIndex, (obj == null) ? null : obj.toString());
        }
    }

    /**
     * Заменяет первое вхождение строки на другую строку.
     *
     * @param pattern     : Строка для замены.
     * @param replacement : Новая строка.
     */
    public void replaceFirst(String pattern, String replacement) {
        final int point = stringBuilder.indexOf(pattern);
        stringBuilder.replace(point, point + pattern.length(), replacement);
    }

    /**
     * Заменяет последнее вхождение строки на другую строку.
     *
     * @param pattern     : Строка для замены.
     * @param replacement : Новая строка.
     */
    public void replaceLast(String pattern, String replacement) {
        final int point = stringBuilder.lastIndexOf(pattern);
        stringBuilder.replace(point, point + pattern.length(), replacement);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
