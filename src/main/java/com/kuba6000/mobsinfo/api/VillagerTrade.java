package com.kuba6000.mobsinfo.api;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

        /**
         * All-in-one constructor, you should use {@link VillagerTrade#createItem} instead!
         */
        public TradeItem(@Nonnull ItemStack stack, @Nullable Set<Integer> possibleSizes,
            @Nullable Integer enchantability) {
            this.stack = stack;
            this.reconstructableStack = new ConstructableItemStack(this.stack);
            this.possibleSizes = possibleSizes;
            this.enchantability = enchantability;
        }

        public TradeItem(@Nonnull ItemStack stack) {
            this(stack, null, null);
        }

        /**
         * Sets this Item possible stack size values
         *
         * @param min From (minimum stack size)
         * @param max To (maximum stack size)
         */
        public TradeItem withPossibleSizes(int min, int max) {
            possibleSizes = new HashSet<>(max - min);
            for (int i = min; i <= max; i++) {
                possibleSizes.add(i);
            }
            return this;
        }

        /**
         * Set this Item to be randomly enchanted
         *
         * @param enchantmentPower Enchantment power
         */
        public TradeItem withRandomEnchant(int enchantmentPower) {
            this.enchantability = enchantmentPower;
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

    /**
     * All-in-one constructor, you should use {@link #create} instead!
     */
    public VillagerTrade(@Nonnull TradeItem firstInput, @Nullable TradeItem secondInput, @Nonnull TradeItem output,
        double chance) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.output = output;
        this.chance = chance;
    }

    /**
     * All-in-one constructor, you should use {@link #create} instead!
     */
    public VillagerTrade(@Nonnull ItemStack firstInput, @Nullable ItemStack secondInput, @Nonnull ItemStack output,
        double chance) {
        this.firstInput = new TradeItem(firstInput);
        this.secondInput = secondInput == null ? null : new TradeItem(secondInput);
        this.output = new TradeItem(output);
        this.chance = chance;
    }

    /**
     * All-in-one constructor, you should use {@link #create} instead!
     */
    public VillagerTrade(@Nonnull Item firstInput, @Nullable Item secondInput, @Nonnull Item output, double chance) {
        this.firstInput = new TradeItem(new ItemStack(firstInput));
        this.secondInput = secondInput == null ? null : new TradeItem(new ItemStack(secondInput));
        this.output = new TradeItem(new ItemStack(output));
        this.chance = chance;
    }

    /**
     * Create Trade, to be used as builder.
     * If you need to set possible sizes or enchantability on the item use {@link #create(TradeItem, TradeItem)}
     *
     * @param input  First input item (item to buy), to set second input use {@link #withSecondaryInput}
     * @param output Output item (item to sell)
     */
    public static VillagerTrade create(@Nonnull Item input, @Nonnull Item output) {
        return new VillagerTrade(input, null, output, 1d);
    }

    /**
     * Create Trade, to be used as builder.
     * If you need to set possible sizes or enchantability on the item use {@link #create(TradeItem, TradeItem)}
     *
     * @param input  First input item (item to buy), to set second input use {@link #withSecondaryInput}
     * @param output Output item (item to sell)
     */
    public static VillagerTrade create(@Nonnull ItemStack input, @Nonnull ItemStack output) {
        return new VillagerTrade(input, null, output, 1d);
    }

    /**
     * Create Trade, to be used as builder. Create Trade Items using {@link #createItem}
     *
     * @param input  First input item (item to buy), to set second input use {@link #withSecondaryInput}
     * @param output Output item (item to sell)
     */
    public static VillagerTrade create(@Nonnull TradeItem input, @Nonnull TradeItem output) {
        return new VillagerTrade(input, null, output, 1d);
    }

    /**
     * Create Trade Item.
     * You should only create Trade Item directly if you want to set possible sizes or enchantability.
     * To be used as builder
     *
     * @param item Item
     */
    public static TradeItem createItem(@Nonnull Item item) {
        return new TradeItem(new ItemStack(item));
    }

    /**
     * Create Trade Item.
     * You should only create Trade Item directly if you want to set possible sizes or enchantability.
     * To be used as builder
     *
     * @param item Item
     */
    public static TradeItem createItem(@Nonnull ItemStack item) {
        return new TradeItem(item);
    }

    /**
     * Set this Trade secondary input (second item to buy).
     *
     * @param secondInput Item
     */
    public VillagerTrade withSecondaryInput(@Nonnull TradeItem secondInput) {
        this.secondInput = secondInput;
        return this;
    }

    /**
     * Set this Trade secondary input (second item to buy)
     * If you need to set possible sizes or enchantability on the item use {@link #withSecondaryInput(TradeItem)}
     *
     * @param secondInput Item
     */
    public VillagerTrade withSecondaryInput(@Nonnull ItemStack secondInput) {
        this.secondInput = new TradeItem(secondInput);
        return this;
    }

    /**
     * Set this Trade secondary input (second item to buy)
     * If you need to set possible sizes or enchantability on the item use {@link #withSecondaryInput(TradeItem)}
     *
     * @param secondInput Item
     */
    public VillagerTrade withSecondaryInput(@Nonnull Item secondInput) {
        this.secondInput = new TradeItem(new ItemStack(secondInput));
        return this;
    }

    /**
     * Set this Trade chance to be added to the list
     *
     * @param chance Drop chance
     */
    public VillagerTrade withChance(double chance) {
        this.chance = chance;
        return this;
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

    public void reconstructStacks() {
        firstInput.reconstructStack();
        if (secondInput != null) secondInput.reconstructStack();
        output.reconstructStack();
    }
}
