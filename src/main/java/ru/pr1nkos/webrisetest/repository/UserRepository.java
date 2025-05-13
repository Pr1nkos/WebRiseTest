package ru.pr1nkos.webrisetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.webrisetest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
