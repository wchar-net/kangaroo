package com.latmn.kangaroo.platform.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.platform.user.model.po.UserPo;

public interface UserService extends IService<UserPo> {

    UserDomain login(String userCode, String userPwd);
}
