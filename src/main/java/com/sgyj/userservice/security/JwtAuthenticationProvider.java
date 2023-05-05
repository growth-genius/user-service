package com.sgyj.userservice.security;

import com.sgyj.userservice.dto.AccountDto;
import com.sgyj.userservice.form.LoginForm;
import com.sgyj.userservice.service.AccountService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.sgyj.userservice.utils.CommonUtil.authorities;
import static org.springframework.util.ClassUtils.isAssignable;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(authenticationToken.getPrincipal()),
                authenticationToken.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String email, String password) {
        try {
            final AccountDto accountDto = accountService.login( LoginForm.builder()
                    .email( email )
                    .password( password )
                    .build() );
            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(
                            new JwtAuthentication(accountDto.getId(), accountDto.getAccountId(), accountDto.getEmail()),
                            password,
                            authorities( accountDto.getRoles() )
                    );
            authenticated.setDetails(accountDto);
            return authenticated;
        } catch ( NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

}