package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.StatCollector;
import net.minecraft.world.biome.BiomeGenBase;

import com.kuba6000.mobsinfo.api.helper.TranslationHelper;

public class SpawnInfo {

    enum Translations {

        BIOME,
        STRUCTURE,
        EVENT;

        final String key;

        Translations() {
            key = "mobsinfo.spawninfo." + this.name()
                .toLowerCase();
        }

        public String get() {
            return StatCollector.translateToLocal(key);
        }

        public List<String> getAllLines() {
            ArrayList<String> lines = new ArrayList<>(Collections.singletonList(StatCollector.translateToLocal(key)));
            int i = 1;
            while (StatCollector.canTranslate(key + "_" + i))
                lines.add(StatCollector.translateToLocal(key + "_" + (i++)));
            return lines;
        }

        public String get(Object... args) {
            return TranslationHelper.translateFormattedFixed(key, args);
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return get();
        }
    }

    private static final HashSet<SpawnInfo> allKnownInfos = new HashSet<>();

    public static Set<SpawnInfo> getAllKnownInfos() {
        return allKnownInfos;
    }

    protected final String info;

    public SpawnInfo() {
        this.info = null;
        if (this.getClass() == SpawnInfo.class) throw new UnsupportedOperationException();
    }

    public SpawnInfo(String info) {
        this.info = info;
        allKnownInfos.add(this);
    }

    public String getInfo() {
        return info;
    }

    @Override
    public int hashCode() {
        return getInfo().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpawnInfo)) return false;
        return this.hashCode() == obj.hashCode();
    }

    public static class SpawnInfoBiome extends SpawnInfo {

        private final BiomeGenBase biome;

        public SpawnInfoBiome(BiomeGenBase biome) {
            this.biome = biome;
            allKnownInfos.add(this);
        }

        @Override
        public String getInfo() {
            return Translations.BIOME.get() + biome.biomeName;
        }

        public BiomeGenBase getBiome() {
            return biome;
        }
    }

    public static class SpawnInfoStructure extends SpawnInfo {

        private final String structureName;

        public SpawnInfoStructure(String structureName) {
            this.structureName = structureName;
            allKnownInfos.add(this);
        }

        @Override
        public String getInfo() {
            return Translations.STRUCTURE.get() + structureName;
        }
    }

    public static class SpawnInfoEvent extends SpawnInfo {

        private final String eventInfo;

        public SpawnInfoEvent(String eventInfo) {
            this.eventInfo = eventInfo;
            // allKnownInfos.add(this); don't save events
        }

        @Override
        public String getInfo() {
            return Translations.EVENT.get() + eventInfo;
        }
    }

}
