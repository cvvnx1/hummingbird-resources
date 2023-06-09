package com.ganwhat.hummingbird.resources.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author cvvnx1@hotmail.com
 * @since Sep 13, 2021
 */
public class JsonUtil {
    private JsonUtil() {
    }

    private final static ObjectMapper DEFAULT_OM = new ObjectMapper();
    private final static ObjectMapper TOENTITY_OM = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    private final static ObjectMapper DENSITY_OM = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String toJson(Object o) {
        return toJson(o, null);
    }

    public static String toJson(Object o, String def) {
        try {
            return DEFAULT_OM.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static ObjectMapper getMapper() {
        return DEFAULT_OM;
    }

    public static <T> T fromJson(String content, TypeReference<T> tr) {
        return fromJson(content, tr, null);
    }

    public static <T> T fromJson(String content, TypeReference<T> tr, T def) {
        try {
            return DEFAULT_OM.readValue(content, tr);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static <T> T fromJson(String content, JavaType jt) {
        return fromJson(content, jt, null);
    }

    public static <T> T fromJson(String content, JavaType jt, T def) {
        try {
            return DEFAULT_OM.readValue(content, jt);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static <T> T fromJson(String content, Class<? extends T> clazz) {
        return fromJson(content, clazz, null);
    }

    public static <T> T fromJson(String content, Class<? extends T> clazz, T def) {
        try {
            return DEFAULT_OM.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static <T> T jsontoEntity(String content, Class<? extends T> clazz) {
        return jsontoEntity(content, clazz, null);
    }
    public static <T> T jsontoEntity(String json, Class<T> clazz, T def) {
        try {
            return TOENTITY_OM.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static String toDenseJson(Object o) {
        return toDenseJson(o, null);
    }

    public static String toDenseJson(Object o, String def) {
        try {
            return DENSITY_OM.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

}
