package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.UserCreateRequest;
import myrzakhan_taskflow.dtos.requests.UserUpdateRequest;
import myrzakhan_taskflow.entities.postgres.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> findAllUsers(Pageable pageable);

    User findUserById(Long id);

    User createUser(UserCreateRequest user);

    User updateUser(Long id, UserUpdateRequest user);

    void deleteUser(Long id);
}
