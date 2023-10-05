package com.github.merelysnow.astralwarden.check.impl.autoclicker;

import com.github.merelysnow.astralwarden.AstralWardenPlugin;
import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.AutoClickerData;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AutoClickTask implements Runnable {

    private final WardenPlayerCache wardenPlayerCache;
    private final FileConfiguration fileConfiguration;
    private final AstralWardenPlugin astralWardenPlugin;

    @Override
    public void run() {
        for (WardenPlayer wardenPlayer : wardenPlayerCache.values()) {
            final Player player = wardenPlayer.getPlayer();
            final AutoClickerData checkData = wardenPlayer.getCheck(CheckType.AUTOCLICKER, new AutoClickerData());

            if(checkData.getViolations() == 0) continue;

            checkData.setClicks(0);
            if (checkData.getViolations() > fileConfiguration.getInt("Configuration.AutoClicker.max-violations")) {
                Bukkit.getScheduler().runTask(astralWardenPlugin, () -> player.kickPlayer("§cAutoClicker detectado"));
            }
        }
    }
}
