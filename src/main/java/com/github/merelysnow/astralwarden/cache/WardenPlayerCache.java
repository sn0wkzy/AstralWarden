package com.github.merelysnow.astralwarden.cache;

import com.github.merelysnow.astralwarden.player.WardenPlayer;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;

import java.util.Map;

public class WardenPlayerCache {

    @Delegate
    private final Map<String, WardenPlayer> cache = Maps.newHashMap();
}
