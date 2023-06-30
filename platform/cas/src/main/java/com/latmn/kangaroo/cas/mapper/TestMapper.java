package com.latmn.kangaroo.cas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.latmn.kangaroo.cas.model.po.TestPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<TestPo> {
}
