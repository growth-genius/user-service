package com.sgyj.userservice.repository;

import com.sgyj.userservice.common.Querydsl5Support;
import com.sgyj.userservice.entity.Account;

public class AccountRepositoryImpl extends Querydsl5Support implements AccountRepositoryQuerydsl {

    public AccountRepositoryImpl() {
        super( Account.class);
    }

}
