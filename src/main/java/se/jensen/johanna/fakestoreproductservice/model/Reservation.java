package se.jensen.johanna.fakestoreproductservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Reservation {

  @Id
  @GeneratedValue
  private UUID reservationId;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "reservation_id")
  private List<ReservationItem> reservedItems;

  @NotNull
  private UUID userId;

  private Instant reservedAt;

  @NotNull
  private Instant expiresAt;

  @PrePersist
  protected void onCreate() {
    this.reservedAt = Instant.now();
  }

  public static Reservation reserve(List<ReservationItem> reservedItems, UUID userId) {
    return Reservation.builder()
        .userId(userId)
        .reservedItems(reservedItems)
        .reservedAt(Instant.now())
        .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
        .build();

  }


}
