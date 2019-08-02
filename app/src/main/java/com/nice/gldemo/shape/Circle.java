package com.nice.gldemo.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.nice.gldemo.base.BaseGLSL;
import com.nice.gldemo.util.BufferUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class Circle extends BaseGLSL implements GLSurfaceView.Renderer {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final FloatBuffer mVertexBuffer;
    private final float[] mShapePos;
    private float radius = 1.0f;
    private int n = 360;  //切割份数
    private int mProgram;
    private float[] mViewMatrix = new float[16];     //相机视觉变换矩阵
    private float[] mProjectMatrix = new float[16]; //透视变换矩阵
    private float[] mMVPMatrix = new float[16];     //传入到OpenGL中的实际变换矩阵
    private int mMatrixHandler;
    private int mPositionHandler;
    private int mColorHandler;

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));//求X坐标
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));  //求Y坐标
            data.add(0.0f);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    private static float[] color = {1.0f, 1.0f, 1.0f, 1.0f};

    public Circle() {
        mShapePos = createPositions();
        mVertexBuffer = BufferUtil.createFloatBuffer(mShapePos);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mVertexBuffer.position(0);
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
        //将程序加入到OpenGL环境中
        GLES20.glUseProgram(mProgram);
        //变换矩阵
        mMatrixHandler = getUniform("vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        mPositionHandler = getAttrib("vPosition");
        //启用顶点坐标器
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        //准备图形数据
        GLES20.glVertexAttribPointer(
                mPositionHandler,
                COORDS_PRE_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride, mVertexBuffer
        );

        //获取片元着色器的vColor
        mColorHandler = getUniform("vColor");
        GLES20.glUniform4fv(mColorHandler, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mShapePos.length / 3);
        GLES20.glDisableVertexAttribArray(mPositionHandler);

    }

}
