package pl.bk.kata;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class VendingMachine {

    private static final int MAX_NUMBER_OF_COIN_UNIT_TO_PUT_IN_SAFE = 50;
    private final Map<AcceptableCoinUnit,Integer> coinsInMachine = new HashMap<>();
    private final Map<ProductLocation,Product> products = new HashMap<>();
    private final Display display;
    private Optional<ProductLocation> chosenProductLocation = Optional.empty();
    private BigDecimal moneyToPay = BigDecimal.ZERO;
    private final ChangeCalculator changeCalculator;

    public VendingMachine(Display display, ChangeCalculator changeCalculator) {
        this.display = display;
        this.changeCalculator = changeCalculator;
    }

    public void insertCoin(Coin coin) {
        if (isProductChosen() && isCoinValueAcceptable(coin)) {
            AcceptableCoinUnit unit = AcceptableCoinUnit.findByKey(coin.getValue());
            if (isSpaceToPutCoinToSafe(unit)) {
                putCoinToSafe(unit);
                calculateMoneyToPay(unit.getValue());
                tryToBuyProduct();
            }
        }
    }

    private void tryToBuyProduct() {
        if (isAllMoneyForProductPaid()) {
            finalizeBuy();
        }
        if (isPaidMoreMoneyThanNeeded()) {
            Optional<List<BigDecimal>> change = changeCalculator.returnChange(coinsInMachine,
                                                                              moneyToPay.abs());
            if (change.isPresent()) {
                removeCoinsFromMachine(change.get());
                finalizeBuy();
            } else {
                display.log("You can't buy this product.");
            }
        }
    }

    private void finalizeBuy() {
        chosenProductLocation.ifPresent(products::remove);
        chosenProductLocation = Optional.empty();
        moneyToPay = BigDecimal.ZERO;
        display.log("Enjoy your meal.");
    }

    private boolean isPaidMoreMoneyThanNeeded() {
        return moneyToPay.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isAllMoneyForProductPaid() {
        return moneyToPay.compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean isProductChosen() {
        if (!chosenProductLocation.isPresent()) {
            display.log("You haven't chosen product.");
        }
        return chosenProductLocation.isPresent();
    }

    private void removeCoinsFromMachine(List<BigDecimal> change) {
        change.forEach(coin -> coinsInMachine.remove(AcceptableCoinUnit.findByKey(coin)));
    }

    private boolean isCoinValueAcceptable(Coin coin) {
        boolean acceptable = false;
        for (AcceptableCoinUnit unit : AcceptableCoinUnit.values()) {
            if (coin.getValue().compareTo(unit.getValue()) == 0) {
                acceptable = true;
                break;
            }
        }
        if (!acceptable) {
            display.log("You have inserted not acceptable coin. Please insert different coin.");
        }
        return acceptable;
    }

    private boolean isSpaceToPutCoinToSafe(AcceptableCoinUnit unit) {
        boolean isSpaceForCoinInMachine = coinsInMachine.entrySet().stream()
                .filter(n -> coinsInMachine.containsKey(unit))
                .findAny()
                .map(coinUnitInSafe ->
                    coinUnitInSafe.getValue() < MAX_NUMBER_OF_COIN_UNIT_TO_PUT_IN_SAFE)
                .orElse(Boolean.TRUE);
        if (!isSpaceForCoinInMachine) {
            display.log("There is no space to put this coin to machine."
                + " Please try with different coin.");
        }
        return isSpaceForCoinInMachine;
    }

    private void putCoinToSafe(AcceptableCoinUnit unit) {
        Integer numberOfCoinUnitInSafe = coinsInMachine.get(unit);
        Integer newNumberOfCoins = numberOfCoinUnitInSafe == null ? 1 : numberOfCoinUnitInSafe + 1;
        coinsInMachine.put(unit, newNumberOfCoins);
    }

    private void calculateMoneyToPay(BigDecimal moneyPaid) {
        moneyToPay = moneyToPay.subtract(moneyPaid);
        displayMoneyToPay();
    }

    private void displayMoneyToPay() {
        if (moneyToPay.compareTo(BigDecimal.ZERO) > 0) {
            display.log("The rest to pay is: " + moneyToPay + ".");
        }
    }

    public void insertProduct(ProductLocation productLocation, Product product) {
        if (isSpaceToInsertProduct(productLocation) && isProductCategoryValid(productLocation,
                                                                              product)) {
            products.put(productLocation, product);
        }
    }

    private boolean isSpaceToInsertProduct(ProductLocation productLocation) {
        return products.entrySet().stream()
                .noneMatch(productLocationEntry ->
                    productLocationEntry.getKey().getPlace() == productLocation.getPlace());
    }

    private boolean isProductCategoryValid(ProductLocation productLocation, Product product) {
        return products.entrySet().stream()
                .filter(productEntry ->
                    productEntry.getKey().getFloor() == productLocation.getFloor())
                .findFirst()
                .map(productEntry ->
                    productEntry.getValue().getProductCategory()
                        .equals(product.getProductCategory()))
                .orElse(Boolean.TRUE);
    }

    public void showProductPrice(ProductLocation productLocation) {
        Optional<Product> product = Optional.ofNullable(products.get(productLocation));
        if (product.isPresent()) {
            display.log(product.get().getName() + " price is "
                + product.get().getPrice() + " zlotych.");
        } else {
            display.log("There is no product in such place.");
        }
    }

    public void chooseProductToBuy(ProductLocation productLocation) {
        Optional<Product> product = Optional.ofNullable(products.get(productLocation));
        if (product.isPresent()) {
            this.moneyToPay = product.get().getPrice();
            this.chosenProductLocation = Optional.of(productLocation);
        } else {
            display.log("There is no product in such place.");
        }
    }

}
