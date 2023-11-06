package com.github.merelysnow.astralwarden.check.impl.nuker;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.NukerData;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class NukerTask implements Runnable {

    private final WardenPlayerCache wardenPlayerCache;

    @Override
    public void run() {
        for (WardenPlayer wardenPlayer : wardenPlayerCache.values()) {
            final Player player = wardenPlayer.getPlayer();

            final NukerData checkData = wardenPlayer.getCheck(CheckType.NUKER, new NukerData());
            if (checkData.getLocationsList().isEmpty()) continue;

            player.sendBlockChange(checkData.getLocationsList().get(0), Material.AIR, (byte) 0);
            checkData.getLocationsList().remove(0);
        }
    }
}
