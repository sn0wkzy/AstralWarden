package com.github.merelysnow.astralwarden.player;

import com.github.merelysnow.astralwarden.check.CheckData;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.AutoClickerData;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@AllArgsConstructor
@Data
public class WardenPlayer {

    private final String playerName;
    private final Map<CheckType, CheckData> checkData = Maps.newConcurrentMap();
    private boolean notify;

    public <T extends CheckData> T getCheck(@NotNull CheckType cheatType, @NotNull T defaultValue) {
        return (T) this.checkData.computeIfAbsent(cheatType, $ -> defaultValue);
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }

    public void sendMessage(String... message) {
        final Player player = getPlayer();
        if(player == null) return;

        player.sendMessage(message);
    }
}
