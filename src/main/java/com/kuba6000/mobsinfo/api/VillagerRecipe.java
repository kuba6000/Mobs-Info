package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class VillagerRecipe {

    public static class TradeItem {

        public ItemStack stack;
        public List<Integer> possibleSizes;
        public Integer enchantability;

        public TradeItem(ItemStack stack, List<Integer> possibleSizes, Integer enchantability) {
            this.stack = stack;
            this.possibleSizes = possibleSizes;
            this.enchantability = enchantability;
        }

        public TradeItem(ItemStack stack) {
            this(stack, null, null);
        }

        public TradeItem setPossibleSizes(int min, int max) {
            possibleSizes = new ArrayList<>(max - min);
            for (int i = min; i <= max; i++) {
                possibleSizes.add(i);
            }
            return this;
        }
    }

    TradeItem firstInput;
    TradeItem secondInput;
    TradeItem output;
    Double chance;

    public VillagerRecipe(TradeItem firstInput, TradeItem secondInput, TradeItem output, Double chance) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.output = output;
        this.chance = chance;
    }

    public VillagerRecipe(ItemStack firstInput, ItemStack secondInput, ItemStack output, Double chance) {
        this.firstInput = new TradeItem(firstInput);
        this.secondInput = secondInput == null ? null : new TradeItem(secondInput);
        this.output = new TradeItem(output);
        this.chance = chance;
    }

    public VillagerRecipe(Item firstInput, Item secondInput, Item output, Double chance) {
        this.firstInput = new TradeItem(new ItemStack(firstInput));
        this.secondInput = secondInput == null ? null : new TradeItem(new ItemStack(secondInput));
        this.output = new TradeItem(new ItemStack(output));
        this.chance = chance;
    }

    public boolean hasSecondInput() {
        return secondInput != null;
    }

    public TradeItem getFirstInput() {
        return firstInput;
    }

    public TradeItem getSecondInput() {
        return secondInput;
    }

    public TradeItem getOutput() {
        return output;
    }
}
