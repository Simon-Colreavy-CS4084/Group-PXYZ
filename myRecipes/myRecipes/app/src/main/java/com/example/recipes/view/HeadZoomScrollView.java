package com.example.recipes.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

   

public class HeadZoomScrollView extends ScrollView {
    private OnScrollChangedListener mOnScrollChangedListener;

    public HeadZoomScrollView(Context context) {
        super(context);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

          private float y = 0f;
          private int zoomViewWidth = 0;
    private int zoomViewHeight = 0;

          private boolean mScaling = false;

          private View zoomView;
    public void setZoomView(View zoomView) {
        this.zoomView = zoomView;
    }

          private float mScaleRatio = 0.4f;
    public void setmScaleRatio(float mScaleRatio) {
        this.mScaleRatio = mScaleRatio;
    }

          private float mScaleTimes = 2f;
    public void setmScaleTimes(int mScaleTimes) {
        this.mScaleTimes = mScaleTimes;
    }

          private float mReplyRatio = 0.5f;
    public void setmReplyRatio(float mReplyRatio) {
        this.mReplyRatio = mReplyRatio;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
          setOverScrollMode(OVER_SCROLL_NEVER);
          if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup && zoomView == null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildCount() > 0) {
                zoomView = vg.getChildAt(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (zoomViewWidth <= 0 || zoomViewHeight <=0) {
            zoomViewWidth = zoomView.getMeasuredWidth();
            zoomViewHeight = zoomView.getMeasuredHeight();
        }
        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        y = ev.getY();                      } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - y)*mScaleRatio);
                if (distance < 0) break;                  mScaling = true;
                setZoom(distance);
                return true;
            case MotionEvent.ACTION_UP:
                mScaling = false;
                replyView();
                break;
        }
        return super.onTouchEvent(ev);
    }

       
    private void setZoom(float s) {
        float scaleTimes = (float) ((zoomViewWidth+s)/(zoomViewWidth*1.0));
          if (scaleTimes > mScaleTimes) return;

        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.width = (int) (zoomViewWidth + s);
        layoutParams.height = (int)(zoomViewHeight*((zoomViewWidth+s)/zoomViewWidth));
          ((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0);
        zoomView.setLayoutParams(layoutParams);
    }

       
    private void replyView() {
        final float distance = zoomView.getMeasuredWidth() - zoomViewWidth;
                  ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * mReplyRatio));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
    }

    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

       
    public  interface OnScrollListener{
        void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener!=null) onScrollListener.onScroll(l,t,oldl,oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}