package ru.pr1nkos.webrisetest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;
import ru.pr1nkos.webrisetest.dto.UserDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.model.User;
import ru.pr1nkos.webrisetest.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    public void createUser(UserDto userDto, String password) {
        userRepository.save(User.builder()
                .name(userDto.username())
                .password(password)
                .build());
    }

    public UserDto findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> UserDto.builder()
                .password(value.getPassword())
                .username(value.getName())
                .subscriptions(value.getSubscriptions())
                .build()).orElse(null);
    }

    public void updatePassword(Long id, String password) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userRepository.save(user.get());
        }
    }

    public void updateUsername(Long id, String username) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setName(username);
            userRepository.save(user.get());
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteSubscription(String subscriptionName, Long userId) {
        subscriptionService.deleteByName(subscriptionName, userId);
    }

    public void addSubscription(Long userId, String subscriptionName) {
        Subscription subscriptionById = subscriptionService.findSubscriptionByName(subscriptionName);
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> value.getSubscriptions().add(subscriptionById));
    }

    public List<SubscriptionDto> findAllSubscriptions(Long userId) {
        List<Subscription> subscriptions = subscriptionService.findAllSubscriptionsByUserId(userId);
        return subscriptions.stream().map(subscription -> SubscriptionDto.builder()
                .userId(subscription.getUserId().getId())
                .name(subscription.getName())
                .build()).toList();
    }
}
