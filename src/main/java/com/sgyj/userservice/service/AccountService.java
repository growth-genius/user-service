package com.sgyj.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sgyj.userservice.annotations.BaseServiceAnnotation;
import com.sgyj.userservice.dto.AccountDto;
import com.sgyj.userservice.dto.TokenDto;
import com.sgyj.userservice.dto.kafka.EmailMessage;
import com.sgyj.userservice.entity.Account;
import com.sgyj.userservice.enums.LoginType;
import com.sgyj.userservice.exception.ExpiredTokenException;
import com.sgyj.userservice.form.AccountSaveForm;
import com.sgyj.userservice.form.AuthCodeForm;
import com.sgyj.userservice.properties.KafkaUserTopicProperties;
import com.sgyj.userservice.repository.AccountRepository;
import com.sgyj.userservice.security.CredentialInfo;
import com.sgyj.userservice.security.Jwt;
import com.sgyj.userservice.security.Jwt.Claims;
import com.sgyj.userservice.service.kafka.KafkaAccountProducer;
import com.sgyj.userservice.service.kafka.KafkaEmailProducer;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final KafkaEmailProducer kafkaEmailProducer;
    private final KafkaUserTopicProperties kafkaUserTopicProperties;
    private final Jwt jwt;
    private final KafkaAccountProducer kafkaAccountProducer;

    /**
     * 회원가입
     *
     * @param accountSaveForm account 저장 폼
     * @return AccountDto account 생성 결과 Dto
     */
    public AccountDto saveAccount(AccountSaveForm accountSaveForm) throws JsonProcessingException {
        accountSaveForm.setPassword(passwordEncoder.encode(accountSaveForm.getPassword()));
        validateAccount(accountSaveForm);

        String authCode = sendSignUpConfirmEmail(accountSaveForm.getEmail());
        Account account = Account.createAccountByFormAndAuthCode(accountSaveForm, authCode);
        // accountRepository.save(account);
        AccountDto accountDto = AccountDto.from(account);
        log.error("topic ::: {}", kafkaUserTopicProperties.getAccountTopic());
        kafkaAccountProducer.send(kafkaUserTopicProperties.getAccountTopic(), accountDto);
        return accountDto;
    }

    /**
     * 이메일 인증코드 전송
     *
     * @param email 이메일
     * @return authCode 인증코드
     */
    private String sendSignUpConfirmEmail(String email) throws JsonProcessingException {
        String authCode = RandomStringUtils.randomAlphanumeric(12);
        EmailMessage emailMessage = EmailMessage.builder().to(email).subject("TGather 회원가입 인증 메일").message(authCode).build();
        // kafkaEmailProducer.send(kafkaUserTopicProperties.getMailSendTopic(), emailMessage);
        return authCode;
    }

    /**
     * 입력된 정보 확인
     *
     * @param accountSaveForm account 저장 폼
     */
    private void validateAccount(AccountSaveForm accountSaveForm) {

        if (validNickname(accountSaveForm.getNickname())) {
            throw new BadCredentialsException("이미 존재하는 닉네임입니다.");
        }

        accountRepository.findByEmail(accountSaveForm.getEmail()).ifPresent(account -> {
            throw new BadCredentialsException("이미 존재하는 이메일입니다.");
        });

    }

    /**
     * nickName 중복확인
     *
     * @param nickName 닉네임
     * @return boolean 닉네임 유효값 확인 결과
     */
    boolean validNickname(String nickName) {
        return accountRepository.findByNickname(nickName).isPresent();
    }

    /**
     * 사용자 로그인
     *
     * @param email      이메일
     * @param credential 인증
     * @return AccountDto 계정 Dto
     */
    public AccountDto login(String email, CredentialInfo credential) {
        Account account = accountRepository.findByEmailAndLoginType(email, credential.getLoginType()).orElseThrow(() -> new NotFoundException("등록된 계정이 없습니다."));
        account.login(passwordEncoder, credential.getCredential());
        account.afterLoginSuccess();
        return AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 이메일 인증 확인
     *
     * @param authCodeForm 인증 코드 확인 form
     * @return AccountDto 인증 확인 AccountDto
     */
    public AccountDto validAuthCode(AuthCodeForm authCodeForm) {
        Account account = accountRepository.findByEmailAndLoginType(authCodeForm.getEmail(), LoginType.TGAHTER)
            .orElseThrow(() -> new NotFoundException("등록된 계정이 없습니다."));

        if (!account.getAuthCode().equals(authCodeForm.getAuthCode())) {
            throw new BadCredentialsException("인증 코드가 잘못되었습니다. 다시 확인해주세요.");
        }
        account.successAuthUser();
        return AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 토큰 갱신
     *
     * @param tokenDto 토큰 갱신 Dto
     * @return TokenDto 갱신 토큰 결과 Dto
     */
    public TokenDto renewalTokenByRefreshToken(TokenDto tokenDto) {
        if (jwt.validateToken(tokenDto.getRefreshToken())) {
            Claims claims = jwt.verify(tokenDto.getRefreshToken());
            Account account = accountRepository.findByEmailAndLoginType(claims.getEmail(), LoginType.TGAHTER)
                .orElseThrow(() -> new NotFoundException("이메일을 찾을 수 없습니다."));
            AccountDto accountDto = AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
            return TokenDto.builder().accessToken(accountDto.getAccessToken()).refreshToken(accountDto.getRefreshToken()).build();
        }
        throw new ExpiredTokenException();
    }

}

