package com.yiwise.utils;

import java.util.UUID;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class IdGen {
    public static String genId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
