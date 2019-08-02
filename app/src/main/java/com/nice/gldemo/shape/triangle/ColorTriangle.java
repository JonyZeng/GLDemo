package com.nice.gldemo.shape.triangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.nice.gldemo.base.BaseGLSL;
import com.nice.gldemo.util.BufferUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.nice.gldemo.shape.triangle.Triangle.vertexBuffer;


public class ColorTriangle extends BaseGLSL implements GLSurfaceView.Renderer {
    private final FloatBuffer mVertexBuffer;
    private final FloatBuffer mColorBuffer;
    private int mProgram;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "varying  vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "  vColor=aColor;" +
                    "}";
    static final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 vColor;\n" +
                    "void main(){\n" +
                    "  gl_FragColor = vColor;\n" +
                    "}";
    static float[] triangleCoords = {
            0.5f, 0.5f, 0.0f,   //左上
            -0.5f, -0.5f, 0.0f, //左下
            0.5f, -0.5f, 0.0f,  //右下
    };
    //设置颜色
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };
    private float[] mViewMatrix = new float[16];      //定义相机矩阵
    private float[] mProjectMatrix = new float[16];   //定义透视矩阵
    private float[] mMVPMatrix = new float[16];       //实际变换矩阵
    static final int vertexCount = triangleCoords.length / COORDS_PRE_VERTEX;
    private int mMatrixHandler;
    private int mPositionHandle;
    private int mColorHandle;

    public ColorTriangle() {
        //1、获取顶点数据
        mVertexBuffer = BufferUtil.createFloatBuffer(triangleCoords);
        mColorBuffer = BufferUtil.createFloatBuffer(color);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mVertexBuffer.position(0);
        mColorBuffer.position(0);
        //加载顶点着色器,获取program
        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //投影矩阵  透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机矩阵
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //将相机矩阵和投影矩阵相乘，得到实际的变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PRE_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //设置绘制三角形的颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, mColorBuffer);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
