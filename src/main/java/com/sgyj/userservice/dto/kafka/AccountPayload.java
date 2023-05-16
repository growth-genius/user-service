package com.sgyj.userservice.dto.kafka;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.sgyj.userservice.dto.AccountDto;
import com.sgyj.userservice.enums.AccountRole;
import com.sgyj.userservice.enums.LoginType;
import com.sgyj.userservice.enums.TravelTheme;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountPayload extends Payload {

    /** 로그인 아이디 */
    private Long accountId;
    /** 고유 식별자 */
    private String uuid;
    /** 이메일 */
    private String email;
    /** 사용자 이름 */
    private String userName;
    /* 사용자 별명 */
    private String nickname;
    /* 패스워드*/
    private String password;
    /** 가입일자 */
    private LocalDateTime joinedAt;
    /** 권한 */
    private Set<AccountRole> roles;
    /** 로그인 횟수 */
    private int loginCount;
    /** 마지막 로그인 일자 */
    private LocalDateTime lastLoginAt;

    private String profileImage;
    private LoginType loginType;
    private String accessToken;
    private String refreshToken;
    /* 나이 */
    private int age;

    private int birth;
    private Set<TravelTheme> travelThemes;

    public static Payload from(AccountDto accountDto) {
        AccountPayload accountPayload = new AccountPayload();
        copyProperties(accountDto, accountPayload);
        return accountPayload;
    }
}
