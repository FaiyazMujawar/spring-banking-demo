package com.tsystems.banking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.tsystems.banking.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @AfterEach
  public void tearDown() {
    userRepository.deleteAll();
  }

  private final User user = new User(
    "John",
    "Doe",
    "johnDoe",
    "john@email.com",
    "123",
    123L
  );

  @Nested
  @DisplayName("Testing findByUsername()")
  public class testFindByUsername {

    @Test
    @DisplayName("Should return user when username exists")
    public void shouldReturnUser() {
      userRepository.save(user);

      User underTest = userRepository.findByUsername(user.getUsername()).get();

      assertThat(underTest.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should return null when username does not exists")
    public void shouldNotReturnUser() {
      assertThat(userRepository.findByUsername("johnDoe")).isEmpty();
    }
  }

  @Nested
  @DisplayName("Testing existsByEmail()")
  public class testExistsByEmail {

    @Test
    @DisplayName("Should return true when email exists")
    public void shouldReturnTrue() {
      userRepository.save(user);

      assertThat(userRepository.existsByEmail(user.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exists")
    public void shouldReturnFalse() {
      assertThat(userRepository.existsByEmail(user.getEmail())).isFalse();
    }
  }
}
