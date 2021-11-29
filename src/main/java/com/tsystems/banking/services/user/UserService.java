package com.tsystems.banking.services.user;

import com.tsystems.banking.exceptions.UserNotFoundException;
import com.tsystems.banking.models.User;

public interface UserService {
  User findByUsername(String username)
    throws UserNotFoundException, IllegalArgumentException;

  User findByEmail(String email)
    throws UserNotFoundException, IllegalArgumentException;

  User createUser(User user) throws IllegalArgumentException;

  Boolean existsByEmail(String email) throws IllegalArgumentException;
}
