package ru.pr1nkos.webrisetest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.repository.SubscriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription findSubscriptionByName(String subscriptionName) {
        Optional<Subscription> subscription = subscriptionRepository.findByName(subscriptionName);
        return subscription.orElse(null);
    }

    public List<Subscription> findAllSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findAllByUserId(userId);
    }

    public void deleteByName(String subscruptionName, Long userId) {
        subscriptionRepository.deleteByNameAndUserId(subscruptionName, userId);
    }

    public Page<SubscriptionDto> findTopSubscriptions(Pageable pageable) {
        Page<Subscription> topSubscriptions = subscriptionRepository.findTopSubscriptions(pageable);
        return topSubscriptions.map(subscription -> SubscriptionDto.builder()
                .userId(subscription.getUserId().getId())
                .name(subscription.getName())
                .build());
    }
}
