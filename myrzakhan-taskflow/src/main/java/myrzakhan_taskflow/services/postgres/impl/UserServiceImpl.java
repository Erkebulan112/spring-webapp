package myrzakhan_taskflow.services.postgres.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.requests.UserCreateRequest;
import myrzakhan_taskflow.dtos.requests.UserUpdateRequest;
import myrzakhan_taskflow.entities.postgres.User;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.repositories.postgres.UserRepository;
import myrzakhan_taskflow.services.postgres.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllUsers(Pageable pageable) {
        log.info("Get all users");
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        log.info("Get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: %d".formatted(id)));
    }

    @Override
    public User createUser(UserCreateRequest request) {
        log.info("Create user: {}", request);
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest request) {
        log.info("Update user: {}", request);
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: %d".formatted(id)));
        user.setFirstName(request.firstname());
        user.setLastName(request.lastname());
        user.setEmail(request.email());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user: {}", id);
        userRepository.deleteById(id);
    }
}
