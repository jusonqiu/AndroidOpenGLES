package com.qzs.androides01;

import android.opengl.GLES20;

/**
 * Created by Jac on 2017/9/18.
 */

public class riGraphicTools {

    public static int sp_SolidColor;

    public static final String vs_solidColor =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition; "+
                    "void main() { "+
                    "  gl_Position = uMVPMatrix * vPosition;"+
                    "}";

    public static final String fs_solidColor =
            "precision mediump float;"+
                    "void main() {"+
                    "  gl_FragColor = vec4(0.5, 0, 0, 1);"+
                    "}";


    public static int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }


}
