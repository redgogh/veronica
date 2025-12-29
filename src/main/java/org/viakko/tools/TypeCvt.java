package org.viakko.tools;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 Viakko  All rights reserved.                          *|
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

import org.json.XML;
import org.viakko.tools.exception.UnsupportedOperationException;
import org.viakko.tools.iface.TypeMapper;
import org.viakko.tools.reflect.UClass;
import org.viakko.tools.string.StringOption;
import org.viakko.tools.string.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.viakko.tools.string.StringUtils.strmatch;

/**
 * TypeCvt 类提供了多种类型转换和比较功能。
 *
 * <p>该类支持将对象转换为基本数据类型，如 int、long、boolean 和 String。
 * 通过自动识别对象类型，该类简化了数据转换过程，确保类型安全。
 * 同时，该类提供了对对象相等性和不相等性的比较方法，方便在不同场景下进行数据处理。
 *
 * <p>示例用法：
 * <pre>
 *     int number = atoi("123");
 *     boolean flag = atobool("true");
 * </pre>
 *
 * @author Ekko
 * @see java.util.Objects
 */
public class TypeCvt {

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// any
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * `any`函数通常用于 Lambda 接口，将任何对象作为`Object`类型
     * 返回出去。
     */
    @SuppressWarnings("unchecked")
    public static <T> T any(Object type) {
        return (T) type;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// int
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 将不同类型的对象转换为字节数组
     *
     * <p>该方法支持将 `byte[]`、`Integer`、`Long` 和 `String` 类型的对象转换为字节数组。
     *
     * <p>具体转换规则如下：
     * <ul>
     *     <li>如果输入对象是 `byte[]`，则直接返回该字节数组。</li>
     *     <li>如果输入对象是 `Integer`，则通过 `ByteBuffer` 将其转换为字节数组。</li>
     *     <li>如果输入对象是 `Long`，则同样通过 `ByteBuffer` 转换为字节数组。</li>
     *     <li>如果输入对象是 `String` 或十六进制字符串，则将其转换为 UTF-8 编码的字节数组。</li>
     * </ul>
     *
     * <p>如果输入对象的类型不在支持范围内，则抛出 `UnsupportedOperationException`。
     *
     * @param obj 要转换的对象，可以是 `byte[]`、`Integer`、`Long` 或 `String`
     * @return 转换后的字节数组
     * @throws UnsupportedOperationException 如果输入对象的类型不被支持
     */
    public static byte[] atob(Object obj) {
        // byte[]
        if (obj instanceof byte[])
            return (byte[]) obj;

        // int
        if (obj instanceof Integer) {
            int writeBytes = Integer.BYTES;
            ByteBuffer buffer = ByteBuffer.allocate(writeBytes);
            buffer.putInt((int) obj);
            return Arrays.copyOf(buffer.array(), writeBytes);
        }

        // long
        if (obj instanceof Long) {
            int writeBytes = Long.BYTES;
            ByteBuffer buffer = ByteBuffer.allocate(writeBytes);
            buffer.putLong((long) obj);
            return Arrays.copyOf(buffer.array(), writeBytes);
        }

        // string or hex string
        if (obj instanceof String)
            return ((String) obj).getBytes(StandardCharsets.UTF_8);

        throw new UnsupportedOperationException("atob：暂不支持当前类型【%s】转字节数组", new UClass(obj));
    }

    /**
     * #brief：自动识别 {@code obj} 类型，并转换成 Integer<p>
     *
     * 自动识别 {@code obj} 类型，并转换成 Integer，如果 {@code obj} 是 String
     * 类型，则会通过 {@code Integer.valueOf()} 方法转换。如果是 byte 则通过字节
     * 缓冲区进行转换。
     *
     * @param obj
     *        要被转换的对象
     *
     * @return 转换后的 int 类型数据。
     */
    public static Integer atoi(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof Integer)
            return (Integer) obj;
        if (obj instanceof byte[])
            return atoi((byte[]) obj);
        return Integer.parseInt(atos(obj));
    }

    /**
     * #brief：从 {@code b[0]} 往后取 4 个字节转为 int 类型<p>
     *
     * 从 {@code b[off]} 往后取 4 个字节转为 int 类型。数组长度必须保证不能
     * 小于 4 个字节。
     *
     * @param b
     *        字节数组，数组长度最低不能小于 4 个字节
     *
     * @return 转换后的 int 类型数据。
     */
    public static int atoi(byte[] b) {
        return atoi(b, 0);
    }

    /**
     * #brief：从 {@code b[off]} 往后取 4 个字节转为 int 类型<p>
     *
     * 从 {@code b[off]} 往后取 4 个字节转为 int 类型。数组长度必须保证不能
     * 小于 4 个字节。如果使用了 {@code off} 字段。那么必须保证 {@code b.length - off >= 4}。
     * 如果不满足这些条件则抛出数组越界异常。
     *
     * @param b
     *        字节数组，数组长度最低不能小于 4 个字节
     *
     * @param off
     *        偏移量，从 {@code b[off]} 往后取 4 个字节。
     *
     * @return 转换后的 int 类型数据。
     */
    public static int atoi(byte[] b, int off) {
        int readBytes = Integer.BYTES;
        if (b == null || off < 0 ||  (b.length - off) < readBytes)
            throw new IndexOutOfBoundsException("无法读取 int，数组长度=" + (b == null ? "null" : b.length) + "，偏移量=" + off);

        return ByteBuffer.wrap(b, off, readBytes).getInt();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// long
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * #brief：自动识别 {@code obj} 类型，并转换成 Long<p>
     *
     * 自动识别 {@code obj} 类型，并转换成 Long，如果 {@code obj} 是 String
     * 类型，则会通过 {@code Long.valueOf()} 方法转换。如果是 byte 则通过字节
     * 缓冲区进行转换。
     *
     * @param obj
     *        要被转换的对象
     *
     * @return 转换后的 long 类型数据。
     */
    public static Long atol(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof Long)
            return (Long) obj;
        if (obj instanceof Number)
            return ((Number) obj).longValue();
        if (obj instanceof byte[])
            return atol((byte[]) obj);
        return Long.parseLong(atos(obj));
    }

    /**
     * #brief：从 {@code b[0]} 往后取 8 个字节转为 long 类型<p>
     *
     * 从 {@code b[off]} 往后取 8 个字节转为 long 类型。数组长度必须保证不能
     * 小于 8 个字节。
     *
     * @param b
     *        字节数组，数组长度最低不能小于 8 个字节
     *
     * @return 转换后的 long 类型数据。
     */
    public static long atol(byte[] b) {
        return atol(b, 0);
    }

    /**
     * #brief：从 {@code b[off]} 往后取 8 个字节转为 long 类型<p>
     *
     * 从 {@code b[off]} 往后取 8 个字节转为 long 类型。数组长度必须保证不能
     * 小于 8 个字节。如果使用了 {@code off} 字段。那么必须保证 {@code b.length - off >= 8}。
     * 如果不满足这些条件则抛出数组越界异常。
     *
     * @param b
     *        字节数组，数组长度最低不能小于 8 个字节
     *
     * @param off
     *        偏移量，从 {@code b[off]} 往后取 8 个字节。
     *
     * @return 转换后的 long 类型数据。
     */
    public static long atol(byte[] b, int off) {
        int readBytes = Long.BYTES;
        if (b == null || off < 0 ||  (b.length - off) < readBytes)
            throw new IndexOutOfBoundsException("无法读取 long，数组长度=" + (b == null ? "null" : b.length) + "，偏移量=" + off);

        return ByteBuffer.wrap(b, off, readBytes).getLong();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// boolean
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 将 {@link Object} 对象实例转换成`Boolean`类型的对象实例。修改原来的 {@link Object} 对象不会对
     * 新的创建的`Boolean`对象实例有任何影响，并且这个函数也不会抛出异常。如果对象是 {@code null} 的话那
     * 么则返回 {@code false}
     *
     * @param obj
     *        {@code Object} 对象实例
     *
     * @return 根据传入的其他对象实例转换成`Boolean`对象实例。
     *
     * @see String#valueOf(Object)
     */
    public static boolean atobool(Object obj) {
        if (obj instanceof Boolean)
            return (Boolean) obj;
        if (obj instanceof Number)
            return ((Number) obj).intValue() > 0;
        String bool = atos(obj, StringUtils::uppercase);
        return strmatch(bool, "TRUE|ON|Y|YES");
    }

    /**
     * 将 {@link Object} 对象实例转换成 {@link String} 类型的对象实例。修改原来的 {@link Object} 对象不会对
     * 新的创建的 {@link String} 对象实例有任何影响。该函数也不会抛出异常，如果传入的对象为 {@code null} 的话
     * 那么新创建出来的字符串就会从常量池中返回一个 "null" 字符串出去。<p>
     *
     * 这个函数提供数据处理 Lambda 函数，可通过 Lambda 处理返回数据。示例：
     * <pre>
     *     // 将字符串转为小写
     *     var str = atos("0xABCDEF", Objects::lowercase);
     * </pre>
     *
     * @param obj
     *        {@code Object} 对象实例
     *
     * @param mapper
     *        数据映射函数
     *
     * @return 根据传入的其他对象实例转换成 {@code String} 对象实例。
     *
     * @see String#valueOf(Object)
     */
    public static String atos(Object obj, TypeMapper<String, String> mapper, StringOption...iface) {
        return StringOption.pipelineExecutor(mapper.call(atos(obj)), iface);
    }

    /**
     * 将 {@link Object} 对象实例转换成 {@link String} 类型的对象实例。修改原来的 {@link Object} 对象不会对
     * 新的创建的 {@link String} 对象实例有任何影响。该函数也不会抛出异常，如果传入的对象为 {@code null} 的话
     * 那么新创建出来的字符串就会从常量池中返回一个 "null" 字符串出去。
     *
     * @param obj
     *        {@code Object} 对象实例
     *
     * @return 根据传入的其他对象实例转换成 {@code String} 对象实例。
     *
     * @see String#valueOf(Object)
     */
    public static String atos(Object obj, StringOption...iface) {
        return switch (obj) {
            case null -> "";
            case String s -> StringOption.pipelineExecutor(s, iface);
            /* 字节数组转字符串 */
            case byte[] bytes -> atos(bytes, 0, bytes.length, iface);
            /* 字符数组转字符串 */
            case char[] chars -> atos(chars, 0, chars.length, iface);
            default -> StringOption.pipelineExecutor(String.valueOf(obj), iface);
        };
    }

    /**
     * 通过 {@code obj} 子字符串、{@code off} 数组偏移量和 {@code len} 长度分配
     * 一个新的字符串对象。{@code off} 是字符数组中第一个字符的索引位置。{@code len} 参数
     * 是整个字符串的长度。
     *
     * @param obj
     *        子字符串对象实例
     *
     * @param off
     *        子字符串中第一个字符开始的索引位置
     *
     * @param len
     *        新的字符串长度，该长度不能超过 {@code sub} 被截取的子字符串长度
     *
     * @return 根据 {@code off} 和 {@code len} 分配出来的新字符串
     *
     * @throws NullPointerException
     *          {@code sub} 子字符串参数不能为空
     *
     * @throws ArrayIndexOutOfBoundsException
     *          如果参数 {@code len} 超出整个子字符串的大小后会抛出数组越界异常。
     */
    public static String atos(Object obj, int off, int len, StringOption...iface) {
        return atos(atos(obj), off, len, iface);
    }

    /**
     * 通过 {@code sub} 子字符串、{@code off} 数组偏移量和 {@code len} 长度分配
     * 一个新的字符串对象。{@code off} 是字符数组中第一个字符的索引位置。{@code len} 参数
     * 是整个字符串的长度。
     *
     * @param sub
     *          子字符串对象实例
     *
     * @param off
     *          子字符串中第一个字符开始的索引位置
     *
     * @param len
     *          新的字符串长度，该长度不能超过 {@code sub} 被截取的子字符串长度。如果长度
     *          是 {@code 0} 那么表示到最后，如果是负数，表示截取到 len - n
     *
     * @return 根据 {@code off} 和 {@code len} 分配出来的新字符串
     *
     * @throws NullPointerException
     *          {@code sub} 子字符串参数不能为空
     *
     * @throws ArrayIndexOutOfBoundsException
     *          如果参数 {@code len} 超出整个子字符串的大小后会抛出数组越界异常。
     */
    public static String atos(String sub, int off, int len, StringOption...iface) {
        return atos(sub.toCharArray(), off, len, iface);
    }

    /**
     * 通过 {@code b} 字节数组分配一个新的字符串对象。
     * <p>
     * 分配新的字符串后，修改字节数组不会对新分配的字符串造成影响。
     *
     * @param b
     *        字节数组
     *
     * @return 新的字符串由传入的 {@code b} 字节数组转的字符串文本
     *
     * @see String#String(byte[])
     */
    public static String atos(byte[] b, StringOption...iface) {
        return atos(b, 0, b.length, iface);
    }

    /**
     * 通过 {@code b} 字节数组、{@code off} 数组偏移量和 {@code len} 长度分配
     * 一个新的字符串对象。{@code off} 是字符数组中第一个字符的索引位置。{@code len} 参数
     * 是整个字符串的长度。
     * <p>
     * 分配新的字符串后，修改字节数组不会对新分配的字符串造成影响。
     *
     * @param b
     *        字节数组
     *
     * @param off
     *        字节数组中第一个字符开始的索引位置
     *
     * @param len
     *        新的字符串长度，该长度不能超过 {@code b} 字节数组长度
     *
     * @return 根据 {@code off} 和 {@code len} 分配出来的新字符串
     *
     * @throws ArrayIndexOutOfBoundsException
     *          如果参数 {@code len} 超出整个字符数组的大小后会抛出数组越界异常。
     *
     * @see String#String(byte[], int, int)
     */
    public static String atos(byte[] b, int off, int len, StringOption...iface) {
        return StringOption.pipelineExecutor(new String(ArrayUtils.slice(b, off, len),
                StandardCharsets.UTF_8), iface);
    }

    /**
     * 通过 {@code a} 字符数组、{@code off} 数组偏移量和 {@code len} 长度分配
     * 一个新的字符串对象。{@code off} 是字符数组中第一个字符的索引位置。{@code len}
     * 参数是整个字符串的长度。
     *
     * <p>分配新的字符串后，修改字符数组不会对新分配的字符串造成影响。
     *
     * @param a
     *        被截取的字符数组
     *
     * @param off
     *        字符数组中第一个字符开始的索引位置
     *
     * @param len
     *        新的字符串长度，该长度不能超过 {@code a} 被截取的字符数组长度
     *
     * @return 根据 {@code off} 和 {@code len} 分配出来的新字符串
     *
     * @throws ArrayIndexOutOfBoundsException
     *          如果参数 {@code len} 超出整个字符数组的大小后会抛出数组越界异常。
     *
     * @see String#String(char[], int, int)
     */
    public static String atos(char[] a, int off, int len, StringOption...iface) {
        return StringOption.pipelineExecutor(new String(ArrayUtils.slice(a, off, len)), iface);
    }

    /**
     * 将XML字符串转换为JSON字符串
     *
     * <p>该方法提供从XML格式到JSON格式的简单转换功能，使用{@link XML}工具类实现核心转换逻辑。
     * 转换过程会保留XML文档的结构信息，包括：
     * <ul>
     *   <li>元素节点转换为JSON对象</li>
     *   <li>属性转换为JSON字段</li>
     *   <li>文本内容存入特殊字段</li>
     * </ul>
     *
     * <p>典型使用场景：
     * <pre>
     * String xml = "&lt;note&gt;&lt;to&gt;Alice&lt;/to&gt;&lt;from&gt;Bob&lt;/from&gt;&lt;/note&gt;";
     * String json = xtos(xml);
     * // 结果: {"note":{"to":"Alice","from":"Bob"}}
     * </pre>
     *
     * @param xmlText 要转换的XML格式字符串，应符合基本XML语法要求：
     *               <ul>
     *                 <li>必须有单个根元素</li>
     *                 <li>标签必须正确闭合</li>
     *                 <li>特殊字符需转义</li>
     *               </ul>
     * @return 转换后的JSON格式字符串，使用UTF-8编码
     * @throws org.json.JSONException 当出现以下情况时：
     *                      <ul>
     *                        <li>XML格式不规范</li>
     *                        <li>存在不支持的XML特性(如DTD)</li>
     *                        <li>转换过程中发生错误</li>
     *                      </ul>
     * @see XML#toJSONObject(String) 实际执行转换的方法
     * @since 1.2
     */
    public static String xtos(String xmlText) {
        return XML.toJSONObject(xmlText).toString();
    }

    /**
     * 将对象转换为指定类型的值。
     *
     * <p>该方法尝试将给定的对象转换为指定类型的值。首先将对象转为字符串形式，
     * 然后根据目标类型进行转换。如果目标类型是基本数据类型，使用反射调用其 `valueOf` 方法进行转换。<p>
     *
     * <h2>功能特点</h2>
     * <ul>
     *     <li>支持将对象转换为基本数据类型或其包装类型。</li>
     *     <li>通过反射动态调用目标类型的 `valueOf` 方法进行转换。</li>
     * </ul>
     *
     * @param obj    要转换的对象
     * @param aClass 目标类型的 Class 对象
     * @param <T>    目标类型的泛型
     * @return 转换后的目标类型的值，如果转换失败则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> T toPrimitiveValue(Object obj, Class<T> aClass) {
        return Rethrow.swallow(() -> (T) new UClass(aClass).staticInvoke("valueOf", obj));
    }

}
