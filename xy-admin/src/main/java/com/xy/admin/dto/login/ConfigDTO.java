package com.xy.admin.dto.login;

import com.xy.common.enums.dictionary.DictionaryData;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author valarchie
 */
@Data
public class ConfigDTO {
    //是否使用验证码
    private Boolean isCaptchaOn;
    private Map<String, List<DictionaryData>> dictionary;
}
