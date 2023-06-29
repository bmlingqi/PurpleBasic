package com.bemil.purplebasic.tasks;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.engine.LanguageManager;
import com.bemil.purplebasic.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TransmitTask implements Listener {
    private JavaPlugin plugin;
    private Player player;
    private int second;
    private String target;
    private BukkitTask runnable;

    public TransmitTask(JavaPlugin plugin, Player player, String target) {
        this(plugin, player, target, 3);
    }

    public TransmitTask(JavaPlugin plugin, Player player, String target, int second) {
        this.plugin = plugin;
        this.player = player;
        this.second = second;
        this.target = target;
        PurpleBasic.getInstance().getServer().getPluginManager().registerEvents(this, PurpleBasic.getInstance());
        HandlerList.unregisterAll(this);
    }

    public void start() {
        cancel();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTask(PurpleBasic.getInstance(), ()-> {
                    if (second <= 0) {
                        PurpleBasic.getInstance()
                                .getServerProvider()
                                .goServer(player, target);
                        Messager.sendMessage(player,
                                LanguageManager.getInstance()
                                        .getLanguage().get("TeleportSuccess"));
                        cancel();
                    } else {
                        Messager.sendMessage(player, LanguageManager.getInstance()
                                .getLanguage().get("TeleportTime")
                                .replace("%time", String.valueOf(second)));
                        second--;
                    }
                });
            }
        }.runTaskTimerAsynchronously(PurpleBasic.getInstance(), 20L, 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().equals(player)) {
            cancel();
        }
    }

    public void cancel() {
        if (runnable != null) {
            runnable.cancel();
            Messager.sendMessage(player, LanguageManager.getInstance()
                    .getLanguage().get("TeleportField"));
        }
    }
}
