package com.example.recipes.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

   
public class PwdCheckUtil {

       
    public static boolean isLetterOrDigit(String str) {
        boolean isLetterOrDigit = false;          for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {                     isLetterOrDigit = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isLetterOrDigit && str.matches(regex);
        return isRight;
    }

       
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;          boolean isLetter = false;          for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {                     isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {                    isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

       
    public static boolean isContainAll(String str) {
        boolean isDigit = false;          boolean isLowerCase = false;          boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {                     isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {                    isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

       
    public static void whatIsInput(Context context, EditText edInput) {
        String txt = edInput.getText().toString();

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "Numbers are entered", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "letters are entered", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "Chinese characters are entered", Toast.LENGTH_SHORT).show();
        }
    }
}