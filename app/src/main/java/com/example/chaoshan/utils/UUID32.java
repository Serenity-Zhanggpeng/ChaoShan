package com.example.chaoshan.utils;

import java.util.UUID;

public class UUID32 {
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
//  return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
