package com.kuba6000.mobsinfo.nei;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import org.lwjgl.opengl.GL11;

import com.kuba6000.mobsinfo.MobsInfo;
import com.kuba6000.mobsinfo.api.helper.TranslationHelper;
import com.kuba6000.mobsinfo.config.Config;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeCatalysts;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MobHandlerFishing extends TemplateRecipeHandler {

    enum Translations {

        TITLE,
        FORMAT,
        FORMAT_1,
        FORMAT_2,
        FORMAT_3,
        FORMAT_4,
        FISH,
        JUNK,
        TREASURE,
        TOOLTIP_CHANCE,

        ;

        final String key;

        Translations() {
            key = "mobsinfo.mobhandlerfishing." + this.name()
                .toLowerCase();
        }

        public String get() {
            return StatCollector.translateToLocal(key);
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

    private static FishingRecipe recipe = null;
    private static final int itemsPerRow = 8, itemXShift = 18, itemYShift = 18, nextRowYShift = 30, itemsYStart = 75;
    private static final double fishBaseChance = 0.85d, junkBaseChance = 0.10d, treasureBaseChance = 0.05d;

    public MobHandlerFishing() {
        if (!NEI_Config.isAdded) {
            FMLInterModComms.sendRuntimeMessage(
                MobsInfo.instance,
                "NEIPlugins",
                "register-crafting-handler",
                "MobsInfo@" + getRecipeName() + "@" + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
        if (recipe == null) {
            recipe = new FishingRecipe();
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new MobHandlerFishing();
    }

    @Override
    public String getOverlayIdentifier() {
        return "mobsinfo.mobhandlerfishing";
    }

    @Override
    public String getGuiTexture() {
        return "mobsinfo:textures/gui/MobHandlerInfernal.png";
    }

    @Override
    public String getRecipeName() {
        return "Fishing Loot Table";
    }

    @Override
    public int getRecipeHeight(int recipe) {
        return MobHandlerFishing.recipe.height;
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        if (!Config.FishingHandler.enabled) return newInstance();
        if (inputId.equals("item")) {
            TemplateRecipeHandler handler = newInstance();
            ItemStack candidate = (ItemStack) ingredients[0];
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                handler.loadCraftingRecipes(getOverlayIdentifier(), (Object) null);
                return handler;
            }
        }
        return this.getUsageHandler(inputId, ingredients);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (!Config.FishingHandler.enabled) return;
        if (recipe == null) return;
        if (outputId.equals(getOverlayIdentifier())) {
            arecipes.add(recipe);
            return;
        }
        super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!Config.FishingHandler.enabled) return;
        if (recipe == null) return;
        for (PositionedStack pstack : recipe.all) {
            if (pstack.containsWithNBT(result)) {
                arecipes.add(recipe);
                return;
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!Config.FishingHandler.enabled) return;
        if (recipe == null) return;
        if (ingredient != null && ingredient.getItem() == Items.fishing_rod) {
            arecipes.add(recipe);
        }
    }

    @Override
    public void drawBackground(int recipeID) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 168, 105);

        int x = 6, y = itemsYStart + 11, yshift = nextRowYShift;

        if (recipe.fishCount > 0) {
            for (int i = 0; i < ((recipe.fishCount - 1) / itemsPerRow) + 1; i++) {
                GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
            }
            y += yshift + ((recipe.fishCount - 1) / itemsPerRow) * 18;
        }
        if (recipe.junkCount > 0) {
            for (int i = 0; i < ((recipe.junkCount - 1) / itemsPerRow) + 1; i++) {
                GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
            }
            y += yshift + ((recipe.junkCount - 1) / itemsPerRow) * 18;
        }
        if (recipe.treasureCount > 0) {
            for (int i = 0; i < ((recipe.treasureCount - 1) / itemsPerRow) + 1; i++) {
                GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
            }
        }
    }

    @Override
    public void drawForeground(int recipeID) {
        int y = 0, yshift = 10, x = 7;

        GuiDraw.drawString(Translations.TITLE.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);
        GuiDraw.drawString(Translations.FORMAT.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);
        GuiDraw.drawString(Translations.FORMAT_1.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);
        GuiDraw.drawString(Translations.FORMAT_2.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);
        GuiDraw.drawString(Translations.FORMAT_3.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);
        GuiDraw.drawString(Translations.FORMAT_4.get(), x, y += yshift, EnumColors.TEXT_DEFAULT.getColor(), false);

        x = 6;
        y = itemsYStart;
        yshift = nextRowYShift;

        if (recipe.fishCount > 0) {
            GuiDraw.drawString(
                Translations.FISH.get(fishBaseChance * 100d),
                x,
                y,
                EnumColors.TEXT_DEFAULT.getColor(),
                false);
            y += yshift + ((recipe.fishCount - 1) / itemsPerRow) * 18;
        }
        if (recipe.junkCount > 0) {
            GuiDraw.drawString(
                Translations.JUNK.get(junkBaseChance * 100d),
                x,
                y,
                EnumColors.TEXT_DEFAULT.getColor(),
                false);
            y += yshift + ((recipe.junkCount - 1) / itemsPerRow) * 18;
        }
        if (recipe.treasureCount > 0) {
            GuiDraw.drawString(
                Translations.TREASURE.get(treasureBaseChance * 100d),
                x,
                y,
                EnumColors.TEXT_DEFAULT.getColor(),
                false);
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack stack, List<String> currenttip, int recipeID) {
        if (recipe == null) return currenttip;
        FishingPositionedStack pstack = (FishingPositionedStack) recipe.all.stream()
            .filter(f -> f.containsWithNBT(stack))
            .findFirst()
            .orElse(null);
        if (pstack != null) pstack.handleTooltip(currenttip);
        return currenttip;
    }

    private static class FishingPositionedStack extends PositionedStack {

        private final double chance;

        public FishingPositionedStack(ItemStack stack, int x, int y, double chance) {
            super(stack, x, y, false);
            this.chance = chance;
        }

        public void handleTooltip(List<String> currenttip) {
            currenttip.add(Translations.TOOLTIP_CHANCE.get(chance * 100d));
        }
    }

    private class FishingRecipe extends TemplateRecipeHandler.CachedRecipe {

        public final int fishCount;
        public final int junkCount;
        public final int treasureCount;

        final List<PositionedStack> all;
        final List<PositionedStack> fish;
        final List<PositionedStack> junk;
        final List<PositionedStack> treasure;

        private final int height;

        public FishingRecipe() {
            int xorigin = 7, xoffset = xorigin, yoffset = itemsYStart + 12;

            List<WeightedRandomFishable> fishList = FishingHooks.fish;
            int fishTotalWeight = WeightedRandom.getTotalWeight(fishList);
            fishCount = fishList.size();
            fish = new ArrayList<>();

            for (Iterator<WeightedRandomFishable> iterator = fishList.iterator(); iterator.hasNext();) {
                WeightedRandomFishable fishable = iterator.next();
                ItemStack stack = fishable.field_150711_b.copy();
                double chance = (double) fishable.itemWeight / fishTotalWeight;

                fish.add(new FishingPositionedStack(stack, xoffset, yoffset, chance));
                xoffset += itemXShift;
                if (xoffset >= xorigin + (itemXShift * itemsPerRow) && iterator.hasNext()) {
                    xoffset = xorigin;
                    yoffset += itemYShift;
                }
            }

            xoffset = xorigin;
            yoffset += nextRowYShift;

            List<WeightedRandomFishable> junkList = FishingHooks.junk;
            int junkTotalWeight = WeightedRandom.getTotalWeight(junkList);
            junkCount = junkList.size();
            junk = new ArrayList<>();

            for (Iterator<WeightedRandomFishable> iterator = junkList.iterator(); iterator.hasNext();) {
                WeightedRandomFishable fishable = iterator.next();
                ItemStack stack = fishable.field_150711_b.copy();
                double chance = (double) fishable.itemWeight / junkTotalWeight;

                junk.add(new FishingPositionedStack(stack, xoffset, yoffset, chance));
                xoffset += itemXShift;
                if (xoffset >= xorigin + (itemXShift * itemsPerRow) && iterator.hasNext()) {
                    xoffset = xorigin;
                    yoffset += itemYShift;
                }
            }

            xoffset = xorigin;
            yoffset += nextRowYShift;

            List<WeightedRandomFishable> treasureList = FishingHooks.treasure;
            int treasureTotalWeight = WeightedRandom.getTotalWeight(treasureList);
            treasureCount = treasureList.size();
            treasure = new ArrayList<>();

            for (Iterator<WeightedRandomFishable> iterator = treasureList.iterator(); iterator.hasNext();) {
                WeightedRandomFishable fishable = iterator.next();
                ItemStack stack = fishable.field_150711_b.copy();
                double chance = (double) fishable.itemWeight / treasureTotalWeight;
                treasure.add(new FishingPositionedStack(stack, xoffset, yoffset, chance));
                xoffset += itemXShift;
                if (xoffset >= xorigin + (itemXShift * itemsPerRow) && iterator.hasNext()) {
                    xoffset = xorigin;
                    yoffset += itemYShift;
                }
            }

            all = new ArrayList<>();
            all.addAll(fish);
            all.addAll(junk);
            all.addAll(treasure);

            height = yoffset + 30;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return all;
        }
    }
}
