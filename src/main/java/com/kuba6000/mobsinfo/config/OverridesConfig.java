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

package com.kuba6000.mobsinfo.config;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuba6000.mobsinfo.Tags;
import com.kuba6000.mobsinfo.api.ConstructableItemStack;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.utils.GSONUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class OverridesConfig {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Config-Overrides]");

    public static class MobDropSimplified {

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

    public static class MobOverride {

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

    public static Map<String, MobOverride> overrides = new HashMap<>();
    private static File overrideFile = null;

    private static final Gson gson = GSONUtils.GSON_BUILDER_PRETTY.create();

    @SuppressWarnings("UnstableApiUsage")
    public static void LoadConfig() {
        LOG.info("Loading Config");
        if (overrideFile == null) overrideFile = Config.getConfigFile("MobOverrides.cfg");
        if (!overrideFile.exists()) writeExampleFile();
        Reader reader = null;
        try {
            reader = Files.newReader(overrideFile, StandardCharsets.UTF_8);
            overrides = gson.fromJson(reader, new TypeToken<Map<String, MobOverride>>() {}.getType());
            overrides.remove("ExampleMob");
            overrides.values()
                .forEach(o -> o.additions.forEach(MobDrop::reconstructStack));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception ignored) {}
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void writeExampleFile() {
        LOG.info("No config has been detected, writing an example one");
        Writer writer = null;
        try {
            writer = Files.newWriter(overrideFile, StandardCharsets.UTF_8);
            Map<String, MobOverride> example = new HashMap<>(1);
            MobOverride ex1 = new MobOverride();
            ex1.removals.add(new MobDropSimplified(new ItemStack(Items.rotten_flesh, 1), MobDrop.DropType.Normal));
            HashMap<Integer, Integer> exdamages = new HashMap<>(3);
            exdamages.put(1, 1);
            exdamages.put(2, 5);
            exdamages.put(3, 10);
            ex1.additions.add(
                new MobDrop(
                    new ItemStack(Items.diamond_sword),
                    MobDrop.DropType.Rare,
                    500,
                    20,
                    exdamages,
                    true,
                    false));
            example.put("ExampleMob", ex1);
            gson.toJson(example, writer);
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (Exception ignored) {}
        }
    }
}
