package com.xy.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.xy.common.constant.Constants.RESOURCE_PREFIX;

/**
 * 读取项目相关配置
 * TODO 移走  不合适放在这里common包底下
 * @author valarchie
 */
@Component
@ConfigurationProperties(prefix = "xyboot")
@Data
public class XYBootConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private static boolean demoEnabled;

    /**
     * 上传路径
     */
    private static String fileBaseDir;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    private static String captchaType;

    /**
     * rsa private key  静态属性的注入！！ set方法一定不能是static 方法
     */
    private static String rsaPrivateKey;

    private static String apiPrefix;

    public static String getFileBaseDir() {
        return fileBaseDir;
    }

    public void setFileBaseDir(String fileBaseDir) {
        XYBootConfig.fileBaseDir = fileBaseDir  + File.separator + RESOURCE_PREFIX;
    }

    public static String getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String apiDocsPathPrefix) {
        XYBootConfig.apiPrefix = apiDocsPathPrefix;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        XYBootConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        XYBootConfig.captchaType = captchaType;
    }

    public static String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        XYBootConfig.rsaPrivateKey = rsaPrivateKey;
    }

    public static boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        XYBootConfig.demoEnabled = demoEnabled;
    }

}
