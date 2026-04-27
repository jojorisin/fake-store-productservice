package se.jensen.johanna.fakestoreproductservice.mapper;

import org.mapstruct.Mapper;
import se.jensen.johanna.fakestoreproductservice.model.Product;
import se.jensen.johanna.fakestoreproductservice.model.ReservationItem;

@Mapper(componentModel = "spring")
public interface ReservationItemMapper {

  default ReservationItem toReservationItem(Product product, Integer quantity) {
    return ReservationItem.create(product, quantity);
  }

}
