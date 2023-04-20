package com.sgyj.userservice.service;

import com.sgyj.userservice.dto.UserDto;
import com.sgyj.userservice.entity.UserEntity;
import com.sgyj.userservice.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = UserEntity.from(userDto, passwordEncoder);
        userRepository.save(userEntity);
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setPassword(null);
        return userDto;
    }
}
