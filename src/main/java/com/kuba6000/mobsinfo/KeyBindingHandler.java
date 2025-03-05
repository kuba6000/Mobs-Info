package com.kuba6000.mobsinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.kuba6000.mobsinfo.nei.MobHandler;
import com.kuba6000.mobsinfo.nei.MobHandlerInfernal;
import com.kuba6000.mobsinfo.nei.VillagerTradesHandler;

import codechicken.nei.recipe.GuiCraftingRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBindingHandler {

    public static KeyBindingHandler INSTANCE = new KeyBindingHandler();
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final KeyBinding openGui = new KeyBinding("Open Mobs Info GUI", Keyboard.KEY_NONE, "Mobs Info");

    public static void init() {
        ClientRegistry.registerKeyBinding(openGui);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent evt) {
        if (mc.thePlayer != null && mc.theWorld != null && openGui.isPressed()) {
            GuiCraftingRecipe gui = (GuiCraftingRecipe) GuiCraftingRecipe.createRecipeGui(
                MobHandler.getInstance()
                    .getOverlayIdentifier(),
                false);
            gui.currenthandlers.add(new MobHandlerInfernal().addAllRecipes());
            gui.currenthandlers.add(new VillagerTradesHandler().addAllRecipes());
            mc.displayGuiScreen(gui);
        }
    }

}
