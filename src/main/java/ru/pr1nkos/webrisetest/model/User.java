package ru.pr1nkos.webrisetest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Data
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String password;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Subscription> subscriptions;
}
