package com.nice.gldemo.base;

import android.opengl.GLES20;

/**
 * 形状基类
 */
public abstract class BaseGLSL {
    private static final String TAG = "BaseGLSL";
    public static final int COORDS_PRE_VERTEX = 3;    //每个顶点的坐标数
    //顶点之间的偏移量
    public static final int vertexStride = COORDS_PRE_VERTEX * 4;  //每个顶点四个字节

    /**
     * 加载着色器
     *
     * @param type       着色器类型
     * @param shaderCode 着色器代码
     * @return
     */
    protected static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);  //根据type创建顶点着色器或者片元着色器
        GLES20.glShaderSource(shader, shaderCode);  //将着色器代码加入到着色器中
        GLES20.glCompileShader(shader); //编译着色器
        return shader;
    }

    public static int createOpenGLProgram(String vertexSource, String fragmentSource) {
        int vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertex == 0) return 0;
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment == 0) return 0;
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertex);
            GLES20.glAttachShader(program, fragment);
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                GLES20.glDeleteProgram(program);
                program = 0;
            }

        }

        return program;
    }
}
