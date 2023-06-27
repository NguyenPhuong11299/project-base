package com.automation.utils;

import java.nio.charset.StandardCharsets;

public class LanguageUtils {
    public static String convertCharset_ISO_8859_1_To_UTF8(String text) {
        return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
