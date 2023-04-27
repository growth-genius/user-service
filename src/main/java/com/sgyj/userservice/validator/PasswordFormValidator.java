package com.sgyj.userservice.validator;


import com.sgyj.userservice.enums.ErrorMessage;
import com.sgyj.userservice.form.PasswordForm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports ( @NonNull Class<?> clazz ) {
        return PasswordForm.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate ( @NonNull Object target, @NonNull Errors errors ) {
        PasswordForm passwordForm = ( PasswordForm ) target;
        if(!passwordForm.getNewPassword().equals( passwordForm.getNewPasswordConfirm() )) {
            errors.rejectValue( "newPassword", ErrorMessage.WRONG_VALUE.getMessage(), ErrorMessage.NEW_PASSWORD.getMessage() );
        }
    }

}
