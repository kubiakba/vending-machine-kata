package pl.bk.kata;

import java.math.BigDecimal;

class Coin {

    private final BigDecimal value;

    public Coin(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
