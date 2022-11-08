package com.food.ordering.system.entity;

import com.ordering.system.entity.BaseEntity;
import com.ordering.system.valueobject.Money;
import com.ordering.system.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    //Reduce time complexity here
    public void updateAndConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
