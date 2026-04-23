package se.jensen.johanna.fakestoreproductservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.dto.UpdateProductRequest;
import se.jensen.johanna.fakestoreproductservice.mapper.ProductMapper;
import se.jensen.johanna.fakestoreproductservice.model.Product;
import se.jensen.johanna.fakestoreproductservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  private ProductService productService;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private RestTemplate restTemplate;
  @Mock
  private ProductMapper productMapper;

  private Long productId;
  private Product product;
  private ProductDTO productDTO;

  @BeforeEach
  void setUp() {
    productId = 1L;
    product = Product.builder().id(productId).title("original title")
        .description("original description").price(100).image("original image").build();
    productDTO = new ProductDTO(productId, "title", 100, "description", "image");
  }


  @Test
  void getAllProducts_WithSearchQuery() {
    String search = "original";
    Pageable pageable = PageRequest.of(0, 10);
    Page<Product> productPage = new PageImpl<>(List.of(product));

    when(productRepository.findAll(eq(search), eq(pageable))).thenReturn(productPage);
    when(productMapper.toProductDTO(product)).thenReturn(productDTO);

    Page<ProductDTO> result = productService.getAllProducts(search, pageable);

    assertThat(result.getContent()).hasSize(1);
    verify(productRepository).findAll(search, pageable);
  }

  @Test
  void updateProduct_ShouldUpdateOnlyTitleWhenOnlyTitleIsProvided() {
    UpdateProductRequest request = new UpdateProductRequest("New Title", null, null, null);
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    productService.updateProduct(productId, request);

    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    verify(productRepository).save(captor.capture());
    Product saved = captor.getValue();
    assertThat(saved.getTitle()).isEqualTo("New Title");
    assertThat(saved.getPrice()).isEqualTo(product.getPrice());
    assertThat(saved.getDescription()).isEqualTo(product.getDescription());
    assertThat(saved.getImage()).isEqualTo(product.getImage());
  }

  @Test
  void updateProduct_ShouldUpdateAllFieldsWhenAllFieldsAreProvided() {
    UpdateProductRequest request = new UpdateProductRequest("New Title", "New Description", 200,
        "New Image");
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    productService.updateProduct(productId, request);

    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    verify(productRepository).save(captor.capture());
    Product saved = captor.getValue();
    assertThat(saved.getTitle()).isEqualTo("New Title");
    assertThat(saved.getPrice()).isEqualTo(200);
    assertThat(saved.getDescription()).isEqualTo("New Description");
    assertThat(saved.getImage()).isEqualTo("New Image");
  }

  @Test
  void updateProduct_shouldNotUpdateWhenRequestValuesAreNullOrEmpty() {
    UpdateProductRequest request = new UpdateProductRequest("", "", null, null);
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    productService.updateProduct(productId, request);

    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    verify(productRepository).save(captor.capture());
    Product saved = captor.getValue();
    assertThat(saved.getTitle()).isEqualTo("original title");
    assertThat(saved.getPrice()).isEqualTo(100);
    assertThat(saved.getDescription()).isEqualTo("original description");
    assertThat(saved.getImage()).isEqualTo("original image");

  }

  @Test
  void getProductById_ShouldReturnProductWhenExists() {
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(productMapper.toProductDTO(product)).thenReturn(productDTO);

    ProductDTO result = productService.getProductById(productId);

    assertThat(result.id()).isEqualTo(productId);
  }

  @Test
  void getProductById_ShouldThrowExceptionWhenProductNotFound() {
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> productService.getProductById(productId));
  }

  @Test
  void deleteProduct() {
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    productService.deleteProduct(productId);

    verify(productRepository).delete(product);
  }
}