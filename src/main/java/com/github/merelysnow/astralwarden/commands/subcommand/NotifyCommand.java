package com.github.merelysnow.astralwarden.commands.subcommand;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class NotifyCommand {

    private final WardenPlayerCache wardenPlayerCache;

    @Command(
            name = "astralwarden.notify",
            aliases = {"aw.notify"},
            permission = "astralwarden.notify"
    )
    public void handleCommand(Context<Player> context) {
        final Player player = context.getSender();
        final WardenPlayer wardenPlayer = wardenPlayerCache.get(player.getName());

        wardenPlayer.setNotify(!wardenPlayer.isNotify());
        player.sendMessage(String.format("§8[AstralWarden] §7Você %s §7as notificações do anticheat.", wardenPlayer.isNotify() ? "§aativou" : "§cdesativou"));
    }
}
