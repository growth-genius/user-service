package com.sgyj.userservice.entity;

import com.sgyj.userservice.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    @Column(nullable = false, unique = true)
    private String userId;

    @CreatedDate
    private LocalDateTime createdAt;

    public static UserEntity from(UserDto userDto, BCryptPasswordEncoder passwordEncoder) {
        UserEntity user = new UserEntity();
        user.userId = userDto.getUserId();
        user.encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.email = userDto.getEmail();
        user.name = userDto.getName();
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

}
