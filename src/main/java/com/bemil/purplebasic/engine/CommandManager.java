package com.bemil.purplebasic.engine;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.annotation.Command;
import com.bemil.purplebasic.commands.BasicCommand;
import com.bemil.purplebasic.provider.CommandProvider;
import com.bemil.purplebasic.utils.ReflectionUtil;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class CommandManager {
    private Set<Class<?>> commands;

    private static volatile CommandManager commandManager;
    private ReflectionUtil reflectionUtil = new ReflectionUtil(
            PurpleBasic.class.getPackage().getName(),
            true);

    private CommandManager() {
    }

    public CommandManager init() {
        commands = reflectionUtil.getTypesAnnotatedWith(Command.class);
        return this;
    }

    public void register() {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(PurpleBasic.getInstance().getServer().getPluginManager());
            for (Class<?> commandClass : this.commands) {
                Constructor<?> constructor = commandClass.getDeclaredConstructor();
                CommandProvider commandProvider = (CommandProvider) constructor.newInstance();
                if (implementsInterface(commandClass, CommandExecutor.class)) {
                    Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                    commandConstructor.setAccessible(true);
                    PluginCommand pluginCommand = commandConstructor.newInstance(commandProvider.getName(), PurpleBasic.getInstance());
                    pluginCommand.setAliases(Arrays.asList(commandProvider.getAliases()));
                    pluginCommand.setDescription(commandProvider.getDescription());
                    pluginCommand.setExecutor(((CommandExecutor) commandProvider));
                    pluginCommand.setUsage(commandProvider.getUsage());
                    pluginCommand.setPermission(commandProvider.getPermission());
                    pluginCommand.setPermissionMessage(commandProvider.getPermissionMessage());
                    if (implementsInterface(commandClass, TabCompleter.class)) {
                        pluginCommand.setTabCompleter(((TabCompleter) commandProvider));
                    }
                    commandMap.register(commandProvider.getName(), pluginCommand);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean implementsInterface(Class<?> clazz, Class<?> interfaceClass) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (anInterface == interfaceClass || anInterface.isAssignableFrom(interfaceClass))
                return true;
        }
        return false;
    }

    public static CommandManager getInstance() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                if (commandManager == null) {
                    commandManager = new CommandManager();
                }
            }
        }
        return commandManager;
    }

}
