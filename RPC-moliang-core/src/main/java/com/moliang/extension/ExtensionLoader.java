package com.moliang.extension;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Use 加载类
 * @Author Chui moliang
 * @Date 2021/1/23 22:25
 * @Version 1.0
 */
@Slf4j
public class ExtensionLoader<T> {
    private static final Yaml yaml = new Yaml();
    private static final String FILE_PATH = "extension.yml";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Map<String, Map<String, Class<?>>> caches = new ConcurrentHashMap<>();

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 此静态方法,
     * @param type
     * @param <S>
     * @return 所需类型相对应的加载类
     */
    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("所需类型不能为空");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("所需类型必须是接口类型");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("所需类型必须有@SPI注解");
        }
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    /**
     * 获取到类型所对应加载类后, 返回指定的所需接口的实现类对象
     * @param name
     * @return 返回类型与加载类的指定类型相同
     */
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("实现类名字不能为空");
        }
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("指定实现类不存在 " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.getDeclaredConstructor().newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        // get the loaded extension class from the cache
        Map<String, Class<?>> classes = cachedClasses.get();
        // double check
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<>();
                    // load all extensions from our extensions directory
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        File file = new File(FILE_PATH);
        try {
            HashMap<String, ArrayList<String>> res = yaml.loadAs(new FileInputStream(file), HashMap.class);
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            ArrayList<String> ans = res.get(type);
            for (String str : ans) {
                if (str.length() > 0) {
                    try {
                        final int ei = str.indexOf('=');
                        String name = str.substring(0, ei).trim();
                        String clazzName = str.substring(ei + 1).trim();
                        if (name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    static class Holder<T> {

        private volatile T value;

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\idiot\\idea\\github\\RPC-moliang\\RPC-moliang-core\\src\\main\\resources\\extension.yml");
        ConcurrentHashMap<String, ArrayList<String>> result = yaml.loadAs(new FileInputStream(file), ConcurrentHashMap.class);
        for(Object e : result.keySet()) {
            System.out.println(e+":\t"+ result.get(e).toString());
        }
    }
}
