package com.sgyj.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sgyj.userservice.annotations.BaseAnnotation;
import com.sgyj.userservice.dto.AccountDto;
import com.sgyj.userservice.form.AccountSaveForm;
import com.sgyj.userservice.form.AuthCodeForm;
import com.sgyj.userservice.form.SignInForm;
import com.sgyj.userservice.security.CredentialInfo;
import com.sgyj.userservice.service.AccountService;
import com.sgyj.userservice.utils.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@BaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ApiUtil.ApiResult<AccountDto> addUser(@RequestBody @Valid AccountSaveForm accountSaveForm) throws JsonProcessingException {
        return ApiUtil.success(accountService.saveAccount(accountSaveForm));
    }

    @PostMapping("/check-email")
    public ApiUtil.ApiResult<AccountDto> authCode(@RequestBody @Valid AuthCodeForm authCodeForm) {
        return ApiUtil.success(accountService.validAuthCode(authCodeForm));
    }

    @PostMapping("/login")
    public ApiUtil.ApiResult<AccountDto> login(@RequestBody @Valid SignInForm signInForm) {
        return ApiUtil.success(accountService.login(signInForm.getEmail(), new CredentialInfo(signInForm.getPassword())));
    }

}
