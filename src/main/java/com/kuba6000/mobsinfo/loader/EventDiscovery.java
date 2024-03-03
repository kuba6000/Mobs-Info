package com.kuba6000.mobsinfo.loader;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kuba6000.mobsinfo.Tags;

import cpw.mods.fml.common.eventhandler.IEventListener;

public class EventDiscovery {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Event Discovery]");

    public static void run() {
        IEventListener[] listeners = new LivingDropsEvent(null, null, null, 0, false, 0).getListenerList()
            .getListeners(0);
        for (IEventListener listener : listeners) {
            LOG.info(listener.toString());
        }
        listeners = new LivingDeathEvent(null, null).getListenerList()
            .getListeners(0);
        for (IEventListener listener : listeners) {
            LOG.info(listener.toString());
        }
    }
}
