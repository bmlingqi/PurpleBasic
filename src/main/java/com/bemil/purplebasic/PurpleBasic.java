package com.bemil.purplebasic;

import com.bemil.purplebasic.engine.CommandManager;
import com.bemil.purplebasic.engine.LanguageManager;
import com.bemil.purplebasic.engine.ListenerManager;
import com.bemil.purplebasic.provider.ServerProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * 跨服传送
 * 经济系统
 * 设置传送点
 * 进服给予物品
 */
public final class PurpleBasic extends JavaPlugin {
    private static PurpleBasic instance;

    private ServerProvider serverProvider;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (getConfig().getBoolean("settings.enabled")) {
             serverProvider = new ServerProvider(this);
            ListenerManager.getInstance().registers();
            LanguageManager.getInstance().load();
            CommandManager.getInstance().init().register();
            LanguageManager.getInstance().getLanguages().get("PluginLoaded");
        }
    }

    public ServerProvider getServerProvider() {
        return serverProvider;
    }

    public File getLanguageFolder() {
        File languageFolder = new File(this.getDataFolder(), "langs");
        if (!languageFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            languageFolder.mkdirs();
        }
        return languageFolder;
    }

    public static PurpleBasic getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
    }
}
