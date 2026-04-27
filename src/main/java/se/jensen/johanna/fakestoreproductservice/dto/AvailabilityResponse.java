package se.jensen.johanna.fakestoreproductservice.dto;

import java.util.Set;

public record AvailabilityResponse(
    Set<CartItemRequest> updatedCart,
    Boolean allAvailable
) {

}
