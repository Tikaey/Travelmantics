package com.aptitude.challengetwo.Util;

import java.util.UUID;

public class RecordIDGenerator {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
