package ru.pr1nkos.webrisetest.dto;

import lombok.Builder;

@Builder
public record SubscriptionDto(String name, Long userId) {
}
