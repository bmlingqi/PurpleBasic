package com.bemil.purplebasic.engine;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.annotation.Listener;
import com.bemil.purplebasic.utils.ReflectionUtil;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ListenerManager {
    private Set<Class<?>> listeners;

    private static volatile ListenerManager listenerManager;

    private ListenerManager() {
        ReflectionUtil reflectionUtil = new ReflectionUtil(PurpleBasic.class.getPackage().getName(), true);
        listeners = reflectionUtil.getTypesAnnotatedWith(Listener.class);
    }

    public void registers() {
        for (Class<?> listener : listeners) {
            try {
                org.bukkit.event.Listener instance = (org.bukkit.event.Listener) listener.getDeclaredConstructor().newInstance();
                Bukkit.getPluginManager().registerEvents(instance, PurpleBasic.getInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ListenerManager getInstance() {
        if (listenerManager == null) {
            synchronized (ListenerManager.class) {
                if (listenerManager == null) {
                    listenerManager = new ListenerManager();
                }
            }
        }
        return listenerManager;
    }

}
