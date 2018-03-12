package com.zhimeng.androidutils.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;

public class AutoPagerView extends SimplePagerView {

    private Timer timer;
    private Handler handler = new Handler(Looper.getMainLooper());

    public AutoPagerView(Context context) {
        super(context);
    }

    public AutoPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(final int delay, int period, SimplePagerView.PageItem... items) {
        super.setup(items);
        if (timer != null) return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFingerTouching()) return;
                if (System.currentTimeMillis() - getLastFlingTime() < delay) return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        turnForward(1);
                    }
                });
            }
        }, delay, period);
    }
}
