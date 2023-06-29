package com.bemil.purplebasic.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener {
    @EventHandler
    public void onPing(ServerListPingEvent event) {
            event.setMotd();
    }
}
