package com.kuba6000.mobsinfo.loader;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.VillagerTrade;

public class VanillaVillagerTradesLoader {

    public static HashMap<Integer, ArrayList<VillagerTrade>> vanillaTrades = new HashMap<>();

    public static void init() {
        { // profession 0

            ArrayList<VillagerTrade> recipes = new ArrayList<>();
            recipes.add(new VillagerTrade(Items.wheat, null, Items.emerald, 0.9d));
            recipes.add(new VillagerTrade(Item.getItemFromBlock(Blocks.wool), null, Items.emerald, 0.5d));
            recipes.add(new VillagerTrade(Items.chicken, null, Items.emerald, 0.5d));
            recipes.add(new VillagerTrade(Items.cooked_fished, null, Items.emerald, 0.4d));

            recipes.add(new VillagerTrade(Items.emerald, null, Items.bread, 0.9d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.melon, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.apple, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.cookie, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.shears, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.flint_and_steel, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.cooked_chicken, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.arrow, 0.3d));

            recipes.add(
                new VillagerTrade(
                    new ItemStack(Blocks.gravel, 10),
                    new ItemStack(Items.emerald),
                    new ItemStack(Items.flint, 5, 0),
                    0.5d));

            vanillaTrades.put(0, recipes);

        }

        { // profession 1
            ArrayList<VillagerTrade> recipes = new ArrayList<>();
            recipes.add(new VillagerTrade(Items.paper, null, Items.emerald, 0.8d));
            recipes.add(new VillagerTrade(Items.book, null, Items.emerald, 0.8d));
            recipes.add(new VillagerTrade(Items.written_book, null, Items.emerald, 0.3d));

            recipes.add(new VillagerTrade(Items.emerald, null, Item.getItemFromBlock(Blocks.bookshelf), 0.8d));
            recipes.add(new VillagerTrade(Items.emerald, null, Item.getItemFromBlock(Blocks.glass), 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.compass, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.clock, 0.2d));

            recipes.add(
                new VillagerTrade(
                    new VillagerTrade.TradeItem(new ItemStack(Items.book)),
                    new VillagerTrade.TradeItem(new ItemStack(Items.emerald)).withPossibleSizes(5, 64),
                    new VillagerTrade.TradeItem(new ItemStack(Items.book), null, 1),
                    0.07d));

            vanillaTrades.put(1, recipes);
        }

        { // profession 2
            ArrayList<VillagerTrade> recipes = new ArrayList<>();
            recipes.add(new VillagerTrade(Items.emerald, null, Items.experience_bottle, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.redstone, 0.4d));
            recipes.add(new VillagerTrade(Items.emerald, null, Item.getItemFromBlock(Blocks.glowstone), 0.3d));

            Item[] aitem = new Item[] { Items.iron_sword, Items.diamond_sword, Items.iron_chestplate,
                Items.diamond_chestplate, Items.iron_axe, Items.diamond_axe, Items.iron_pickaxe,
                Items.diamond_pickaxe };

            for (Item item : aitem) {
                recipes.add(
                    new VillagerTrade(
                        new VillagerTrade.TradeItem(new ItemStack(item)),
                        new VillagerTrade.TradeItem(new ItemStack(Items.emerald, 4)),
                        new VillagerTrade.TradeItem(new ItemStack(item), null, 5 + 7),
                        0.05d));
            }

            vanillaTrades.put(2, recipes);
        }

        { // profession 3

            ArrayList<VillagerTrade> recipes = new ArrayList<>();
            recipes.add(new VillagerTrade(Items.coal, null, Items.emerald, 0.7d));
            recipes.add(new VillagerTrade(Items.iron_ingot, null, Items.emerald, 0.5d));
            recipes.add(new VillagerTrade(Items.gold_ingot, null, Items.emerald, 0.5d));
            recipes.add(new VillagerTrade(Items.diamond, null, Items.emerald, 0.5d));

            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_sword, 0.5d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_sword, 0.5d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_axe, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_axe, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_pickaxe, 0.5d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_pickaxe, 0.5d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_shovel, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_shovel, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_hoe, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_hoe, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_boots, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_boots, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_helmet, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_helmet, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_chestplate, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_chestplate, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.iron_leggings, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.diamond_leggings, 0.2d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.chainmail_boots, 0.1d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.chainmail_helmet, 0.1d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.chainmail_chestplate, 0.1d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.chainmail_leggings, 0.1d));

            vanillaTrades.put(3, recipes);
        }

        { // profession 4
            ArrayList<VillagerTrade> recipes = new ArrayList<>();
            recipes.add(new VillagerTrade(Items.coal, null, Items.emerald, 0.7d));
            recipes.add(new VillagerTrade(Items.porkchop, null, Items.emerald, 0.5d));
            recipes.add(new VillagerTrade(Items.beef, null, Items.emerald, 0.5d));

            recipes.add(new VillagerTrade(Items.emerald, null, Items.saddle, 0.1d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.leather_chestplate, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.leather_boots, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.leather_helmet, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.leather_leggings, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.cooked_porkchop, 0.3d));
            recipes.add(new VillagerTrade(Items.emerald, null, Items.cooked_beef, 0.3d));

            vanillaTrades.put(4, recipes);
        }
    }

}
