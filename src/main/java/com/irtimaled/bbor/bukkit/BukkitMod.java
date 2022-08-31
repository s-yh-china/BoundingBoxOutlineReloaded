package com.irtimaled.bbor.bukkit;

import com.irtimaled.bbor.Logger;
import com.irtimaled.bbor.common.CommonProxy;
import com.irtimaled.bbor.common.messages.SubscribeToServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMod extends JavaPlugin {

    private final CommonProxy commonProxy;
    private final Events events;

    public BukkitMod() {
        commonProxy = new CommonProxy();
        commonProxy.init();
        events = new Events();
    }

    @Override
    public void onEnable() {
        Logger.init(this);

        Logger.info("This plugin is in dev, and is not official");
        Logger.info("please issues to https://github.com/s-yh-china/BoundingBoxOutlineReloaded");

        int version = VersionHelper.getVersion();
        if (version < VersionHelper.lowestSupportVersion || version >= VersionHelper.lowestUnSupportVersion) {
            Logger.error("MC version " + VersionHelper.getPackVersion() + " is not support");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!VersionHelper.init(this)) {
            Logger.error("Version Helper init error");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        events.enable();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, events::onTick, 11, 11);
        getServer().getPluginManager().registerEvents(events, this);
        getServer().getMessenger().registerIncomingPluginChannel(this, SubscribeToServer.NAME, events);
    }

    @Override
    public void onDisable() {
        events.disable();
        commonProxy.clearCaches();
    }
}
