package com.latmn.kangaroo.platform.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.framework.core.exception.UserException;
import com.latmn.kangaroo.platform.user.mapper.UserMapper;
import com.latmn.kangaroo.platform.user.model.po.UserPo;
import com.latmn.kangaroo.platform.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
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
        return UserDomain.builder().id(userPo.getId()).userCode(userPo.getUserCode()).nickName(userPo.getNickName()).lastLogin(userPo.getLastLogin())
                .userState(userPo.getUserState()).createTime(userPo.getCreateTime()).updateTime(userPo.getUpdateTime())
                .expireTime(userPo.getExpireTime()).createBy(userPo.getCreateBy()).updateBy(userPo.getUpdateBy()).build();
    }
}
