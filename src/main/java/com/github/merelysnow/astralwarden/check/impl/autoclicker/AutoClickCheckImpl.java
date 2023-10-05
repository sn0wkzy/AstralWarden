package com.github.merelysnow.astralwarden.check.impl.autoclicker;

import com.github.merelysnow.astralwarden.AstralWardenPlugin;
import com.github.merelysnow.astralwarden.check.Check;
import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.AutoClickerData;
import com.github.merelysnow.astralwarden.logger.AstralWardenLogger;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AutoClickCheckImpl extends Check {

    private final WardenPlayerCache wardenPlayerCache;
    private final AstralWardenLogger astralWardenLogger;
    private final FileConfiguration fileConfiguration;
    private final AstralWardenPlugin astralWardenPlugin;

    public AutoClickCheckImpl(WardenPlayerCache wardenPlayerCache, AstralWardenLogger astralWardenLogger, AstralWardenPlugin astralWardenPlugin) {
        this.astralWardenPlugin = astralWardenPlugin;
        this.wardenPlayerCache = wardenPlayerCache;
        this.astralWardenLogger = astralWardenLogger;
        this.fileConfiguration = astralWardenPlugin.getConfig();
    }

    public String getName() {
        return "AutoClick A";
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        final PacketTypeCommon packetType = event.getPacketType();
        if (packetType != PacketType.Play.Client.INTERACT_ENTITY) return;

        final WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);
        if (wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;

        final User user = event.getUser();
        final WardenPlayer wardenPlayer = wardenPlayerCache.get(user.getName());
        final Player player = wardenPlayer.getPlayer();
        final AutoClickerData checkData = wardenPlayer.getCheck(CheckType.AUTOCLICKER, new AutoClickerData());

        if (System.currentTimeMillis() - checkData.getStartDelay() < 1000) {
            checkData.incrementClicks();
            return;
        }

        if (checkData.getClicks() > fileConfiguration.getInt("Configuration.AutoClicker.max-cps")) {
            astralWardenLogger.log(wardenPlayer, this, checkData);

            if (checkData.getViolations() > fileConfiguration.getInt("Configuration.AutoClicker.max-violations")) {
                Bukkit.getScheduler().runTask(astralWardenPlugin, () -> player.kickPlayer("§cAutoClicker detectado"));
            }
        }

        checkData.setClicks(0);
        checkData.setStartDelay(System.currentTimeMillis());
    }
}
