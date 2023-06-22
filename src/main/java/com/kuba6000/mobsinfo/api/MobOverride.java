package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MobOverride {

    public boolean removeAll = false;
    public final List<MobDrop> additions = new ArrayList<>();
    public final List<MobDropSimplified> removals = new ArrayList<>();

    private static final ByteBuf BufHelper = Unpooled.buffer();

    public void writeToByteBuf(ByteBuf byteBuf) {
        BufHelper.clear();
        BufHelper.writeBoolean(removeAll);
        BufHelper.writeInt(additions.size());
        additions.forEach(drop -> drop.writeToByteBuf(BufHelper));
        BufHelper.writeInt(removals.size());
        removals.forEach(drop -> drop.writeToByteBuf(BufHelper));
        byteBuf.writeInt(BufHelper.readableBytes());
        byteBuf.writeBytes(BufHelper);
    }

    public static MobOverride readFromByteBuf(ByteBuf byteBuf) {
        int size = byteBuf.readInt();
        MobOverride mobOverride = new MobOverride();
        mobOverride.removeAll = byteBuf.readBoolean();
        int additionssize = byteBuf.readInt();
        for (int i = 0; i < additionssize; i++) mobOverride.additions.add(MobDrop.readFromByteBuf(byteBuf));
        int removalssize = byteBuf.readInt();
        for (int i = 0; i < removalssize; i++) mobOverride.removals.add(MobDropSimplified.readFromByteBuf(byteBuf));
        return mobOverride;
    }
}
