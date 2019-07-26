package com.nice.gldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nice.gldemo.glsv.TriangleGlSurfaceView;


public class MainActivity extends AppCompatActivity {


    private TriangleGlSurfaceView mTriangleGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTriangleGlSurfaceView = new TriangleGlSurfaceView(this);
        setContentView(mTriangleGlSurfaceView);
    }


}
