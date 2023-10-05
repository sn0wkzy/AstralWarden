package com.github.merelysnow.astralwarden.check.manager;

import com.github.merelysnow.astralwarden.AstralWardenPlugin;
import com.github.merelysnow.astralwarden.check.Check;
import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.impl.autoclicker.AutoClickCheckImpl;
import com.github.merelysnow.astralwarden.check.impl.nuker.NukerCheckImpl;
import com.github.merelysnow.astralwarden.logger.AstralWardenLogger;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CheckManager {

    private final WardenPlayerCache wardenPlayerCache;
    private final AstralWardenLogger astralWardenLogger;
    private final AstralWardenPlugin astralWardenPlugin;

    public void init() {
        final List<Check> checks = List.of(new AutoClickCheckImpl(wardenPlayerCache, astralWardenLogger, astralWardenPlugin),
                new NukerCheckImpl(wardenPlayerCache, astralWardenLogger, astralWardenPlugin));

        for (Check check : checks) {
            PacketEvents.getAPI().getEventManager().registerListener(check,
                    PacketListenerPriority.LOW);
            System.out.println("[AstralWarden] O módulo " + check.getName() + " foi iniciado");
        }
    }
}
