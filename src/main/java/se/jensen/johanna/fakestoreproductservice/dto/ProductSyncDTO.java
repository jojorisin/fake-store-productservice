package se.jensen.johanna.fakestoreproductservice.dto;

public record ProductSyncDTO(
    Long id,
    String title,
    Integer price,
    String description,
    String image

) {

}
