package ru.pr1nkos.webrisetest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.webrisetest.dto.SubscriptionCountDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.model.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findAllByUserId(Pageable pageable, User user);

    void deleteByNameAndUserId(String name, User user);

    @Query("SELECT new ru.pr1nkos.webrisetest.dto.SubscriptionCountDto(s.name, COUNT(s.name)) " +
            "FROM Subscription s " +
            "GROUP BY s.name " +
            "ORDER BY COUNT(s.name) DESC")
    Page<SubscriptionCountDto> findTopSubscriptions(Pageable pageable);
}
