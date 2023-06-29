package com.bemil.purplebasic.provider;

public abstract class CommandProvider {
    public abstract String getName();
    public abstract String getUsage();
    public abstract String[] getAliases();
    public abstract String getDescription();
    public abstract String getPermission();
    public abstract String getPermissionMessage();
    public abstract boolean execute();
}
