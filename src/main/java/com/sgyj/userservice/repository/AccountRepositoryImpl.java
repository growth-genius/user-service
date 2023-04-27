package com.sgyj.userservice.repository;

import com.sgyj.userservice.common.Querydsl4RepositorySupport;
import com.sgyj.userservice.entity.Account;

public class AccountRepositoryImpl extends Querydsl4RepositorySupport implements AccountRepositoryQuerydsl {

    public AccountRepositoryImpl() {
        super( Account.class);
    }


}
