package com.kuba6000.mobsinfo.api;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.biome.BiomeGenBase;

public class SpawnInfo {

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
        return this.info.hashCode();
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
            return "Biome: " + biome.biomeName;
        }

        public BiomeGenBase getBiome() {
            return biome;
        }

        @Override
        public int hashCode() {
            return biome.biomeName.hashCode();
        }
    }

}
