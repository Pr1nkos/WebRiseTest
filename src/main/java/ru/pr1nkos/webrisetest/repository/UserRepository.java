package ru.pr1nkos.webrisetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pr1nkos.webrisetest.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
