package ru.pr1nkos.webrisetest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.webrisetest.dto.SubscriptionCountDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.model.User;
import ru.pr1nkos.webrisetest.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Поиск подписки по имени
     */
    @Transactional
    public Page<Subscription> findAllSubscriptionsByUserId(Pageable pageable, User user) {
        log.debug("Fetching subscriptions for user ID: {} with page={} and size={}",
                user.getId(), pageable.getPageNumber(), pageable.getPageSize());

        Page<Subscription> subscriptions = subscriptionRepository.findAllByUserId(pageable, user);
        log.info("Fetched {} subscriptions for user {}", subscriptions.getNumberOfElements(), user.getId());

        return subscriptions;
    }

    /**
     * Удаление подписки по названию и пользователю
     */
    @Transactional
    public void deleteByName(String subscriptionName, User user) {
        log.info("Deleting subscription '{}' for user ID: {}", subscriptionName, user.getId());
        subscriptionRepository.deleteByNameAndUserId(subscriptionName, user);
    }

    /**
     * Получение топ подписок
     */
    @Transactional
    public Page<SubscriptionCountDto> findTopSubscriptions(Pageable pageable) {
        log.debug("Fetching top subscriptions with page={} and size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<SubscriptionCountDto> topSubscriptions = subscriptionRepository.findTopSubscriptions(pageable);
            log.info("Fetched {} top subscriptions", topSubscriptions.getNumberOfElements());
            return topSubscriptions;
        } catch (Exception e) {
            log.error("Error occurred while fetching top subscriptions: {}", e.getMessage(), e);
            throw e;
        }
    }
}
