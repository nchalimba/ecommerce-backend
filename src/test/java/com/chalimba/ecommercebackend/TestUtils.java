package com.chalimba.ecommercebackend;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
