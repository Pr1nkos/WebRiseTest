package ru.pr1nkos.webrisetest.page;

import lombok.Builder;
import lombok.Data;
import ru.pr1nkos.webrisetest.dto.SubscriptionDto;

import java.util.List;

@Builder
@Data
public class SubscriptionsPage{
    private List<SubscriptionDto> subscriptions;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
