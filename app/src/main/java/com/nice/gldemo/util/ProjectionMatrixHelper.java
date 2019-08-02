package com.nice.gldemo.util;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 正交投影矩阵辅助类
 */
public class ProjectionMatrixHelper {
    int program;
    String name;
    private final int uMatrixLocation;
    private float[] mProjectionMatrix = {
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    };

    public ProjectionMatrixHelper(int program, String name) {
        this.program = program;
        this.name = name;
        uMatrixLocation = GLES20.glGetUniformLocation(program, name);
    }

    public void enable(int width, int height) {
        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }

}
