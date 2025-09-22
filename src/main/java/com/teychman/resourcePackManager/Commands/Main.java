package com.teychman.resourcePackManager.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public record Main(JavaPlugin plugin) implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§f§l╹§c§l⟡§f§l╻ §cИспользование: /rp <update|reload|apply>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload": {
                plugin.reloadConfig();
                sender.sendMessage("§f§l╹§a§l⟡§f§l╻ §aКонфиг успешно перезагружен!");
                break;
            }

            case "update": {
                String url = plugin.getConfig().getString("URL");
                String sha1 = plugin.getConfig().getString("HASH");

                if (url == null || sha1 == null) {
                    sender.sendMessage("§f§l╹§c§l⟡§f§l╻ §cВ конфиге не найдена ссылка или sha1 для ресурспака!");
                    break;
                }

                byte[] sha1Bytes = fromHexString(sha1);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setResourcePack(url, sha1Bytes);
                }
                sender.sendMessage("§f§l╹§a§l⟡§f§l╻ §aРесурспак отправлен всем онлайн игрокам.");
                break;
            }

            case "apply": {
                if (args.length < 4) {
                    sender.sendMessage("§f§l╹§c§l⟡§f§l╻ §cИспользование: /rp apply <ссылка> <sha1> <@a|ник1 ник2 ...>");
                    break;
                }

                String url = args[1];
                String sha1 = args[2];
                byte[] sha1Bytes = fromHexString(sha1);

                if (args[3].equalsIgnoreCase("@a")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setResourcePack(url, sha1Bytes);
                    }
                    sender.sendMessage("§f§l╹§a§l⟡§f§l╻ §aРесурспак применён ко всем игрокам!");
                } else {
                    for (int i = 3; i < args.length; i++) {
                        Player target = Bukkit.getPlayerExact(args[i]);
                        if (target != null) {
                            target.setResourcePack(url, sha1Bytes);
                            sender.sendMessage("§f§l╹§a§l⟡§f§l╻ §aРесурспак отправлен игроку " + target.getName());
                        } else {
                            sender.sendMessage("§f§l╹§c§l⟡§f§l╻ §cИгрок " + args[i] + " не найден.");
                        }
                    }
                }
                break;
            }

            default:
                sender.sendMessage("§f§l╹§c§l⟡§f§l╻ §cНеизвестная подкоманда. Используйте: update, reload, apply");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("update");
            completions.add("apply");
        } else if (args.length == 4 && args[0].equalsIgnoreCase("apply")) {
            completions.add("@a");
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }

    private byte[] fromHexString(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
