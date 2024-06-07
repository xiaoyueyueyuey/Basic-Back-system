package com.xy.admin.customize.securityService;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.xy.admin.customize.async.AsyncTaskFactory;
import com.xy.common.enums.common.LoginStatusEnum;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.system.userLogin.LoginService;
import com.xy.infrastructure.config.XYBootConfig;
import com.xy.infrastructure.thread.ThreadPoolManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;


    @Override
    public void userAuthenticate(String username, String password) {
        // 用户验证
        log.info("加密前密码:{}", password);
        Authentication authentication;
        String decryptPassword = decryptPassword(password);
        log.info("解密后密码:{}", decryptPassword);
        try {
            // 该方法会去调用UserDetailsServiceImpl#loadUserByUsername  校验用户名和密码  认证鉴权
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, decryptPassword));

        } catch (BadCredentialsException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL,
//                MessageUtils.message("Business.LOGIN_WRONG_USER_PASSWORD")
                    "Business.LOGIN_WRONG_USER_PASSWORD"
            ));
            throw new ApiException(e, ErrorCode.Business.LOGIN_WRONG_USER_PASSWORD);
        } catch (AuthenticationException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, ErrorCode.Business.LOGIN_ERROR, e.getMessage());
        } catch (Exception e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, ErrorCode.Business.LOGIN_ERROR, e.getMessage());
        }
        // 把当前登录用户 放入上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 解密密码，这里是前端对用户密码加密的密码，后端需要解密
    public String decryptPassword(String originalPassword) {
        byte[] decryptBytes = SecureUtil.rsa(XYBootConfig.getRsaPrivateKey(), null)
                .decrypt(Base64.decode(originalPassword), KeyType.PrivateKey);
        return StrUtil.str(decryptBytes, CharsetUtil.CHARSET_UTF_8);
    }
}
