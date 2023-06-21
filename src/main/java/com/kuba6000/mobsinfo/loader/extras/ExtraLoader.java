package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.List;

import com.kuba6000.mobsinfo.api.LoaderReference;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

public class ExtraLoader {

    private static boolean initialized = false;
    private static final List<IExtraLoader> loaders = new ArrayList<>();

    private static void init() {
        initialized = true;
        if (LoaderReference.MineTweaker) loaders.add(new MineTweaker());
        if (LoaderReference.DraconicEvolution) loaders.add(new DraconicEvolution());
        if (LoaderReference.TinkersConstruct) loaders.add(new TinkersConstruct());
    }

    public static void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (!initialized) init();
        for (IExtraLoader loader : loaders) {
            loader.process(k, drops, recipe);
        }
    }
}
