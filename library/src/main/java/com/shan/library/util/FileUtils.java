package com.shan.library.util;

public final class FileUtils {
    private FileUtils() {}

    public static String getExtension(String filename) {
        if (filename == null)
            return null;
        int index = filename.lastIndexOf('.');
        return index == -1 ? null : filename.substring(index + 1);
    }
}
