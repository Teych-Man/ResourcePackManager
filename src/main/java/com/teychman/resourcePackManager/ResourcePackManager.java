package com.teychman.resourcePackManager;

import com.teychman.resourcePackManager.Commands.Main;
import com.teychman.resourcePackManager.Listeners.onPlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ResourcePackManager extends JavaPlugin {

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults(true);
        saveConfig();
        saveDefaultConfig();
        reloadConfig();

        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(this), this);

        Objects.requireNonNull(getCommand("resourcepack")).setExecutor(new Main(this));
        Objects.requireNonNull(getCommand("resourcepack")).setTabCompleter(new Main(this));


        getLogger().info("§a╔═════════════════════════════§r");
        getLogger().info("§a╠  Плагин запущен!§r");
        getLogger().info("§a╚═════════════════════════════§r");

    }

    @Override
    public void onDisable() {
        getLogger().info("§c╔═════════════════════════════§r");
        getLogger().info("§c╠  Плагин выгружен!§r");
        getLogger().info("§c╚═════════════════════════════§r");
    }
}
