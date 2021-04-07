package com.moliang.extension;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Use 扩展加载类
 * @Author Chui moliang
 * @Date 2021/1/23 22:25
 * @Version 1.0
 */
@Slf4j
public class ExtensionLoader<T> {

    /**
     * 代码参考了dubbo中加载扩展的方式
     * 与标准的 SPI 的区别在于
     * (dubbo)文档原话 :
     *  1. JDK 标准的 SPI 会一次性实例化扩展点所有实现，如果有扩展实现初始化很耗时，但如果没用上也加载，会很浪费资源。
     *  2. 如果扩展点加载失败，连扩展点的名称都拿不到了。比如：JDK 标准的 ScriptEngine，
     *      通过 getName() 获取脚本类型的名称，但如果 RubyScriptEngine 因为所依赖的 jruby.jar 不存在，
     *      导致 RubyScriptEngine 类加载失败，这个失败原因被吃掉了，和 ruby 对应不起来，
     *      当用户执行 ruby 脚本时，会报不支持 ruby，而不是真正失败的原因。
     *  3. 增加了对扩展点 IoC 和 AOP 的支持，一个扩展点可以直接 setter 注入其它扩展点
     *
     *  我这里并没有实现 对扩展点 IoC 和 AOP 的支持
     */

    private static final Yaml yaml = new Yaml();
    private static final String FILE_PATH = "META-INF/services/extension.yml";
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
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<String, Class<?>>();
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            URL url = classLoader.getResource(FILE_PATH);
            HashMap<String, LinkedHashMap<String, String>> res = yaml.loadAs(new FileInputStream(url.getFile()), HashMap.class);
            LinkedHashMap<String, String> ans = res.get(type.getName());
            for(Map.Entry<String, String> t : ans.entrySet()) {
                try {
                    Class<?> clazz = classLoader.loadClass(t.getValue());
                    extensionClasses.put(t.getKey(), clazz);
                } catch (ClassNotFoundException e) {
                    log.error("ClassNotFoundException - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("IOException - " + e.getMessage());
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

    /**
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\idiot\\idea\\github\\RPC-moliang\\RPC-moliang-core\\src\\main\\resources\\extension.yml");
        ConcurrentHashMap<String, ArrayList<String>> result = yaml.loadAs(new FileInputStream(file), ConcurrentHashMap.class);
        for(Object e : result.keySet()) {
            System.out.println(e+":\t"+ result.get(e).toString());
        }
    }
     **/
}
