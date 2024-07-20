package com.example.onlineStoreApi.features.users.services;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.utils.UpdateUserNameParams;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User createUser(User user) throws Exception;

    User getUserById(String id);

    String updatePassword(String user, String updatePasswordParams);

    User updateUserName(String id, UpdateUserNameParams updateNameParams);

    String updateUserRoles(Long id, String user);

    void updateUserIsActive(String id, Boolean isActive);

    Boolean deleteUser(String id);
}
