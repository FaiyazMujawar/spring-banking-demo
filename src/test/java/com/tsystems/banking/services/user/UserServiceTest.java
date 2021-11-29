package com.tsystems.banking.services.user;

import com.tsystems.banking.exceptions.UserNotFoundException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.models.User;
import com.tsystems.banking.repository.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  private UserService userService;

  private final User user = new User(
    1L,
    "John",
    "Doe",
    "johnDoe",
    "john@email.com",
    "123",
    123L
  );

  @BeforeEach
  void setUp() {
    userService = new UserServiceImplementation(userRepository);
  }

  @Nested
  @DisplayName("Testing findByUsername")
  public class testFindByUsername {

    @Test
    @DisplayName("Should return user when username exists")
    public void shouldReturnUser() {
      Mockito
        .when(userRepository.findByUsername(Mockito.anyString()))
        .thenReturn(Optional.of(user));

      Assertions
        .assertThat(userService.findByUsername(user.getUsername()))
        .isEqualTo(user);
    }

    @Test
    @DisplayName(
      "Should throw UserNotFoundException when username does not exist"
    )
    public void shouldThrowNotFoundException() {
      Assertions
        .assertThatThrownBy(
          () -> userService.findByUsername(user.getUsername())
        )
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(Constants.USER_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName(
      "Should IllegalArgumentException when passed argument is null or empty"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> userService.findByUsername(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_USERNAME_ERROR);

      Assertions
        .assertThatThrownBy(() -> userService.findByUsername(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_USERNAME_ERROR);

      Assertions
        .assertThatThrownBy(() -> userService.findByUsername(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_USERNAME_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing existsByEmail()")
  public class testExistsByEmail {

    @Test
    @DisplayName("Should return true when email exists")
    public void shouldReturnTrue() {
      Mockito
        .when(userRepository.existsByEmail(Mockito.anyString()))
        .thenReturn(true);

      Assertions
        .assertThat(userService.existsByEmail(user.getEmail()))
        .isTrue();
    }

    @Test
    @DisplayName("Should return true when email does not exist")
    public void shouldReturnFalse() {
      Mockito
        .when(userRepository.existsByEmail(Mockito.anyString()))
        .thenReturn(false);

      Assertions
        .assertThat(userService.existsByEmail(user.getEmail()))
        .isFalse();
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null or blank"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> userService.existsByEmail(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);

      Assertions
        .assertThatThrownBy(() -> userService.existsByEmail(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);
      Assertions
        .assertThatThrownBy(() -> userService.existsByEmail(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing findByEmail()")
  public class testFindByEmail {

    @Test
    @DisplayName("Should return user when email exists")
    public void shouldReturnUser() {
      Mockito
        .when(userRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(user));

      Assertions
        .assertThat((userService.findByEmail(user.getEmail())))
        .isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when email does not exist")
    public void shouldThrowNotFoundException() {
      Assertions
        .assertThatThrownBy(() -> userService.findByEmail(user.getEmail()))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(Constants.USER_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException if the passed argument is null or blank"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> userService.findByEmail(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);

      Assertions
        .assertThatThrownBy(() -> userService.findByEmail(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);

      Assertions
        .assertThatThrownBy(() -> userService.findByEmail(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EMAIL_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing createUser()")
  public class testCreateUser {

    @Test
    @DisplayName("Should create user when passed argument is valid")
    public void shouldCreateUser() {
      Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

      Assertions.assertThat(userService.createUser(user)).isEqualTo(user);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> userService.createUser(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_USER_ARG_ERROR);
    }
  }
}
