package com.github.merelysnow.astralwarden.check;

import lombok.*;

@RequiredArgsConstructor
@Getter
public abstract class CheckData {

    private int violations = 0;

    public void incrementViolations(int amount) {
        this.violations += amount;
    }

    public void incrementViolation() {
        incrementViolations(1);
    }

}