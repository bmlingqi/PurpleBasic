package com.bemil.purplebasic.utils;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.annotation.Command;

import java.util.Set;

public class CommandUtil {
    private static volatile CommandUtil instance;
    private ReflectionUtil reflectionUtil = new ReflectionUtil(
            PurpleBasic.class.getPackage().getName(),
            true);

    private CommandUtil() {
    }

    public Set<Class<?>> getCommands() {
        return reflectionUtil.getTypesAnnotatedWith(Command.class);
    }

    public static CommandUtil getInstance() {
        if (instance == null) {
            synchronized (CommandUtil.class) {
                if (instance == null) {
                    instance = new CommandUtil();
                }
            }
        }
        return instance;
    }
}
