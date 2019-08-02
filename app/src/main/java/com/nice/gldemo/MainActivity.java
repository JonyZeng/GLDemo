package com.nice.gldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nice.gldemo.glsv.RotateTriangleGLSurfaceView;
import com.nice.gldemo.glsv.TriangleGlSurfaceView;


public class MainActivity extends AppCompatActivity {


    private TriangleGlSurfaceView mTriangleGlSurfaceView;
    private RotateTriangleGLSurfaceView mRotateTriangleGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTriangleGlSurfaceView = new TriangleGlSurfaceView(this);
//        mRotateTriangleGLSurfaceView = new RotateTriangleGLSurfaceView(this);
        setContentView(mTriangleGlSurfaceView);
    }


}
