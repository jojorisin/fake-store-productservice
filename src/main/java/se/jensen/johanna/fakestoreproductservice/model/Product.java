package se.jensen.johanna.fakestoreproductservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {

  @Id
  private Long id;
  private String title;
  private Integer price;
  @Column(columnDefinition = "TEXT")
  private String description;
  @Column(columnDefinition = "TEXT")
  private String image;
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

}

