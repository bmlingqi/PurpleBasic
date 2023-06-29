package com.bemil.purplebasic.engine;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.entitys.Language;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LanguageManager {
    private static volatile LanguageManager languageManager;
    private File languageFolder;
    private Map<String, File> languages;

    private LanguageManager() {
    }

    private Language language;

    public void load() {
        languageFolder = PurpleBasic.getInstance().getLanguageFolder();
        languages = new HashMap<>();
        loadAllLanguage();
    }

    public boolean setLanguage(String languageName) {
        if (this.getLanguages().containsKey(languageName)) {
            FileConfiguration languageContents = YamlConfiguration.loadConfiguration(
                    new File(
                            PurpleBasic.getInstance().getLanguageFolder(),
                            languageName + ".yml"
                    )
            );
            Set<String> keys = languageContents.getKeys(false);
            for (String key : keys) {
                this.language.set(key, languageContents.getString(key));
            }
            return true;
        } else return false;
    }

    public Language getLanguage() {
        return language;
    }

    public LanguageManager setLanguage(Language language) {
        this.language = language;
        return this;
    }

    private void loadAllLanguage() {
        File[] files = this.getLanguageFolder().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".yml");
            }
        });
        if (files != null) {
            for (File file : files) {
                String languageName = getFileName(file.getName());
                this.getLanguages().put(languageName, file);
            }
        }
    }

    private String getFileName(String fullFileName) {
        return fullFileName.split("[.]")[0];
    }

    private String getFileSuffix(String fullFileName) {
        return fullFileName.split("[.]")[1];
    }

    public File getLanguageFolder() {
        return languageFolder;
    }

    public Map<String, File> getLanguages() {
        return languages;
    }

    public static LanguageManager getInstance() {
        if (languageManager == null) {
            synchronized (LanguageManager.class) {
                if (languageManager == null) {
                    languageManager = new LanguageManager();
                }
            }
        }
        return languageManager;
    }
}
