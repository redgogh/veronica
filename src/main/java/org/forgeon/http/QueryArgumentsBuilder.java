package org.forgeon.http;

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

/* Creates on 2023/6/26. */

import org.forgeon.string.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.forgeon.utils.TypeCvt.atos;

/**
 * `QueryBuilder` 是一个继承自 `LinkedHashMap<String, String>` 的类，用于构建 HTTP 请求的查询参数。
 * 该类存储键值对形式的查询参数，并提供方法将这些参数拼接到 URL 中。
 *
 * <p>主要功能包括：
 * <ul>
 *     <li>存储和管理查询参数。</li>
 *     <li>将查询参数格式化并拼接到指定的 URL 上。</li>
 * </ul>
 *
 * <p>使用场景：
 * <ul>
 *     <li>动态构建 URL 查询参数以便发送 HTTP 请求。</li>
 *     <li>简化查询参数的拼接过程，避免手动拼接字符串。</li>
 * </ul>
 *
 * @see LinkedHashMap
 * @since 1.0
 * @author Red Gogh
 */
public class QueryArgumentsBuilder extends LinkedHashMap<String, String> {

    /**
     * 无参数构造方法
     *
     * <p>创建一个空的 `QueryBuilder` 实例。此实例不包含任何参数。
     */
    public QueryArgumentsBuilder() {
        this((String[]) null);
    }

    /**
     * #brief: 使用参数数组初始化 `QueryBuilder`
     *
     * <p>该构造方法接收一组参数字符串，每个参数应为“key=value”的格式，并将这些参数解析后
     * 添加到 `QueryBuilder` 实例中。适用于需要根据给定的参数字符串快速构建查询参数的场景。
     *
     * <h2>示例</h1>
     * <pre>
     *     new QueryBuilder("name=zs", "age=10");
     * </pre>
     *
     * @param parameters 参数数组，每个参数应为“key=value”格式的字符串
     */
    public QueryArgumentsBuilder(String ...parameters) {
        if (parameters != null) {
            for (String parameter : parameters) {
                String[] a = StringUtils.strtok(parameter, "=");
                put(a[0], a[1]);
            }
        }
    }

    /**
     * #brief: 将查询参数拼接到 URL 上
     *
     * <p>该方法将当前 `QueryBuilder` 中的所有查询参数拼接到指定的 URL 上。
     *
     * @param url 要拼接查询参数的原始 URL
     * @return 包含查询参数的完整 URL
     */
    public String argConcatBuild(String url) {
        if (isEmpty())
            return url;

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : entrySet())
            builder.append(StringUtils.strwfmt("%s=%s&", entry.getKey(), entry.getValue()));
        String finalArguments = atos(builder, 0, -1); /* 删掉最后一个字符 ‘&’ */

        return StringUtils.strwfmt("%s?%s", url, finalArguments);
    }
}
