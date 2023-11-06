package com.github.merelysnow.astralwarden.commands.subcommand;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.CheckData;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProfileCommand {

    private final WardenPlayerCache wardenPlayerCache;

    @Command(
            name = "astralwarden.profile",
            aliases = {"aw.profile"},
            permission = "astralwarden.profile"
    )
    public void handleCommand(Context<Player> context, String targetName) {
        final Player player = context.getSender();
        final Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendMessage("§cOops! o jogador alvo está offline ou não existe.");
            return;
        }

        final WardenPlayer wardenPlayer = wardenPlayerCache.get(target.getName());
        final List<CheckData> checks = wardenPlayer.getCheckData().values()
                .stream().filter(checkData -> checkData.getViolations() > 0).collect(Collectors.toList());

        player.sendMessage(String.format("§a   Perfil de %s", wardenPlayer.getPlayerName()));
        player.sendMessage(" ");

        //TODO end later
    }
}
