package com.example.schedule;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyConstraintLayout extends ConstraintLayout {


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

    public MyConstraintLayout(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MyConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }




    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScaleDetector.isInProgress()) {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_1_DOWN: {
                if (mScaleDetector.isInProgress()) {
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();
                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    float tempPosX =-(mPosX+dx);
                    float tempPosY = -(mPosY+dy);

                    Resources r = getResources();
                    int minWid = 35, minHei=50;
                    int pxWidthHour= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minWid,r.getDisplayMetrics());
                    int pxHeighDay = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minHei,r.getDisplayMetrics());

                    int height =    this.getLayoutParams().height-Resources.getSystem().getDisplayMetrics().heightPixels;
                    int width = this.getLayoutParams().width-Resources.getSystem().getDisplayMetrics().widthPixels;



                    if(this.getLayoutParams().width>Resources.getSystem().getDisplayMetrics().widthPixels){
                        if(tempPosX>=0 && tempPosX<=width)
                            mPosX += dx;
                        else if(tempPosX<0) mPosX=0;
                        else if(tempPosX>width) mPosX=-width;
                    }
                    else mPosX=0;

                    if(this.getLayoutParams().height>Resources.getSystem().getDisplayMetrics().heightPixels){
                        if(tempPosY>=0 && tempPosY<=height)
                            mPosY += dy;
                        else if(tempPosY<0) mPosY=0;
                        else if(tempPosY>height) mPosY=-height;
                    }
                    else  mPosY=0;





                    this.setScrollX((int) -mPosX);
                    this.setScrollY((int) -mPosY);

                    Log.d("onTouchEvent", ": "+mScaleFactor+" fX="+mScaleDetector.getFocusX()+" fY="+mScaleDetector.getFocusY());


                    invalidate();

                    mLastTouchX = x;
                    mLastTouchY = y;
                }
                else{
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();

                    final float gdx = gx - mLastGestureX;
                    final float gdy = gy - mLastGestureY;

                    float tempPosX =mPosX+gx;
                    float tempPosY = mPosY+gy;


                    mPosX += gdx;

                    mPosY += gdy;

                    invalidate();

                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                else{
                    final int tempPointerIndex = ev.findPointerIndex(mActivePointerId);
                    mLastTouchX = ev.getX(tempPointerIndex);
                    mLastTouchY = ev.getY(tempPointerIndex);
                }

                break;
            }
        }

        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(mPosX, mPosY);

        this.setScrollX((int)-mPosX);
        this.setScrollY((int)-mPosY);

        if (mScaleDetector.isInProgress()) {
            canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        }
        else{
            canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);
        }
        super.onDraw(canvas);
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.2f, Math.min(mScaleFactor, 5.0f));



            Log.d("asdfasdftag", "onScale: "+getmScaleFactor());
            invalidate();
            return true;
        }
    }
}
