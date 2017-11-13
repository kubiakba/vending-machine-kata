package pl.bk.kata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ChangeCalculator {

    private static final int FINDING_CHANGE_NUMBER_OF_ITERATIONS = 10000;

    public Optional<List<BigDecimal>> returnChange(
                                final Map<AcceptableCoinUnit,Integer> coinsInMachine,
                                final BigDecimal changeToDeliver) {
        List<BigDecimal> coins = convertMapWithCoinsToList(coinsInMachine);
        Optional<List<BigDecimal>> change = Optional.empty();
        for (int i = 0; i < FINDING_CHANGE_NUMBER_OF_ITERATIONS; i++) {
            Collections.shuffle(coins);
            change = calculateChange(changeToDeliver, coins);
            if (change.isPresent()) {
                break;
            }
        }
        return change;
    }

    private Optional<List<BigDecimal>> calculateChange( BigDecimal changeToDeliver,
                                                        List<BigDecimal> coins) {
        List<BigDecimal> coinsToReturn = new ArrayList<>();
        boolean canChangeBeReturned = false;
        BigDecimal tempSum = BigDecimal.ZERO;
        for (BigDecimal coinValue : coins) {
            tempSum = tempSum.add(coinValue);
            coinsToReturn.add(coinValue);
            if (tempSum.compareTo(changeToDeliver) == 0) {
                canChangeBeReturned = true;
                break;
            } else if (tempSum.compareTo(changeToDeliver) > 0) {
                tempSum = tempSum.subtract(coinValue);
                coinsToReturn.remove(coinValue);
            }
        }
        if (canChangeBeReturned) {
            return Optional.of(coinsToReturn);
        } else {
            return Optional.empty();
        }
    }

    private List<BigDecimal> convertMapWithCoinsToList( Map<AcceptableCoinUnit,
                                                        Integer> coinsInMachine) {
        return coinsInMachine.entrySet().stream()
                .map(entry -> {
                    List<BigDecimal> amountOfCoins = new ArrayList<>();
                    IntStream.range(0, entry.getValue())
                            .forEach(n -> amountOfCoins.add(entry.getKey().getValue()));
                    return amountOfCoins;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
