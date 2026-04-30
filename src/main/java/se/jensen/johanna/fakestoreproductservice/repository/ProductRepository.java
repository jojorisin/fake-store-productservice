package se.jensen.johanna.fakestoreproductservice.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.jensen.johanna.fakestoreproductservice.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("SELECT p FROM Product p WHERE " +
      "(:query IS NULL OR :query = '' OR " +
      "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
      "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
  Page<Product> findAll(String query, Pageable pageable);

  @Query("SELECT p.externalId FROM Product p")
  Set<Long> findAllExternalIds();

  @Query("SELECT p.stock FROM Product p WHERE p.productId = :productId")
  Integer getStockByProductId(UUID productId);

  @Query("SELECT p FROM Product p WHERE p.productId = :productId")
  Optional<Product> findByProductId(UUID productId);

  List<Product> findAllByProductIdIn(Set<UUID> productIds);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.productId = :productId")
  Optional<Product> findForStockUpdate(UUID productId);

  @Query("SELECT p.productId as productId, p.stock as stock FROM Product p WHERE p.productId IN :productIds")
  List<StockProjection> getStockByProductIds(@Param("productIds") Set<UUID> productIds);

  interface StockProjection {

    UUID getProductId();

    Integer getStock();
  }

}
