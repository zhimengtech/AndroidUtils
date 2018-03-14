package com.zhimeng.androidutils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhimeng.androidutils.opengl.demo.GLDemoRenderer;
import com.zhimeng.androidutils.opengl.demo.GLDemoView;
import com.zhimeng.androidutils.widget.SimplePagerView;

public class AndroidUtilsTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_utils_test);
    }
}
