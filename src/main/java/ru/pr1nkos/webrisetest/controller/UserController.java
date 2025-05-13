package ru.pr1nkos.webrisetest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;
import ru.pr1nkos.webrisetest.dto.UserDto;
import ru.pr1nkos.webrisetest.model.User;
import ru.pr1nkos.webrisetest.service.SubscriptionService;
import ru.pr1nkos.webrisetest.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestParam UserDto userDto, @RequestParam String password) {
        try {
            userService.createUser(userDto, password);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        UserDto userById = userService.findUserById(id);
        if (userById != null) {
            return new ResponseEntity<>(userById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update/password")
    public ResponseEntity<String> updatePassword(@SessionAttribute User user, @RequestParam String password) {
        try {
            userService.updatePassword(user.getId(), password);
            return new ResponseEntity<>(String.format("Password successfully changed on: %s", password), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/username")
    public ResponseEntity<String> updateUsername(@SessionAttribute User user, @RequestParam String username) {
        try {
            userService.updateUsername(user.getId(), username);
            return new ResponseEntity<>(String.format("Username successfully changed on: %s", username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUser(@SessionAttribute User user) {
        try {
            userService.deleteUser(user.getId());
            return new ResponseEntity<>(String.format("User %s successfully deleted", user.getId()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/subscriptions/add")
    public ResponseEntity<String> addSubscription(@SessionAttribute User user, @RequestBody String subscription) {
        try{
            userService.addSubscription(user.getId(), subscription);
            return new ResponseEntity<>(subscription, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(@SessionAttribute User user) {
        try {
            List<SubscriptionDto> subscriptionDtos = userService.findAllSubscriptions(user.getId());
            return new ResponseEntity<>(subscriptionDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSubscription(@SessionAttribute User user, @RequestBody String subscription) {
        try{
            userService.deleteSubscription(subscription, user.getId());
            return new ResponseEntity<>(String.format("Subscription %s successfully deleted", subscription), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
