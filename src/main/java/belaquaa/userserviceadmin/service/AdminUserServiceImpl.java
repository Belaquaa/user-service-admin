package belaquaa.userserviceadmin.service;

import belaquaa.userserviceadmin.exception.UserNotFoundException;
import belaquaa.userserviceadmin.model.User;
import belaquaa.userserviceadmin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;

    @Override
    @CachePut(value = "users", key = "#result.id")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        user.setUsername(userDetails.getUsername());
        user.setAge(userDetails.getAge());

        return userRepository.save(user);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    @Cacheable(value = "users", key = "#id + '-includeDeleted'")
    public User getUserByIdIncludingDeleted(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void restoreUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        user.setIsDeleted(false);
        userRepository.save(user);
        kafkaTemplate.send("user-restored-topic", id);
    }

    @Override
    public List<User> getAllUsers(boolean includeDeleted) {
        if (includeDeleted) {
            return userRepository.findAll();
        } else {
            return userRepository.findAllByIsDeletedFalse();
        }
    }
}