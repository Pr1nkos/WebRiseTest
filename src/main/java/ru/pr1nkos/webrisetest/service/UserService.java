package ru.pr1nkos.webrisetest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;
import ru.pr1nkos.webrisetest.dto.UserDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.model.User;
import ru.pr1nkos.webrisetest.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    /**
     * Создание нового пользователя
     */
    @Transactional
    public void createUser(String username, String password) {
        log.info("Creating new user with username: {}", username);
        User user = User.builder()
                .name(username)
                .password(password)
                .build();

        userRepository.save(user);
        log.info("User {} created successfully with ID: {}", username, user.getId());
    }

    /**
     * Поиск пользователя по ID
     */
    @Transactional(readOnly = true)
    public UserDto findUserById(long id) {
        log.debug("Fetching user by ID: {}", id);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            log.info("User with ID {} found", id);
            return UserDto.builder()
                    .username(user.get().getName())
                    .password(user.get().getPassword())
                    .subscriptions(user.get().getSubscriptions())
                    .build();
        } else {
            log.warn("User with ID {} not found", id);
            return null;
        }
    }

    /**
     * Обновление пароля пользователя
     */
    @Transactional
    public void updatePassword(Long id, String password) {
        log.info("Updating password for user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userRepository.save(user.get());
            log.info("Password updated successfully for user ID: {}", id);
        } else {
            log.warn("User with ID {} not found during password update", id);
        }
    }

    /**
     * Обновление имени пользователя
     */
    @Transactional
    public void updateUsername(Long id, String username) {
        log.info("Updating username for user ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setName(username);
            userRepository.save(user.get());
            log.info("Username changed to '{}' for user ID: {}", username, id);
        } else {
            log.warn("User with ID {} not found during username update", id);
        }
    }

    /**
     * Удаление пользователя по ID
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User with ID {} does not exist and cannot be deleted", id);
            return;
        }

        userRepository.deleteById(id);
        log.info("User with ID {} deleted successfully", id);
    }

    /**
     * Удаление подписки у пользователя
     */
    @Transactional
    public void deleteSubscription(String subscriptionName, Long userId) {
        log.info("Deleting subscription '{}' for user ID: {}", subscriptionName, userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            subscriptionService.deleteByName(subscriptionName, user.get());
            log.info("Subscription '{}' deleted from user {}", subscriptionName, userId);
        } else {
            log.warn("User with ID {} not found while deleting subscription '{}'", userId, subscriptionName);
        }
    }

    /**
     * Добавление подписки пользователю
     */
    @Transactional
    public void addSubscription(Long userId, String subscriptionName) {
        log.info("Adding subscription '{}' to user ID: {}", subscriptionName, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });

        Subscription subscription = Subscription.builder()
                .name(subscriptionName)
                .userId(user)
                .build();

        user.getSubscriptions().add(subscription);
        userRepository.save(user);
        log.info("Subscription '{}' added to user ID: {}", subscriptionName, userId);
    }

    /**
     * Получение всех подписок пользователя
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionDto> findAllSubscriptions(Pageable pageable, Long userId) {
        log.debug("Fetching subscriptions for user ID: {}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });

        Page<Subscription> subscriptions = subscriptionService.findAllSubscriptionsByUserId(pageable, user);

        log.info("Fetched {} subscriptions for user ID: {}", subscriptions.getNumberOfElements(), userId);

        return subscriptions.map(subscription -> SubscriptionDto.builder()
                .name(subscription.getName())
                .userId(userId)
                .build());
    }
}