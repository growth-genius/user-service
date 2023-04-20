package com.sgyj.userservice.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.sgyj.userservice.vo.RequestUser;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String name;
    private String password;
    private String userId;
    private LocalDateTime createdAt;
    private String encryptPassword;

    public UserDto(RequestUser requestUser) {
        copyProperties(requestUser, this);
    }

}
