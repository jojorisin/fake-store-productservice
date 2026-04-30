package se.jensen.johanna.fakestoreproductservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.jensen.johanna.fakestoreproductservice.model.ReservationItem;

@Repository
public interface ReservationItemRepository extends JpaRepository<ReservationItem, UUID> {


}
