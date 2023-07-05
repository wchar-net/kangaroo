package com.latmn.kangaroo.platform.gateway.repository;

import com.latmn.kangaroo.framework.core.domain.UserDomain;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RBACUserRepository {
    Mono<UserDomain> findUser(String userId);

    /**
     * 获取某个用户能访问的所有uri
     *
     * @param userId
     * @return
     */
    Mono<List<String>> findAllUserUri(String userId);
}
