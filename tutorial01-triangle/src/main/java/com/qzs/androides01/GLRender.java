package com.qzs.androides01;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jac on 2017/9/18.
 */

public abstract class GLRender implements GLSurfaceView.Renderer {

    private boolean mSurceFaceCreated;
    private boolean mFirstDraw;
    private int mFps;
    private int mWidth;
    private int mHeigth;

    private long lastTimestamp;

    public GLRender() {
        mSurceFaceCreated = false;
        mFirstDraw = true;
        mFps = 0;
        mWidth = -1;
        mHeigth = -1;
        lastTimestamp = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSurceFaceCreated = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (!mSurceFaceCreated &&
                width == mWidth && height == mHeigth){
            return;
        }

        mWidth = width;
        mHeigth = height;
        onCreate(mWidth, mHeigth, mSurceFaceCreated);
        mSurceFaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame(mFirstDraw);

        mFps ++;
        long cur = System.currentTimeMillis();

        if (cur - lastTimestamp >= 1000){
            mFps = 0;
            lastTimestamp = cur;
        }

        if (mFirstDraw){
            mFirstDraw = false;
        }
    }

    public abstract void onDrawFrame(Boolean firstFrame);
    public abstract void onCreate(int width, int height, boolean created);

}
