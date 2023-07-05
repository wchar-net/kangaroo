package com.latmn.kangaroo.framework.core.cache;

import java.util.concurrent.TimeUnit;

public interface ICache {
    void put(String key, Object value);

    void put(String key, Object value, final long timeout, final TimeUnit unit);


    Object get(String key);

    Object getAndDelete(String key);

    void delete(String key);
}
