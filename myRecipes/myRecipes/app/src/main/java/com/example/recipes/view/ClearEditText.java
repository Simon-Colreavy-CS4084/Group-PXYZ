package com.example.recipes.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.recipes.R;

import java.util.regex.Pattern;


   
public class ClearEditText extends AppCompatEditText implements
        TextWatcher {

          private String inputMatches = "";

          private InputMatchesListener mInputMatchesListener = null;

          private String validatorRegex = "";

          private ValidatorRegexListener mValidatorRegexListener = null;


          private final int CLEAR = R.drawable.ic_clear;
          private final int ANIMATOR_TIME = 200;
          private final int INTERVAL = 4;
          private final int WIDTH_OF_CLEAR = 15;


          private int Interval;
          private int mWidth_clear;
          private int mPaddingRight;
          private Bitmap mBitmap_clear;
          private ValueAnimator mAnimator_visible;
          private ValueAnimator mAnimator_gone;
          private boolean isVisible = false;

          private int mRight = 0;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
                  this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
          
        mBitmap_clear = createBitmap(CLEAR, context);

        Interval = dp2px(INTERVAL);
        mWidth_clear = dp2px(WIDTH_OF_CLEAR);
        mPaddingRight = Interval + mWidth_clear + Interval;
        mAnimator_gone = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mAnimator_visible = ValueAnimator.ofInt(mWidth_clear + Interval, 0).setDuration(ANIMATOR_TIME);
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

                  setPadding(getPaddingLeft(), getPaddingTop(), mPaddingRight + mRight, getPaddingBottom());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));          if (mAnimator_visible.isRunning()) {
            int x = (int) mAnimator_visible.getAnimatedValue();
            drawClear(x, canvas);
            invalidate();
        } else if (isVisible) {
            drawClear(0, canvas);
        }

        if (mAnimator_gone.isRunning()) {
            float scale = (float) mAnimator_gone.getAnimatedValue();
            drawClearGone(scale, canvas);
            invalidate();
        }
    }

       
    protected void drawClear(int translationX, Canvas canvas) {
        int right = getWidth() + getScrollX() - Interval - mRight + translationX;
        int left = right - mWidth_clear;
        int top = (getHeight() - mWidth_clear) / 2;
        int bottom = top + mWidth_clear;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);

    }

       
    protected void drawClearGone(float scale, Canvas canvas) {
        int right = (int) (getWidth() + getScrollX() - Interval - mRight - mWidth_clear * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - Interval - mRight - mWidth_clear * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mWidth_clear * scale) / 2);
        int bottom = (int) (top + mWidth_clear * scale);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);
    }

       
    private void startVisibleAnimator() {
        endAnaimator();
        mAnimator_visible.start();
        invalidate();
    }

       
    private void startGoneAnimator() {
        endAnaimator();
        mAnimator_gone.start();
        invalidate();
    }

       
    private void endAnaimator() {
        mAnimator_gone.end();
        mAnimator_visible.end();
    }

       
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            boolean touchable = (getWidth() - Interval - mRight - mWidth_clear < event.getX()) && (event.getX() < getWidth() - Interval - mRight);
            if (touchable) {
                setError(null);
                this.setText("");
                if (mValidatorRegexListener != null) {
                    mValidatorRegexListener.onValidatorRegex(
                            Pattern.matches(validatorRegex, ""), "");
                }
            }
        }
        return super.onTouchEvent(event);
    }

       
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (s.length() > 0) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String temp = s.toString();

        if (temp.isEmpty()){
            if (mValidatorRegexListener != null) {
                mValidatorRegexListener.onValidatorRegex(false, temp);
            }
            return;
        }

        if (!TextUtils.isEmpty(inputMatches) && !temp.matches(inputMatches)) {
            if (mInputMatchesListener != null) {
                mInputMatchesListener.onInputMatches();
            }
            s.delete(temp.length() - 1, temp.length());
            setSelection(s.length());
        }
        if (mValidatorRegexListener != null) {
            mValidatorRegexListener.onValidatorRegex(Pattern.matches(validatorRegex, temp), temp);
        }
    }

       
    public void setShakeAnimation() {
        if (getAnimation() == null) {
            this.setAnimation(setShakeAnimation(4));
        }
        this.startAnimation(getAnimation());
    }

       
    private Animation setShakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

       
    public Bitmap createBitmap(int resources, Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, resources);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        return drawableToBitamp(wrappedDrawable);
    }

       
    private Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

          public void setInputMatches(String inputMatches) {
        this.inputMatches = inputMatches;
    }


    public interface InputMatchesListener {
        void onInputMatches();
    }

    public void setInputMatchesListener(InputMatchesListener inputMatchesListener) {
        this.mInputMatchesListener = inputMatchesListener;
    }

          public void setValidatorRegex(String validatorRegex) {
        this.validatorRegex = validatorRegex;
    }


    public interface ValidatorRegexListener {
        void onValidatorRegex(boolean isValidatorSuccess, String temp);
    }

    public void setValidatorRegexListener(ValidatorRegexListener validatorRegexListener) {
        this.mValidatorRegexListener = validatorRegexListener;
    }


       
    public void setTextVisibleOrGone(final ImageView showView, final int showResId, final int goneResId) {
        showView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    if (showView != null)
                        showView.setImageResource(showResId);
                } else {
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    if (showView != null)
                        showView.setImageResource(goneResId);
                }
                String pwd = getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    setSelection(pwd.length());
            }
        });

    }
}
