package com.latmn.kangaroo.platform.user.controller;


import com.latmn.kangaroo.platform.user.model.vo.UserLoginVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户接口", description = "用户接口")
@RestController
public class UserController {

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody UserLoginVo userLoginVo
    ) {
        return "abc";
    }
}
