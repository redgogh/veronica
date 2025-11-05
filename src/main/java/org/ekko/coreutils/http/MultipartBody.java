package org.ekko.coreutils.http;

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

import java.util.LinkedHashMap;

/**
 * `MultipartBody` 是一个继承自 `LinkedHashMap<String, Object>` 的类，用于构建 multipart/form-data 请求体，
 * 主要用于处理文件上传和复杂数据提交。
 *
 * <p>该类存储键值对形式的请求体，其中键为字段名，值为字段值或文件对象。
 *
 * <p>主要功能包括：
 * <ul>
 *     <li>存储和管理 multipart 请求体中的各个部分。</li>
 *     <li>支持存储文件和其他数据类型。</li>
 * </ul>
 *
 * <p>使用场景：
 * <ul>
 *     <li>构建 multipart/form-data 请求体，特别是在需要上传文件或发送复杂数据时。</li>
 *     <li>简化 multipart 请求体的构建过程。</li>
 * </ul>
 *
 * @see LinkedHashMap
 * @since 1.0
 * @author Ekko
 */
public class MultipartBody extends LinkedHashMap<String, Object> {

    /**
     * 默认构造函数
     *
     * <p>创建一个空的 `MultipartBody` 实例，之后可以通过 `put` 方法添加字段。
     */
    public MultipartBody() {

    }

    /**
     * 使用一个键值对构造 `MultipartBody` 实例
     *
     * <p>创建一个包含一个字段的 `MultipartBody` 实例。字段名为 `k1`，字段值为 `v1`。
     *
     * @param k1 字段名
     * @param v1 字段值
     */
    public MultipartBody(String k1, Object v1) {
        put(k1, v1);
    }

    /**
     * 使用两个键值对构造 `MultipartBody` 实例
     *
     * <p>创建一个包含两个字段的 `MultipartBody` 实例。字段名为 `k1` 和 `k2`，字段值为 `v1` 和 `v2`。
     *
     * @param k1 第一个字段名
     * @param v1 第一个字段值
     * @param k2 第二个字段名
     * @param v2 第二个字段值
     */
    public MultipartBody(String k1, Object v1, String k2, Object v2) {
        put(k1, v1);
        put(k2, v2);
    }

    /**
     * 使用三个键值对构造 `MultipartBody` 实例
     *
     * <p>创建一个包含三个字段的 `MultipartBody` 实例。字段名为 `k1`、`k2` 和 `k3`，字段值为 `v1`、`v2` 和 `v3`。
     *
     * @param k1 第一个字段名
     * @param v1 第一个字段值
     * @param k2 第二个字段名
     * @param v2 第二个字段值
     * @param k3 第三个字段名
     * @param v3 第三个字段值
     */
    public MultipartBody(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        put(k1, v1);
        put(k2, v2);
        put(k3, v3);
    }

}
