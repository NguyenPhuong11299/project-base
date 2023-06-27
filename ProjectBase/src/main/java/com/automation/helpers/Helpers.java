package com.automation.helpers;

import java.io.File;

public class Helpers {
    /**
     * @return lấy đường dẫn đến thư mục nguồn source ( thu muc chua project), mình có thêm dấu / ở cuối luôn
     * trong he thong co property = user.dir
     */
    public static String getCurrentDir() {
        String current = System.getProperty("user.dir") + File.separator; // File.separator = '/'
        return current;
    }
}
