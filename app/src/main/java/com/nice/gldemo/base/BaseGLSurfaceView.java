package com.nice.gldemo.base;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * GLSurfaceView基类
 */
public class BaseGLSurfaceView extends GLSurfaceView {


    public BaseGLSurfaceView(Context context) {
        super(context);
        initEGLContext();
    }

    private void initEGLContext() {
        setEGLContextClientVersion(2);
    }

}
