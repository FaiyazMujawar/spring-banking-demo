package com.tsystems.banking.services.user;

import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.User;
import java.util.List;

public interface UserService {
  User findById(Long userId) throws ApiException;

  User findByUsername(String username) throws ApiException;

  List<User> findAllById(List<Long> ids);

  User createUser(User user);

  Boolean existsByEmail(String email);
}
