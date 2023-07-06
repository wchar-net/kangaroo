package com.latmn.kangaroo.platform.user.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.core.cache.ICache;
import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.framework.core.exception.UserException;
import com.latmn.kangaroo.framework.core.util.IdUtil;
import com.latmn.kangaroo.platform.user.model.vo.CaptchaVo;
import com.latmn.kangaroo.platform.user.model.vo.UserLoginVo;
import com.latmn.kangaroo.platform.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@Tag(name = "用户接口", description = "用户接口")
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final ICache redisCache;
    private final ObjectMapper objectMapper;

    private final UserService userService;

    public UserController(ICache redisCache, ObjectMapper objectMapper, UserService userService) {
        this.redisCache = redisCache;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/getCaptcha")
    public CaptchaVo getCaptcha() {
        //长、宽、验证码字符数、干扰元素个数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        String imageBase64Data = lineCaptcha.getImageBase64();

        String captchaHeader = IdUtil.uuid32();
        String captchaVale = lineCaptcha.getCode();
        //3分钟内有效
        redisCache.put(Define.CACHE_CAPTCHA_PREFIX + captchaHeader, captchaVale, 3, TimeUnit.MINUTES);

        logger.info("captchaVale: {} captchaHeader: {}", captchaVale, captchaHeader);
        return CaptchaVo.builder().base64Img(imageBase64Data).captchaHeader(captchaHeader).build();
    }


    @Operation(summary = "登录")
    @PostMapping("/login")
    public String login(
            @Valid @RequestBody UserLoginVo userLoginVo
    ) {
        Object captchaValueObj = redisCache.get(Define.CACHE_CAPTCHA_PREFIX + userLoginVo.getCaptchaHeader());
        if (null == captchaValueObj) {
            throw new UserException(Define.CAPTCHA_NOT_EXIST);
        }
        String captchaValue = (String) captchaValueObj;
        if (!StringUtils.hasText(captchaValue)) {
            throw new UserException(Define.CAPTCHA_NOT_EXIST);
        }
        if (!userLoginVo.getCaptchaValue().equalsIgnoreCase(captchaValue)) {
            throw new UserException(Define.CAPTCHA_NOT_INCORRECT);
        }
        UserDomain domain = userService.login(userLoginVo.getUserCode(), userLoginVo.getUserPwd());
        if (null == domain || !StringUtils.hasText(domain.getId())) {
            throw new UserException(Define.ACCOUNT_OR_PASSWORD_ERROR);
        }
        //删除redis缓存
        redisCache.delete(Define.CACHE_CAPTCHA_PREFIX + userLoginVo.getCaptchaHeader());
        //30天
        String authToken = IdUtil.uuid32();
        redisCache.put(Define.CACHE_USER_PREFIX + authToken, domain, 30, TimeUnit.DAYS);
        return authToken;
    }


    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    public UserDomain getUser(
            HttpServletRequest request
    ) {
        String userId = request.getHeader(Define.USER_REQUEST_KEY);
        return userService.getUserInfo(userId);
    }
}
