package com.github.merelysnow.astralwarden.check.impl.killaura;

import com.github.merelysnow.astralwarden.AstralWardenPlugin;
import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.Check;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.KillAuraData;
import com.github.merelysnow.astralwarden.logger.AstralWardenLogger;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class KillAuraCheckImpl extends Check {

    private final WardenPlayerCache wardenPlayerCache;
    private final AstralWardenLogger astralWardenLogger;
    private final FileConfiguration fileConfiguration;
    private final AstralWardenPlugin astralWardenPlugin;

    public KillAuraCheckImpl(WardenPlayerCache wardenPlayerCache, AstralWardenLogger astralWardenLogger, AstralWardenPlugin astralWardenPlugin) {
        this.astralWardenPlugin = astralWardenPlugin;
        this.wardenPlayerCache = wardenPlayerCache;
        this.astralWardenLogger = astralWardenLogger;
        this.fileConfiguration = astralWardenPlugin.getConfig();

        Bukkit.getScheduler().runTaskTimerAsynchronously(astralWardenPlugin, new KillAuraTask(wardenPlayerCache), 20L, 20L);
    }

    public String getName() {
        return "KillAura";
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        final PacketTypeCommon packetType = event.getPacketType();
        if (packetType != PacketType.Play.Client.INTERACT_ENTITY) return;

        final WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);
        if (wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK)
            return;

        final User user = event.getUser();
        final WardenPlayer wardenPlayer = wardenPlayerCache.get(user.getName());
        final Player player = wardenPlayer.getPlayer();
        final KillAuraData checkData = wardenPlayer.getCheck(CheckType.KILLAURA, new KillAuraData());

        final Optional<Vector3f> targetVector = wrapperPlayClientInteractEntity.getTarget();
        final Location targetLocation = new Location(Bukkit.getWorld("world"),
                targetVector.get().getX(),
                targetVector.get().getY(),
                targetVector.get().getZ());

        if (checkData.getLocation() != null && checkData.getLocation().distance(targetLocation) < 1) {
            if (System.currentTimeMillis() - checkData.getLastHitDelay() < 15000) return;

            final Location location = player.getLocation().clone().add(3, 0, 3);
            final EntityArmorStand entity = new EntityArmorStand(((CraftPlayer) player).getHandle().world);
            entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

            final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);

            user.sendPacket(packet);
            checkData.setLocation(location);
            checkData.addID(entity.getId());
            checkData.setLastHitDelay(System.currentTimeMillis());
            return;
        }

        astralWardenLogger.log(wardenPlayer, this, checkData);
        if (checkData.getViolations() > 2) {
            Bukkit.getScheduler().runTask(astralWardenPlugin, () -> player.kickPlayer("§cKillaura detectado"));
        }
    }
}
