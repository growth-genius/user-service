package com.sgyj.userservice.security;

import static com.google.common.base.Preconditions.checkNotNull;

public record JwtAuthentication(Long accountId, String email) {

    public JwtAuthentication {
        checkNotNull(accountId, "accountId must be provided");
        checkNotNull(email, "email must be provided");
    }

}