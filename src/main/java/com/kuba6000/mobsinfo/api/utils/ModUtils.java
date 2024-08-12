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

package com.kuba6000.mobsinfo.api.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import com.kuba6000.mobsinfo.MobsInfo;
import com.kuba6000.mobsinfo.mixin.minecraft.ASMEventHandlerAccessor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.ASMEventHandler;
import cpw.mods.fml.common.eventhandler.IEventListener;

public class ModUtils {

    public static final boolean isDeobfuscatedEnvironment = (boolean) Launch.blackboard
        .get("fml.deobfuscatedEnvironment");
    public static boolean isClientSided = false;

    public static boolean isClientThreaded() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient();
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {

        void accept(T t, U u, V v);
    }

    private static final HashMap<String, ModContainer> classNamesToModContainer = new HashMap<>();
    private static final Map.Entry<String, ModContainer> nullEntry = new AbstractMap.SimpleEntry<>("", null);

    private static void initClassNamesToMods() {
        if (classNamesToModContainer.size() == 0) {
            classNamesToModContainer.put(
                "net.minecraft",
                Loader.instance()
                    .getMinecraftModContainer());
            Loader.instance()
                .getActiveModList()
                .forEach(m -> {
                    Object Mod = m.getMod();
                    if (Mod != null) {
                        Package modPackage = Mod.getClass()
                            .getPackage();
                        if (modPackage == null) { // HOW CAN THIS EVEN HAPPEN ?!
                            MobsInfo.warn("Mod " + m.getModId() + " package is not loaded yet!");
                            return;
                        }
                        classNamesToModContainer.put(modPackage.getName(), m);
                    }
                });
        }
    }

    public static String getModNameFromClassName(String classname) {
        initClassNamesToMods();
        Map.Entry<String, ModContainer> entry = classNamesToModContainer.entrySet()
            .stream()
            .filter(e -> classname.startsWith(e.getKey()))
            .findAny()
            .orElse(null);

        if (entry == null) return "Unknown";
        else return entry.getValue()
            .getName();
    }

    public static ModContainer getModContainerFromClassName(String classname) {
        initClassNamesToMods();
        return classNamesToModContainer.entrySet()
            .stream()
            .filter(e -> classname.startsWith(e.getKey()))
            .findAny()
            .orElse(nullEntry)
            .getValue();
    }

    private static String modListVersion = null;

    public static String getModListVersion() {
        if (modListVersion != null) return modListVersion;

        HashSet<ModContainer> modsWithEntities = new HashSet<>();
        // noinspection unchecked
        for (Class<? extends Entity> value : ((Map<String, Class<? extends Entity>>) EntityList.stringToClassMapping)
            .values()) {
            ModContainer mod = getModContainerFromClassName(value.getName());
            if (mod != null) modsWithEntities.add(mod);
        }

        IEventListener[] listeners = new LivingDeathEvent(null, null).getListenerList()
            .getListeners(0 /* MinecraftForge.EVENT_BUS.busID */);
        for (IEventListener listener : listeners) {
            if (listener instanceof ASMEventHandler) {
                modsWithEntities.add(((ASMEventHandlerAccessor) listener).getOwner());
            }
        }

        listeners = new LivingDropsEvent(null, null, null, 0, false, 0).getListenerList()
            .getListeners(0 /* MinecraftForge.EVENT_BUS.busID */);
        for (IEventListener listener : listeners) {
            if (listener instanceof ASMEventHandler) {
                modsWithEntities.add(((ASMEventHandlerAccessor) listener).getOwner());
            }
        }

        String sortedList = modsWithEntities.stream()
            .filter(m -> m.getMod() != null)
            .sorted(Comparator.comparing(ModContainer::getModId))
            .collect(
                StringBuilder::new,
                (a, b) -> a.append(b.getModId())
                    .append(b.getVersion()),
                (a, b) -> a.append(", ")
                    .append(b))
            .toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            modListVersion = DatatypeConverter.printHexBinary(md.digest(sortedList.getBytes(StandardCharsets.UTF_8)))
                .toUpperCase();
            return modListVersion;
        } catch (Exception e) {
            modListVersion = sortedList;
            return sortedList;
        }
    }

    private static String modListVersionIgnoringModVersions = null;

    public static String getModListVersionIgnoringModVersions() {
        if (modListVersionIgnoringModVersions != null) return modListVersionIgnoringModVersions;

        HashSet<ModContainer> modsWithEntities = new HashSet<>();
        // noinspection unchecked
        for (Class<? extends Entity> value : ((Map<String, Class<? extends Entity>>) EntityList.stringToClassMapping)
            .values()) {
            ModContainer mod = getModContainerFromClassName(value.getName());
            if (mod != null) modsWithEntities.add(mod);
        }

        IEventListener[] listeners = new LivingDeathEvent(null, null).getListenerList()
            .getListeners(0 /* MinecraftForge.EVENT_BUS.busID */);
        for (IEventListener listener : listeners) {
            if (listener instanceof ASMEventHandler) {
                modsWithEntities.add(((ASMEventHandlerAccessor) listener).getOwner());
            }
        }

        listeners = new LivingDropsEvent(null, null, null, 0, false, 0).getListenerList()
            .getListeners(0 /* MinecraftForge.EVENT_BUS.busID */);
        for (IEventListener listener : listeners) {
            if (listener instanceof ASMEventHandler) {
                modsWithEntities.add(((ASMEventHandlerAccessor) listener).getOwner());
            }
        }

        String sortedList = modsWithEntities.stream()
            .filter(m -> m.getMod() != null)
            .sorted(Comparator.comparing(ModContainer::getModId))
            .collect(
                StringBuilder::new,
                (a, b) -> a.append(b.getModId()),
                (a, b) -> a.append(", ")
                    .append(b))
            .toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            modListVersionIgnoringModVersions = DatatypeConverter
                .printHexBinary(md.digest(sortedList.getBytes(StandardCharsets.UTF_8)))
                .toUpperCase();
            return modListVersionIgnoringModVersions;
        } catch (Exception e) {
            modListVersionIgnoringModVersions = sortedList;
            return sortedList;
        }
    }

}
