package pl.bk.kata

import spock.lang.Specification

class ChangeCalculatorSpecification extends Specification {

    def "should return change"(){
        given:
            def changeCalculator = new ChangeCalculator()
            def coins = new HashMap<>()
            def change = new BigDecimal("0.8")
        when:
            coins.put(AcceptableCoinUnit.FIVE_ZLOTY,50)
            coins.put(AcceptableCoinUnit.TWENTY_GROSZY,4)
            coins.put(AcceptableCoinUnit.FIFTY_GROSZY,50)
            coins.put(AcceptableCoinUnit.ONE_ZLOTY,50)
            coins.put(AcceptableCoinUnit.TWO_ZLOTY,50)
            def result = changeCalculator.returnChange(coins,change)
        then:
            result.isPresent()
        where:
            i << (1..10)
    }
}
