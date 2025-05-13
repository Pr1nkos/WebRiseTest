package ru.pr1nkos.webrisetest.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pr1nkos.webrisetest.dto.SubscriptionCountDto;
import ru.pr1nkos.webrisetest.page.SubscriptionsPage;
import ru.pr1nkos.webrisetest.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<SubscriptionsPage> findTopSubscriptions(@RequestParam int page, @RequestParam int size) {
        log.debug("Fetching top subscriptions with page={} and size={}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SubscriptionCountDto> subscriptions = subscriptionService.findTopSubscriptions(pageable);

            log.info("Fetched {} top subscriptions for page {}", subscriptions.getNumberOfElements(), page);

            SubscriptionsPage response = SubscriptionsPage.builder()
                    .subscriptions(subscriptions.getContent())
                    .currentPage(subscriptions.getNumber())
                    .totalPages(subscriptions.getTotalPages())
                    .totalElements(subscriptions.getTotalElements())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching top subscriptions: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
