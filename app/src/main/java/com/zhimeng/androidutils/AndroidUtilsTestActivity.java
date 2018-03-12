package com.zhimeng.androidutils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhimeng.androidutils.widget.SimplePagerView;

public class AndroidUtilsTestActivity extends AppCompatActivity {

    private static class MyPagerItem implements SimplePagerView.PageItem {

        private static final Paint PAINT = new Paint();

        private final int mColor;

        public MyPagerItem(int color) {
            mColor = color;
        }

        @Override
        public void draw(Canvas canvas, RectF rect) {
            PAINT.setColor(mColor);
            canvas.drawRect(rect, PAINT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_utils_test);
        SimplePagerView pagerView = findViewById(R.id.pager_view);
        pagerView.setup(new MyPagerItem(0xffffff00), new MyPagerItem(0xff00ffff), new MyPagerItem(0xffff00ff));
    }
}
