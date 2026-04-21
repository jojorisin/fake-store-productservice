package se.jensen.johanna.fakestoreproductservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestoreproductservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreproductservice.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<Page<ProductDTO>> getAllProducts(
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(productService.getAllProducts(pageable));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDTO> getProductById(Long productId) {
    return ResponseEntity.ok(productService.getProductById(productId));
  }

  @PostMapping("/sync")
  public ResponseEntity<Void> syncProducts() {
    productService.syncProducts();
    return ResponseEntity.ok().build();
  }

}

