package se.jensen.johanna.fakestoreproductservice.repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.jensen.johanna.fakestoreproductservice.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

  @Query("SELECT ri.product.productId as productId, SUM(ri.quantity) as count " +
      "FROM Reservation r JOIN r.reservedItems ri " +
      "WHERE ri.product.productId IN :productIds " +
      "AND r.expiresAt > :now " +
      "GROUP BY ri.product.productId")
  List<ReservationCountProjection> countActiveReservationsByProductIds(
      @Param("productIds") Set<UUID> productIds,
      @Param("now") Instant now
  );

  interface ReservationCountProjection {

    UUID getProductId();

    Integer getCount();
  }


}
