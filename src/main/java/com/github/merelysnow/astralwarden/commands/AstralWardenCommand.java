package com.github.merelysnow.astralwarden.commands;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

public class AstralWardenCommand {

    @Command(
            name = "astralwarden",
            aliases = { "aw"},
            permission = "astralwarden.admin"
    )
    public void handleCommand(Context<Player> context, String argument) {
        final Player player = context.getSender();
    }
}
