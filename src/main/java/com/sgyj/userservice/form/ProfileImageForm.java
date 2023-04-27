package com.sgyj.userservice.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileImageForm {

    @NotBlank(message = "프로필 사진을 선택해 주세요.")
    private String profileImage;

}
