package com.github.merelysnow.astralwarden.check.impl.killaura;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.CheckType;
import com.github.merelysnow.astralwarden.check.data.KillAuraData;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class KillAuraTask implements Runnable {

    private final WardenPlayerCache wardenPlayerCache;

    @Override
    public void run() {
        for (WardenPlayer wardenPlayer : wardenPlayerCache.values()) {
            final Player player = wardenPlayer.getPlayer();
            final CraftPlayer craftPlayer = ((CraftPlayer) player);

            final KillAuraData checkData = wardenPlayer.getCheck(CheckType.KILLAURA, new KillAuraData());
            if (checkData.getEntitiesID().isEmpty()) continue;

            final int entityID = checkData.getLastID();
            final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityID);

            craftPlayer.getHandle().playerConnection.sendPacket(packet);
            checkData.removeLastID();
            checkData.resetLocation();
        }
    }
}
