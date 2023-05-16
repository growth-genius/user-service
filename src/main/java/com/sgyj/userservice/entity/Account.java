package com.sgyj.userservice.entity;


import static jakarta.persistence.FetchType.LAZY;

import com.sgyj.userservice.common.UpdatedEntity;
import com.sgyj.userservice.enums.AccountRole;
import com.sgyj.userservice.enums.AccountStatus;
import com.sgyj.userservice.enums.ErrorMessage;
import com.sgyj.userservice.enums.LoginType;
import com.sgyj.userservice.enums.TravelTheme;
import com.sgyj.userservice.form.AccountSaveForm;
import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends UpdatedEntity {

    /* 아이디 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    /* 고유 식별자 */
    @Column(unique = true)
    private String uuid;
    /* 사용자 이름 */
    private String username;
    /* 사용자 별명 */
    private String nickname;
    /* 이메일 */
    private String email;
    /* 비밀번호 */
    private String password;
    /* 로그인 형태 */
    private LoginType loginType;
    /* 권한 */
    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    private Set<AccountRole> roles = Set.of(AccountRole.USER);
    /* 나이 */
    private int age;

    private int birth;
    /* 인증코드 */
    private String authCode;

    private LocalDateTime authCodeModifiedAt;

    /* 여행 테마 */
    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "travel_themes_account", joinColumns = @JoinColumn(name = "account_id"))
    private Set<TravelTheme> travelThemes;

    /** 계정 상태 */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.VERIFY_EMAIL;

    /** 프로필 이미지 */
    @Lob
    @Basic
    private String profileImage;

    /** 가입일자 */
    private LocalDateTime joinedAt;

    /** 로그인 횟수 */
    private int loginCount;

    /** 로그인 실패 회수 */
    private int loginFailCount;

    /** 마지막 로그인 일자 */
    private LocalDateTime lastLoginAt;

    /** 로그인 후 세팅 */
    public void afterLoginSuccess() {
        this.loginFailCount = 0;
        this.loginCount++;
        this.lastLoginAt = LocalDateTime.now();
    }

    /** 비밀번호 변경 */
    public void changePassword(String password) {
        this.password = password;
    }

    /** 프로필 사진 변경 */
    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /** 이름 변경 */
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void login(PasswordEncoder passwordEncoder, String credential) {
        if (!passwordEncoder.matches(credential, this.password)) {
            this.loginFailCount++;
            throw new BadRequestException(ErrorMessage.NOT_MATCHED_ACCOUNT.getMessage());
        } else if (this.accountStatus != AccountStatus.NORMAL) {
            throw new BadRequestException(ErrorMessage.VERIFY_EMAIL.getMessage());
        }
    }

    public void successAuthUser() {
        this.accountStatus = AccountStatus.NORMAL;
    }

    /**
     * 입력 데이터로 Account 계정 생성
     *
     * @param accountSaveForm Account입력 form
     * @param authCode
     * @return
     */
    public static Account createAccountByFormAndAuthCode(AccountSaveForm accountSaveForm, String authCode) {
        Account account = new Account();
        account.username = accountSaveForm.getUsername();
        account.password = accountSaveForm.getPassword();
        account.email = accountSaveForm.getEmail();
        account.birth = accountSaveForm.getBirth();
        account.nickname = accountSaveForm.getNickname();
        account.profileImage = accountSaveForm.getProfileImage();
        account.loginType = LoginType.TGAHTER;
        account.joinedAt = LocalDateTime.now();
        account.authCode = authCode;
        account.authCodeModifiedAt = LocalDateTime.now();
        return account;
    }

}