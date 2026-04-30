package se.jensen.johanna.fakestoreproductservice.dto;

import java.util.Set;

public record AvailabilityRequest(
    Set<CartItemRequest> cartItemRequests
) {

}
