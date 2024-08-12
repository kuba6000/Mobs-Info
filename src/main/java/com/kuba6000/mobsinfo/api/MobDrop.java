/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023-2024  kuba6000
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
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.helper.ByteBufHelper;
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
    public ArrayList<IChanceModifier> chanceModifiers = new ArrayList<>();
    public ArrayList<String> additionalInfo = new ArrayList<>();

    private MobDrop() {}

    /**
     * All-in-one constructor, you should use {@link #create(ItemStack)} instead!
     */
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

    /**
     * Create mob drop, to be used as builder
     *
     * @param stack Dropped item ()
     */
    public static MobDrop create(ItemStack stack) {
        return new MobDrop(stack, DropType.Normal, 10000, null, null, false, false);
    }

    /**
     * Set drop type
     *
     * @param type Drop type
     */
    public MobDrop withType(DropType type) {
        this.type = type;
        return this;
    }

    /**
     * Set drop chance, if you are having trouble with chance calculation, use {@link #getChanceBasedOnFromTo(int, int)}
     * but remember to set the stacksize to 1 on the create call !
     *
     * @param chance Drop chance 0 = 0%, 10000 = 100%
     */
    public MobDrop withChance(int chance) {
        this.chance = chance;
        return this;
    }

    /**
     * Set this drop to be randomly enchanted
     *
     * @param enchantPower Enchantment power
     */
    public MobDrop withRandomEnchant(int enchantPower) {
        this.enchantable = enchantPower;
        return this;
    }

    /**
     * Set this drop to be randomly damaged
     *
     * @param damages Damage-Weight map
     */
    public MobDrop withRandomDamage(HashMap<Integer, Integer> damages) {
        this.damages = damages;
        return this;
    }

    /**
     * Set this drop to be randomly damaged
     *
     * @param minDamage Minimum damage
     * @param maxDamage Maximum damage
     */
    public MobDrop withRandomDamage(int minDamage, int maxDamage) {
        this.damages = new HashMap<>();
        for (int i = minDamage; i <= maxDamage; i++) this.damages.put(i, 1);
        return this;
    }

    /**
     * Marks this drop as lootable
     */
    public MobDrop withLooting() {
        this.lootable = true;
        return this;
    }

    /**
     * Marks this drop as player only
     */
    public MobDrop withHardPlayerRestriction() {
        this.playerOnly = true;
        return this;
    }

    /**
     * Add chance modifiers, you can use modifiers implemented in {@link IChanceModifier} or implement some yourself!
     * After adding chance modifiers, normal chance is no longer shown in tooltip
     * Do not use in {@link IMobInfoProvider}, you should only use this in {@link IMobExtraInfoProvider} !
     */
    public MobDrop withChanceModifiers(IChanceModifier... modifiers) {
        this.variableChance = true;
        Collections.addAll(this.chanceModifiers, modifiers);
        return this;
    }

    /**
     * Adds additional tooltip on the drop
     * Do not use in {@link IMobInfoProvider}, you should only use this in {@link IMobExtraInfoProvider} !
     */
    public MobDrop withAdditionalInfo(String... additionalInfo) {
        Collections.addAll(this.additionalInfo, additionalInfo);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MobDrop copy() {
        MobDrop copy = new MobDrop(
            this.stack.copy(),
            this.type,
            this.chance,
            this.enchantable,
            this.damages == null ? null : (HashMap<Integer, Integer>) this.damages.clone(),
            this.lootable,
            this.playerOnly);
        copy.variableChance = this.variableChance;
        copy.chanceModifiers = (ArrayList<IChanceModifier>) this.chanceModifiers.clone();
        copy.additionalInfo = (ArrayList<String>) this.additionalInfo.clone();
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
        BufHelper.writeBoolean(variableChance);
        if (variableChance) {
            BufHelper.writeInt(chanceModifiers.size());
            chanceModifiers.forEach(i -> IChanceModifier.saveToByteBuf(BufHelper, i));
        }
        if (additionalInfo == null) BufHelper.writeInt(0);
        else {
            BufHelper.writeInt(additionalInfo.size());
            additionalInfo.forEach(i -> ByteBufHelper.writeString(BufHelper, i));
        }
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
        mobDrop.variableChance = byteBuf.readBoolean();
        if (mobDrop.variableChance) {
            mobDrop.chanceModifiers = new ArrayList<>();
            int chanceModifiersSize = byteBuf.readInt();
            for (int i = 0; i < chanceModifiersSize; i++) {
                mobDrop.chanceModifiers.add(IChanceModifier.loadFromByteBuf(byteBuf));
            }
        }
        int additionalInfoSize = byteBuf.readInt();
        for (int i = 0; i < additionalInfoSize; i++) mobDrop.additionalInfo.add(ByteBufHelper.readString(byteBuf));
        mobDrop.reconstructStack();
        return mobDrop;
    }

    public static double getChanceBasedOnFromTo(int from, int to) {
        return (((double) to * (double) to) + (double) to - ((double) from * (double) from) + (double) from)
            / (2 * ((double) to - (double) from + 1));
    }
}
