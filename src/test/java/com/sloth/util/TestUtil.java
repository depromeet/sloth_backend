package com.sloth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

public class TestUtil {
    public static <T> T convert(MvcResult result, Class<T> clazz) throws Exception {
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), clazz);
    }

    public static <T> T convert(MvcResult result, TypeReference<Object> typeReference) throws Exception {
        return (T) new ObjectMapper().readValue(result.getResponse().getContentAsString(), typeReference);
    }

    public static HashMap convert(MvcResult result) throws Exception{
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);
    }
}
