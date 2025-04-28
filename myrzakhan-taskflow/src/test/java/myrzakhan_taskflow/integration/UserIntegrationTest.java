package myrzakhan_taskflow.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import myrzakhan_taskflow.dtos.requests.UserCreateRequest;
import myrzakhan_taskflow.dtos.requests.UserUpdateRequest;
import myrzakhan_taskflow.entities.postgres.User;
import myrzakhan_taskflow.repositories.postgres.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setActive(true);
        user1.setCreatedAt(LocalDateTime.of(2025, 4, 10, 10, 0));
        user1.setUpdatedAt(LocalDateTime.of(2025, 4, 10, 11, 0));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setActive(true);
        user2.setCreatedAt(LocalDateTime.of(2025, 4, 10, 10, 0));
        user2.setUpdatedAt(LocalDateTime.of(2025, 4, 10, 11, 0));

        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    void testGetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content", hasSize(2)))
                        .andExpect(jsonPath("$.content[0].firstName").value("Jane"))
                        .andExpect(jsonPath("$.content[*].email",
                                containsInAnyOrder("john.doe@example.com", "jane.doe@example.com")));
    }

    @Test
    void testGetUserById() throws Exception {

        var user = userRepository.findAll().getFirst();

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {

        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User not found with id: 999"));
    }

    @Test
    void testCreateUser() throws Exception {

        var user = new UserCreateRequest("John", "Duran", "john00@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email", containsString("@example.com")));
    }

    @Test
    void testUpdateUser() throws Exception {

        var existingUser = userRepository.findAll().getFirst();

        var user = new UserUpdateRequest("Cristiano", "Ronaldo", "cr7@gmail.com");

        mockMvc.perform(put("/api/users/{id}", existingUser.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Cristiano"))
                .andExpect(jsonPath("$.email", containsString("@gmail.com")));
    }

    @Test
    void testDeleteUser() throws Exception {

        var existingUser = userRepository.findAll().getFirst();

        mockMvc.perform(delete("/api/users/{id}", existingUser.getId()))
                .andExpect(status().isNoContent());
    }

}
