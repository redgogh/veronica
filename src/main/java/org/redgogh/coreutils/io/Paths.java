package org.redgogh.coreutils.io;

import org.redgogh.coreutils.system.SystemUtils;

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

/* Creates on 2025/7/31. */

import java.net.URI;
import java.nio.file.Path;

/**
 * 路径相关的工具方法
 *
 * @author Red Gogh
 */
public class Paths {

    /**
     * 根据路径字符串构造 Path 对象
     *
     * @param first 路径的第一个部分（不能为null）
     * @param more 路径的额外部分（可选）
     * @return 结果 Path 对象
     * @see java.nio.file.Paths#get(String, String...)
     */
    public static Path get(String first, String... more) {
        return java.nio.file.Paths.get(first, more);
    }

    /**
     * 根据 URI 构造 Path 对象
     *
     * @param uri 要转换的 URI（不能为null）
     * @return 结果 Path 对象
     * @see java.nio.file.Paths#get(URI)
     */
    public static Path get(URI uri) {
        return java.nio.file.Paths.get(uri);
    }

    /**
     * 解析路径字符串，支持使用 Unix 路径方式解析，如 ~/ 表示用户
     * 所在目录，$JAVA_HOME/bin 替换环境变量等操作。
     *
     * @param pathname 要解析的路径字符串
     * @return 解析后的绝对路径字符串
     * @see SystemUtils#resolvePath(String)
     */
    public static String resolve(String pathname) {
        return SystemUtils.resolvePath(pathname);
    }

}
