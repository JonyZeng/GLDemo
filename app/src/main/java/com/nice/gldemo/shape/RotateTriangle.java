package com.nice.gldemo.shape;

import android.opengl.GLES20;

import com.nice.gldemo.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RotateTriangle extends BaseGLSL {
    private final String vertexShaderCode = //顶点着色器代码
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =   //片元着色器代码
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer; //顶点浮点byte数组
    private final int mProgram;
    private int mPositionHandle;        //三角巷顶点坐标句柄
    private int mColorHandle;           //三角形颜色句柄
    private int mMVPMatrixHandle;       //变换坐标句柄

    static final int COORDS_PER_VERTEX = 3;
    static float[] triangleCoords = {
            // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;  //顶点坐标数量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex   顶点坐标偏移

    float[] color = {0.63671875f, 0.76953125f, 0.22265625f, 0.0f};//三角行颜色

    public RotateTriangle() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);   //把顶点数组放入byteBuffer中
        byteBuffer.order(ByteOrder.nativeOrder());  //逆时针排序， 排序方式很关键

        //将坐标数据转换成floatBuffer，用以传入程序
        vertexBuffer = byteBuffer.asFloatBuffer();   //将处理后的数组赋给顶点浮点数组
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);//将程序添加OpenGLES 环境
        //获取顶点着色器vPosition成员的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer
        );

        //获取顶点着色器vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //获得三角形变换矩阵的坐标句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        //指定vMvpMatrix的值
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        //禁止顶点数据的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);


    }
}
