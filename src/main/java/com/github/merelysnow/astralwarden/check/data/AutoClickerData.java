package com.github.merelysnow.astralwarden.check.data;

import com.github.merelysnow.astralwarden.check.CheckData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


@Data
public class AutoClickerData extends CheckData {

    private long startDelay = 0L;
    private int clicks = 0;

    public void incrementClicks() {
        setClicks(getClicks() + 1);
    }

}
