package com.example.daniel_szabo.sensors.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public final class LargeDataTransferUtil {

    private LargeDataTransferUtil() {}

    public static boolean writeDataToTempFile(Context context, String fileName, Serializable data) throws IOException {
        return FileUtil.writeObjectToFile(getTempFile(context, fileName), data);
    }

    public static <T> T readDataFromTempFile(Context context, String fileName) throws IOException {
        return FileUtil.readObjectFromFile(getTempFile(context, fileName));
    }

    private static File getTempFile(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName);
    }
}
