package com.example.recipes.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataDispose {

    public static byte[] compressionStringToGZIP(String content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(content.getBytes());
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out.toByteArray();
    }

    public static String decompressionGZIPToString(String content) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(
                    content.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError oe) {
            oe.printStackTrace();
        }
        return "";
    }

    public static String ExceptionToString(Throwable ex) {
        try {
            StringBuffer sb = new StringBuffer();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decompressionGZIPToString(byte[] content) {
        try {
            if (content == null)
                return "";
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(content);
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isApplicationInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static Bitmap getApplicationIcon(Context context, String packageName) {
        try {
            PackageManager packageManager = null;
            ApplicationInfo applicationInfo = null;
            try {
                packageManager = context.getApplicationContext()
                        .getPackageManager();
                applicationInfo = packageManager.getApplicationInfo(
                        packageName, 0);
            } catch (NameNotFoundException e) {
                applicationInfo = null;
            }
            Drawable d = packageManager.getApplicationIcon(applicationInfo);               BitmapDrawable bd = (BitmapDrawable) d;
            return bd.getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean applicationIsRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo rapi : infos) {
            if (rapi.processName.equals(packageName)) return true;
        }
        return false;
    }

    public static String getPackageMD5(Context context) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        String test = "";
        try {
            fis = new FileInputStream(context.getPackageResourcePath());
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            test = byte2hex(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return test.toString().toLowerCase();
    }

    public static String currentTimeToString() {
        return DataDispose.LongTimetoString(System.currentTimeMillis());
    }

    public static String getStringMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : digest.digest()) {
                builder.append(Integer.toHexString((b >> 4) & 0xf));
                builder.append(Integer.toHexString(b & 0xf));
            }
            return builder.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else hs = hs + stmp;
        }
        return hs.toLowerCase();
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String toLocalTime(long unix) {
        Long timestamp = unix * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
        return date;
    }

    public static long toUnixTime(String local) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long unix = 0;
        try {
            unix = df.parse(local).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unix;
    }

    public static String getIntervalTime(long time) {
        long diff = System.currentTimeMillis() - time;
        try {
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (days == 0 && hours == 0 && minutes < 2) {
                return "just now";
            }
            if (days == 0 && hours == 0 && minutes >= 0)
                return minutes + "minutes ago";
            else if (days == 0 && hours > 0)
                return hours + "hours ago";
            else if (days > 0 && days <= 30)
                return days + "days ago";
            else return LongTimeto_Y_M_D_H_M_String(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getIntervalTime(String time) {
        return getIntervalTime(StringTimeY_M_D_H_M_S_ToLong(time));

    }


    public static long StringTimeToLong(String stime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            Date ddate = formatter.parse(stime);
            return ddate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static long StringTimeY_M_D_H_M_S_ToLong(String stime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date ddate = formatter.parse(stime);
            return ddate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static long StringTimeY_M_D_ToLong(String stime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date ddate = formatter.parse(stime);
            return ddate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static String LongTimeto_Y_M_D_String(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String LongTimeto_H_M_String(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "HH:mm", Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String LongTimeto_Y_M_D_H_M_S_String(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String LongTimeto_Y_M_D_H_M_String(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String LongTimetoString(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String LongTimetoString(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String longTimeToMin(long time) {
        int mint = (int) (time / 60000);
        int sect = ((int) (time / 1000)) % 60;
        String timeStr = "";
        if (mint < 10)
            timeStr = "0" + mint;
        else timeStr = "" + mint;
        if (sect < 10)
            timeStr += ":0" + sect;
        else timeStr += ":" + sect;
        return timeStr;
    }

    public String longTimeToHour(long time) {
        int hourt = (int) (time / 3600000);
        int mint = ((int) (time / 60000)) % 60;
        String timeStr = "";
        if (hourt < 10)
            timeStr = "0" + hourt;
        else timeStr = "" + hourt;
        if (mint < 10)
            timeStr += ":0" + mint;
        else timeStr += ":" + mint;
        return timeStr;
    }

    public static String uriToLocalPath(Context context, Uri fileUrl) {
        String fileName = null;
        if (fileUrl != null) {
            if (fileUrl.getScheme().toString().compareTo("content") == 0) {                     Cursor cursor = context.getContentResolver().query(fileUrl, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileName = cursor.getString(column_index);                          if (!fileName.startsWith("file://")) {   // 检查是否有”/mnt“前缀
                        fileName = "file://" + fileName;
                    }
                    try {
                        cursor.close();
                    } catch (Exception e) {
                    }
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                }

            } else {                     fileName = fileUrl.toString();
            }
        }
        return fileName;
    }

          public static boolean iSChineseStr(String str) {
        String reg = "[\\u4e00-\\u9fa5]+";
        boolean result = str.matches(reg);
        return result;
    }


    public static int getNumeric(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim());
    }

    public static boolean isNumeric(String str) {
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getJSONObjectData(JSONObject data) {
        List<String> result = new ArrayList<String>();
        try {
            Iterator<?> it = data.keys();
            String tempss = null;
            while (it.hasNext()) {                  tempss = (String) it.next().toString();
                result.add(data.getString(tempss));
            }
        } catch (Exception e) {
        }
        return result;
    }

          public static double roundDoubleChange(double a, int b) {
        if (b < 0)
            return a;
        int k = 1;
        for (int i = 0; i < b; i++) {
            k = k * 10;
        }
        return ((double) Math.round(a * k)) / k;
    }

    public static double coverDoubleTo6(double value) {
          DecimalFormat df = new DecimalFormat("###########.000000");
        double result = Double.parseDouble(df.format(value));
        return result;
    }


       
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

       
    public static String getDateToString(String milSecond, String pattern) {
        Date date = new Date(StringTimeY_M_D_H_M_S_ToLong(milSecond));
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

          public static boolean isSameDay(final String time1, final String time2) {
        Date date1 = new Date(StringTimeY_M_D_H_M_S_ToLong(time1));
        Date date2 = new Date(StringTimeY_M_D_H_M_S_ToLong(time2));
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
