package com.tsystems.banking.services.user;

import com.tsystems.banking.models.User;
import java.util.Optional;

public interface UserService {
  Optional<User> getUserById(Long userId);

  Optional<User> getUserByUsername(String username);

  User createUser(User user);
}
