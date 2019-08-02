package com.nice.gldemo.base;


import android.opengl.GLES20;

import com.nice.gldemo.util.ShaderHelper;


/**
 * 形状基类
 */
public abstract class BaseGLSL {
    private static final String TAG = "BaseGLSL";
    public static final int COORDS_PRE_VERTEX = 3;    //每个顶点的坐标数
    //顶点之间的偏移量
    public static final int vertexStride = COORDS_PRE_VERTEX * 4;  //每个顶点四个字节，一个float占4个字节
    protected static int sProgramId;

    /**
     * 创建OPenGL对象，链接顶点着色器、片段着色器
     *
     * @param vertexShader
     * @param fragmentShader
     * @return
     */
    protected static int createOpenGLProgram(String vertexShader, String fragmentShader) {
        //1、编译顶点着色器
        int vertexShaderId = ShaderHelper.compileVertexShader(vertexShader);
        //2、编译片段着色器
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader);

        //3、将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        sProgramId = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId);
        ShaderHelper.validateProgram(sProgramId);
        //4、返回程序ID,后续调用 GLES20.glUseProgram(mProgram); 通知OpenGL开始使用该程序
        return sProgramId;
    }

    protected static int getUniform(String name) {
        return GLES20.glGetUniformLocation(sProgramId, name);
    }

    protected static int getAttrib(String name) {
        return GLES20.glGetAttribLocation(sProgramId, name);
    }

}
