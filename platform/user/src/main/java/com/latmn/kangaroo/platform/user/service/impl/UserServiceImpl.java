package com.latmn.kangaroo.platform.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.framework.core.exception.UserException;
import com.latmn.kangaroo.platform.user.mapper.UserMapper;
import com.latmn.kangaroo.platform.user.model.po.UserPo;
import com.latmn.kangaroo.platform.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDomain login(String userCode, String userPwd) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserPo userPo = lambdaQuery().eq(UserPo::getDelFlag, 1).eq(UserPo::getUserCode, userCode).one();
        if (null == userPo || !StringUtils.hasText(userPo.getId())) {
            return null;
        }
        if (!bCryptPasswordEncoder.matches(userPwd, userPo.getUserPwd())) {
            return null;
        }
        boolean flag = lambdaUpdate().eq(UserPo::getDelFlag, 1).eq(UserPo::getId, userPo.getId()).set(UserPo::getLastLogin, LocalDateTime.now()).update();
        if (!flag) {
            throw new UserException("登录失败!");
        }
        return conventUserDomain(userPo);
    }

    private UserDomain conventUserDomain(UserPo userPo) {
        return UserDomain.builder().id(userPo.getId()).userCode(userPo.getUserCode()).nickName(userPo.getNickName()).lastLogin(userPo.getLastLogin())
                .userState(userPo.getUserState()).createTime(userPo.getCreateTime()).updateTime(userPo.getUpdateTime())
                .expireTime(userPo.getExpireTime()).createBy(userPo.getCreateBy()).updateBy(userPo.getUpdateBy()).build();
    }

    @Override
    public UserDomain getUserInfo(String id) {
        UserPo userPo = getById(id);
        if (null == userPo) {
            throw new UserException(Define.AUTH_TOKEN_ABSENT);
        }
        Integer userState = userPo.getUserState();
        if (null == userState) {
            logger.info("用户状态配置不正确: {}", userPo);
            throw new UserException(Define.AUTH_TOKEN_ABSENT);
        }
        if (1 != userState) {
            logger.info("用户状态 != 1: {}", userPo);
            throw new UserException(Define.AUTH_TOKEN_ABSENT);
        }
        LocalDateTime expireTime = userPo.getExpireTime();
        if (null != expireTime) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(expireTime)) {
                logger.info("用户已经过期! userDomain = {}, expireTime = {},currentTime = {}", userPo, expireTime, currentTime);
                throw new UserException(Define.AUTH_TOKEN_ABSENT);
            }
        }
        return conventUserDomain(userPo);
    }
}
