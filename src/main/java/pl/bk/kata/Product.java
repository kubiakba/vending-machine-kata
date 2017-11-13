package pl.bk.kata;

import java.math.BigDecimal;

class Product {

    private final ProductCategory productCategory;
    private final String name;
    private final BigDecimal price;

    public Product(String name, BigDecimal price, ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.name = name;
        this.price = price;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
