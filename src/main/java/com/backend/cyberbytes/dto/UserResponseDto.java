package com.backend.cyberbytes.dto;

import com.backend.cyberbytes.model.User;
import com.backend.cyberbytes.model.UserRole;

public record UserResponseDto(String id, String name, String email, String password, UserRole role) {
    public UserResponseDto(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
