package se.jensen.johanna.fakestoreproductservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.dto.ProductSyncDTO;
import se.jensen.johanna.fakestoreproductservice.model.Product;

class ProductMapperTest {

  private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

  @Test
  void toProduct() {
    ProductSyncDTO dto = new ProductSyncDTO(1L, "title", 100, "description", "image");

    Product product = mapper.toProduct(dto);

    assertThat(product).isNotNull();
    assertThat(product.getExternalId()).isEqualTo(dto.id());
    assertThat(product.getTitle()).isEqualTo(dto.title());
    assertThat(product.getPrice()).isEqualTo(dto.price());
    assertThat(product.getDescription()).isEqualTo(dto.description());
    assertThat(product.getImage()).isEqualTo(dto.image());

  }

  @Test
  void toProductDTO() {
    Product product = Product.builder().productId(UUID.randomUUID()).title("title").price(100)
        .description("description")
        .image("image").build();

    ProductDTO dto = mapper.toProductDTO(product);

    assertThat(dto).isNotNull();
    assertThat(dto.productId()).isEqualTo(product.getProductId());
    assertThat(dto.title()).isEqualTo(product.getTitle());
    assertThat(dto.price()).isEqualTo(product.getPrice());
    assertThat(dto.description()).isEqualTo(product.getDescription());
    assertThat(dto.image()).isEqualTo(product.getImage());

  }
}