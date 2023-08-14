package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;

import com.kuba6000.mobsinfo.api.ConstructableItemStack;
import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import io.netty.buffer.ByteBuf;

public interface IExtraLoader {

    void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe);

    class NormalChance implements IChanceModifier {

        double chance;

        NormalChance() {}

        NormalChance(double chance) {
            this.chance = chance;
        }

        @Override
        public int getPriority() {
            return 999999;
        }

        @Override
        public String getDescription() {
            return Translations.CHANCE.get(chance);
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return this.chance;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeDouble(chance);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            chance = byteBuf.readDouble();
        }
    }

    class BaseChance extends NormalChance {

        BaseChance() {}

        BaseChance(double chance) {
            super(chance);
        }

        @Override
        public String getDescription() {
            return Translations.BASE_CHANCE.get(chance);
        }
    }

    class DropsOnlyWithWeaknessII implements IChanceModifier {

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.DROPS_ONLY_WITH_WEAKNESS_2.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (victim == null) return 0d;
            if (victim.isPotionActive(Potion.weakness) && victim.getActivePotionEffect(Potion.weakness)
                .getAmplifier() >= 2) return chance;
            return 0d;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {

        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {

        }
    }

    class DropsOnlyUsing implements IChanceModifier {

        ItemStack weapon;

        DropsOnlyUsing() {}

        DropsOnlyUsing(ItemStack weapon) {
            this.weapon = weapon;
        }

        DropsOnlyUsing(Item weapon) {
            this.weapon = new ItemStack(weapon);
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.DROPS_ONLY_USING.get(weapon.getDisplayName());
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (!(attacker instanceof EntityPlayer)) return 0d;
            ItemStack weapon = ((EntityPlayer) attacker).getHeldItem();
            if (weapon == null) return 0d;
            if (weapon.isItemEqual(this.weapon)) return chance;
            return 0d;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            new ConstructableItemStack(weapon).writeToByteBuf(byteBuf);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            weapon = ConstructableItemStack.readFromByteBuf(byteBuf)
                .construct();
        }
    }

    class OrUsing extends DropsOnlyUsing {

        double newChance;

        OrUsing() {}

        OrUsing(ItemStack weapon, double newChance) {
            super(weapon);
            this.newChance = newChance;
        }

        OrUsing(Item weapon, double newChance) {
            super(weapon);
            this.newChance = newChance;
        }

        @Override
        public String getDescription() {
            return Translations.OR_USING.get(newChance, weapon.getDisplayName());
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (!(attacker instanceof EntityPlayer)) return 0d;
            ItemStack weapon = ((EntityPlayer) attacker).getHeldItem();
            if (weapon == null) return chance;
            if (weapon.isItemEqual(this.weapon)) return newChance;
            return chance;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            super.writeToByteBuf(byteBuf);
            byteBuf.writeDouble(newChance);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            super.readFromByteBuf(byteBuf);
            newChance = byteBuf.readDouble();
        }
    }

    class OrBiome implements IChanceModifier {

        BiomeGenBase biome;
        double newChance;

        OrBiome() {}

        OrBiome(BiomeGenBase biome, double newChance) {
            this.biome = biome;
            this.newChance = newChance;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.OR_BIOME.get(newChance, biome.biomeName);
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (victim == null) return chance;
            if (world.getBiomeGenForCoords((int) victim.posX, (int) victim.posZ) == biome) return newChance;
            return chance;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeInt(biome.biomeID);
            byteBuf.writeDouble(newChance);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            biome = BiomeGenBase.getBiome(byteBuf.readInt());
            newChance = byteBuf.readDouble();
        }
    }

    class DropsOnlyWithEnchant implements IChanceModifier {

        EnchantmentData enchantmentData;

        DropsOnlyWithEnchant() {}

        DropsOnlyWithEnchant(EnchantmentData enchantmentData) {
            this.enchantmentData = enchantmentData;
        }

        DropsOnlyWithEnchant(Enchantment enchantment) {
            this.enchantmentData = new EnchantmentData(enchantment, 1);
        }

        DropsOnlyWithEnchant(int enchantment) {
            this.enchantmentData = new EnchantmentData(enchantment, 1);
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.DROPS_ONLY_WITH_ENCHANT
                .get(StatCollector.translateToLocal(enchantmentData.enchantmentobj.getName()));
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (!(attacker instanceof EntityPlayer)) return 0d;
            ItemStack weapon = ((EntityPlayer) attacker).getHeldItem();
            if (weapon == null) return 0d;
            if (EnchantmentHelper.getEnchantmentLevel(enchantmentData.enchantmentobj.effectId, weapon)
                >= enchantmentData.enchantmentLevel) return chance;
            return 0d;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeInt(enchantmentData.enchantmentobj.effectId);
            byteBuf.writeInt(enchantmentData.enchantmentLevel);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            enchantmentData = new EnchantmentData(byteBuf.readInt(), byteBuf.readInt());
        }
    }

    class DropsOnlyInDimension implements IChanceModifier {

        int dimension;

        DropsOnlyInDimension() {}

        DropsOnlyInDimension(int dimension) {
            this.dimension = dimension;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.DROPS_ONLY_IN_DIMENSION.get(
                WorldProvider.getProviderForDimension(dimension)
                    .getDimensionName());
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (world.provider.dimensionId == dimension) return chance;
            return 0d;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeInt(dimension);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            dimension = byteBuf.readInt();
        }
    }

    class EachLevelOfGives implements IChanceModifier {

        Enchantment enchantment;
        double change;

        EachLevelOfGives() {}

        EachLevelOfGives(Enchantment enchantment, double change) {
            this.enchantment = enchantment;
            this.change = change;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.EACH_LEVEL_OF_GIVES
                .get(StatCollector.translateToLocal(enchantment.getName()), change + "%");
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (!(attacker instanceof EntityPlayer)) return chance;
            ItemStack weapon = ((EntityPlayer) attacker).getHeldItem();
            if (EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, weapon) > 0) return chance + change;
            return chance;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeInt(enchantment.effectId);
            byteBuf.writeDouble(change);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            enchantment = Enchantment.enchantmentsList[byteBuf.readInt()];
            change = byteBuf.readDouble();
        }
    }
}
