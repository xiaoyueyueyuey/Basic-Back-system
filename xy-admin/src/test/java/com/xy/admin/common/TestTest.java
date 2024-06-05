package com.xy.admin.common;

import com.xy.AdminApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
class TestTest {
    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    void test1() {
        System.out.println(passwordEncoder.encode("密码"+"123456"));

    }
}