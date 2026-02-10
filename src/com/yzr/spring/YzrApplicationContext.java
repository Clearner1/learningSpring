package com.yzr.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class YzrApplicationContext {
    private Class configClass;
    // 存储 bean 的元信息P
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 单例池
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final static String SINGLETON = "singleton";

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
                                    beanDefinition.setScope(SINGLETON);
                                }

                                if (clazz.isAnnotationPresent(Lazy.class)) {
                                    beanDefinition.setLazy(true);
                                }

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                if (beanName.equals("")) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        // 创建 bean 对象 如果是单例 bean 直接创建
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(SINGLETON) &&
                    !beanDefinition.getLazy()) {
                Object bean = createBean(beanName, beanDefinition);
                // 将单例 Bean 存储到单例池
                singletonObjects.put(beanName, bean);
            }
        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object instance = clazz.getConstructor().newInstance();

            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true);
                    // IOC: 在容器中查找当前Name 的 Bean
                    f.set(instance, getBean(f.getName()));
                }
            }

            // Aware 回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + beanName, e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null) {
            throw new NullPointerException();
        } else {
            String scope = beanDefinition.getScope();
            if (scope.equals(SINGLETON)) {
                Object bean = singletonObjects.get(beanName);
                if (bean == null) { // 为了实现懒加载机制
                    Object bean2 = createBean(beanName, beanDefinition);
                    // 创建 Bean 之后需要放到单例池中
                    singletonObjects.put(beanName, bean2);
                    return bean2;
                }
                return bean;
            } else {
                // 多例
                return createBean(beanName, beanDefinition);
            }
        }
    }
}
