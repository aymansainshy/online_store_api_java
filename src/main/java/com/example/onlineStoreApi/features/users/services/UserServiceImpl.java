package com.example.onlineStoreApi.features.users.services;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.features.users.utils.CreateUserDto;
import com.example.onlineStoreApi.features.users.utils.UpdateUserNameDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User createUser(CreateUserDto createUserDto) {
        Optional<User> existingUser = userRepository.findByEmail(createUserDto.getEmail());

        if (existingUser.isPresent()) {
            throw new ConflictException("User already exist");
        }

        User user = User.builder()
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .build();

        return userRepository.save(user);
    }


    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(Long.parseLong(id));
        if (user.isEmpty()) throw new ResourceNotFoundException("User Not found");
        return user.get();
    }


    @Override
    public String updatePassword(String user, String updatePasswordParams) {
        return "";
    }


    @Override
    @Transactional
    public User updateUserName(String id, UpdateUserNameDto userNameParams) {
        User existingUser = userRepository
                .findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if ((userNameParams.getFirstName() != null) &&
                !userNameParams.getFirstName().isEmpty() &&
                !Objects.equals(existingUser.getFirstName(), userNameParams.getFirstName())) {
            existingUser.setFirstName(userNameParams.getFirstName());
        }

        if ((userNameParams.getLastName() != null) &&
                !userNameParams.getLastName().isEmpty() &&
                !Objects.equals(existingUser.getLastName(), userNameParams.getLastName())) {
            existingUser.setLastName(userNameParams.getLastName());
        }

        return existingUser;
    }


    @Override
    public String updateUserRoles(Long id, String user) {
        return "";
    }


    @Override
    public void updateUserIsActive(String id, Boolean isActive) {

    }


    @Override
    public Boolean deleteUser(String id) {
        boolean isExist = userRepository.existsById(Long.parseLong(id));
        if (!isExist) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(Long.parseLong(id));
        return true;
    }
}

