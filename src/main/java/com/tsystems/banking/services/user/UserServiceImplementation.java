package com.tsystems.banking.services.user;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.User;
import com.tsystems.banking.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation
  implements UserService, UserDetailsService {
  private final UserRepository userRepo;

  /**
   * @param userRepo
   */
  @Autowired
  public UserServiceImplementation(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public User findById(Long userId) {
    return userRepo
      .findById(userId)
      .orElseThrow(
        () ->
          new ApiException(
            NOT_FOUND,
            String.format("User with id {%d} not found", userId)
          )
      );
  }

  @Override
  public User findByUsername(String username) {
    return userRepo
      .findByUsername(username)
      .orElseThrow(
        () ->
          new ApiException(
            NOT_FOUND,
            String.format("User with username {%s} not found", username)
          )
      );
  }

  @Override
  public User createUser(User user) {
    return userRepo.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    User user = userRepo
      .findByUsername(username)
      .orElseThrow(
        () ->
          new UsernameNotFoundException(
            String.format("User with username {%s} not found", username)
          )
      );

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      authorities
    );
  }

  @Override
  public Boolean existsByEmail(String email) {
    return userRepo.existsByEmail(email);
  }
}
