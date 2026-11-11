package com.sqx.common.core.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/27 15:40
 */
public class CustomBeanUtils extends BeanUtils {

    /**
     * 复制源对象的非空属性到目标对象(如想排除固定的，可以添加excludes)。
     *
     * @param source      源对象
     * @param destination 目标对象
     * @param excludes    要排除的属性名数组
     */
    public static void copyNonNullProperties(Object source, Object destination, String... excludes) {
        PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
        try {
            // 获取源对象的所有属性名
            Map<String, Object> properties = propertyUtils.describe(source);

            // 排除不需要复制的属性
            Set<String> excludeSet = new HashSet<>(Arrays.asList(excludes));
            // 添加 'class' 到排除列表
            excludeSet.add("class");

            for (String propertyName : properties.keySet()) {
                if (!excludeSet.contains(propertyName)) {
                    Object value = properties.get(propertyName);
                    // 只有非 null 的值才进行复制
                    if (value != null) {
                        propertyUtils.setProperty(destination, propertyName, value);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to copy properties", e);
        }
    }


    /**
     * 复制源对象的非空属性到目标对象。
     *
     * @param source      源对象
     * @param destination 目标对象
     */
    public static void copyNotNullProperties(Object source, Object destination) {
        PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
        try {
            // 获取源对象的所有属性名
            Set<String> propertyNames = propertyUtils.describe(source).keySet();

            for (String propertyName : propertyNames) {
                Object value = propertyUtils.getProperty(source, propertyName);
                // 只有非 null 的值才进行复制
                if (value != null) {
                    propertyUtils.setProperty(destination, propertyName, value);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to copy properties", e);
        }
    }

}
