package com.latmn.kangaroo.platform.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.latmn.kangaroo.platform.user.model.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserPo> {
}
