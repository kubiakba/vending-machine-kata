package pl.bk.kata

import spock.lang.Specification

class VendingMachineSpecification extends Specification {

    def machine = new VendingMachine(new Display(),new ChangeCalculator() )
    def product = new Product("Coca-cola",new BigDecimal("2.0"),ProductCategory.BEVERAGE)

    def "should validate coin inserted to machine"(){
        given:
            def coin = new Coin(new BigDecimal(coinValue))
        when:
            machine.insertProduct(ProductLocation.FIRST_FLOOR_FIRST_PLACE, product)
            machine.chooseProductToBuy(ProductLocation.FIRST_FLOOR_FIRST_PLACE)
            machine.insertCoin(coin)
        then:
            machine.coinsInMachine.size() == expectedSize
        where:
            coinValue || expectedSize
            "5.0"     || 1
            "2.0"     || 1
            "0.5"     || 1
            "10.0"    || 0
            "0.01"    || 0
    }

    def "should put only fixed number of coins to machine"(){
        given:
            def coin = new Coin(new BigDecimal("5.0"))
        when:
            machine.insertProduct(ProductLocation.FIRST_FLOOR_FIRST_PLACE, product)
            machine.chooseProductToBuy(ProductLocation.FIRST_FLOOR_FIRST_PLACE)
            numberOfCoinInsertion.times {machine.insertCoin(coin)}
        then:
            machine.coinsInMachine.get(AcceptableCoinUnit.FIVE_ZLOTY) == expectedSize
        where:
            numberOfCoinInsertion || expectedSize
            3                     || 3
            50                    || 50
            60                    || 50
    }

    def "should add product"(){
        when:
            machine.insertProduct(ProductLocation.FIRST_FLOOR_FIRST_PLACE, product)
        then:
            machine.products.size() == 1
    }

    def "should not add product with different category on same floor"(){
        given:
            def product2 = new Product("Mars",new BigDecimal("2.0"),ProductCategory.SNACK)
        when:
            machine.insertProduct(ProductLocation.FIRST_FLOOR_FIRST_PLACE, product)
            machine.insertProduct(ProductLocation.FIRST_FLOOR_SECOND_PLACE, product)
            machine.insertProduct(ProductLocation.FIRST_FLOOR_THIRD_PLACE, product2)
            machine.insertProduct(ProductLocation.SECOND_FLOOR_SECOND_PLACE, product2)
            machine.insertProduct(ProductLocation.SECOND_FLOOR_FOURTH_PLACE, product)
        then:
            machine.products.size() == 3
    }

    def "should not insert more than one product to the same location"(){
        when:
            5.times{machine.insertProduct(ProductLocation.FIRST_FLOOR_FIRST_PLACE,product)}
        then:
            machine.products.size() == 1
    }

    def "should display price for product"(){
        given:
            def display = Mock(Display)
            def machine = new VendingMachine(display, new ChangeCalculator())
            def location = ProductLocation.FIRST_FLOOR_FIRST_PLACE
        when:
            machine.insertProduct(location, product)
            machine.showProductPrice(location)
        then:
            1 * display.log("Coca-cola price is 2.0 zlotych.")
    }

    def "should choose product to buy"(){
        given:
            def location = ProductLocation.FIRST_FLOOR_FIRST_PLACE
        when:
            machine.insertProduct(location, product)
            machine.chooseProductToBuy(locationChosen)
        then:
            machine.chosenProductLocation.isPresent() == productHasBeenChosen
        where:
            locationChosen                          || productHasBeenChosen
            ProductLocation.FIRST_FLOOR_FIRST_PLACE || true
            ProductLocation.FIRST_FLOOR_SECOND_PLACE|| false
    }

    def "should buy a product"(){
        given:
            def coin = new Coin(new BigDecimal(coinValue))
            def location = ProductLocation.FIRST_FLOOR_FIRST_PLACE
        when:
            machine.insertProduct(location, product)
            machine.chooseProductToBuy(location)
            machine.insertCoin(coin)
        then:
            machine.products.size() == productSize
        where:
            coinValue || productSize
            "1.0"     || 1
            "0.5"     || 1
            "0.2"     || 1
            "2.0"     || 0
    }

}