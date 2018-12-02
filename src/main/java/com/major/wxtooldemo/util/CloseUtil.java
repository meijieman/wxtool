package com.major.wxtooldemo.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {

    public static void close(Closeable... args) {
        if (args != null) {
            for (Closeable arg : args) {
                if (arg != null) {
                    try {
                        arg.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        arg = null;
                    }
                }
            }
        }
    }
}
