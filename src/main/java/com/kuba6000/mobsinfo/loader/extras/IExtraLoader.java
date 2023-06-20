package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

public interface IExtraLoader {

    void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe);
}
