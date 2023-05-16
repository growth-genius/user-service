package com.sgyj.userservice.security;

import com.sgyj.userservice.enums.LoginType;
import lombok.Data;

@Data
public class CredentialInfo {

    private String credential;
    private LoginType loginType;

    public CredentialInfo(String password, LoginType loginType) {
        this.credential = password;
        this.loginType = loginType;
    }

    public CredentialInfo(String password) {
        this.credential = password;
        this.loginType = LoginType.TGAHTER;
    }

}
