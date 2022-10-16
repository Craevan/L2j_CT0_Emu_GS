package com.l2j.emu.commons.io;

import java.io.*;

public class UnicodeReader extends Reader {

    private static final int BOM_SIZE = 4;

    private final PushbackInputStream pushbackInputStream;
    private final String defaultEnc;
    private InputStreamReader inputStreamReader = null;

    public UnicodeReader(final InputStream in, final String defaultEnc) {
        pushbackInputStream = new PushbackInputStream(in, BOM_SIZE);
        this.defaultEnc = defaultEnc;
    }

    public String getDefaultEncoding() {
        return defaultEnc;
    }

    public String getEncoding() {
        if (inputStreamReader == null)
            return null;

        return inputStreamReader.getEncoding();
    }

    /**
     * Упреждающее чтение четырех байтов и проверка меток BOM.
     * Лишние байты не считываются обратно в поток, пропускаются только байты BOM.
     *
     * @throws IOException исключение ввода-вывода
     */
    protected void init() throws IOException {
        if (inputStreamReader != null) {
            return;
        }

        final String encoding;
        final byte[] bom = new byte[BOM_SIZE];
        final int n;
        final int unread;
        n = pushbackInputStream.read(bom, 0, bom.length);

        if (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
            encoding = "UTF-8";
            unread = n - 3;
        } else if (bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF) {
            encoding = "UTF-16BE";
            unread = n - 2;
        } else if (bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE) {
            encoding = "UTF-16LE";
            unread = n - 2;
        } else if (bom[0] == (byte) 0x00 && bom[1] == (byte) 0x00 && bom[2] == (byte) 0xFE && bom[3] == (byte) 0xFF) {
            encoding = "UTF-32BE";
            unread = n - 4;
        } else {
            // Метка Unicode BOM не найдена, все байты не прочитаны
            encoding = defaultEnc;
            unread = n;
        }

        if (unread > 0) {
            pushbackInputStream.unread(bom, (n - unread), unread);
        }

        // Использовать заданную кодировку
        inputStreamReader = encoding == null ?
                new InputStreamReader(pushbackInputStream)
                :
                new InputStreamReader(pushbackInputStream, encoding);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        init();
        return inputStreamReader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        init();
        pushbackInputStream.close();
        inputStreamReader.close();
    }
}
