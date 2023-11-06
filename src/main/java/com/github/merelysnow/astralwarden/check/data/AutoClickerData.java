package com.github.merelysnow.astralwarden.check.data;

import com.github.merelysnow.astralwarden.check.CheckData;
import lombok.Data;


@Data
public class AutoClickerData extends CheckData {

    private long startDelay = 0L;
    private int clicks = 0;

    public void incrementClicks() {
        setClicks(getClicks() + 1);
    }

}
