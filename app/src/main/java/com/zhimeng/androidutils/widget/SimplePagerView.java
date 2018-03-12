package com.zhimeng.androidutils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

public class SimplePagerView extends CustomPagerView {

    private static final String TAG = "SimplePagerView";

    public interface PageItem {
        void draw(Canvas canvas, RectF rect);
    }

    private static class ItemHolder {

        public final PageItem item;
        public final RectF rect;

        public ItemHolder(PageItem item, RectF rect) {
            this.item = item;
            this.rect = rect;
        }
    }

    private ItemHolder[] mItems;
    private int mWidth;
    private int mHeight;
    private int mShowingItem = 0;
    private int mPaddingLeft = 0;

    public SimplePagerView(Context context) {
        super(context);
    }

    public SimplePagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void drawItem(Canvas canvas, int index) {
        mItems[index].item.draw(canvas, mItems[index].rect);
    }

    @Override
    public void drawPage(Canvas canvas, float centerPosition) {
        while (centerPosition >= 0) centerPosition-=mItems.length;
        while (centerPosition + mItems.length <= 0) centerPosition+=mItems.length;
        int i = 0;
        while (centerPosition < 1) {
            if (centerPosition + 1 > 0) {
                mItems[i].rect.offsetTo(centerPosition * mWidth + mPaddingLeft, mItems[i].rect.top);
                drawItem(canvas, i);
            }
            centerPosition += 1;
            i = (i + 1) % mItems.length;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        mPaddingLeft = getPaddingLeft();
        if (mItems == null) return;
        for (ItemHolder i : mItems) {
            i.rect.set(mPaddingLeft
                    , getPaddingTop()
                    , mWidth - getPaddingRight()
                    , mHeight - getPaddingBottom());
        }
    }

    public void setup(PageItem... items) {
        mItems = new ItemHolder[items.length];
        for (int i = 0; i < items.length; i++)
            mItems[i] = new ItemHolder(items[i], new RectF(0, 0, mWidth, mHeight));
    }

    public int nextIndex() {
        return (mShowingItem + 1) % mItems.length;
    }

    public int currentPage() {
        float centerPosition = getCenterPosition();
        while (centerPosition >= 0) centerPosition-=mItems.length;
        while (centerPosition + mItems.length <= 0) centerPosition+=mItems.length;
        int i = 0;
        while (centerPosition < 1) {
            if (centerPosition + 1 > 0) {
                if (centerPosition + 1 >= 0.5) return i;
                else return (i + 1) % mItems.length;
            }
            centerPosition += 1;
            i = (i + 1) % mItems.length;
        }
        Log.e(TAG, "unknown error");
        return 0;
    }

    public int getShowingItem() {
        return mShowingItem;
    }
}
