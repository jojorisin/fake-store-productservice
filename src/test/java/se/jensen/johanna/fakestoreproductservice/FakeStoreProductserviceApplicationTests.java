package se.jensen.johanna.fakestoreproductservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class FakeStoreProductserviceApplicationTests {

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @Test
  void contextLoads() {
  }

}
