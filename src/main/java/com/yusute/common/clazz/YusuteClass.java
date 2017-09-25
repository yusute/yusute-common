package com.yusute.common.clazz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yusutehot
 */
public class YusuteClass<T> {

    private YusuteClass(T obj) {
        this.obj = obj;
    }

    private T obj;

    public String getClassName() {
        return obj.getClass().getName();
    }

    public String getClassSimpleName() {
        return obj.getClass().getSimpleName();
    }

    /**
     * 初始化对象
     */
    public static <T> Optional<YusuteClass<T>> newObject(Class<T> cls) {
        try {
            Optional<T> optional = Optional.ofNullable(cls.newInstance());
            T t = optional.get();
            return Optional.ofNullable(new YusuteClass<T>(t));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public <V> void setProperty(String name, V value) {
        try {
            List<Field> fields = getDeclaredField(name);
            if (fields.size() == 1){
                Field field = fields.get(0);
                field.setAccessible(true);// 设置操作权限为true
                field.set(obj, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setProperties(Map<String, ?> properties) {
        for (Map.Entry<String, ?> entry : properties.entrySet()) {
            setProperty(entry.getKey(), entry.getValue());
        }
    }

    public <T> T getProperty(String name) {
        try {
            List<Field> declaredFields = getDeclaredField(name);
            if (declaredFields.size() == 1) {
                Field field = declaredFields.get(0);
                field.setAccessible(true);// 设置操作权限为true
                Object o = field.get(obj);
                return (T) o;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getOject() {
        return obj;
    }

    private List<Field> getDeclaredField(String name) {
        List<Field> declaredFields = new ArrayList<>();
        for (Class<?> cls = obj.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            Field[] fields = cls.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase(name)) {
                        List<Field> newDeclaredFields = new ArrayList<>();
                        newDeclaredFields.add(field);
                        return newDeclaredFields;
                    }
                    declaredFields.add(field);
                }
            }
        }
        return declaredFields;
    }

    public List<Field> getDeclaredFields() {
        return getDeclaredField(null);
    }
}
