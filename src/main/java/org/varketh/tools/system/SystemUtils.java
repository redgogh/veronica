package org.varketh.tools.system;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 Varketh Nockrath  All rights reserved.                *|
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

import org.varketh.tools.time.Chrono;

import java.util.Map;
import java.util.regex.Pattern;

import static org.varketh.tools.string.StringUtils.*;

/**
 * `SystemUtils` 是一个类，用于管理和操作操作系统环境变量。
 *
 * <p>该类提供了访问和检索操作系统环境变量的功能。主要功能包括获取指定名称的环境
 * 变量值等。可以通过静态方法访问环境变量，无需实例化该类。
 *
 * <p>该类的主要特点包括：
 * <ul>
 *     <li>提供简单的接口来获取操作系统环境变量。</li>
 *     <li>封装了环境变量的访问逻辑，易于管理和扩展。</li>
 * </ul>
 *
 * <h2>使用示例</h2>
 * <pre>
 *     // 获取指定名称的环境变量值
 *     String value = OSEnvironment.getEnvironment("PATH");
 * </pre>
 *
 * @author Varketh Nockrath
 * @see System
 * @since 1.0
 */
public class SystemUtils {

    /**
     * 当前操作系统枚举
     */
    public static final OperatorSystem OPERATOR_SYSTEM = getos();

    /**
     * 该常量用于获取并保存当前运行环境的操作系统名称。
     * 通过调用 {@code System.getProperty("os.name")} 来初始化，
     * 返回的操作系统名称可能是 "Windows", "Linux", "Mac OS X" 等。
     * <p>
     * 注意事项：
     * - 该值在 JVM 启动时被初始化，不会随操作系统的变化而更新。
     * - 使用此常量时，确保不会直接依赖于操作系统名称进行特定逻辑操作，
     *   应该使用操作系统类型判断。
     */
    private static final String OS_NAME = System.getProperty("os.name");

    /**
     * 操作系统环境变量
     */
    private static final Map<String, String> ENV = System.getenv();

    /**
     * @return 返回当前操作系统枚举对象
     */
    private static OperatorSystem getos() {
        // initialize
        String _OS_NAME = System.getProperty("os.name");
        if (strihas(_OS_NAME, "Windows"))
            return OperatorSystem.WINDOWS;
        else if (strihas(_OS_NAME, "Linux"))
            return OperatorSystem.LINUX;
        else if (strihas(_OS_NAME, "Mac"))
            return OperatorSystem.MACOS;
        return OperatorSystem.UNKNOWN;
    }

    /**
     * 检查当前操作系统是否为 Windows
     *
     * <p>该函数用于判断当前运行环境是否为 Windows 操作系统。
     *
     * @return 如果操作系统是 Windows，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isWindows() {
        return OPERATOR_SYSTEM == OperatorSystem.WINDOWS;
    }

    /**
     * 检查当前操作系统是否为 Linux
     *
     * <p>该函数用于判断当前运行环境是否为 Linux 操作系统。
     *
     * @return 如果操作系统是 Linux，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isLinux() {
        return OPERATOR_SYSTEM == OperatorSystem.LINUX;
    }

    /**
     * 检查当前操作系统是否为 MacOS
     *
     * <p>该函数用于判断当前运行环境是否为 MacOS 操作系统。
     *
     * @return 如果操作系统是 MacOS，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isMacOS() {
        return OPERATOR_SYSTEM == OperatorSystem.MACOS;
    }

    /**
     * 获取当前操作系统的名称
     *
     * <p>通过调用 `System.getProperty("os.name")` 获取操作系统的名称，
     * 如 "Windows 11"、"Linux"、"Mac OS X" 等。
     *
     * @return 当前操作系统的名称字符串
     */
    public static String osName() {
        return System.getProperty("os.name");
    }

    /**
     * 获取当前操作系统的架构
     *
     * <p>通过调用 `System.getProperty("os.arch")` 获取操作系统的体系结构，
     * 如 "x86", "amd64", "aarch64" 等。
     *
     * @return 当前操作系统的架构字符串
     */
    public static String osArch() {
        return System.getProperty("os.arch");
    }

    /**
     * 获取当前操作系统的版本
     *
     * <p>通过调用 `System.getProperty("os.version")` 获取操作系统的版本信息，
     * 如 "10.0"、"11.2.3" 等。
     *
     * @return 当前操作系统的版本字符串
     */
    public static String osVersion() {
        return System.getProperty("os.version");
    }

    /**
     * 获取当前用户的工作目录
     *
     * <p>返回当前程序运行时的工作目录路径，等同于 `System.getProperty("user.dir")`，
     * 通常是程序启动时所在的目录。
     *
     * @return 当前工作目录的绝对路径
     */
    public static String workDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取系统的临时目录路径
     *
     * <p>通过调用 `System.getProperty("java.io.tmpdir")` 获取系统的临时文件目录，
     * 通常用于存放临时文件，路径依操作系统不同而异。
     *
     * @return 系统临时目录路径
     */
    public static String tempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取当前用户的主目录路径
     *
     * <p>该函数返回当前用户的主目录路径，路径中的所有反斜杠（\）会被替换为正斜杠（/）。
     *
     * @return 返回当前用户的主目录路径
     */
    public static String getUserHome() {
        return getUserHome(null);
    }

    /**
     * 获取当前用户的主目录路径，并可选择性地附加一个子路径
     *
     * <p>该函数返回当前用户的主目录路径，路径中的所有反斜杠（\）会被替换为正斜杠（/）。
     * 可以通过传入 {@code concat} 参数，在主目录路径后附加一个子路径。
     *
     * @param sub
     *        要附加在用户主目录路径后的子路径，如果为 {@code null}，则不附加任何内容
     *
     * @return 返回当前用户的主目录路径，如果 {@code concat} 不为空，则在路径后附加该子路径
     */
    public static String getUserHome(String sub) {
        String home = strrexp(System.getProperty("user.home"), "\\\\", "/");
        if (sub != null)
            return home + "/" + sub;
        return home;
    }

    /**
     * @return 获取所有环境变量
     */
    public static Map<String, String> getenv() {
        return ENV;
    }

    /**
     * 根据名称获取对应的环境变量值
     *
     * <p>从环境变量集合中查找指定名称的环境变量，并返回其对应的值。
     * 如果环境变量名称不存在，返回 `null`。
     *
     * @param name 环境变量的名称
     * @return 对应名称的环境变量值，如果名称未找到，则返回 `null`
     */
    public static String getenv(String name) {
        return ENV.get(name);
    }

    /**
     * 返回当前系统时间的毫秒数
     *
     * <p>该方法调用 `System.currentTimeMillis()`，获取自 Unix 纪元（1970-01-01 00:00:00 UTC）
     * 以来的当前时间，以毫秒为单位返回。
     *
     * <p>该方法适用于需要精确测量时间间隔或记录当前时间的场景。
     *
     * @return 当前时间的毫秒数
     */
    public static long timestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 返回当前系统时间的 `Chrono` 对象
     *
     * <p>该方法基于当前时间的毫秒数创建并返回一个新的 `Chrono` 对象。当前时间是通过调用
     * `time()` 方法获取的，该方法返回自 Unix 纪元（1970-01-01 00:00:00 UTC）以来的毫秒数。
     *
     * <p>该方法适用于需要获取当前时间的 `Chrono` 表示的场景。
     *
     * @return 当前时间的 `Chrono` 对象
     */
    public static Chrono chrono() {
        return Chrono.now();
    }

    /**
     * 获取当前可用的处理器数量
     *
     * <p>该方法调用 `Runtime.getRuntime().availableProcessors()`，返回当前 Java 虚拟机可用的处理器数量。
     *
     * <p>此方法适用于需要根据系统可用处理器数量进行资源分配或线程管理的场景。
     *
     * @return 当前可用的处理器数量
     */
    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取指定路径对应的文件资源
     *
     * <p>该方法用于根据提供的路径字符串获取对应的文件资源。支持特殊路径格式：
     * <ul>
     *     <li>如果路径以"~/"开头，表示用户主目录路径，会自动转换为用户主目录下的实际路径</li>
     *     <li>其他路径会直接作为文件路径处理</li>
     * </ul>
     *
     * <p>该方法不会检查文件是否实际存在，仅进行路径转换和文件对象创建。
     *
     * @param path 文件路径字符串，可以是以"~/"开头的用户主目录相对路径或普通文件路径
     * @return 对应路径的File对象，不会为null
     *
     * @see #getUserHome(String) 用于处理用户主目录路径转换的方法
     */
    public static String resolvePath(String path) {
        // 解析路径开头是否为用户目录
        if (path.startsWith("~/"))
            path = getUserHome(path.substring(2));

        // 解析环境变量
        if (!strhas(path, "$"))
            return path;

        String[] environments = strfind(path, "\\$[A-Za-z_][A-Za-z0-9_]*");
        for (String environment : environments) {
            String envName = strcut(environment, 1, 0);
            path = strrexp(path, Pattern.quote(environment), getenv(envName));
        }

        return path;
    }

}