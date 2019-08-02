package com.nice.gldemo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {
    /**
     * Float类型占4Byte
     */
    static int BYTES_PER_FLOAT = 4;
    /**
     * Short类型占2Byte
     */
    static int BYTES_PER_SHORT = 2;

    public static FloatBuffer createFloatBuffer(float[] floats) {
        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(floats.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(floats);
        return floatBuffer;
    }

    public static ShortBuffer createShortBuffer(short[] shorts) {
        ShortBuffer shortBuffer = ByteBuffer
                .allocateDirect(shorts.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        shortBuffer.put(shorts);
        return shortBuffer;
    }
}
