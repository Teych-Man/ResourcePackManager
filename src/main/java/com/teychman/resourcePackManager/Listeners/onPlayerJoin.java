package com.teychman.resourcePackManager.Listeners;

import com.teychman.resourcePackManager.ResourcePackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;


public record onPlayerJoin(ResourcePackManager plugin) implements Listener {

    @EventHandler
    public void payerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String url = plugin.getConfig().getString("URL");
        String hash = plugin.getConfig().getString("HASH");

        if (url == null || url.isEmpty()) return;

        if (hash != null && !hash.isEmpty()) {
            byte[] sha1 = fromHexString(hash);
            player.setResourcePack(url, sha1);
        } else {
            player.setResourcePack(url);
        }
    }

    private byte[] fromHexString(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }


    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED:
                plugin.getLogger().fine("Игрок " + event.getPlayer() + " успешно загрузил ресурспак");
                break;
            case DECLINED:
                plugin.getLogger().fine("Игрок " + event.getPlayer() + " отклонил ресурспак и был кикнут");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer("§cВы отклонили ресурс-пак! Без него играть не получится");
                    }
                }.runTaskLater(plugin, 5*20);
                break;

            case FAILED_DOWNLOAD:
                plugin.getLogger().warning("У игрока " + event.getPlayer() + " произошла ошибка при загрузке ресурспака");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer("§cУ вас возникла ошибка с загрузкой ресурспака!\n\n§eОбратитесь в поддержку:\n§bTG | @bangun_minecraft_bot\n§1Discord | https://discord.gg/k72TC2rWwe");
                    }
                }.runTaskLater(plugin, 5*20);
                break;

            case ACCEPTED:
                plugin.getLogger().fine("Игрок " + event.getPlayer() + " принял ресурспак, идет загрузка");
                break;
        }
    }

}
