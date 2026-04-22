package se.jensen.johanna.fakestoreproductservice.dto;

import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(
    String title,
    String description,
    @Positive(message = "Price must be over 0.")
    Integer price,
    String image
) {

}
