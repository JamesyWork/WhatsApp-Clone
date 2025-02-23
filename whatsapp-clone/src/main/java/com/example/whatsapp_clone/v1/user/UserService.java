package com.example.whatsapp_clone.v1.user;

import com.example.whatsapp_clone.v1.user.model.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsersExceptSelf(Authentication auth) {
        return userRepository.findAllUsersExceptSelf(auth.getName())
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
