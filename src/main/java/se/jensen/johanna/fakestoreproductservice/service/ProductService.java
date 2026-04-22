package se.jensen.johanna.fakestoreproductservice.service;


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
  private String fakeStoreurl;
  private final ProductMapper productMapper;
  private final ProductRepository productRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  @Transactional
  public void syncProducts() {
    log.info("Syncing products from fake store");
    int syncedProducts = 1;
    ProductDTO[] products = restTemplate.getForObject(fakeStoreurl, ProductDTO[].class);
    if (products != null) {
      for (ProductDTO product : products) {
        if (!productRepository.existsById(product.id())) {
          productRepository.save(productMapper.toProduct(product));
          syncedProducts++;
        }
      }

    }
    log.info("Synced {} products", syncedProducts);

  }

  @Transactional(readOnly = true)
  public Page<ProductDTO> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable).map(productMapper::toProductDTO);
  }

  @Transactional
  public ProductDTO updateProduct(Long productId, UpdateProductRequest request) {
    Product product = getProductOrThrow(productId);
    if (request.title() != null && !request.title().isBlank()) {
      product.updateTitle(request.title());
    }
    if (request.description() != null && !request.description().isBlank()) {
      product.updateDescription(request.description());
    }
    if (request.price() != null) {
      product.updatePrice(request.price());
    }
    if (request.image() != null && !request.image().isBlank()) {
      product.updateImage(request.image());
    }
    return productMapper.toProductDTO(productRepository.save(product));

  }

  public ProductDTO getProductById(Long productId) {
    return productMapper.toProductDTO(
        productRepository.findById(productId).orElseThrow(IllegalArgumentException::new));
  }

  public void deleteProductById(Long productId) {
    productRepository.deleteById(productId);
  }

  private Product getProductOrThrow(Long productId) {
    return productRepository.findById(productId).orElseThrow(IllegalArgumentException::new);
  }
}

