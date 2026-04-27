package se.jensen.johanna.fakestoreproductservice.dto;

import java.util.Set;

public record ReservationRequest(
    Set<CartItemRequest> cartItemRequests
) {

}
