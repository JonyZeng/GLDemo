package com.nice.gldemo.glsv;

import android.content.Context;
import android.opengl.GLES20;

import com.nice.gldemo.base.BaseGLSL;
import com.nice.gldemo.base.BaseGLSurfaceView;
import com.nice.gldemo.shape.square.Square;
import com.nice.gldemo.shape.triangle.CameraTriangle;
import com.nice.gldemo.shape.triangle.ColorTriangle;
import com.nice.gldemo.shape.triangle.Triangle;
import com.nice.gldemo.util.BufferUtil;
import com.nice.gldemo.util.ProjectionMatrixHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleGlSurfaceView extends BaseGLSurfaceView {


    public TriangleGlSurfaceView(Context context) {
        super(context);
//        setRenderer(new CrameraTriangleRender());
//        setRenderer(new CameraTriangleRender());
//        setRenderer(new CameraColorTriangleRender());
//        setRenderer(new ColorTriangle());
        setRenderer(new Square());
    }

    /**
     * 绘制普通三角形
     */
    private class TriangleRender implements Renderer {
        Triangle mTriangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mTriangle = new Triangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height); //改变视角，指定OpenGL的可视区域
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            mTriangle.draw();
        }
    }

    /**
     * 绘制相机下的三角形
     */
    private class CameraTriangleRender implements Renderer {

        private CameraTriangle mCameraTriangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mCameraTriangle = new CameraTriangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mCameraTriangle.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            mCameraTriangle.draw();
        }
    }

    /**
     * 绘制摄像机下的彩色三角形
     */
    private class CameraColorTriangleRender extends BaseGLSL implements Renderer {

        private String VERTEX_SHADER = "" +
                "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                // vec4：4个分量的向量：r、g、b、a
                // a_Color：从外部传递进来的每个顶点的颜色值
                "attribute vec4 a_Color;\n" +
                // v_Color：将每个顶点的颜色值传递给片段着色器
                "varying vec4 v_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    v_Color = a_Color;\n" +
                "    gl_PointSize = 30.0;\n" +
                "    gl_Position = u_Matrix * a_Position;\n" +
                "}";
        private String FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                // v_Color：从顶点着色器传递过来的颜色值
                "varying vec4 v_Color;\n" +
                "void main()\t\t\n" +
                "{\n" +
                "    gl_FragColor = v_Color;\n" +
                "}";
        private final FloatBuffer mColorData;

        private float[] POINT_DATA = {
                -0.5f, -0.5f,
                0.5f, -0.5f,
                -0.5f, 0.5f,
                0.5f, 0.5f
        };
        private float[] COLOR_DATA = {
                // 一个顶点有3个向量数据：r、g、b
                1f, 0.5f, 0.5f, 1f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 0f};
        private final FloatBuffer mVertexData;
        public ProjectionMatrixHelper mProjectionMatrixHelper;
        /**
         * 坐标占用的向量个数
         */
        private int POSITION_COMPONENT_COUNT = 2;
        /**
         * 颜色占用的向量个数
         */
        private int COLOR_COMPONENT_COUNT = 3;

        public CameraColorTriangleRender() {
            mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
            mColorData = BufferUtil.createFloatBuffer(COLOR_DATA);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            //2、获取到programID
            createOpenGLProgram(VERTEX_SHADER, FRAGMENT_SHADER);
            //将programID传入OpenGL
            GLES20.glUseProgram(sProgramId);

            int a_position = getAttrib("a_Position");
            int a_color = getAttrib("a_Color");
            mProjectionMatrixHelper = new ProjectionMatrixHelper(sProgramId, "u_Matrix");
            //准备顶点坐标数据
            mVertexData.position(0);
            GLES20.glVertexAttribPointer(a_position, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData);
            GLES20.glEnableVertexAttribArray(a_position);
            //准备顶点颜色数据
            mColorData.position(0);
            GLES20.glVertexAttribPointer(a_color, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mColorData);
            GLES20.glEnableVertexAttribArray(a_color);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            mProjectionMatrixHelper.enable(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
        }
    }
}
