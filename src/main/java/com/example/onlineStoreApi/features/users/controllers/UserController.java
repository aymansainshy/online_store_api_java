package com.example.onlineStoreApi.features.users.controllers;

import com.example.onlineStoreApi.core.utils.ApiResponse;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.services.UserService;
import com.example.onlineStoreApi.features.users.utils.CreateUserDto;
import com.example.onlineStoreApi.features.users.utils.UpdateUserNameDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        ApiResponse<List<User>> apiResponse = new ApiResponse<>(userList, "Successful");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @PostMapping()
    public ResponseEntity<ApiResponse<User>> createUser(
            @Validated
            @RequestBody
            CreateUserDto createUserParams
    ) throws Exception {
        User createdUser = userService.createUser(createUserParams);
        ApiResponse<User> apiResponse = new ApiResponse<>(createdUser, "User Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") String id) {
        User foundedUser = userService.getUserById(id);
        ApiResponse<User> apiResponse = new ApiResponse<>(foundedUser, "Successful");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUserName(
            @Valid
            @PathVariable("id") String id,
            @RequestBody UpdateUserNameDto updateUserNameDto
    ) {
        System.out.println(updateUserNameDto.getFirstName());
        System.out.println(updateUserNameDto.getLastName());

        User updateUser = userService.updateUserName(id, updateUserNameDto);
        ApiResponse<User> apiResponse = new ApiResponse<>(updateUser, "User name updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(@PathVariable("id") String id) {
        boolean deleteResult = userService.deleteUser(id);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>(deleteResult, "User deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}

