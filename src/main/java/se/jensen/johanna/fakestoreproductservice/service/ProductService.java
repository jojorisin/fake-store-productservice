package se.jensen.johanna.fakestoreproductservice.service;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.dto.UpdateProductRequest;
import se.jensen.johanna.fakestoreproductservice.mapper.ProductMapper;
import se.jensen.johanna.fakestoreproductservice.model.Product;
import se.jensen.johanna.fakestoreproductservice.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  @Value("${fake-store-url}")
  private String fakeStoreUrl;
  private final ProductMapper productMapper;
  private final ProductRepository productRepository;
  private final RestTemplate restTemplate;

  @Transactional
  public void syncProducts() {
    log.info("Syncing products from fake store");

    try {
      ProductDTO[] remoteProducts = restTemplate.getForObject(fakeStoreUrl, ProductDTO[].class);
      if (remoteProducts == null) {
        log.warn("Product sync aborted: Returned null from {}", fakeStoreUrl);
        return;
      }
      if (remoteProducts.length == 0) {
        log.info("Product sync skipped. No products returned from {}.", fakeStoreUrl);
        return;
      }

      Set<Long> existingIds = productRepository.findAllIds();
      List<Product> newProducts = Arrays.stream(remoteProducts)
          .filter(remote -> !existingIds.contains(remote.id())).map(productMapper::toProduct)
          .toList();

      if (!newProducts.isEmpty()) {
        productRepository.saveAll(newProducts);
        log.info("Successfully synced {} new products from {}", newProducts.size(), fakeStoreUrl);
      } else {
        log.info("No new unique products found in {}", fakeStoreUrl);
      }

    } catch (Exception e) {
      log.error("Failed to sync products from fake store {}", e.getMessage(), e);
    }
  }

  @Transactional(readOnly = true)
  public Page<ProductDTO> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable).map(productMapper::toProductDTO);
  }

  @Transactional
  public ProductDTO updateProduct(Long productId, UpdateProductRequest request) {
    Product product = getProductOrThrow(productId);
    log.info("Updating product {}, request: {}", productId, request);

    if (request.title() != null && !request.title().isBlank()) {
      product.updateTitle(request.title());
      log.info("Product {} updated with title {}", productId, request.title());
    }
    if (request.description() != null && !request.description().isBlank()) {
      product.updateDescription(request.description());
      log.info("Product {} updated with description {}", productId, request.description());
    }
    if (request.price() != null) {
      log.info("Updating price: current price {}, product {}", product.getPrice(), productId);
      product.updatePrice(request.price());
      log.info("Product {} updated with price {}", productId, request.price());
    }
    if (request.image() != null && !request.image().isBlank()) {
      product.updateImage(request.image());
      log.info("Product {} updated with image {}", productId, request.image());
    }
    productRepository.save(product);
    return productMapper.toProductDTO(product);
  }

  public ProductDTO getProductById(Long productId) {
    return productMapper.toProductDTO(
        productRepository.findById(productId).orElseThrow(IllegalArgumentException::new));
  }

  public void deleteProduct(Long productId) {
    log.info("Deleting product {}", productId);
    Product product = getProductOrThrow(productId);
    productRepository.delete(product);
    log.info("Product {} deleted", productId);
  }

  private Product getProductOrThrow(Long productId) {
    return productRepository.findById(productId).orElseThrow(() -> {
      log.error("Product {} not found", productId);
      return new IllegalArgumentException("Product not found");
    });
  }
}

