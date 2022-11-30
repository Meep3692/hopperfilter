package ca.awoo.hopperfilter;

import org.bukkit.plugin.java.JavaPlugin;

public final class HopperFilter extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new HopperListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
