package com.latmn.kangaroo.platform.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest extends UserApplicationTests {
    @Test
    void testPwd() {
        String pwd = "admin123456...";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePwd = bCryptPasswordEncoder.encode(pwd);
        System.out.println(encodePwd);
        BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();
        String encodePwd2 = bCryptPasswordEncoder2.encode(pwd);
        System.out.println(bCryptPasswordEncoder2.matches(pwd, encodePwd2));
    }
}
