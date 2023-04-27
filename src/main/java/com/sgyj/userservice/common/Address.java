package com.sgyj.userservice.common;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    /** 도시 (시/도) */
    private String city;
    /** 도로명 주소 */
    private String roadName;
    /** 상세주소 */
    private String detailAddress;
    /** 시군구 */
    private String siGunGu;
    /** 동 */
    private String dong;
    /** 빌딩 명 */
    private String buildingName;
    /** 빌딩 번호 */
    private String buildingNumber;
    /** 우편번호 */
    private String zoneCode;
    /** 위도 */
    private Double latitude;
    /** 경도 */
    private Double longitude;

}
