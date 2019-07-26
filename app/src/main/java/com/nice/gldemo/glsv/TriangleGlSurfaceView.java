package com.nice.gldemo.glsv;

import android.content.Context;
import android.opengl.GLES20;

import com.nice.gldemo.base.BaseGLSurfaceView;
import com.nice.gldemo.shape.triangle.CameraTriangle;
import com.nice.gldemo.shape.triangle.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleGlSurfaceView extends BaseGLSurfaceView {


    public TriangleGlSurfaceView(Context context) {
        super(context);
//        setRenderer(new TriangleRender());
//        setRenderer(new CameraTriangleRender());
        setRenderer(new CameraColorTriangleRender());
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
            GLES20.glViewport(0, 0, width, height);
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
    private class CameraColorTriangleRender implements Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
