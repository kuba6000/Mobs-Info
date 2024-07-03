package com.kuba6000.mobsinfo.loader;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

import org.apache.commons.lang3.tuple.Pair;

import com.kuba6000.mobsinfo.api.DummyWorld;
import com.kuba6000.mobsinfo.nei.VillagerTradesHandler;

public class VillagerTradesLoader {

    private static boolean alreadyGenerated = false;

    public static void generateVillagerTrades() {
        if (alreadyGenerated) return;
        alreadyGenerated = true;

        DummyWorld world = new DummyWorld();

        // vanilla recipes?

        { // profession 0
            EntityVillager entity = new EntityVillager(world);
            entity.setProfession(0);

            ArrayList<Pair<MerchantRecipe, Double>> recipes = new ArrayList<>();
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.wheat), new ItemStack(Items.emerald)), 0.9D));
            recipes.add(
                Pair.of(
                    new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.wool)), new ItemStack(Items.emerald)),
                    0.5D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.chicken), new ItemStack(Items.emerald)), 0.5D));
            recipes.add(
                Pair.of(new MerchantRecipe(new ItemStack(Items.cooked_fished), new ItemStack(Items.emerald)), 0.4D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.bread)), 0.9D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.melon)), 0.3D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.apple)), 0.3D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.cookie)), 0.3D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.shears)), 0.3D));
            recipes.add(
                Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.flint_and_steel)), 0.3D));
            recipes.add(
                Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.cooked_chicken)), 0.3D));
            recipes.add(Pair.of(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(Items.arrow)), 0.5D));
            recipes.add(
                Pair.of(
                    new MerchantRecipe(
                        new ItemStack(Blocks.gravel, 10),
                        new ItemStack(Items.emerald),
                        new ItemStack(Items.flint, 5, 0)),
                    0.5D));

            VillagerTradesHandler.addRecipe(recipes, entity);
        }

    }

}
