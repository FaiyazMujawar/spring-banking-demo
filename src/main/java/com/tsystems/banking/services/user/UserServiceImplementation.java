package com.tsystems.banking.services.user;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.User;
import com.tsystems.banking.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation
  implements UserService, UserDetailsService {
  private final UserRepository userRepository;

  /**
   * @param userRepository
   */
  @Autowired
  public UserServiceImplementation(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User findById(Long userId) {
    return userRepository
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
    return userRepository
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
    return userRepository.save(user);
  }

  @Override
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public List<User> findAllById(List<Long> ids) {
    return userRepository.findAllById(ids);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    User user = userRepository
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
}
