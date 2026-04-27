package se.jensen.johanna.fakestoreproductservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID reservationItemId;

  @ManyToOne
  private Product product;

  private Integer quantity;

  public static ReservationItem create(Product product, Integer quantity) {
    return ReservationItem.builder()
        .product(product)
        .quantity(quantity)
        .build();
  }

}
