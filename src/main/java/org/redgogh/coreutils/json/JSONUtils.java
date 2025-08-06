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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

import static org.redgogh.coreutils.TypeCvt.xtos;
import static org.redgogh.coreutils.string.StringUtils.strip;

/**
 * JSON 工具类，基于 fastjson 封装常用 JSON 操作。
 *
 * <p>提供对象序列化、反序列化、格式化、压缩、路径提取、自动格式识别等功能。
 *
 * <h2>功能说明：</h2>
 * <ul>
 *     <li>支持任意对象与 JSON 字符串的互转。</li>
 *     <li>自动识别字符串为 XML 时进行转换处理。</li>
 *     <li>支持通过 JSONPath 提取指定字段或数组。</li>
 *     <li>支持格式化输出和压缩输出。</li>
 * </ul>
 *
 * <h2>依赖说明：</h2>
 * 基于 Alibaba fastjson（推荐使用 1.2.83+）。
 *
 * <h2>注意事项：</h2>
 * <ul>
 *     <li>对于字符串格式自动处理 XML 的逻辑比较宽松，仅以 `&lt;` 开头作为判断依据。</li>
 *     <li>路径提取函数默认返回 JSONObject 或 JSONArray。</li>
 * </ul>
 *
 * @author Red Gogh
 * @see com.alibaba.fastjson.JSON
 * @see com.alibaba.fastjson.JSONObject
 * @see com.alibaba.fastjson.JSONPath
 */
public class JSONUtils {

    /**
     * 将对象序列化为 JSON 字符串
     *
     * @param obj 任意 Java 对象
     * @return JSON 格式字符串
     */
    public static String stringify(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型对象
     *
     * @param text JSON 文本
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 反序列化对象
     */
    public static <T> T from(String text, Class<T> clazz) {
        return ((JSONObject) parse(text)).toJavaObject(clazz);
    }

    /**
     * 将 JSON 字符串美化输出
     *
     * @param json 原始 JSON 字符串
     * @return 格式化后的 JSON 字符串
     */
    public static String format(String json) {
        return JSON.toJSONString(parse(json), SerializerFeature.PrettyFormat);
    }

    /**
     * 压缩 JSON 字符串（去除空格与换行）
     *
     * @param json 原始 JSON 字符串
     * @return 紧凑格式的 JSON 字符串
     */
    public static String compact(String json) {
        return JSON.toJSONString(parse(json));
    }

    /**
     * 将任意对象解析为 JSON 对象，支持字符串、JavaBean、Map 等类型
     * <p>如果为字符串且以 "&lt;" 开头，则自动识别为 XML 并转换为 JSON。
     *
     * @param obj 源对象
     * @param <T> JSON 类型（JSONObject 或 JSONArray）
     * @return 解析后的 JSON 对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends JSON> T parse(Object obj) {
        if (obj instanceof String content) {
            if (strip(content).startsWith("<"))
                content = xtos(content);
            return (T) JSONObject.parse(content);
        }
        return (T) JSONObject.parse(stringify(obj));
    }

    /**
     * 通过 JSONPath 路径提取对象并转为指定类型
     *
     * @param json JSON 字符串
     * @param path JSONPath 路径
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 解析后的对象
     */
    public static <T> T get(String json, String path, Class<T> clazz) {
        Object obj = JSONPath.eval(parse(json), path);
        return ((JSONObject) obj).toJavaObject(clazz);
    }

    /**
     * 通过 JSONPath 路径提取数组并转为指定类型列表
     *
     * @param json JSON 字符串
     * @param path JSONPath 路径
     * @param clazz 元素类型
     * @param <T> 类型参数
     * @return 类型安全的 List
     */
    public static <T> List<T> getList(String json, String path, Class<T> clazz) {
        Object obj = JSONPath.eval(parse(json), path);
        return ((JSONArray) obj).toJavaList(clazz);
    }

}
