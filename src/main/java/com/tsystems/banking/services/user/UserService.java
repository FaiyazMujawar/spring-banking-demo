package com.tsystems.banking.services.user;

import com.tsystems.banking.models.User;
import java.util.List;

public interface UserService {
  User findById(Long userId) throws Exception;

  User findByUsername(String username) throws Exception;

  List<User> findAllById(List<Long> ids);

  User createUser(User user);

  Boolean existsByEmail(String email);
}
