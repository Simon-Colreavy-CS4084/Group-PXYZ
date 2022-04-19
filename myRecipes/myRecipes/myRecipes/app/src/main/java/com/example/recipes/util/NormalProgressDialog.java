package com.example.recipes.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.lang.ref.WeakReference;

  public class NormalProgressDialog extends ProgressDialog implements DialogInterface.OnCancelListener {

    private WeakReference<Context> mContext = new WeakReference<>(null);
    private volatile static NormalProgressDialog sDialog;

    private NormalProgressDialog(Context context) {
        this(context, -1);
    }

    private NormalProgressDialog(Context context, int theme) {
        super(context, theme);

        mContext = new WeakReference<>(context);
        setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
                  Context context = mContext.get();
        if (context != null) {
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
          }
    }

    public static synchronized void showLoading(Context context) {
        showLoading(context, "加载中...");
    }

    public static synchronized void showLoading(Context context, CharSequence message) {
        showLoading(context, message, true);
    }

    public static synchronized void showLoading(Context context, CharSequence message, boolean cancelable) {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }

        if (context == null || !(context instanceof Activity)) {
            return;
        }
        sDialog = new NormalProgressDialog(context);
        sDialog.setMessage(message);
        sDialog.setCancelable(cancelable);

        if (sDialog != null && !sDialog.isShowing() && !((Activity) context).isFinishing()) {
            sDialog.show();
        }
    }

    public static synchronized void stopLoading() {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }
        sDialog = null;
    }
}
