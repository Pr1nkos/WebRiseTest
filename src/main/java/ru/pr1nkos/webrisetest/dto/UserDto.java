package ru.pr1nkos.webrisetest.dto;

import lombok.Builder;
import ru.pr1nkos.webrisetest.model.Subscription;

import java.util.List;

@Builder
public record UserDto(
        List<Subscription> subscriptions,
        String username,
        String password) {
}
