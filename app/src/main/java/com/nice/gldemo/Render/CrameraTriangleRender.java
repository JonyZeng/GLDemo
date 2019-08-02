package com.nice.gldemo.Render;

import com.nice.gldemo.base.BaseGLSL;

import java.nio.FloatBuffer;

public class CrameraTriangleRender extends BaseGLSL {
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

}
