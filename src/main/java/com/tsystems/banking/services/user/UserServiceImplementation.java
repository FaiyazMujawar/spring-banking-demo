package com.tsystems.banking.services.user;

import com.tsystems.banking.models.User;
import com.tsystems.banking.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
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
  public Optional<User> findById(Long userId) {
    return userRepo.findById(userId);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepo.findByUsername(username);
  }

  @Override
  public User createUser(User user) {
    return userRepo.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepo.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new UsernameNotFoundException(
        String.format("User with username %s not found", username)
      );
    }
    User user = optionalUser.get();

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      authorities
    );
  }
}
