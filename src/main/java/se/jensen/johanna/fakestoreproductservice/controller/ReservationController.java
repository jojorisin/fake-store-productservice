package se.jensen.johanna.fakestoreproductservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestoreproductservice.dto.ReservationRequest;
import se.jensen.johanna.fakestoreproductservice.dto.ReservationResponse;
import se.jensen.johanna.fakestoreproductservice.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping("/reserve-cart")
  public ResponseEntity<ReservationResponse> reserveCart(@AuthenticationPrincipal Jwt jwt,
      @RequestBody @Valid ReservationRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(reservationService.reserveCart(jwt, request));
  }


}
