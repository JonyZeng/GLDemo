package com.nice.gldemo.util;

import android.opengl.GLES20;
import android.util.Log;


public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    /**
     * 编译顶点着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 编译片段着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        //1、根据type，创建一个新的着色器对象
        final int shaderObjectId = GLES20.glCreateShader(type);


        //2、获取创建状态
        if (shaderObjectId == 0) {
            //在OpenGL中，都是通过整形值去作为OpenGL对象的引用。之后进行操作的时候都是将这个整形值传回OpenGL进行操作

            //返回值0代表创建对象失败
            Log.w(TAG, "compileShader: Could not create new shader ");
            return 0;
        }
        //3.将着色器代码上传到着色器对象中。
        GLES20.glShaderSource(shaderObjectId, shaderCode);

        //4.编译着色器对象
        GLES20.glCompileShader(shaderObjectId);

        //5.获取编译状态：OpenGL将想要获取的值放入长度为1的数组的首位
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.i(TAG, "compileShader:  Results of compiling source:" + "\n" + shaderCode + "\n:"
                + GLES20.glGetShaderInfoLog(shaderObjectId));

        //6.验证编译状态
        if (compileStatus[0] == 0) {
            //如果编译失败，则删除创建的着色器对象
            GLES20.glDeleteShader(shaderObjectId);
            Log.w(TAG, "compileShader: failed");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //1.创建一个OpenGL对象
        int programId = GLES20.glCreateProgram();

        //2.获取创建状态
        if (programId == 0) {
            Log.w(TAG, "linkProgram: Could not create new program");
            return 0;
        }
        //3.将顶点着色器依附到OpenGL程序对象
        GLES20.glAttachShader(programId, vertexShaderId);
        //4、将片段着色器依附到OpenGL程序对象
        GLES20.glAttachShader(programId, fragmentShaderId);

        // 将两个着色器链接到OpenGL对象
        GLES20.glLinkProgram(programId);
        //5.获取链接状态。OpenGL将想要 获取的值放入长度为1的数组的首位
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.i(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programId));
        //6.验证链接状态
        if (linkStatus[0] == 0) {
            //链接失败删除程序对象
            GLES20.glDeleteProgram(programId);
            Log.w(TAG, "linkProgram: failed");
            return 0;
        }
        return programId;

    }

    /**
     * 验证OpenGL程序对象状态
     *
     * @param programObjectId OpenGL程序ID
     * @return 是否可用
     */
    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
