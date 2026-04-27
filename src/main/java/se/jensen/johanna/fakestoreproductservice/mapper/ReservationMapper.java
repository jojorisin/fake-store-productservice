package se.jensen.johanna.fakestoreproductservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.jensen.johanna.fakestoreproductservice.dto.AvailableCartItem;
import se.jensen.johanna.fakestoreproductservice.dto.CartItemRequest;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  @Mapping(target = "productId", source = "product.productId")
  CartItemRequest toCartItem(AvailableCartItem availableCartItem);

}
