package com.latmn.kangaroo.platform.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.latmn.kangaroo.platform.user.model.po.TestPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<TestPo> {
}
