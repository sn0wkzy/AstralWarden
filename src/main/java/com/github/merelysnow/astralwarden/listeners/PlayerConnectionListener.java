package com.github.merelysnow.astralwarden.listeners;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.player.WardenPlayer;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final WardenPlayerCache wardenPlayerCache;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        wardenPlayerCache.put(player.getName(), new WardenPlayer(player.getName(), false));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        wardenPlayerCache.remove(player.getName());
    }
}
