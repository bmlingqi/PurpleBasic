package com.bemil.purplebasic.commands;

import com.bemil.purplebasic.PurpleBasic;
import com.bemil.purplebasic.annotation.Command;
import com.bemil.purplebasic.provider.CommandProvider;
import com.bemil.purplebasic.tasks.TransmitTask;
import com.bemil.purplebasic.utils.Messager;
import com.bemil.purplebasic.utils.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// /<command> basic
@Command
public class BasicCommand extends CommandProvider implements CommandExecutor {

    @Override
    public String getName() {
        return "purplebasic";
    }

    @Override
    public String getUsage() {
        return "/basic";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "pbc",
                "basic"
        };
    }

    @Override
    public String getDescription() {
        return "Basic Command";
    }

    @Override
    public String getPermission() {
        return "PurpleBasic.basic";
    }

    @Override
    public String getPermissionMessage() {
        return "Because you hasn't permission. So can't execute";
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("server")) {
            switch (args[1]) {
                case "list":
                    if (sender.hasPermission("PurpleBasic.basic.servers.list")) {
                        List<String> serversList = PurpleBasic.getInstance().getServerProvider().getServersList();
                        TextComponent messageComponent = new TextComponent(TextUtil.build("&a当前可用服务器有:\n"));
                        for (String server : serversList) {
                            TextComponent serverComponent = new TextComponent(TextUtil.build("  &e-&b " + server + "\n"));
                            serverComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/" + getName() +
                                            " server go " + server));
                            messageComponent.addExtra(serverComponent);
                        }
                        sender.spigot().sendMessage(messageComponent);
                    }
                    break;
                case "go":
                    if (sender.hasPermission("PurpleBasic.basic.servers.go")) {
                        TransmitTask transmitTask = new TransmitTask(PurpleBasic.getInstance(), ((Player) sender), args[2]);
                        transmitTask.start();
                    }
                    //Messager.sendMessage(sender, TextUtil.build("&a请稍后..."));
                    break;
            }
        }
        return false;
    }
}
