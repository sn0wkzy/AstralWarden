package com.github.merelysnow.astralwarden.check.data;

import com.github.merelysnow.astralwarden.check.CheckData;
import com.google.common.collect.Lists;
import lombok.Data;
import org.bukkit.Location;

import java.util.List;


@Data
public class KillAuraData extends CheckData {

    private List<Integer> entitiesID = Lists.newLinkedList();
    private long lastHitDelay = 0L;
    private Location location = null;

    public void resetLocation() {
        setLocation(null);
    }

    public int getLastID() {
        return entitiesID.get(0);
    }

    public void addID(int id) {
        entitiesID.add(id);
    }

    public void removeLastID() {
        entitiesID.remove(0);
    }
}
