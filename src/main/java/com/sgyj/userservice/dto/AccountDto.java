package com.sgyj.userservice.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.sgyj.userservice.entity.Account;
import com.sgyj.userservice.enums.AccountRole;
import com.sgyj.userservice.security.Jwt;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AccountDto {

    /** 로그인 아이디 */
    private Long id;

    private String accountId;

    /** 이메일 */
    private String email;

    /** 사용자 이름 */
    private String userName;

    /** 가입일자 */
    private LocalDateTime joinedAt;

    /** 권한 */
    private Set<AccountRole> roles;

    /** 로그인 횟수 */
    private int loginCount;

    /** 마지막 로그인 일자 */
    private LocalDateTime lastLoginAt;

    private String accessToken;

    private String refreshToken;

    private String profileImage;

    private String position;

    private String departmentName;

    private boolean superAdmin;

    public AccountDto(Account account) {
        copyProperties(account, this);
        this.accountId = account.getAccountNo();
    }

    public void generateAccessToken ( Jwt jwt) {
        Jwt.Claims claims = Jwt.Claims.of(id, accountId, email, roles.stream().map( AccountRole::name ).toArray(String[]::new));
        this.accessToken = jwt.createAccessToken( claims );
        this.refreshToken = jwt.createRefreshToken( claims );
    }

}
