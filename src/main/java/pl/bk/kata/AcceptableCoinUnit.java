package pl.bk.kata;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

enum AcceptableCoinUnit {

    FIVE_ZLOTY(new BigDecimal("5.0")), TWO_ZLOTY(new BigDecimal("2.0")),
    ONE_ZLOTY(new BigDecimal("1.0")), FIFTY_GROSZY(new BigDecimal("0.5")),
    TWENTY_GROSZY(new BigDecimal("0.2")), TEN_GROSZY(new BigDecimal("0.1"));

    private BigDecimal value;
    AcceptableCoinUnit(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    private static final Map<BigDecimal,AcceptableCoinUnit> mapOfUnits;

    static {
        mapOfUnits = new HashMap<>();
        for (AcceptableCoinUnit unit : AcceptableCoinUnit.values()) {
            mapOfUnits.put(unit.value, unit);
        }
    }

    public static AcceptableCoinUnit findByKey(BigDecimal value) {
        return mapOfUnits.get(value);
    }
}
