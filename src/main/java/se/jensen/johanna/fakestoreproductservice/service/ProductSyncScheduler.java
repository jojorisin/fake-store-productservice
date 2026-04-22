package se.jensen.johanna.fakestoreproductservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSyncScheduler {

  private final ProductService productService;

  @Scheduled(cron = "${product.sync.cron}")
  public void scheduledSync() {
    log.info("Triggering scheduled sync...");
    productService.syncProducts();

  }

  @EventListener(ApplicationReadyEvent.class)
  public void startupSync() {
    log.info("Triggering startup sync...");
    productService.syncProducts();
  }

}
