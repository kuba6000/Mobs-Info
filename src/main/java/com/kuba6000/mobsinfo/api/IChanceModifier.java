package com.kuba6000.mobsinfo.api;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.helper.ByteBufHelper;

import io.netty.buffer.ByteBuf;

public interface IChanceModifier {

    default int getPriority() {
        return 0;
    }

    String getDescription();

    default void applyTooltip(List<String> currentTooltip) {
        currentTooltip.add(getDescription());
    }

    double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
        EntityLiving victim);

    default void writeToByteBuf(ByteBuf byteBuf) {};

    default void readFromByteBuf(ByteBuf byteBuf) {};

    static void saveToByteBuf(ByteBuf byteBuf, IChanceModifier modifier) {
        String className = modifier.getClass()
            .getName();
        ByteBufHelper.writeString(byteBuf, className);
        modifier.writeToByteBuf(byteBuf);
    }

    static IChanceModifier loadFromByteBuf(ByteBuf byteBuf) {
        String className = ByteBufHelper.readString(byteBuf);
        Class<?> cl;
        try {
            cl = Class.forName(className, false, null);
            if (!cl.isAssignableFrom(IChanceModifier.class)) {
                throw new SecurityException();
            }
            IChanceModifier modifier = (IChanceModifier) cl.getDeclaredConstructor()
                .newInstance();
            modifier.readFromByteBuf(byteBuf);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException
            | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
