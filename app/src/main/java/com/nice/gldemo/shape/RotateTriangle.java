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
        vertexBuffer.put(triangleCoords);       //将Java Dalvik的内存数据复制到native内存中。
        vertexBuffer.position(0);       //// 将缓冲区的指针移动到头部，保证数据是从最开始处读取
        //获取到程序ID
        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);//将程序添加OpenGLES 环境
        //获取顶点着色器vPosition成员的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //关联顶点坐标属性和缓存数据
        GLES20.glVertexAttribPointer(
                mPositionHandle,    //位置索引
                COORDS_PER_VERTEX,  //每个顶点属性需要关联的分量个数(必须为1、2、3、4)
                GLES20.GL_FLOAT,    //数据类型
                false,    //指定当被访问时,固定点数据值是否应该被归一化(GL_TRUE)或者直接转换为固定点值(GL_FALSE)(只有使用整数数据时)
                vertexStride,       //指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：紧密排列在一起的，初始值0
                vertexBuffer        //数据缓冲区
        );
        //通知OpenGL启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //获取顶点着色器vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //设置绘制三角形的颜色,给图形上色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //获得三角形变换矩阵的坐标句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        //指定vMvpMatrix的值
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        //绘制三角形，根据数组绘制图形
        GLES20.glDrawArrays(
                GLES20.GL_TRIANGLES,    //绘制的图形类型
                0,      //从顶点数组读取的起点
                vertexCount     //从顶点数组读取的数据长度
        );

        //禁止顶点数据的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);


    }
}
