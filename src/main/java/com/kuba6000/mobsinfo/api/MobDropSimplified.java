package com.kuba6000.mobsinfo.api;

import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.utils.GSONUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MobDropSimplified {

    @GSONUtils.SkipGSON
    ItemStack stack;

    ConstructableItemStack reconstructableStack;
    MobDrop.DropType type;

    private MobDropSimplified() {}

    public MobDropSimplified(ItemStack stack, MobDrop.DropType type) {
        reconstructableStack = new ConstructableItemStack(stack);
        this.type = type;
    }

    public void reconstructStack() {
        stack = reconstructableStack.construct();
    }

    public boolean isMatching(MobDrop drop) {
        return reconstructableStack.isSame(drop.reconstructableStack, true);
    }

    private static final ByteBuf BufHelper = Unpooled.buffer();

    public void writeToByteBuf(ByteBuf byteBuf) {
        BufHelper.clear();
        reconstructableStack.writeToByteBuf(BufHelper);
        BufHelper.writeInt(type.ordinal());
        byteBuf.writeInt(BufHelper.readableBytes());
        byteBuf.writeBytes(BufHelper);
    }

    public static MobDropSimplified readFromByteBuf(ByteBuf byteBuf) {
        MobDropSimplified mobDropSimplified = new MobDropSimplified();
        int size = byteBuf.readInt();
        mobDropSimplified.reconstructableStack = ConstructableItemStack.readFromByteBuf(byteBuf);
        mobDropSimplified.type = MobDrop.DropType.get(byteBuf.readInt());
        mobDropSimplified.reconstructStack();
        return mobDropSimplified;
    }
}
