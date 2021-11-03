package com.tsystems.banking.services.user;

import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.User;

public interface UserService {
  User findById(Long userId) throws ApiException;

  User findByUsername(String username) throws ApiException;

  User createUser(User user);

  Boolean existsByEmail(String email);
}
