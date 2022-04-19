package com.example.recipes.util;

public class FileUtils {

       
    public static int getFileType(String filePath) {
        String[] videoStr = {"mp4", "mov", "mkv", "avi", "wmv", "m4v", "mpg", "vob", "webm", "ogv", "3gp", "flv", "f4v", "swf"};
        for (int i = 0; i < videoStr.length; i++) {
            if (filePath.toLowerCase().endsWith(videoStr[i])) {                  return 2;
            }
        }
        String[] imgStr = {"jpg", "png", "bmp", "jpeg", "jp2", "gif", "tiff", "exif", "wbmp", "mbm"};
        for (int j = 0; j < imgStr.length; j++) {
            if (filePath.toLowerCase().endsWith(imgStr[j])) {
                return 1;
            }
        }
        return 0;
    }

}
