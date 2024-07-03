package com.kuba6000.mobsinfo.nei;

import static com.kuba6000.mobsinfo.MobsInfo.MODID;

import java.awt.Rectangle;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.kuba6000.mobsinfo.MobsInfo;
import com.kuba6000.mobsinfo.api.utils.MobUtils;
import com.kuba6000.mobsinfo.config.Config;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeCatalysts;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;

public class VillagerTradesHandler extends TemplateRecipeHandler {

    private static final List<VillagerCachedRecipe> cachedRecipes = new ArrayList<>();
    private static final Logger LOG = LogManager.getLogger(MODID + "[Villager Trades Handler]");
    private static final VillagerTradesHandler instance = new VillagerTradesHandler();

    public static void addRecipe(List<Pair<MerchantRecipe, Double>> recipeList, EntityVillager displayMob) {
        ArrayList<Pair<Pair<Pair<PositionedStack, PositionedStack>, PositionedStack>, Double>> tradeList = new ArrayList<>(
            recipeList.size());
        int x = 7;
        int y = 12 + 83;
        boolean second = false;
        for (Pair<MerchantRecipe, Double> recipe : recipeList) {
            tradeList.add(
                Pair.of(
                    Pair.of(
                        Pair.of(
                            new PositionedStack(
                                recipe.getLeft()
                                    .getItemToBuy(),
                                x,
                                y),
                            recipe.getLeft()
                                .hasSecondItemToBuy()
                                    ? new PositionedStack(
                                        recipe.getLeft()
                                            .getSecondItemToBuy(),
                                        x + 18,
                                        y)
                                    : null),
                        new PositionedStack(
                            recipe.getLeft()
                                .getItemToSell(),
                            x + 59,
                            y)),
                    recipe.getRight()));
            if (!second) {
                x += 59 + 20;
                second = true;
            } else {
                x = 7;
                y += 20;
                second = false;
            }
        }
        instance.addRecipeInt(tradeList, displayMob);
    }

    private void addRecipeInt(
        ArrayList<Pair<Pair<Pair<PositionedStack, PositionedStack>, PositionedStack>, Double>> tradeList,
        EntityVillager displayMob) {
        cachedRecipes.add(new VillagerCachedRecipe(tradeList, displayMob));
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new VillagerTradesHandler();
    }

    @Override
    public String getOverlayIdentifier() {
        return "mobsinfo.villagertradeshandler";
    }

    @Override
    public String getGuiTexture() {
        return "mobsinfo:textures/gui/VillagerTrades.png";
    }

    @Override
    public String getRecipeName() {
        return "Villager Trades";
    }

    public VillagerTradesHandler() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(7, 62, 16, 16), getOverlayIdentifier()));
        if (!NEI_Config.isAdded) {
            FMLInterModComms.sendRuntimeMessage(
                MobsInfo.instance,
                "NEIPlugins",
                "register-crafting-handler",
                "MobsInfo@" + getRecipeName() + "@" + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 168, 192);

        VillagerCachedRecipe currentRecipe = (VillagerCachedRecipe) arecipes.get(recipe);

        {
            int x = 6;
            int y = 11 + 83;
            boolean second = false;
            for (int i = 0; i < currentRecipe.tradeList.size(); i++) {
                GuiDraw.drawTexturedModalRect(x, y, 0, 192, 77, 18);
                if (!second) {
                    x += 59 + 20;
                    second = true;
                } else {
                    x = 6;
                    y += 20;
                    second = false;
                }
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1f, 1f, 1f, 1f);

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int width = scale.getScaledWidth();
        int height = scale.getScaledHeight();
        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseZ = height - Mouse.getY() * height / mc.displayHeight - 1;

        // Get current x,y from matrix
        matrixBuffer.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matrixBuffer);
        float x = matrixBuffer.get(12);
        float y = matrixBuffer.get(13);

        int stackdepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glPushMatrix();

        try {
            EntityLiving e = currentRecipe.displayMob;

            int mobx = 31, moby = 50;
            e.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            e.lastTickPosX = e.posX;
            e.lastTickPosY = e.posY;
            e.lastTickPosZ = e.posZ;

            org.lwjgl.util.Rectangle v = MobUtils.getMobSizeInGui(e, mobx, moby, 20);

            // convert to local coordinate:
            float ylocal = (v.getY() + v.getHeight()) - y;
            float wantedy = 54.f;

            float new_scale = (40.f / v.getHeight());
            float new_scale_x = (38.f / v.getWidth());
            if (new_scale_x < new_scale) new_scale = new_scale_x;

            new_scale = (float) Math.round(20.f * new_scale) / 20.f;

            float a = moby - ylocal;
            float aa = a - (a * new_scale);
            float aaa = (wantedy - ylocal) - aa;

            // ARGS: x, y, scale, rot, rot, entity
            GuiInventory.func_147046_a(
                mobx,
                (int) (moby + aaa),
                Math.round(20.f * new_scale),
                (x + mobx) - mouseX,
                y + moby - 25 - mouseZ,
                e);

        } catch (Throwable ex) {
            Tessellator tes = Tessellator.instance;
            try {
                tes.draw();
            } catch (Exception ignored) {}
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        stackdepth -= GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
        if (stackdepth < 0) for (; stackdepth < 0; stackdepth++) GL11.glPopMatrix();
        if (stackdepth > 0) {
            for (; stackdepth > 0; stackdepth--) GL11.glPushMatrix();
            GL11.glLoadMatrix(matrixBuffer);
        }
        GL11.glPopAttrib();

        int err;
        while ((err = GL11.glGetError()) != GL11.GL_NO_ERROR) if (Config.Debug.showRenderErrors) LOG.error(
            "Profession {} | GL ERROR: {} / {}",
            currentRecipe.displayMob.getProfession(),
            err,
            GLU.gluErrorString(err));

        GL11.glDisable(GL11.GL_DEPTH_TEST);

    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
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
        if (outputId.equals(getOverlayIdentifier())) {
            arecipes.addAll(cachedRecipes);
            return;
        }
        super.loadCraftingRecipes(outputId, results);
    }

    class VillagerCachedRecipe extends TemplateRecipeHandler.CachedRecipe {

        private final ArrayList<Pair<Pair<Pair<PositionedStack, PositionedStack>, PositionedStack>, Double>> tradeList;
        private final ArrayList<PositionedStack> mOutputs;
        private final ArrayList<PositionedStack> mInputs;
        private final EntityVillager displayMob;

        public VillagerCachedRecipe(
            ArrayList<Pair<Pair<Pair<PositionedStack, PositionedStack>, PositionedStack>, Double>> tradeList,
            EntityVillager displayMob) {
            this.tradeList = tradeList;
            this.mOutputs = new ArrayList<>();
            this.mInputs = new ArrayList<>();
            for (Pair<Pair<Pair<PositionedStack, PositionedStack>, PositionedStack>, Double> trade : this.tradeList) {
                mOutputs.add(
                    trade.getLeft()
                        .getRight());
                mInputs.add(
                    trade.getLeft()
                        .getLeft()
                        .getLeft());
                if (trade.getLeft()
                    .getLeft()
                    .getRight() != null)
                    mInputs.add(
                        trade.getLeft()
                            .getLeft()
                            .getRight());
            }
            this.displayMob = displayMob;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return mOutputs;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return mInputs;
        }
    }
}
