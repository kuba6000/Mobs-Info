package com.kuba6000.mobsinfo.api;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.utils.GSONUtils;

public class VillagerTrade {

    public static class TradeItem {

        @GSONUtils.SkipGSON
        public ItemStack stack;
        public ConstructableItemStack reconstructableStack;
        public Set<Integer> possibleSizes;
        public Integer enchantability;

        public TradeItem(ItemStack stack, Set<Integer> possibleSizes, Integer enchantability) {
            this.stack = stack;
            this.reconstructableStack = new ConstructableItemStack(this.stack);
            this.possibleSizes = possibleSizes;
            this.enchantability = enchantability;
        }

        public TradeItem(ItemStack stack) {
            this(stack, null, null);
        }

        public TradeItem setPossibleSizes(int min, int max) {
            possibleSizes = new HashSet<>(max - min);
            for (int i = min; i <= max; i++) {
                possibleSizes.add(i);
            }
            return this;
        }

        public void reconstructStack() {
            this.stack = this.reconstructableStack.construct();
        }
    }

    TradeItem firstInput;
    TradeItem secondInput;
    TradeItem output;
    double chance;

    public VillagerTrade(TradeItem firstInput, TradeItem secondInput, TradeItem output, Double chance) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.output = output;
        this.chance = chance;
    }

    public VillagerTrade(ItemStack firstInput, ItemStack secondInput, ItemStack output, Double chance) {
        this.firstInput = new TradeItem(firstInput);
        this.secondInput = secondInput == null ? null : new TradeItem(secondInput);
        this.output = new TradeItem(output);
        this.chance = chance;
    }

    public VillagerTrade(Item firstInput, Item secondInput, Item output, Double chance) {
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

    public double getChance() {
        return chance;
    }
}
