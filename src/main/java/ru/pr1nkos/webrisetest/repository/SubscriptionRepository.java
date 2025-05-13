package ru.pr1nkos.webrisetest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.model.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByName(String subscriptionName);

    List<Subscription> findAllByUserId(Long userId);

    void deleteByNameAndUserId(String subscruptionName, Long userId);

    Optional<Subscription> findByNameAndUserId(String name, User userId);

    @Query("SELECT s.name, COUNT(s.name) as count " +
            "FROM Subscription s " +
            "GROUP BY s.name " +
            "ORDER BY count DESC ")
    Page<Subscription> findTopSubscriptions(Pageable pageable);
}
