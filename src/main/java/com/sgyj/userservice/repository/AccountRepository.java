package com.sgyj.userservice.repository;

import com.sgyj.userservice.entity.Account;
import com.sgyj.userservice.enums.LoginType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByNickname(String nickname);

    Optional<Account> findByEmailAndLoginType(String email, LoginType loginType);

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
    
}

