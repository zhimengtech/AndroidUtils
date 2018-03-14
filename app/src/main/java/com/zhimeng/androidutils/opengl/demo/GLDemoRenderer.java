package com.zhimeng.androidutils.opengl.demo;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.zhimeng.androidutils.opengl.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLDemoRenderer implements GLSurfaceView.Renderer {

    private static final String FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";

    private static final String FILTER_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor =  texture2D(inputImageTexture, textureCoordinate);\n" +
            "}";

    private static final float[] TEX_VERTEX = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    private static final float[] CUBE = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    private FloatBuffer mCubeBuffer;
    private FloatBuffer mTexBuffer;
    private int mTextureId;
    private int mProgramId;
    private int mGLAttribPosition;
    private int mGLUniformTexture;
    private int mGLAttribTextureCoordinate;
    private Bitmap mBitmap;

    public GLDemoRenderer(Bitmap bitmap) {
        mBitmap = bitmap;
        mCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(CUBE);
        mCubeBuffer.position(0);
        mTexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(TEX_VERTEX);
        mTexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.5f, 1, 1, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE);//you can recycle this bitmap now
        mProgramId = OpenGlUtils.loadProgram(FILTER_VERTEX_SHADER, FILTER_FRAGMENT_SHADER);

        mGLAttribPosition = GLES20.glGetAttribLocation(mProgramId, "position");
        mGLUniformTexture = GLES20.glGetUniformLocation(mProgramId, "inputImageTexture");
        mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mProgramId, "inputTextureCoordinate");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //step 1
        GLES20.glUseProgram(mProgramId);

        //step 2
        mCubeBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, mCubeBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);

        //step 3
        mTexBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);

        //step 4
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mGLUniformTexture, 0);

        //step 5
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //step 6
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
