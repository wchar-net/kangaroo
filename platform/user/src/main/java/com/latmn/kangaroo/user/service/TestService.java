package com.latmn.kangaroo.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.latmn.kangaroo.user.model.po.TestPo;
import com.latmn.kangaroo.user.model.vo.TestVo;

import java.util.List;

public interface TestService extends IService<TestPo> {
    List<TestVo> findAll();
    TestVo findTestById(String id);
}
