package com.flipkart.redis;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saurabh.agrawal on 21/10/14.
 */
public abstract class AbstractRedisRepo<T> {

    private final Class<T> clazz;

    private final Provider<RedisClient> redisClientProvider;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    public AbstractRedisRepo(Class<T> clazz, Provider<RedisClient> redisClientProvider) {
        this.clazz = clazz;
        this.redisClientProvider = redisClientProvider;
    }

    /**
     * The entity name is used as the redis key prefix.
     * @return
     */
    protected String getEntityName() {
        return clazz.getSimpleName();
    }

    /**
     * An array of attribute names of the entity T that are to be serialized
     * @return
     */
    // TODO - Default implementation to get list of attributes from clazz using reflection
    protected abstract String[] getEntityProperties();

    /**
     *
     * @param key The logical key
     * @return The raw key used for redis storage
     */
    protected String getRedisKey(String key) {
        return getEntityName() + ":" + key;
    }

    /**
     * Store an entity in Redis
     * @param key The logical key of the entity
     * @param bean The entity object to store
     * @return
     */
    protected T writeToRedis(String key, T bean) {
        Map<String, String> redisValue = new HashMap<String, String>();
        try {
            for (String attr : getEntityProperties()) {
                Object value = PropertyUtils.getSimpleProperty(bean, attr);
                if (value != null) {
                    String valueStr = formatAttribute(attr, value);
                    redisValue.put(attr, valueStr);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        RedisClient redisClient = redisClientProvider.get();
        redisClient.hmset(getRedisKey(key), redisValue);

        return bean;
    }

    /**
     * Read an entity from Redis
     * @param key The logical key of the entity
     * @return The entity associated with the input key, or null if no such entity exists.
     */
    protected T readFromRedis(String key) {
        RedisClient redisClient = redisClientProvider.get();
        List<String> attrValues = redisClient.hmget(getRedisKey(key), getEntityProperties());

        try {
            T bean = clazz.newInstance();
            int index = 0;
            for (String attr : getEntityProperties()) {
                String valueStr = attrValues.get(index++);
                if (valueStr != null) {
                    Class<?> propType = PropertyUtils.getPropertyType(bean, attr);
                    Object value = parseAttribute(attr, valueStr, propType);
                    PropertyUtils.setSimpleProperty(bean, attr, value);
                }
            }

            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deserializes an entity attribute
     * @param name Attribute name
     * @param value Attribute value
     * @param type Attribute data type
     * @return
     */
    protected Object parseAttribute(String name, String value, Class<?> type) {
        Object parsedValue = value;
        if (type.equals(Long.class)) {
            parsedValue = Long.parseLong(value);
        } else if (type.equals(Integer.class)) {
            parsedValue = Integer.parseInt(value);
        } else if (type.equals(Boolean.class)) {
            parsedValue = Boolean.parseBoolean(value);
        } else if (type.equals(Date.class)) {
            try {
                parsedValue = df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return parsedValue;
    }

    /**
     * Serializes an entity attribute
     * @param name Attribute name
     * @param obj Attribute value
     * @return
     */
    protected String formatAttribute(String name, Object obj) {
        return (obj instanceof Date) ? df.format(obj) : String.valueOf(obj);
    }

    /**
     * Expire an entity after a specified duration
     * @param key The logical key of the entity
     * @param seconds Duration after the which the entity should expire
     * @return
     */
    protected Long expireEntity(String key, int seconds) {
        String redisKey = getRedisKey(key);
        RedisClient redisClient = redisClientProvider.get();
        return redisClient.expire(redisKey, seconds);
    }

}
