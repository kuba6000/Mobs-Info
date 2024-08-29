package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.passive.EntityVillager;

public class VillagerRecipe {

    public static HashMap<Integer, VillagerRecipe> recipes = new HashMap<>();

    public final ArrayList<VillagerTrade> trades;
    public final int profession;
    public final EntityVillager mob;

    public VillagerRecipe(ArrayList<VillagerTrade> trades, int profession, EntityVillager villager) {
        this.trades = trades;
        this.profession = profession;
        this.mob = villager;
    }

}
