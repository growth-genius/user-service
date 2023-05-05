package com.sgyj.userservice.repository;

import com.sgyj.userservice.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryQuerydsl {

    Optional<Account> findByUserName ( String nickname );

    @EntityGraph("Account.withAccountRoles")
    Optional<Account> findByEmail ( String email );

    boolean existsByEmail ( String email );


    @EntityGraph("Account.withAccountRoles")
    Optional<Account> findByAccountNo(String accountId);

}
