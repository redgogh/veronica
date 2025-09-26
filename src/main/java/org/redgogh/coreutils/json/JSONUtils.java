package org.redgogh.coreutils.json;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 RedGogh All rights reserved.                          *|
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

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;

import static org.redgogh.coreutils.TypeCvt.xtos;
import static org.redgogh.coreutils.string.StringUtils.strip;

/**
 * JSON 工具类，基于 fastjson 封装常用 JSON 操作。
 *
 * <p>提供对象序列化、反序列化、格式化、压缩、路径提取、自动格式识别等功能。
 *
 * @author Red Gogh
 * @see com.alibaba.fastjson.JSON
 * @see com.alibaba.fastjson.JSONObject
 * @see com.alibaba.fastjson.JSONPath
 */
public class JSONUtils {

    /**
     * 将 Java 对象转换为 JSON 字符串。
     *
     * @param rootObject 要序列化的 Java 对象
     * @return 对象的 JSON 字符串表示
     * @throws JSONException 如果序列化过程中发生错误
     */
    public static String stringify(Object rootObject) {
        return JSON.toJSONString(rootObject);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的 Java 对象。
     *
     * @param <T> 目标对象的类型参数
     * @param json 要反序列化的 JSON 字符串
     * @param clazz 目标对象的 Class 类型
     * @return 反序列化后的 Java 对象
     * @throws JSONException 如果 JSON 格式无效或反序列化失败
     * @throws IllegalArgumentException 如果 json 参数为 null 或空字符串
     */
    public static <T> T from(String json, Class<T> clazz) {
        return parse(json).toJavaObject(clazz);
    }

    /**
     * 将对象格式化为美观打印（缩进格式化）的 JSON 字符串。
     *
     * @param rootObject 要格式化的 Java 对象
     * @return 格式化后的 JSON 字符串
     * @throws JSONException 如果序列化过程中发生错误
     */
    public static String format(Object rootObject) {
        return JSON.toJSONString(parse(rootObject), SerializerFeature.PrettyFormat);
    }

    /**
     * 将对象转换为紧凑格式（无多余空格）的 JSON 字符串。
     *
     * @param rootObject 要压缩的 Java 对象
     * @return 紧凑格式的 JSON 字符串
     * @throws JSONException 如果序列化过程中发生错误
     */
    public static String compact(Object rootObject) {
        return stringify(parse(rootObject));
    }

    /**
     * 解析输入对象为 JSON 对象或数组。
     * 支持字符串形式的 JSON 或 XML（自动转换），以及任何可序列化的 Java 对象。
     *
     * @param <T> 返回的 JSON 类型（JSONObject 或 JSONArray）
     * @param rootObject 要解析的对象，可以是 JSON 字符串、XML 字符串或 Java 对象
     * @return 解析后的 JSON 对象或数组
     * @throws JSONException 如果输入不是有效的 JSON/XML 格式
     * @throws IllegalArgumentException 如果 rootObject 为 null
     */
    @SuppressWarnings("unchecked")
    public static <T extends JSON> T parse(Object rootObject) {
        if (rootObject instanceof String content) {
            /* 如果是 XML */
            if (strip(content).startsWith("<"))
                content = xtos(content);
            return (T) JSONObject.parse(content);
        }
        return (T) JSONObject.parse(stringify(rootObject));
    }

    /**
     * 从 JSON 对象中提取指定路径的值，并转换为目标类型。
     *
     * @param <T> 目标类型参数
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @param uClass 目标类型的 Class 对象
     * @return 路径对应的值，转换为指定类型
     * @throws IllegalArgumentException 如果路径不存在或值无法转换为目标类型
     * @throws JSONException 如果 JSON 解析失败
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object rootObject, String path, Class<?> uClass) {
        Object o = get(rootObject, path);

        if (o instanceof JSONObject object)
            return (T) object.toJavaObject(uClass);

        if (o instanceof JSONArray array)
            return (T) array.toJavaList(uClass);

        throw new IllegalArgumentException(String.format("%s is not a JSON object", path));
    }

    /**
     * 从 JSON 对象中提取指定路径的值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的值，类型为 Object（可能是基本类型、JSONObject 或 JSONArray）
     * @throws JSONException 如果路径不存在或 JSON 解析失败
     */
    public static Object get(Object rootObject, String path) {
        return JSONPath.eval(parse(rootObject), path);
    }

    /**
     * 从 JSON 对象中读取指定路径的整数值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的整数值，如果不存在则返回 null
     * @throws NumberFormatException 如果值存在但不是有效的整数格式
     */
    public static Integer readInt(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, Integer.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的长整数值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的长整数值，如果不存在则返回 null
     * @throws NumberFormatException 如果值存在但不是有效的长整数格式
     */
    public static Long readLong(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, Long.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的浮点数值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的浮点数值，如果不存在则返回 null
     * @throws NumberFormatException 如果值存在但不是有效的浮点数格式
     */
    public static Float readFloat(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, Float.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的双精度浮点数值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的双精度浮点数值，如果不存在则返回 null
     * @throws NumberFormatException 如果值存在但不是有效的双精度浮点数格式
     */
    public static Double readDouble(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, Double.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的布尔值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的布尔值，如果不存在则返回 null
     * @throws IllegalArgumentException 如果值存在但不是有效的布尔值格式
     */
    public static Boolean readBoolean(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, Boolean.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的字符串值。
     *
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @return 路径对应的字符串值，如果不存在则返回 null
     */
    public static String readString(Object rootObject, String path) {
        return JSONPath.read(format(rootObject), path, String.class);
    }

    /**
     * 从 JSON 对象中读取指定路径的值，并转换为指定类型。
     *
     * @param <T> 目标类型参数
     * @param rootObject 源 JSON 对象
     * @param path JSONPath 路径表达式
     * @param type 目标类型的 Type 对象，支持泛型类型
     * @return 路径对应的值，转换为指定类型
     * @throws IllegalArgumentException 如果值无法转换为目标类型
     */
    public static <T> T read(Object rootObject, String path, Type type) {
        return JSONPath.read(format(rootObject), path, type);
    }

    /**
     * 设置 JSON 对象中指定路径的值。
     *
     * @param rootObject 目标 JSON 对象
     * @param path JSONPath 路径表达式
     * @param value 要设置的值
     * @throws JSONException 如果路径无效或设置操作失败
     * @throws IllegalArgumentException 如果 rootObject 或 path 为 null
     */
    public static void set(Object rootObject, String path, Object value) {
        JSONPath.set(format(rootObject), path, value);
    }

}
