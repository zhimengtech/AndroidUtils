package com.zhimeng.androidutils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class CustomPagerView extends View {

    private static class FromToInterpolator {

        private Interpolator mInterpolator;

        private FromToInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
        }

        public float get(float from, float to, float input) {
            return mInterpolator.getInterpolation(input) * (to - from) + from;
        }
    }

    public interface ScrollingStopListener {
        void scrollStop(float centerPosition);
    }

    private static final int FLING_DURATION = 500;

    //params
    private float mPageWeight = 1.0f;
    private FromToInterpolator mFlingInterpolator = new FromToInterpolator(new DecelerateInterpolator(2));
    private float mMinCenterPosition = Integer.MIN_VALUE;
    private float mMaxCenterPosition = Integer.MAX_VALUE;
    private ScrollingStopListener mScrollingStopListener;

    //status
    private boolean mIsFling = false;
    private long mFlingStartTime = 0;
    private float mLastTouchWeight = 0;
    private float mTouchStartWeight = 0;
    private boolean mIsFingerTouching = false;
    private long mLastFlingTime = 0;
    private float mCenterPosition = 0;
    private float mFlingFrom = 0;
    private float mFlingTo = 0;

    public CustomPagerView(Context context) {
        super(context);
    }

    public CustomPagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void drawPage(Canvas canvas, float centerPosition);

    @Override
    protected void onDraw(Canvas canvas) {
        long time = System.currentTimeMillis();
        drawPage(canvas, mCenterPosition);
        if (!mIsFling) return;
        float progress = (float) (time - mFlingStartTime) / FLING_DURATION;
        mCenterPosition = mFlingInterpolator.get(mFlingFrom, mFlingTo, progress);
        if (time - mFlingStartTime >= FLING_DURATION) {
            mIsFling = false;
            mCenterPosition = mFlingTo;
            mLastFlingTime = time;
            if (mScrollingStopListener != null) {
                mScrollingStopListener.scrollStop(mCenterPosition);
            }
        }
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        mIsFingerTouching = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;
        mIsFling = !mIsFingerTouching;
        float weight = event.getX() / getWidth();
        if (action == MotionEvent.ACTION_DOWN) {
            mLastTouchWeight = weight;
            mTouchStartWeight = weight;
        }
        mCenterPosition += (weight - mLastTouchWeight) / mPageWeight;
        if (mCenterPosition > mMaxCenterPosition) mCenterPosition = mMaxCenterPosition;
        if (mCenterPosition < mMinCenterPosition) mCenterPosition = mMinCenterPosition;
        if (mIsFling) {
            if (weight - mTouchStartWeight >= 0.1 || weight < mTouchStartWeight && mTouchStartWeight - weight < 0.1) turnBackward(0);
            else turnForward(0);
        } else {
            mLastTouchWeight = weight;
            invalidate();
        }
        return true;
    }

    public void turnForward(int count) {
        mIsFling = true;
        mFlingStartTime = System.currentTimeMillis();
        mFlingFrom = mCenterPosition;
        mFlingTo = (float) Math.floor(mCenterPosition - count);
        if (mFlingTo > mMaxCenterPosition) mFlingTo = mMaxCenterPosition;
        if (mFlingTo < mMinCenterPosition) mFlingTo = mMinCenterPosition;
        invalidate();
    }

    public void turnBackward(int count) {
        mIsFling = true;
        mFlingStartTime = System.currentTimeMillis();
        mFlingFrom = mCenterPosition;
        mFlingTo = (float) Math.ceil(mCenterPosition + count);
        if (mFlingTo > mMaxCenterPosition) mFlingTo = mMaxCenterPosition;
        if (mFlingTo < mMinCenterPosition) mFlingTo = mMinCenterPosition;
        invalidate();
    }

    public void turnToPage(int index) {
        mIsFling = true;
        mFlingStartTime = System.currentTimeMillis();
        mFlingFrom = mCenterPosition;
        mFlingTo = - index;
        if (mFlingTo > mMaxCenterPosition) mFlingTo = mMaxCenterPosition;
        if (mFlingTo < mMinCenterPosition) mFlingTo = mMinCenterPosition;
        invalidate();
    }

    public boolean isFingerTouching() {
        return mIsFingerTouching;
    }

    public long getLastFlingTime() {
        return mLastFlingTime;
    }

    public float getVisibleDividerPosition() {
        if (mCenterPosition >= 0) return mCenterPosition % 1;
        else return 1 - (-mCenterPosition % 1);
    }

    public void setMinCenterPosition(float minCenterPosition) {
        mMinCenterPosition = minCenterPosition;
    }

    public void setMaxCenterPosition(float maxCenterPosition) {
        mMaxCenterPosition = maxCenterPosition;
    }

    public float getCenterPosition() {
        return mCenterPosition;
    }

    public void setPageWeight(float pageWeight) {
        mPageWeight = pageWeight;
    }

    public void setScrollingStopListener(ScrollingStopListener scrollingStopListener) {
        mScrollingStopListener = scrollingStopListener;
    }

    public boolean isFling() {
        return mIsFling;
    }
}
