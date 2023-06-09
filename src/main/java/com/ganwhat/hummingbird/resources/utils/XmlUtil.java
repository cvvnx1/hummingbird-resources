package com.ganwhat.hummingbird.resources.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

/**
 * @author cvvnx1@hotmail.com
 * @since Jun 08, 2023
 */
public class XmlUtil {

    private XmlUtil() {
    }

    private static final XmlMapper MAPPER = new XmlMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        MAPPER.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    }

    public static String toXml(Object o) {
        return toXml(o, null);
    }

    public static String toXml(Object o, String def) {
        try {
            return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return def;
        }
    }

    public static <T> T fromXml(String content, Class<? extends T> clazz) {
        return fromXml(content, clazz, null);
    }

    public static <T> T fromXml(String content, Class<? extends T> clazz, T def) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            return def;
        }
    }

}
