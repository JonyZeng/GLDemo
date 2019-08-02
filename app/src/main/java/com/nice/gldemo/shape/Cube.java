package com.nice.gldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.nice.gldemo.base.BaseGLSL;
import com.nice.gldemo.util.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Cube extends BaseGLSL implements GLSurfaceView.Renderer {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "varying vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main(){" +
                    "   gl_Position = vMatrix*vPosition;" +
                    "   vColor = aColor;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    "   gl_FragColor = vColor;" +
                    "}";
    private float[] mViewMatrix = new float[16];     //相机视觉变换矩阵
    private float[] mProjectMatrix = new float[16]; //透视变换矩阵
    private float[] mMVPMatrix = new float[16];     //传入到OpenGL中的实际变换矩阵
    //绘制立方体，按照顺时针进行绘制,8个顶点
    final float[] cubePositions = {
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,    //反面左上4
            -1.0f, -1.0f, -1.0f,   //反面左下5
            1.0f, -1.0f, -1.0f,    //反面右下6
            1.0f, 1.0f, -1.0f,     //反面右上7
    };
    //正面由032和021
    final short[] index = {
            0, 3, 2, 0, 2, 1,  //正面
            0, 1, 5, 0, 5, 4,   //左面
            0, 7, 3, 0, 4, 7,    //上面
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2     //下面
    };
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mIndexBuffer;
    private final FloatBuffer mColorBuffer;
    private int mProgram;

    //八个顶点颜色 与顶点坐标一一对应
    float[] color = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };
    private int mMatrixHandler;
    private int mPositionHandler;
    private int mColorHandler;

    public Cube() {
        mVertexBuffer = BufferUtil.createFloatBuffer(cubePositions);
        mIndexBuffer = BufferUtil.createShortBuffer(index);
        mColorBuffer = BufferUtil.createFloatBuffer(color);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mVertexBuffer.position(0);
        mIndexBuffer.position(0);
        mColorBuffer.position(0);
        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        mMatrixHandler = getUniform("vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        mPositionHandler = getAttrib("vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(
                mPositionHandler,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                mVertexBuffer
        );
        mColorHandler = getAttrib("aColor");
        GLES20.glEnableVertexAttribArray(mColorHandler);
        GLES20.glVertexAttribPointer(
                mColorHandler,
                4,
                GLES20.GL_FLOAT,
                false,
                0,
                mColorBuffer
        );
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandler);
    }
}
