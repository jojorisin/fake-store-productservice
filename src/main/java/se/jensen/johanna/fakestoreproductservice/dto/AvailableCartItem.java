package se.jensen.johanna.fakestoreproductservice.dto;

import se.jensen.johanna.fakestoreproductservice.model.Product;

public record AvailableCartItem(
    Product product,
    Integer quantity
) {

}
