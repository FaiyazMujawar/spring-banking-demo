package com.tsystems.banking.services.user;

import com.tsystems.banking.models.User;
import java.util.Optional;

public interface UserService {
  Optional<User> findById(Long userId);

  Optional<User> findByUsername(String username);

  User createUser(User user);
}
