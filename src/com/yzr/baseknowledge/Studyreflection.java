package com.yzr.baseknowledge;

import java.lang.reflect.Field;

public class Studyreflection {
    public static void main1(String[] args) throws Exception {
        // 通过配置类中的类名，反射的方式获取一个类
        String classNameFromConfig = "com.yzr.baseknowledge.testRelection";
        Class<?> clazz = Class.forName(classNameFromConfig);
        // 通过无参构造创建对象
        Object obj = clazz.getDeclaredConstructor().newInstance();
        String name = obj.getClass().getName();
        System.out.println(name);

        Integer yItem = 666;

        // 我们可以直接通过clazz修改对象obj的值，我们也可以使用obj来显示修改
        // 但是obj无法修改private类型的属性
        Field filed = clazz.getDeclaredField("y");
        filed.set(obj, yItem);
        System.out.println(((testRelection) obj).y);
        printObjectDetail(obj);

        printObjectDetail("Hello Reflection"); // 传个 String
    }

    public static void printObjectDetail(Object obj) {
        Class<?> c = obj.getClass();
        String simpleName = c.getSimpleName();
        System.out.println("printObjectDetail->" + simpleName);
        Field[] fields = c.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("  这个类没有定义字段");
        }
        for (Field field : fields) {

            try {
                field.setAccessible(true); // 防止字段是private
                System.out.println("  字段名: " + field.getName());
                System.out.println("  字段值: " + field.get(obj));
            } catch (Exception e) {
                // 如果是 InaccessibleObjectException，说明撞墙了
                System.out.println("  字段名: " + field.getName() + " [无法读取：受 Java 模块保护]");
            }

        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> class1 = testRelection.class;
        Object obj = class1.getConstructor().newInstance();
        System.out.println(obj);
    }
}
