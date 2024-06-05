package com.xy.admin.dto.login;

import lombok.Data;

@Data
public class CaptchaDTO {
    private Boolean isCaptchaOn;
    private String captchaCodeKey;
    private String captchaCodeImg;
}
