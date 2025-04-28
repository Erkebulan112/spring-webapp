package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.UserCreateRequest;
import myrzakhan_taskflow.entities.postgres.User;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.repositories.postgres.UserRepository;
import myrzakhan_taskflow.services.postgres.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private User existingUser;
    private UserCreateRequest userRequest;

    @BeforeEach
    void setUp() {

        userRequest = new UserCreateRequest("User", "Lastname", "example@email.com");

        user = new User();
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        ;

        existingUser = new User();
        existingUser.setFirstName(userRequest.firstName());
        existingUser.setLastName(userRequest.lastName());
        existingUser.setEmail(userRequest.email());
    }

    @Test
    void testFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var response = userService.findUserById(1L);

        assertAll("Testing findUserById method",
                () -> assertNotNull(response, "Response is null"),
                () -> assertEquals(user.getFirstName(), response.getFirstName(), "First name does not match"),
                () -> assertEquals(user, response, "Objects do not match"),
                () -> verify(userRepository, times(1)).findById(1L));
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        var response = userService.createUser(userRequest);

        assertAll("Testing createUser method",
                () -> assertNotNull(response, "Response is null"),
                () -> assertEquals(user.getFirstName(), response.getFirstName(), "First name does not match"),
                () -> assertEquals(user, response, "Objects do not match"),
                () -> verify(userRepository, times(1)).save(any(User.class)));
    }

    @Test
    void shoudThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> userService.findUserById(999L), "Expected NotFoundException");
    }
}
