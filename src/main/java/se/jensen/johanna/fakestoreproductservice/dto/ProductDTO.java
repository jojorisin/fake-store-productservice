package se.jensen.johanna.fakestoreproductservice.dto;

public record ProductDTO(
    Long id,
    String title,
    Integer price,
    String description,
    String image

) {

}
