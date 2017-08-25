package com.example.daniel_szabo.sensors.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public final class FileUtil {

    private FileUtil() {}

    public static boolean writeObjectToFile(File file, Serializable object) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            return writeObjectToFile(fos, object);
        } finally {
            closeStream(fos);
        }
    }

    public static boolean writeObjectToFile(OutputStream os, Serializable object) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(object);
            return true;
        } finally {
            closeStream(oos);
        }
    }

    public static <T> T readObjectFromFile(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return readObjectFromFile(fis);
        } finally {
            closeStream(fis);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromFile(InputStream is) throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            Object object = ois.readObject();
            return object != null ? (T) object : null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeStream(ois);
        }
        return null;
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
