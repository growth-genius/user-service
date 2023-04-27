package com.sgyj.userservice.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpForm {

    @NotBlank(message = "이름을 입력해 주세요.")
    @Size(min = 3, max = 20, message = "이름을 3자리에서 20자리 사이로 입력해 주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "닉네임은 3자리에서 20자리까지, 한글과 숫자만 가능합니다.")
    private String userName;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern( regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,12}$", message = "숫자와 영문자, 특수 문자 조합으로 8~12자리를 사용해야 합니다.")
    private String password;

    private boolean admin;

}
