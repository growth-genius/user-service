package com.sgyj.userservice.security;

import static com.google.common.base.Preconditions.checkNotNull;

public record JwtAuthentication(Long id, String accountId, String email) {

    public JwtAuthentication {
        checkNotNull(id, "id must be provided");
        checkNotNull(accountId, "accountId must be provided");
        checkNotNull(email, "email must be provided");
    }

}