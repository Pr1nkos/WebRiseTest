package ru.pr1nkos.webrisetest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SubscriptionDto(String name, Long userId) {
}
