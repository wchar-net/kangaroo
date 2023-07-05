package com.latmn.kangaroo.platform.gateway.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class PathUtil {
    public static boolean pathMatch(String path, List<String> pathList) {
        if (CollectionUtils.isEmpty(pathList)) {
            return false;
        }
        for (String item : pathList) {
            boolean flag = new AntPathMatcher().match(item,path);
            if (flag) {
                return true;
            }
        }
        return false;
    }
}
