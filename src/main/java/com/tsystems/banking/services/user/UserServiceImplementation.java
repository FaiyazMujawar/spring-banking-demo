package com.tsystems.banking.services.user;

import com.tsystems.banking.exceptions.UserNotFoundException;
import com.tsystems.banking.misc.Constants;
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
  public User findById(Long userId) throws UserNotFoundException {
    return userRepository
      .findById(userId)
      .orElseThrow(
        () -> new UserNotFoundException(Constants.USER_NOT_FOUND_ERROR)
      );
  }

  @Override
  public User findByUsername(String username) throws UserNotFoundException {
    return userRepository
      .findByUsername(username)
      .orElseThrow(
        () -> new UserNotFoundException(Constants.USER_NOT_FOUND_ERROR)
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
        () -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_ERROR)
      );

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      authorities
    );
  }
}
