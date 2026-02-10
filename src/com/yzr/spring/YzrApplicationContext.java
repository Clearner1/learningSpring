package com.yzr.spring;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class YzrApplicationContext {
    private Class configClass;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public YzrApplicationContext(Class configClass) {
        this.configClass = configClass;
        // 检查配置类中是否带有 ComponentScan 注解，快速定位 bean 的位置
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            // @ComponentScan("com.yzr.service")
            path = path.replace(".", "/"); // com/yzr/service
            // 转换为绝对路径
            ClassLoader classLoader = YzrApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            // 需要解码 URL，因为路径中的空格会被编码为 %20
            String decodedPath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
            File file = new File(decodedPath);
            System.out.println(file);
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();

                for (File f : listFiles) {
                    String absolutePath = f.getAbsolutePath();
                    // System.out.println(absolutePath);
                    if (absolutePath.endsWith(".class")) {
                        // 获取到每一个.class 文件：com/yzr/service/AppConfig.class
                        String className = absolutePath.substring(absolutePath.indexOf("com"),
                                absolutePath.indexOf(".class"));
                        className = className.replace("/", ".");
                        System.out.println(className);
                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            // .class 内有 Component 注解
                            if (clazz.isAnnotationPresent(Component.class)) {

                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    String scopeValue = scopeAnnotation.value();
                                    beanDefinition.setScope(scopeValue);
                                } else {
                                    // 默认不添加注解为单例
                                    beanDefinition.setScope("singletion");
                                }

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                beanDefinitionMap.put(beanName, beanDefinition);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

    }

    public Object getBean(String beanName) {

        return null;

    }
}
