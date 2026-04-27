package se.jensen.johanna.fakestoreproductservice.dto;

import java.util.UUID;

public record CartItemRequest(
    UUID productId,
    Integer quantity
) {

}
