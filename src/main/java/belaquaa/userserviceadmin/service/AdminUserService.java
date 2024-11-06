package belaquaa.userserviceadmin.service;

import belaquaa.userserviceadmin.model.User;

import java.util.List;

public interface AdminUserService {
    User createUser(User user);

    User updateUser(Long id, User user);

    User getUserById(Long id, boolean includeDeleted);

    void restoreUserById(Long id);

    List<User> getAllUsers(boolean includeDeleted);
}
