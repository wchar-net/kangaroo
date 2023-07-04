package com.latmn.kangaroo.platform.gateway.repository;

import com.latmn.kangaroo.framework.core.domain.UserDomain;
import reactor.core.publisher.Mono;
public interface RBACUserRepository {
    Mono<UserDomain> findUser(String userId);
}
