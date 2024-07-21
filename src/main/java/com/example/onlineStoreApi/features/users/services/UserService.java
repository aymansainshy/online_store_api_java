package com.example.onlineStoreApi.features.users.services;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.utils.CreateUserDto;
import com.example.onlineStoreApi.features.users.utils.UpdateUserNameDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User createUser(CreateUserDto createUserDto) throws Exception;

    User getUserById(String id);

    String updatePassword(String user, String updatePasswordParams);

    User updateUserName(String id, UpdateUserNameDto updateUserNameDto);

    String updateUserRoles(Long id, String user);

    void updateUserIsActive(String id, Boolean isActive);

    Boolean deleteUser(String id);
}
