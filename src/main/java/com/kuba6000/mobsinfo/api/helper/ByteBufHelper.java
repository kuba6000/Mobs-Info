package com.kuba6000.mobsinfo.api.helper;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public class ByteBufHelper {

    public static void writeString(ByteBuf byteBuf, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public static String readString(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
