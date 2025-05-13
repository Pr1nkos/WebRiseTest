package ru.pr1nkos.webrisetest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;
import ru.pr1nkos.webrisetest.dto.UserDto;
import ru.pr1nkos.webrisetest.model.Subscription;
import ru.pr1nkos.webrisetest.page.SubscriptionsPage;
import ru.pr1nkos.webrisetest.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        log.info("Creating user with username: {}", username);
        try {
            userService.createUser(username, password);
            log.info("User {} created successfully", username);
            return new ResponseEntity<>(String.format("User %s created successfully", username), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating user {}: {}", username, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        log.debug("Fetching user by ID: {}", id);
        UserDto userById = userService.findUserById(id);
        if (userById != null) {
            log.info("User with ID {} found", id);
            return new ResponseEntity<>(userById, HttpStatus.OK);
        }
        log.warn("User with ID {} not found", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/update/password")
    public ResponseEntity<String> updatePassword(@PathVariable long id, @RequestParam String password) {
        log.info("Updating password for user ID: {}", id);
        try {
            userService.updatePassword(id, password);
            log.info("Password for user {} updated successfully", id);
            return new ResponseEntity<>(String.format("Password successfully changed on: %s", password), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update password for user {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/update/username")
    public ResponseEntity<String> updateUsername(@PathVariable long id, @RequestParam String username) {
        log.info("Updating username for user ID: {}", id);
        try {
            userService.updateUsername(id, username);
            log.info("Username for user {} updated to {}", id, username);
            return new ResponseEntity<>(String.format("Username successfully changed on: %s", username), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update username for user {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUser(id);
            log.info("User {} deleted successfully", id);
            return new ResponseEntity<>(String.format("User %s successfully deleted", id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete user {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/subscriptions/add")
    public ResponseEntity<String> addSubscription(@PathVariable long id, @RequestParam String subscription) {
        log.debug("Checking if user {} already has subscription '{}'", id, subscription);
        UserDto userById = userService.findUserById(id);
        if (userById.subscriptions().stream().map(Subscription::getName).anyMatch(subscription::equals)) {
            log.warn("User {} already has subscription '{}'", id, subscription);
            return new ResponseEntity<>(String.format("Subscription %s already exists", subscription), HttpStatus.CONFLICT);
        }
        log.info("Adding subscription '{}' to user {}", subscription, id);
        userService.addSubscription(id, subscription);
        log.info("Subscription '{}' added successfully to user {}", subscription, id);
        return new ResponseEntity<>(String.format("Subscription %s successfully added", subscription), HttpStatus.CREATED);
    }


    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<SubscriptionsPage> getSubscriptions(@RequestParam int page, @RequestParam int size, @PathVariable long id) {
        log.debug("Fetching subscriptions for user {} with page={} and size={}", id, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SubscriptionDto> subscriptions = userService.findAllSubscriptions(pageable, id);
            SubscriptionsPage response = SubscriptionsPage.builder()
                    .subscriptions(subscriptions.getContent())
                    .currentPage(subscriptions.getNumber())
                    .totalPages(subscriptions.getTotalPages())
                    .totalElements(subscriptions.getTotalElements())
                    .build();
            log.info("Fetched {} subscriptions for user {}", subscriptions.getNumberOfElements(), id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching subscriptions for user {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/delete_subscription")
    public ResponseEntity<String> deleteSubscription(@PathVariable long id, @RequestParam String subscription) {
        log.info("Deleting subscription '{}' from user {}", subscription, id);
        try {
            userService.deleteSubscription(subscription, id);
            log.info("Subscription '{}' successfully deleted from user {}", subscription, id);
            return new ResponseEntity<>(String.format("Subscription %s successfully deleted", subscription), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete subscription '{}' from user {}: {}", subscription, id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
