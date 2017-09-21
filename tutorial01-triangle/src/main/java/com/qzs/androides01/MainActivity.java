package com.qzs.androides01;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initalize();
    }

    private boolean hasGLES2(){
        ActivityManager service = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        ConfigurationInfo info = service.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x00020000;
    }



    private class GLES20Render extends GLRender{

        private final float[] mtrxProjection = new float[16];
        private final float[] mtrxView = new float[16];
        private final float[] mtrxProjectionAndView = new float[16];

        public float vertices[];
        public short indices[];
        public FloatBuffer vertextBuffer;
        public ShortBuffer drawListBuffer;

        float mScreenWidth = 1280;
        float mScreenHeight = 768;

        long mLastTime = System.currentTimeMillis() + 100;
        int mProgram;

        public void setupTriangle(){
            vertices = new float[]{
                    10.0f, 200f, 0.0f,
                    10.0f, 100f, 0.0f,
                    100f,  100f, 0.0f
            };
            indices = new short[]{0, 1, 2};

            ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());

            vertextBuffer = bb.asFloatBuffer();
            vertextBuffer.put(vertices);
            vertextBuffer.position(0);

            ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length*2);
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
            drawListBuffer.put(indices);
            drawListBuffer.position(0);
        }
        @Override
        public void onDrawFrame(Boolean firstFrame) {

            if (firstFrame){
                setupTriangle();
            }
            long now = System.currentTimeMillis();

            if (mLastTime > now) return;

            long elapsed = now -mLastTime;

            render(mtrxProjectionAndView);

            mLastTime = now;
        }

        @Override
        public void onCreate(int width, int height, boolean created) {

            if (created) {
                GLES20.glClearColor(.0f, .0f, .0f, .0f);

                int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                        riGraphicTools.vs_solidColor);

                int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                        riGraphicTools.fs_solidColor);

                riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();

                GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
                GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
                GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);

                GLES20.glUseProgram(riGraphicTools.sp_SolidColor);

            }else{
                // onSurfaceChanged
                GLES20.glViewport(0, 0, width, height);

                for (int i = 0; i < 16; i++){
                    mtrxProjection[i] = 0.0f;
                    mtrxView[i] = 0.0f;
                    mtrxProjectionAndView[i] = 0.0f;
                }

                Matrix.orthoM(mtrxProjection, 0, 0f, width, 0, height, 0, 50);
                Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
                Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
            }
        }

        private void render(float[] m){

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");

            GLES20.glEnableVertexAttribArray(mPositionHandle);

            GLES20.glVertexAttribPointer(mPositionHandle, 3,
                    GLES20.GL_FLOAT, false, 0, vertextBuffer);

            int mtrxHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

            GLES20.glUniformMatrix4fv(mtrxHandle, 1, false, m, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

            GLES20.glDisableVertexAttribArray(mPositionHandle);

        }
    }

    private void initalize(){
        if (hasGLES2()){
            mGLView = new GLSurfaceView(this);
            mGLView.setEGLContextClientVersion(2);
            mGLView.setPreserveEGLContextOnPause(true);
            mGLView.setRenderer(new GLES20Render());
            setContentView(mGLView);
        }else{
            setContentView(R.layout.activity_main);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}
