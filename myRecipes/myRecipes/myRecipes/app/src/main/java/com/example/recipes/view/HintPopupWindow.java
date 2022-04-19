package com.example.recipes.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.recipes.R;

import java.util.ArrayList;
import java.util.List;

   
public class HintPopupWindow {

    private Activity activity;
    private WindowManager.LayoutParams params;
    private boolean isShow;
    private WindowManager windowManager;
    private ViewGroup rootView;
    private ViewGroup linearLayout;

    private final int animDuration = 250;       private boolean isAniming;   
       
    public HintPopupWindow(Activity activity, List<String> contentList, List<View.OnClickListener> clickList){

        this.activity = activity;
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

        initLayout(contentList, clickList);
    }

       
    public void initLayout(List<String> contentList, List<View.OnClickListener> clickList){

                   rootView = (ViewGroup) View.inflate(activity, R.layout.item_root_hintpopupwindow, null);
        linearLayout = (ViewGroup) rootView.findViewById(R.id.linearLayout);

                   List<View> list = new ArrayList<>();
        for(int x=0; x<contentList.size(); x++){
            View view = View.inflate(activity, R.layout.item_hint_popupwindow, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_content);
            View v_line = view.findViewById(R.id.v_line);
            textView.setText(contentList.get(x));
            linearLayout.addView(view);
            list.add(view);
            if(x == 0){
                v_line.setVisibility(View.INVISIBLE);
            }else{
                v_line.setVisibility(View.VISIBLE);
            }
        }
        for (int x=0; x<list.size(); x++){
            list.get(x).setOnClickListener(clickList.get(x));
        }

                   params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;           params.gravity = Gravity.LEFT | Gravity.TOP;

                   rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                   if(keyCode == KeyEvent.KEYCODE_BACK && isShow) dismissPopupWindow();
                return isShow;
            }
        });
    }

       
    public void showPopupWindow(View locationView){
        Log.i("Log.i", "showPopupWindow: "+isAniming);
        if(!isAniming) {
            isAniming = true;
            try {
                                   int[] arr = new int[2];
                locationView.getLocationOnScreen(arr);
                linearLayout.measure(0, 0);
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);                      float x = arr[0]  - locationView.getWidth();
                float y = arr[1] - frame.top + locationView.getHeight();
                linearLayout.setX(x);
                linearLayout.setY(y);

               
                View decorView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getBitmapByView(decorView);                   setBlurBackground(bitmap);   
                                   windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                windowManager.addView(rootView, params);

                                   showAnim(linearLayout, 0, 1, animDuration, true);

                                   rootView.setFocusable(true);
                rootView.setFocusableInTouchMode(true);
                rootView.requestFocus();
                rootView.requestFocusFromTouch();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

       
    public static Bitmap getBitmapByView(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    private void setBlurBackground(Bitmap bitmap) {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);
        Bitmap blurBitmap = getBlurBitmap(activity, scaledBitmap, 5);
        rootView.setAlpha(0);
        rootView.setBackgroundDrawable(new BitmapDrawable(blurBitmap));
        alphaAnim(rootView, 0, 1, animDuration);
    }

    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, int radius) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return blurBitmap(context, bitmap, radius);
        }
        return bitmap;
    }

       
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, int radius) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                           Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                           RenderScript rs = RenderScript.create(context);
                           ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                           Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
                           blurScript.setRadius(radius);
                           blurScript.setInput(allIn);
            blurScript.forEach(allOut);
                           allOut.copyTo(outBitmap);
                           bitmap.recycle();
                           rs.destroy();
            return outBitmap;
        }else{
            return bitmap;
        }
    }

    public void dismissPopupWindow(){
        Log.i("Log.i", "dismissPopupWindow: "+isAniming);
        if(!isAniming) {
            isAniming = true;
            isShow = false;
            goneAnim(linearLayout, 0.95f, 1, animDuration / 3, true);
        }
    }

    public WindowManager.LayoutParams getLayoutParams(){
        return params;
    }

    public ViewGroup getLayout(){
        return linearLayout;
    }

       
    public boolean isShow(){
        return isShow;
    }

    private void alphaAnim(final View view, int start, int end, int duration){

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        va.start();
    }

    private void showAnim(final View view, float start, final float end, int duration, final boolean isWhile) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setPivotX(view.getWidth());
                view.setPivotY(0);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isWhile) {
                    showAnim(view, end, 0.95f, animDuration / 3, false);
                }else{
                    isAniming = false;
                }
            }
        });
        va.start();
    }

    public void goneAnim(final View view, float start, final float end, int duration, final boolean isWhile){

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setPivotX(view.getWidth());
                view.setPivotY(0);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(isWhile){
                    alphaAnim(rootView, 1, 0, animDuration);
                    goneAnim(view, end, 0f, animDuration, false);
                }else{
                    try {
                        windowManager.removeViewImmediate(rootView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    isAniming = false;
                }
            }
        });
        va.start();
    }
}
