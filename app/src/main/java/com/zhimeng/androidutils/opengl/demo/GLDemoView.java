package com.zhimeng.androidutils.opengl.demo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.zhimeng.androidutils.R;

public class GLDemoView extends GLSurfaceView {
    public GLDemoView(Context context) {
        super(context);
        init();
    }

    public GLDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setRenderer(new GLDemoRenderer(BitmapFactory.decodeResource(getResources(), R.drawable.test)));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
