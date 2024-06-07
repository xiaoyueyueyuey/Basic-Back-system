package com.xy.admin.common;

import com.xy.AdminApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
class TestTest {
    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    void test1() {
        System.out.println(passwordEncoder.encode("123456"));
        Assert.isTrue(
                passwordEncoder.matches("123456", "$2a$10$8rvjH2kDWpgnCsScahAdduAbUTQpD.lwR5tAcA8yJ686bpEDbH.l")
        , "密码不匹配");
//        IpUtil
//        AsyncTaskFactory
    }
}