package com.moliang.util;

import com.moliang.convention.enums.RpcConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.net.URL;
import java.util.*;

/** yml文件读取
 * @Use yml文件读取工具类
 * @Author Chui moliang
 * @Date 2021/2/4 16:55
 * @Version 1.0
 */
@Slf4j
public class YamlFileUtil {

    private static String FILENAME = RpcConfigEnum.RPC_CONFIG_YAML_PATH.getPropertyValue();

    private YamlFileUtil() {
    }

    public static String readPropertiesFile(String typeName)  {
        URL url = Thread.currentThread().getContextClassLoader().getResource(FILENAME);
        String ans = null;
        try {
            Yaml yaml = new Yaml();
            HashMap<String, String> res = yaml.loadAs(new FileInputStream(url.getFile()), HashMap.class);
            ans = res.get(typeName);
        } catch (Exception e) {
            log.error("读取yml配置文件时发生异常 [{}]", FILENAME + " - " + typeName);
        }
        return ans;
    }
}
