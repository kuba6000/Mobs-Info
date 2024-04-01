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

import static com.kuba6000.mobsinfo.MobsInfo.MODID;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobDropSimplified;
import com.kuba6000.mobsinfo.api.MobOverride;
import com.kuba6000.mobsinfo.api.event.PostMobsOverridesLoadEvent;
import com.kuba6000.mobsinfo.api.utils.GSONUtils;

public class OverridesConfig {

    private static final Logger LOG = LogManager.getLogger(MODID + "[Config-Overrides]");

    public static Map<String, MobOverride> overrides = new HashMap<>();
    private static File overrideFile = null;

    private static final Gson gson = GSONUtils.GSON_BUILDER_PRETTY.create();

    @SuppressWarnings("UnstableApiUsage")
    public static void LoadConfig() {
        LOG.info("Loading Config");
        if (overrideFile == null) overrideFile = Config.getConfigFile("MobOverrides.json");
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
        MinecraftForge.EVENT_BUS.post(new PostMobsOverridesLoadEvent(overrides));
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
