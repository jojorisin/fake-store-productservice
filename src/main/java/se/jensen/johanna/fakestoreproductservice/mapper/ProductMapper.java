package se.jensen.johanna.fakestoreproductservice.mapper;

import org.mapstruct.Mapper;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toProduct(ProductDTO productDTO);

  ProductDTO toProductDTO(Product product);

}