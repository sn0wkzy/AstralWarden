package com.github.merelysnow.astralwarden.check.data;

import com.github.merelysnow.astralwarden.check.CheckData;
import com.google.common.collect.Lists;
import lombok.Data;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Data
public class NukerData extends CheckData {

    private List<Location> locationsList = Lists.newLinkedList();

    public boolean containsLocation(@NotNull Location location) {
        return this.locationsList.contains(location);
    }
}
