package com.nice.gldemo.glsv;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.nice.gldemo.base.BaseGLSurfaceView;
import com.nice.gldemo.shape.RotateTriangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RotateTriangleGLSurfaceView extends BaseGLSurfaceView {
    private static final String TAG = "RotateTriangleGLSurface";
    private final RotateTriangleRenderer mRenderer;

    public RotateTriangleGLSurfaceView(Context context) {
        super(context);
        mRenderer = new RotateTriangleRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }
                Log.e(TAG, "onTouchEvent: dx = " + dx + "===>  dy=" + dy);
                mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class RotateTriangleRenderer implements Renderer {
        // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];
        private final float[] mRotationMatrix = new float[16];

        private float mAngle;
        private RotateTriangle mRotateTriangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//添加背景色
            mRotateTriangle = new RotateTriangle();

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            //通过方法 填充到投影变换矩阵中
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            float[] floats = new float[16];
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            Log.d(TAG, "onDrawFrame: mAngle  = " + mAngle);
            //设置相机的位置
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            //将其与之前计算出的投影矩阵结合在一起。合并后的矩阵 传递给绘制的图形

            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//            long time = SystemClock.uptimeMillis() % 4000L;
//            float angle = 0.090f * ((int) time);
            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);
            Matrix.multiplyMM(floats, 0, mMVPMatrix, 0, mRotationMatrix, 0);

            mRotateTriangle.draw(floats);
        }

        /**
         * Returns the rotation angle of the triangle shape (mTriangle).
         *
         * @return - A float representing the rotation angle.
         */
        public float getAngle() {
            return mAngle;
        }

        /**
         * Sets the rotation angle of the triangle shape (mTriangle).
         */
        public void setAngle(float angle) {
            mAngle = angle;
        }
    }
}
