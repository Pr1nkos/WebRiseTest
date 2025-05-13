package ru.pr1nkos.webrisetest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String password;

    @OneToMany
    List<Subscription> subscriptions;
}
