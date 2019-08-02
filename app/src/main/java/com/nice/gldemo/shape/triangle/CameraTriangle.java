package com.nice.gldemo.shape.triangle;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 相机下的三角形
 */
public class CameraTriangle extends Triangle {
    // 支持矩阵变换的顶点着色器
    public static final String vertexMatrixShaderCode =
            "attribute vec4 vPosition;\n" +
                    "uniform mat4 vMatrix;\n" +
                    "void main() {\n" +
                    "    gl_Position = vMatrix * vPosition;\n" +
                    "}";
    private float[] mViewMatrix = new float[16];      //定义相机矩阵
    private float[] mProjectMatrix = new float[16];   //定义透视矩阵
    private float[] mMVPMatrix = new float[16];       //实际变换矩阵

    private int mMatrixHandler;
    private int mPositionHandle;
    private int mColorHandle;

    //设置颜色，参数为红绿蓝和透明值,目前设置的颜色为白色
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    public CameraTriangle() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        mProgram = createOpenGLProgram(vertexMatrixShaderCode, fragmentShaderCode);

    }

    public void onSurfaceChanged(int width, int height) {

        //1.获取GLSurfaceView的宽高比例
        float ratio = (float) width / height;
        //填充了一个投影矩阵
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        // 在绘图对象上只应用一个投影变换时会导致显示empty display。所以我们在投影变化时通常还需要进行一个相机视角转化，使得显示对象能全部出现在屏幕上。

        //设置相机的视角位置
        Matrix.setLookAtM(mViewMatrix, 0,
                0, 0, -3,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);

        //将之前计算的投影矩阵结合起来，结合后的变换矩阵传递给绘制对象
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void draw() {
        super.draw();
        //将程序加入OpenGLES2.0环境
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
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PRE_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        //准备片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);


    }
}
