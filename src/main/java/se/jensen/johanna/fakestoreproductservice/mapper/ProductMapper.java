package se.jensen.johanna.fakestoreproductservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.dto.ProductSyncDTO;
import se.jensen.johanna.fakestoreproductservice.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  /*@Mapping(target = "externalId", source = "id")
  Product toProduct(ProductSyncDTO productSyncDTO);*/

  @Mapping(target = "productId", source = "productId")
  ProductDTO toProductDTO(Product product);


  default Product toProduct(ProductSyncDTO dto) {
    return Product.create(dto.id(), dto.title(), dto.price(), dto.description(), dto.image());

  }

}