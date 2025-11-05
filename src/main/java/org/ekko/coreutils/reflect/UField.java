package org.ekko.coreutils.reflect;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 Ekko All rights reserved.                          *|
|*                                                                                  *|
|*    Licensed under the Apache License, Version 2.0 (the "License");               *|
|*    you may not use this file except in compliance with the License.              *|
|*    You may obtain a copy of the License at                                       *|
|*                                                                                  *|
|*        http://www.apache.org/licenses/LICENSE-2.0                                *|
|*                                                                                  *|
|*    Unless required by applicable law or agreed to in writing, software           *|
|*    distributed under the License is distributed on an "AS IS" BASIS,             *|
|*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.      *|
|*    See the License for the specific language governing permissions and           *|
|*    limitations under the License.                                                *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

/* Creates on 2019/5/16. */

import org.ekko.coreutils.Assert;
import org.ekko.coreutils.Rethrow;
import org.ekko.coreutils.TryUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.ekko.coreutils.string.StringUtils.strfmt;

/**
 * `UField` 类封装了 Java 反射中的 `Field` 对象，提供对字段的访问和操作功能。
 *
 * <p>该类允许通过反射机制获取字段信息，读取和写入字段值，并支持属性的复制和注解检查等操作。
 *
 * <h2>主要功能</h2>
 * <ul>
 *     <li>封装了 `Field` 对象，用于访问字段的元数据和操作字段值。</li>
 *     <li>支持通过字段名称和类描述符查找字段。</li>
 *     <li>提供读取、写入字段值的方法，以及将字段值从一个对象复制到另一个对象的方法。</li>
 *     <li>支持检查字段上的注解。</li>
 * </ul>
 *
 * @author Ekko
 * @since 1.0
 */
public class UField {
    /**
     * 成员属性对象
     */
    private final Field field;
    /**
     * 属性修饰符
     */
    private final int modifiers;
    /**
     * 属性所在类
     */
    private final Class<?> inClass;
    /**
     * 属性路径
     */
    private final String path;
    /**
     * 属性类型
     */
    private final Class<?> originType;
    /**
     * 成员名称
     */
    private final String name;

    /**
     * 线程安全的字段元数据缓存类
     *
     * <p>该类提供对类字段元数据(UField)的高效缓存和访问功能，采用双重映射结构存储：
     * <ul>
     *   <li>第一级映射：Class对象 → 字段映射表</li>
     *   <li>第二级映射：字段名称 → UField对象</li>
     * </ul>
     *
     * <p>特性：
     * <ul>
     *   <li>全线程安全：使用ConcurrentHashMap实现并发访问</li>
     *   <li>惰性初始化：字段映射表在首次访问时创建</li>
     *   <li>原子操作：computeIfAbsent保证初始化操作的原子性</li>
     * </ul>
     *
     * <p>典型使用场景：
     * <pre>
     * // 获取字段元数据
     * UField field = cache.get(User.class, "username");
     *
     * // 缓存新字段
     * cache.put(Order.class, new UField(...));
     * </pre>
     */
    static class Cache {
        /**
         * 核心缓存存储结构
         * Key: 目标类的Class对象
         * Value: 字段名称到UField对象的并发映射
         */
        private final Map<Class<?>, Map<String, UField>> cache = new ConcurrentHashMap<>();

        /**
         * 检查缓存中是否存在指定字段
         *
         * <p>该方法原子性地检查两个条件：
         * <ol>
         *   <li>类是否已注册到缓存</li>
         *   <li>指定名称的字段是否存在</li>
         * </ol>
         *
         * @param inClass 目标类的Class对象(非null)
         * @param name 字段名称(非null)
         * @return 当且仅当字段存在时返回true
         * @throws NullPointerException 如果任一参数为null
         */
        public boolean contains(Class<?> inClass, String name) {
            return cache.containsKey(inClass) && cache.get(inClass).containsKey(name);
        }

        /**
         * 添加或更新字段缓存
         *
         * <p>原子性操作流程：
         * <ol>
         *   <li>如果类未注册，创建新的字段映射表</li>
         *   <li>将字段存入映射表</li>
         *   <li>返回之前同名字段(如果存在)</li>
         * </ol>
         *
         * @param inClass 目标类的Class对象(非null)
         * @param field 要缓存的UField对象(非null)
         * @return 被替换的旧字段对象，如无则返回null
         * @throws NullPointerException 如果任一参数为null
         */
        public UField put(Class<?> inClass, UField field) {
            return cache.computeIfAbsent(inClass, k -> new ConcurrentHashMap<>())
                    .put(field.getName(), field);
        }

        /**
         * 获取缓存的字段元数据
         *
         * <p>注意：
         * <ul>
         *   <li>不执行自动初始化，未缓存的类将返回null</li>
         *   <li>对返回对象的修改会影响缓存内容</li>
         * </ul>
         *
         * @param inClass 目标类的Class对象(非null)
         * @param name 字段名称(非null)
         * @return 对应的UField对象，未找到时返回null
         * @throws NullPointerException 如果参数为null或类未缓存
         * @throws NullPointerException 如果类已缓存但get(inClass)返回null
         */
        public UField get(Class<?> inClass, String name) {
            return cache.get(inClass).get(name);
        }
    }

    private static final Cache _cache = new Cache();

    private UField(String name, Class<?> inClass) {
        this(findDescriptorField(name, inClass));
    }

    /**
     * #brief：通过`Field`对象初始化`ObjectField`<p>
     *
     * 通过`Field`对象初始化`ObjectField`, 这个构造器会通过 {@code field} 参数
     * 获取属性的名称，类型等元数据信息。从而初始化`ObjectField`对象实例。
     *
     * @param field
     *        属性
     */
    private UField(Field field) {
        this.field = field;
        this.modifiers = field.getModifiers();
        this.inClass = field.getDeclaringClass();
        this.name = field.getName();
        this.path = strfmt("%s#%s", inClass.getName(), name);
        this.originType = field.getType();
    }

    public static UField get(Field field) {
        return get(field.getDeclaringClass(), field.getName());
    }

    public static UField get(Class<?> inClass, String name) {
        if (!_cache.contains(inClass, name))
            _cache.put(inClass, new UField(name, inClass));
        return _cache.get(inClass, name);
    }

    ///////////////////////////////////////////////////////////////////////////
    /// get
    ///////////////////////////////////////////////////////////////////////////

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    /**
     * 返回当前属性对象类型
     */
    public UClass getType() {
        return new UClass(originType);
    }

    /**
     * 返回当前属性原始对象类型
     */
    public Class<?> getOriginType() {
        return originType;
    }

    /**
     * 当前成员是否使用了`static`修饰
     */
    public boolean isStatic() {
        return (modifiers & Modifier.STATIC) > 0;
    }

    /**
     * 当前成员是否使用了`final`修饰
     */
    public boolean isFinal() {
        return (modifiers & Modifier.FINAL) > 0;
    }

    /** 属性数据读取接口 */
    interface FieldReader<T> { T read(); }
    /** 属性数据写入接口 */
    interface FieldWriter { void write(Object value); }

    /**
     * 可访问的`Field`对象实例，该实例默认`Field`对象的
     * `Accessible`为true；并且支持读写操作。
     */
    static class FieldDescriptor implements FieldReader<Object>, FieldWriter {
        private final Field field; /* 可访问的 field */
        private final Object instance;
        /**
         * 创建 Field
         */
        public FieldDescriptor(Field field, Object instance) {
            this.field = field;
            this.field.setAccessible(true);
            this.instance = instance;
        }
        /**
         * 获取当前属性中的值
         */
        public Object read() {
            return Rethrow.allow(() -> field.get(instance));
        }
        /**
         * 设置当前属性中的值
         */
        public void write(Object value) {
            Rethrow.allow(() -> field.set(instance, value));
        }
    }

    private FieldDescriptor createFieldDescriptor(Object obj) {
        return new FieldDescriptor(field, obj);
    }

    /**
     * 查找 {@code descriptor} 参数中是否存在 {@code name} 属性。这个
     * 函数会优先从当前 {@code descriptor} 类对象中查找属性;<p>
     *
     * 如果当前类中找不到 {@code name} 属性则会从父类一直往上查找。直到找不到为止。如果
     * 存在 {@code name} 属性则返回该属性 Field 对象。不存在则抛出异常。
     */
    static Field findDescriptorField(String name, Class<?> descriptor) {
        Field field = TryUtils.ifError(() -> descriptor.getDeclaredField(name),
                findDescriptorField0(name, descriptor));
        Assert.notNull(field, "属性 %s 在 %s 类中不存在", name, descriptor.getName());
        return field;
    }

    /** 递归从 {@code descriptor} 的父类查找 {@code name} 属性 */
    static Field findDescriptorField0(String name, Class<?> descriptor) {
        Field rfield;
        Class<?> superclass = descriptor.getSuperclass();

        /* 如果没有父类直接跳出该方法 */
        if (superclass == null)
            return null;

        if ((rfield = TryUtils.ifError(() -> superclass.getDeclaredField(name), null)) == null)
            rfield = findDescriptorField0(name, superclass);

        return rfield;
    }

    /**
     * #brief：根据属性名称写入对象属性数据<p>
     *
     * 根据属性名写入数据到指定属性中，通过反射获取属性`Field`并对这个属性
     * 对象进行反射赋值。不会经过`set`函数。
     *
     * @param instance
     *        对象实例
     */
    public Object read(Object instance) {
        return createFieldDescriptor(instance).read();
    }

    /**
     * #brief：根据属性名称写入对象属性数据<p>
     *
     * 根据属性名写入数据到指定属性中，通过反射获取属性`Field`并对这个属性
     * 对象进行反射赋值。不会经过`set`函数。
     *
     * @param value
     *        写入的数据
     *
     * @param instance
     *        对象实例
     */
    public void write(Object instance, Object value) {
        createFieldDescriptor(instance).write(value);
    }

    /**
     * #brief：将`src`实例中的当前属性对象拷贝到`dest`中<p>
     *
     * 将`src`实例中的当前属性对象拷贝到`dest`中，首先必须确保`src`对象中
     * 和`dest`对象中都存在当前`Field`属性。否则拷贝失败。
     *
     * @param src
     *        源对象实例
     *
     * @param dest
     *        目标对象实例
     */
    public static void copy(Object src, Object dest, String name) {
        /* read value of src. */
        Object value = UField.get(src.getClass(), name).read(src);
        /* write to dest. */
        UField destUField = UField.get(dest.getClass(), name);
        destUField.write(dest, value);
    }

    /**
     * #brief：将`src`实例中的当前属性对象拷贝到`dest`中（字段不存在则忽略）<p>
     *
     * 将`src`实例中的当前属性对象拷贝到`dest`中，如果字段不存在则忽略掉
     * 不抛出异常。
     *
     * @param src
     *        源对象实例
     *
     * @param dest
     *        目标对象实例
     */
    public static void copyIgnoreError(Object src, Object dest, String name) {
        Rethrow.swallow(() -> copy(src, dest, name));
    }

    /**
     * #brief：判断当前属性上是否存在 {@code annotation} 注解<p>
     *
     * 判断当前属性上是否存在 {@code annotation} 注解，如果存在传入的注解类则
     * 返回 {@code true}；否则不存在返回 {@code false}。
     *
     * @param annotation
     *        注解类
     *
     * @return 如果存在传入的注解类则返回 {@code true}；否则不存在
     *         则返回 {@code false}。
     */
    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return getAnnotation(annotation) != null;
    }

    /**
     * #brief：获取属性上的注解信息<p>
     *
     * 获取属性上的注解信息，根据 {@code annotation} 参数获取当前属性上的注解。
     * 如果注解存在则返回注解对象，不存在则返回 {@code null}。
     *
     * @param annotation
     *        注解类
     *
     * @return 如果 {@code annotation} 被注解在当前属性上，那么就
     *         返回注解对象。如果不存在则返回 {@code null}。
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        T a = field.getDeclaredAnnotation(annotation);
        if (a == null)
            a = field.getAnnotation(annotation);
        return a;
    }

    public boolean typecheck(Class<?> aClass) {
        return aClass == getOriginType();
    }

    /**
     * 检查当前描述符是否为基本类型或包装类型。
     *
     * <p>该方法判断当前对象是否为原生基本类型或其对应的包装类型（如 `int` 或 {@link Integer}）。
     * 优先检查描述符是否为基本类型，随后判断是否为常见包装类型之一。
     *
     * @return 如果当前描述符是基本类型或包装类型，则返回 true；否则返回 false
     */
    public boolean isPrimitiveCheck() {
        return getType().isPrimitiveCheck();
    }

}
