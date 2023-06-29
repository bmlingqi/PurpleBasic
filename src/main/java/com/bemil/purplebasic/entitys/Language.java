package com.bemil.purplebasic.entitys;

import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class Language {
    public String PluginLoaded;
    public String TeleportSuccess;
    public String TeleportTime;
    public String TeleportField;

    public void set(String key, String value) {
        try {
            Field field = this.getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(this, value);
        } catch (Exception ignored) {
        }
    }

    public String get(String key) {
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            return null;
        }
        field.setAccessible(true);
        try {
            return (String) field.get(this);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
