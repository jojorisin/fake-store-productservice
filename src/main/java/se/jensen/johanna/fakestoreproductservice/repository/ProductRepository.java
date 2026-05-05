package se.jensen.johanna.fakestoreproductservice.repository;

import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
