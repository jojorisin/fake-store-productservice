package se.jensen.johanna.fakestoreproductservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

  @Id
  @GeneratedValue
  @EqualsAndHashCode.Include
  private UUID productId;

  private Long externalId;
  private String title;
  private Integer price;
  @Column(columnDefinition = "TEXT")
  private String description;
  @Column(columnDefinition = "TEXT")
  private String image;
  private Integer stock;
  private Instant createdAt;
  private Instant updatedAt;


  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }


  public static Product create(Long externalId, String title, Integer price, String description,
      String image) {
    return Product.builder()
        .externalId(externalId)
        .title(title)
        .price(price)
        .description(description)
        .image(image)
        .stock(10)
        .build();
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updatePrice(Integer price) {
    this.price = price;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void updateImage(String image) {
    this.image = image;
  }

}

