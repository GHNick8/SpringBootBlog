package com.techsphere.techsphere.util;

import java.io.File;

public class AppUtil {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public static String get_upload_path(String fileName) {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); 
        }
        return UPLOAD_DIR + fileName;
    }
}
