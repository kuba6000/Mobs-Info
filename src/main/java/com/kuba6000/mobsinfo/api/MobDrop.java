/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.utils.GSONUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MobDrop {

    public enum DropType {

        Normal,
        Rare,
        Additional,
        Infernal;

        private static final DropType[] values = values();

        public static DropType get(int ordinal) {
            return values[ordinal];
        }
    }

    @GSONUtils.SkipGSON
    public ItemStack stack;

    public ConstructableItemStack reconstructableStack;
    public DropType type;
    public int chance;
    public Integer enchantable;
    public HashMap<Integer, Integer> damages;
    public boolean lootable = false;
    public boolean playerOnly = false;
    public boolean variableChance = false;
    public List<String> variableChanceInfo = new ArrayList<>();

    private MobDrop() {}

    public MobDrop(ItemStack stack, DropType type, int chance, Integer enchantable, HashMap<Integer, Integer> damages,
        boolean lootable, boolean playerOnly) {
        this.stack = stack;
        this.reconstructableStack = new ConstructableItemStack(stack);
        this.type = type;
        this.chance = chance;
        this.enchantable = enchantable;
        this.damages = damages;
        this.lootable = lootable;
        this.playerOnly = playerOnly;
    }

    public MobDrop copy() {
        @SuppressWarnings("unchecked")
        MobDrop copy = new MobDrop(
            this.stack.copy(),
            this.type,
            this.chance,
            this.enchantable,
            this.damages == null ? null : (HashMap<Integer, Integer>) this.damages.clone(),
            this.lootable,
            this.playerOnly);
        copy.variableChance = this.variableChance;
        copy.variableChanceInfo = this.variableChanceInfo;
        return copy;
    }

    public void reconstructStack() {
        this.stack = reconstructableStack.construct();
    }

    public void clampChance() {
        if (chance > 10000) {
            int div = (int) Math.ceil(chance / 10000d);
            stack.stackSize *= div;
            chance /= div;
            reconstructableStack = new ConstructableItemStack(stack);
        }
    }

    private static final ByteBuf BufHelper = Unpooled.buffer();

    public void writeToByteBuf(ByteBuf byteBuf) {
        BufHelper.clear();
        reconstructableStack.writeToByteBuf(BufHelper);
        BufHelper.writeInt(type.ordinal());
        BufHelper.writeInt(chance);
        BufHelper.writeBoolean(enchantable != null);
        if (enchantable != null) BufHelper.writeInt(enchantable);
        BufHelper.writeBoolean(damages != null);
        if (damages != null) {
            BufHelper.writeInt(damages.size());
            damages.forEach((k, v) -> {
                BufHelper.writeInt(k);
                BufHelper.writeInt(v);
            });
        }
        BufHelper.writeBoolean(lootable);
        BufHelper.writeBoolean(playerOnly);
        byteBuf.writeInt(BufHelper.readableBytes());
        byteBuf.writeBytes(BufHelper);
    }

    public static MobDrop readFromByteBuf(ByteBuf byteBuf) {
        MobDrop mobDrop = new MobDrop();
        int size = byteBuf.readInt();
        mobDrop.reconstructableStack = ConstructableItemStack.readFromByteBuf(byteBuf);
        mobDrop.type = DropType.get(byteBuf.readInt());
        mobDrop.chance = byteBuf.readInt();
        if (byteBuf.readBoolean()) mobDrop.enchantable = byteBuf.readInt();
        else mobDrop.enchantable = null;
        if (byteBuf.readBoolean()) {
            mobDrop.damages = new HashMap<>();
            int damagessize = byteBuf.readInt();
            for (int i = 0; i < damagessize; i++) mobDrop.damages.put(byteBuf.readInt(), byteBuf.readInt());
        } else mobDrop.damages = null;
        mobDrop.lootable = byteBuf.readBoolean();
        mobDrop.playerOnly = byteBuf.readBoolean();
        mobDrop.reconstructStack();
        return mobDrop;
    }

    public static double getChanceBasedOnFromTo(int from, int to) {
        return (((double) to * (double) to) + (double) to - ((double) from * (double) from) + (double) from)
            / (2 * ((double) to - (double) from + 1));
    }
}
