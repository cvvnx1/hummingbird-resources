package com.ganwhat.hummingbird.resources.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created Time : 2018-02-08 12:40
 * Author       : cvvnx1@hotmail.com
 */
@Slf4j
public class GreedyWrapBean implements Iterable<GreedyWrapBean> {

    public static final GreedyWrapBean NULL_BEAN = new GreedyWrapBean(null);

    private static final String THIS = "this";

    private GreedyWrapBean parent = null;
    private final Object instance;
    private final Class<?> clazz;
    private final Map<String, GreedyWrapBean> cache = new ConcurrentHashMap<>();

    public GreedyWrapBean(Object instance) {
        this(instance, instance == null ? null : instance.getClass());
    }

    private Field field(String name) {
        if (clazz == null) {
            return null;
        }
        try {
            return FieldUtils.getField(clazz, name, true);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public GreedyWrapBean(Object instance, Class<?> clazz) {
        this.instance = instance;
        this.clazz = clazz;
    }

    private Object value(Field field) {
        if (instance == null || field == null) {
            return null;
        }
        try {
            return FieldUtils.readField(field, instance, true);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public Object value() {
        return instance;
    }

    public Object value(Object def) {
        return (null != instance) ? instance : def;
    }

    public <T> T value(Class<T> clazz, T def) {
        return (null == instance) ? def : clazz.cast(instance);
    }

    public String stringValue() {
        return (null != instance) ? String.valueOf(instance) : null;
    }

    public String stringValue(String def) {
        try {
            String value = (null != instance) ? String.valueOf(instance) : def;
            return StringUtils.isEmpty(value.trim()) ? def : value;
        } catch (Exception e) {
            return def;
        }
    }


    public Integer intValue(Integer def) {
        try {
            return Integer.valueOf(stringValue());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public BigDecimal decimalValue(BigDecimal def) {
        try {
            return new BigDecimal(stringValue().trim().replace(",", ""));
        } catch (NullPointerException | NumberFormatException e) {
            return def;
        }
    }

    public Date dateValue(String fmt, Date def) {
        try {
            return DateUtils.parseDate(stringValue(""), fmt);
        } catch (Exception e) {
            log.warn("date获取异常", e);
            return def;
        }
    }

    public Date dateValue(Date def, String... fmt) {
        try {
            return DateUtils.parseDate(stringValue(""), fmt);
        } catch (Exception e) {
            log.warn("date获取异常", e);
            return def;
        }
    }

    public GreedyWrapBean g(String path) {
        return get(path);
    }

    public GreedyWrapBean get(String path) {
        if (StringUtils.isBlank(path)) {
            return this;
        }
        String[] names = path.split("\\.");
        GreedyWrapBean cur = this;
        for (String name : names) {
            cur = cur.sampleGet(name);
        }
        return cur;
    }

    private GreedyWrapBean sampleGet(String name) {
        if (name.matches("\\S+\\[\\d+\\]")) {
            return indexGet(name);
        }
        if (Objects.equals(THIS, name)) {
            return this;
        }
        try {
            GreedyWrapBean wrapBean;
            if (cache.containsKey(name)) {
                return cache.get(name);
            } else {
                Object value = value(field(name));
                if (null == value) {
                    wrapBean = new GreedyWrapBean(null, null);
                } else {
                    wrapBean = new GreedyWrapBean(value, value.getClass());
                }
                cache.put(name, wrapBean);
                wrapBean.parent = this;
                return wrapBean;
            }
        } catch (IllegalArgumentException e) {
            return new GreedyWrapBean(null, null);
        }
    }

    private GreedyWrapBean indexGet(String name) {
        String[] split = name.split("[\\[\\]]");
        Iterator<GreedyWrapBean> itr = sampleGet(split[0]).iterator();
        int index = NumberUtils.toInt(split[1]);
        GreedyWrapBean result = new GreedyWrapBean(null);
        while (itr.hasNext()) {
            result = itr.next();
            if (--index < 0) {
                break;
            }
        }
        if (index >= 0 || index < -1) {
            result = new GreedyWrapBean(null);
        }
        return result;
    }

    public GreedyWrapBean getParent() {
        return parent;
    }

    @Override
    public Iterator<GreedyWrapBean> iterator() {
        if (instance instanceof Collection) {
            return new Itr((Collection) instance);
        } else {
            return new Itr(new ArrayList<>());
        }
    }

    public Stream<GreedyWrapBean> stream() {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.ORDERED), false);
    }

    private class Itr implements Iterator<GreedyWrapBean> {

        int cursor;
        Object[] elements;

        public Itr(Collection<GreedyWrapBean> col) {
            elements = col.toArray();
        }

        @Override
        public boolean hasNext() {
            return cursor != elements.length;
        }

        @Override
        public GreedyWrapBean next() {
            int i = cursor;
            if (i >= elements.length) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return new GreedyWrapBean(elements[i], elements[i].getClass());
        }

    }

}
