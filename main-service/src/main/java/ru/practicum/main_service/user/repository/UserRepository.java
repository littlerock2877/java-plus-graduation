package ru.practicum.main_service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}