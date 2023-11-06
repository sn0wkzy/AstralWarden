package com.github.merelysnow.astralwarden.logger;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.Check;
import com.github.merelysnow.astralwarden.check.CheckData;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AstralWardenLogger {

    private final WardenPlayerCache wardenPlayerCache;

    public void log(@NotNull WardenPlayer wardenPlayer, Check check, CheckData checkData) {
        checkData.incrementViolation();

        final List<WardenPlayer> staffList = wardenPlayerCache.values().stream().filter(WardenPlayer::isNotify).collect(Collectors.toList());
        if (staffList.isEmpty()) return;

        for (WardenPlayer all : staffList) {
            all.sendMessage(String.format("§8[AstralWarden] §a%s §7falhou §c%s %sx", wardenPlayer.getPlayerName(), check.getName(), checkData.getViolations()));
        }
    }
}
