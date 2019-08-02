package com.nice.gldemo.shape.square;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.nice.gldemo.base.BaseGLSL;
import com.nice.gldemo.util.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareColor extends BaseGLSL implements GLSurfaceView.Renderer {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    // a_Color：从外部传递进来的每个顶点的颜色值
                    "attribute vec4 a_Color;\n" +
                    // v_Color：将每个顶点的颜色值传递给片段着色器
                    "varying vec4 v_Color;\n" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    static final float triangleCoords[] = {
            -0.5f, -0.5f, 0.0f, // top left
            0.5f, -0.5f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, // bottom right
            0.5f, 0.5f, 0.0f  // top right
    };
    //采用坐标索引法来绘制图形
    static short index[] = {
            0, 1, 2, 1, 2, 3
    };
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mIndexBuffer;
    private final FloatBuffer mColorBuffer;
    private int mProgram;
    private float[] mViewMatrix = new float[16];     //相机视觉变换矩阵
    private float[] mProjectMatrix = new float[16]; //透视变换矩阵
    private float[] mMVPMatrix = new float[16];     //传入到OpenGL中的实际变换矩阵

    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PRE_VERTEX;   //顶点个数等于数组的长度除以顶点坐标数

    //顶点之间的偏移量
    private final int vertexStride = COORDS_PRE_VERTEX * 4; // float每个顶点四个字节

    //设置颜色
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private int mMatrixHandler;
    private int mPositionHandle;
    private int mColorHandle;

    public SquareColor() {
        mVertexBuffer = BufferUtil.createFloatBuffer(triangleCoords);
        mColorBuffer = BufferUtil.createFloatBuffer(color);
        mIndexBuffer = BufferUtil.createShortBuffer(index);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mVertexBuffer.position(0);
        mColorBuffer.position(0);
        mIndexBuffer.position(0);
        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
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
        mColorHandle = getAttrib("aColor");
        //绘制图形颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(
                mColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                0,
                mColorBuffer
                );
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
//        索引法绘制正方形
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
