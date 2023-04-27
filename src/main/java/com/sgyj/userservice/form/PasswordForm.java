package com.sgyj.userservice.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForm {

    @NotNull(message = "기존 비밀번호를 입력해 주세요.")
    private String originPassword;

    @NotNull(message = "변경하실 비밀번호를 입력해 주세요.")
    @Pattern( regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,12}$", message = "숫자와 영문자, 특수 문자 조합으로 8~12자리를 사용해야 합니다.")
    private String newPassword;

    @NotEmpty(message = "비밀번호 확인을 입력해 주세요.")
    private String newPasswordConfirm;

}
