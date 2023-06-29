package com.bemil.purplebasic.provider;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.List;

public class ServerProvider implements PluginMessageListener {
    public final String CHANNEL = "BungeeCord";
    private JavaPlugin plugin;
    private String[] servers;

    public ServerProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        registerPluginChannels();
    }

    private void registerPluginChannels() {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL, this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
    }

    public List<String> getServersList() {
        sendPluginMessage("GetServers");
        return Arrays.asList(this.servers);
    }

    public void goServer(Player player, String serverName) {
        sendPluginMessage("Connect;" + serverName, player);
    }

    public void sendPluginMessage(String message) {
        plugin.getServer().sendPluginMessage(plugin, CHANNEL, message.getBytes());
    }

    public void sendPluginMessage(String message, Player player) {
        player.sendPluginMessage(plugin, CHANNEL, message.getBytes());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(CHANNEL)) {
            return;
        }
        String msg = new String(message);
        if (msg.startsWith("ServerList")) {
            this.servers = msg.split("\00");
        }
    }
}
