package se.jensen.johanna.fakestoreproductservice.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.johanna.fakestoreproductservice.dto.AvailabilityResponse;
import se.jensen.johanna.fakestoreproductservice.dto.CartItemRequest;
import se.jensen.johanna.fakestoreproductservice.dto.ReservationRequest;
import se.jensen.johanna.fakestoreproductservice.dto.ReservationResponse;
import se.jensen.johanna.fakestoreproductservice.mapper.ReservationMapper;
import se.jensen.johanna.fakestoreproductservice.model.Product;
import se.jensen.johanna.fakestoreproductservice.model.Reservation;
import se.jensen.johanna.fakestoreproductservice.model.ReservationItem;
import se.jensen.johanna.fakestoreproductservice.repository.ProductRepository;
import se.jensen.johanna.fakestoreproductservice.repository.ProductRepository.StockProjection;
import se.jensen.johanna.fakestoreproductservice.repository.ReservationRepository;
import se.jensen.johanna.fakestoreproductservice.repository.ReservationRepository.ReservationCountProjection;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ProductRepository productRepository;
  private final ReservationMapper reservationMapper;

  /**
   * For when orderservice is creating an order. They reserve the cart and gets the reservationID so
   * later it's easy to update the whole stock. Unsure of this flow because they are very dependant
   * on eachother. If productservice can't respond, will you cancel the order or continue with the
   * order and trust the previous check? Don't overthink this...
   */
  @Transactional
  public ReservationResponse reserveCart(Jwt jwt, ReservationRequest request) {
    UUID userId = UUID.fromString(jwt.getSubject());
    AvailabilityResponse response = checkCartAvailability(request.cartItemRequests());
    if (!response.allAvailable()) {
      throw new IllegalArgumentException("Stock is not available.");
    }

    Set<UUID> productIds = request.cartItemRequests().stream().map(CartItemRequest::productId)
        .collect(Collectors.toSet());
    List<Product> products = productRepository.findAllByProductIdIn(productIds);

    Map<UUID, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getProductId, p -> p));
    List<ReservationItem> reservationItems = request.cartItemRequests().stream()
        .map(cartItem -> ReservationItem.create(
            productMap.get(cartItem.productId()),
            cartItem.quantity()
        ))
        .toList();

    Reservation reservation = Reservation.reserve(reservationItems, userId);
    reservationRepository.save(reservation);
    return new ReservationResponse(reservation.getReservationId());

  }

  /*
   * Purpose is to give a quich check of available stock when customer goes to check cart.
   * If customer wants two t-shirts but only one is available it updates the quantity and frontend will have to notify
   * */
  public AvailabilityResponse checkCartAvailability(Set<CartItemRequest> cartItemRequests) {
    Set<UUID> productIds = cartItemRequests.stream()
        .map(CartItemRequest::productId)
        .collect(Collectors.toSet());

    Map<UUID, Integer> availableStock = getAvailableStock(productIds);
    Set<CartItemRequest> updatedItems = new HashSet<>();
    boolean allAvailable = true;
    for (CartItemRequest item : cartItemRequests) {
      int available = availableStock.getOrDefault(item.productId(), 0);
      if (available < item.quantity()) {
        allAvailable = false;
        updatedItems.add(new CartItemRequest(item.productId(), item.quantity()));
      } else {
        updatedItems.add(item);
      }
    }
    return new AvailabilityResponse(updatedItems, allAvailable);
  }

  /*
  Returns a map of productid and CURRENT available stock, stock - reserved.
  */

  private Map<UUID, Integer> getAvailableStock(Set<UUID> productIds) {
    Map<UUID, Integer> stock = productRepository.getStockByProductIds(productIds)
        .stream()
        .collect(Collectors.toMap(StockProjection::getProductId, StockProjection::getStock));

    Map<UUID, Integer> reserved = reservationRepository.countActiveReservationsByProductIds(
        productIds,
        Instant.now()).stream().collect(Collectors.toMap(ReservationCountProjection::getProductId,
        ReservationCountProjection::getCount));

    return productIds.stream().collect(Collectors.toMap(
        id -> id,
        id -> stock.getOrDefault(id, 0) - reserved.getOrDefault(id, 0)
    ));
  }


}
