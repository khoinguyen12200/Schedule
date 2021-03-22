package com.example.schedule;

import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;


public class VScrollView extends ScrollView {
    public HorizontalScrollView sv;

    public VScrollView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(getContext(), new VScrollView.ScaleListener());
    }

    public VScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(getContext(), new VScrollView.ScaleListener());
    }

    public VScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(getContext(), new VScrollView.ScaleListener());
    }

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        if(event.getPointerCount() == 1){
            super.onTouchEvent(event);
            sv.dispatchTouchEvent(event);

        }else{

        }

        return true;
    }
    private boolean XSpan(){
        float xspan = mScaleDetector.getCurrentSpanX() - mScaleDetector.getPreviousSpanX();
        float yspan = mScaleDetector.getCurrentSpanY() - mScaleDetector.getPreviousSpanY();

        if(xspan<0) xspan = -xspan;
        if(yspan<0) yspan = -yspan;



        if(xspan>yspan)
            return true;
        else return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        sv.onInterceptTouchEvent(event);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {


            mScaleFactor *= detector.getScaleFactor();
            Log.d("onTouchEvent", ""+mScaleFactor);
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.2f, Math.min(mScaleFactor, 5.0f));
            invalidate();

            return true;
        }
    }
    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(mPosX, mPosY);

        if (mScaleDetector.isInProgress()) {
            canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        }
        else{
            canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);
        }
        super.onDraw(canvas);

        canvas.restore();
    }


    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public static int getInvalidPointerId() {
        return INVALID_POINTER_ID;
    }

    public float getmPosX() {
        return mPosX;
    }

    public void setmPosX(float mPosX) {
        this.mPosX = mPosX;
    }

    public float getmPosY() {
        return mPosY;
    }

    public void setmPosY(float mPosY) {
        this.mPosY = mPosY;
    }

    public float getmLastTouchX() {
        return mLastTouchX;
    }

    public void setmLastTouchX(float mLastTouchX) {
        this.mLastTouchX = mLastTouchX;
    }

    public float getmLastTouchY() {
        return mLastTouchY;
    }

    public void setmLastTouchY(float mLastTouchY) {
        this.mLastTouchY = mLastTouchY;
    }

    public float getmLastGestureX() {
        return mLastGestureX;
    }

    public void setmLastGestureX(float mLastGestureX) {
        this.mLastGestureX = mLastGestureX;
    }

    public float getmLastGestureY() {
        return mLastGestureY;
    }

    public void setmLastGestureY(float mLastGestureY) {
        this.mLastGestureY = mLastGestureY;
    }

    public int getmActivePointerId() {
        return mActivePointerId;
    }

    public void setmActivePointerId(int mActivePointerId) {
        this.mActivePointerId = mActivePointerId;
    }

    public ScaleGestureDetector getmScaleDetector() {
        return mScaleDetector;
    }

    public void setmScaleDetector(ScaleGestureDetector mScaleDetector) {
        this.mScaleDetector = mScaleDetector;
    }

    public float getmScaleFactor() {
        return mScaleFactor;
    }

    public void setmScaleFactor(float mScaleFactor) {
        this.mScaleFactor = mScaleFactor;
    }

}