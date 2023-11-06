package com.github.merelysnow.astralwarden.check.impl.nuker;

import com.github.merelysnow.astralwarden.AstralWardenPlugin;
import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.Check;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.NukerData;
import com.github.merelysnow.astralwarden.logger.AstralWardenLogger;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NukerCheckImpl extends Check {

    private final WardenPlayerCache wardenPlayerCache;
    private final FileConfiguration fileConfiguration;
    private final AstralWardenLogger astralWardenLogger;
    private final AstralWardenPlugin astralWardenPlugin;

    public NukerCheckImpl(WardenPlayerCache wardenPlayerCache, AstralWardenLogger astralWardenLogger, AstralWardenPlugin astralWardenPlugin) {
        this.astralWardenPlugin = astralWardenPlugin;
        this.wardenPlayerCache = wardenPlayerCache;
        this.astralWardenLogger = astralWardenLogger;
        this.fileConfiguration = astralWardenPlugin.getConfig();

        Bukkit.getScheduler().runTaskTimerAsynchronously(astralWardenPlugin, new NukerTask(wardenPlayerCache), 1L, 1L);
    }

    public String getName() {
        return "Nuker";
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        final PacketTypeCommon packetType = event.getPacketType();
        if (packetType != PacketType.Play.Client.PLAYER_DIGGING) return;

        final WrapperPlayClientPlayerDigging wrapperPlayClientPlayerDigging = new WrapperPlayClientPlayerDigging(event);
        if (wrapperPlayClientPlayerDigging.getAction() != DiggingAction.START_DIGGING) return;

        final User user = event.getUser();
        final WardenPlayer wardenPlayer = wardenPlayerCache.get(user.getName());
        final NukerData checkData = wardenPlayer.getCheck(CheckType.NUKER, new NukerData());

        final Vector3i blockPosition = wrapperPlayClientPlayerDigging.getBlockPosition();
        final Location location = new Location(Bukkit.getWorld("world"),
                blockPosition.getX(),
                blockPosition.getY(),
                blockPosition.getZ());

        if (!checkData.containsLocation(location)) {
            final Player player = wardenPlayer.getPlayer();
            final Location newLocation = getBlockBehindPlayer(player).getLocation();

            player.sendBlockChange(newLocation, Material.STONE, (byte) 0);
            checkData.getLocationsList().add(newLocation);
            return;
        }

        astralWardenLogger.log(wardenPlayer, this, checkData);
        if (checkData.getViolations() > fileConfiguration.getInt("Configuration.Nuker.max-violations")) {
            final Player player = wardenPlayer.getPlayer();
            if (player == null) return;

            Bukkit.getScheduler().runTask(astralWardenPlugin, () -> player.kickPlayer("§cNuker detectado"));
        }
    }

    public Block getBlockBehindPlayer(Player player) {
        final Vector direction = player.getLocation().getDirection();
        final Vector oppositeDirection = direction.multiply(-1);
        final Location blockLocation = player.getLocation().add(oppositeDirection);

        blockLocation.add(0, 2, 0);

        return blockLocation.getBlock();
    }
}
