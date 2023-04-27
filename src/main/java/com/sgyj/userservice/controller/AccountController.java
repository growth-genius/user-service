package com.sgyj.userservice.controller;

import static com.sgyj.userservice.utils.ApiUtil.success;

import com.sgyj.userservice.annotations.BaseAnnotation;
import com.sgyj.userservice.dto.AccountDto;
import com.sgyj.userservice.form.SignUpForm;
import com.sgyj.userservice.service.AccountService;
import com.sgyj.userservice.utils.ApiUtil;
import com.sgyj.userservice.validator.SignUpFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@BaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder( WebDataBinder webDataBinder ) {
        webDataBinder.addValidators( signUpFormValidator );
    }

    /**
     * 회원가입
     * @param signUpForm : 회원가입 폼
     * @return AccountDto
     */
    @PostMapping("/sign-up")
    public ApiUtil.ApiResult<AccountDto> signUp(@Valid @RequestBody SignUpForm signUpForm ) {
        return success( new AccountDto(accountService.processNewAccount( signUpForm )));
    }

    @GetMapping("/check-email-token/{token}/{email}")
    public ApiUtil.ApiResult<Boolean> checkEmailToken( @PathVariable String token, @PathVariable String email) {
        return success( accountService.checkEmailToken( token, email ), "이메일 인증에 성공하였습니다. 로그인을 진행해 주세요." );
    }

}
