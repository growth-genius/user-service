package com.sgyj.userservice.validator;


import com.sgyj.userservice.dto.TokenDto;
import com.sgyj.userservice.enums.ErrorMessage;
import com.sgyj.userservice.form.LoginForm;
import com.sgyj.userservice.security.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TokenDtoValidator implements Validator {

    private final Jwt jwt;

    @Override
    public boolean supports ( @NonNull Class<?> clazz ) {
        return LoginForm.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate ( @NonNull Object target, @NonNull Errors errors ) {
        TokenDto tokenDto = (TokenDto ) target;
        if ( !jwt.validateToken( tokenDto.getRefreshToken() ) ) {
            errors.rejectValue( "refreshToken", "wrong.value", ErrorMessage.VALIDATE_REFRESH_TOKEN.getMessage() );
        }
    }

}
