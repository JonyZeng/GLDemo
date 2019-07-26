package com.nice.gldemo.shape.triangle;

import android.opengl.GLES20;

import com.nice.gldemo.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * 绘制形状比较重要的三个参数
 * Vertex Shader(定点着色器) 用于渲染形状的定点的OpenGLES代码
 * Fragment Shader(片段着色器) 用于渲染形状的外观（颜色或纹理）的OpenGLES代码
 * Program(程式) 一个OpenGLES对象，包含了你想要用来绘制一个或多个状态的shader
 */
public class Triangle extends BaseGLSL {
    static final String vertexShaderCode =
            "attribute vec4 vPosition;\n" +
                    "void main(){\n" +
                    " gl_Position = vPosition;\n" +
                    "}";
    static final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform vec4 vColor;\n" +
                    "void main(){\n" +
                    "  gl_FragColor = vColor;\n" +
                    "}";

    int mProgram;
    //三角形的坐标,每一个向量点都是三维的，需要三组数据确定一个向量点
    static float[] triangleCoords = {
            0.5f, 0.5f, 0.0f,   //左上
            -0.5f, -0.5f, 0.0f, //左下
            0.5f, -0.5f, 0.0f,  //右下
    };
    //设置三角形颜色，red green blue 透明度
    private static float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
    //顶点个数
    static final int vertexCount = triangleCoords.length / COORDS_PRE_VERTEX;
    static FloatBuffer vertexBuffer;


    public Triangle() {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);    //申请底色空间
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Triangle.triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        //将坐标数据转换成floatBuffer，用以传入程序
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(Triangle.triangleCoords);
        vertexBuffer.position(0);

        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);

    }

    public void draw() {
        //将程序添加到openGLES2.0环境中
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的vPosition成员句柄
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(positionHandle);

        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PRE_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员句柄
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数据的句柄
        GLES20.glDisableVertexAttribArray(positionHandle);


    }
}
