package com.latmn.kangaroo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.latmn.kangaroo.user.mapper.TestMapper;
import com.latmn.kangaroo.user.model.po.TestPo;
import com.latmn.kangaroo.user.model.vo.TestVo;
import com.latmn.kangaroo.user.service.TestService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestPo> implements TestService {
    private final TestMapper testMapper;

    public TestServiceImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }


    @Override
    public List<TestVo> findAll() {
        List<TestPo> pos = testMapper.selectList(null);
        if (CollectionUtils.isEmpty(pos)) {
            return new ArrayList<>();
        } else {
            return pos.stream().map(it -> TestVo.builder().name(it.getName()).uuid(it.getUuid()).build()).toList();
        }
    }

    @Override
    public TestVo findTestById(String id) {
        Assert.hasText(id, "id不能为空!");
        TestPo po = getById(id);
        if (null == po) {
            return new TestVo();
        } else {
            return TestVo.builder().name(po.getName()).uuid(po.getUuid()).build();
        }
    }
}
