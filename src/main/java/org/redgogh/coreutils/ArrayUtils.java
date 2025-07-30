package org.redgogh.coreutils;

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

/**
 * #brief：提供数组操作的实用工具类。
 *
 * <p>{@code ArrayUtils} 类包含了用于处理数组的常用静态方法。这些方法可以简化数组的拷贝、
 * 检查以及其他常见的操作，避免重复编写低级别的数组处理代码。该类主要用于字节数组的操作，
 * 但可以扩展以支持其他类型的数组。
 *
 * <p>常见方法包括数组的部分拷贝、数组索引和长度检查等。所有方法均为静态方法，因此不需要
 * 实例化该类即可使用。
 *
 * <p>该类旨在为开发者提供高效、可靠的数组操作，减少手动处理数组的复杂度。
 *
 * <p>示例用法：
 * <pre>
 *     byte[] original = {1, 2, 3, 4, 5};
 *     byte[] copy = ArrayUtils.slice(original, 0, 3);  // 拷贝前3个元素
 *     ArrayUtils.checkIndexSize(0, 3, original.length);  // 检查索引是否有效
 * </pre>
 *
 * <p>主要功能包括：
 * <ul>
 *   <li>数组拷贝</li>
 *   <li>数组长度与边界检查</li>
 *   <li>其他与数组操作相关的常见功能</li>
 * </ul>
 *
 * @author Red Gogh
 * @since 1.8
 */
public class ArrayUtils {

    /**
     * 根据偏移和长度，计算可用的截取长度。
     * 当 len <= 0 时，表示自动取 size - off - abs(len)，用于排除尾部。
     *
     * <p>适用于字符串、数组等截取场景，如 off=5, len=-1 表示从 5 开始到倒数第2位。
     *
     * @param size 总长度
     * @param off  起始偏移
     * @param len  截取长度，负数表示排除尾部
     * @return 实际可用长度
     */
    public static int sliceLength(int size, int off, int len) {
        return len <= 0 ? (size - off) - Math.abs(len) : len;
    }

    /**
     * 根据偏移和长度，计算可用的截取长度。
     * 当 off <= 0 时，表示自动取 size - abs(off)，用于排除尾部。
     *
     * @param size 总长度
     * @param off  起始偏移
     * @return 实际可用长度
     */
    public static int sliceOffset(int size, int off) {
        return off < 0 ? (size - Math.abs(off)) : off;
    }

    /**
     * #brief：检查数组偏移量和长度是否在数组大小范围内。
     *
     * <p>该方法用于检查偏移量 {@code off} 和长度 {@code len} 的和是否超出数组的大小 {@code size}。
     * 如果超出范围，则会抛出异常，提示索引越界的错误。
     *
     * <p>该方法通常用于确保在进行数组操作时不会发生越界，保证偏移量和长度合法。
     *
     * @param off  偏移量，从该位置开始进行数组操作
     * @param len  要操作的长度
     * @param size 数组的总大小，用于判断是否越界
     *
     * @throws IllegalArgumentException 如果偏移量和长度的和超出数组大小范围
     */
    public static void checkIndexSize(int off, int len, int size) {
        Assert.ensure(!((off + len ) > size), "Array offset and size out of index: %s", size);
    }

    /**
     * #brief: 检查数组是否为空或null
     *
     * <p>该方法用于安全地检查数组是否为空（无元素）或null。
     *
     * @param <E> 数组元素类型
     * @param a 要检查的数组，可以为null
     * @return 如果数组为null或长度为0返回true，否则返回false
     */
    public static <E> boolean isEmpty(E[] a) {
        return a == null || a.length == 0;
    }

    /**
     * #brief: 检查数组是否非空
     *
     * <p>该方法用于安全地检查数组是否包含元素且不为null。
     *
     * @param <E> 数组元素类型
     * @param a 要检查的数组，可以为null
     * @return 如果数组不为null且长度大于0返回true，否则返回false
     */
    public static <E> boolean isNotEmpty(E[] a) {
        return !isEmpty(a);
    }

    /**
     * #brief: 获取数组长度
     *
     * <p>安全地获取数组长度，即使数组为null也不会抛出异常。
     *
     * @param <E> 数组元素类型
     * @param a 要检查的数组，可以为null
     * @return 数组长度，如果数组为null则返回0
     */
    public static <E> int length(E[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * #brief: 获取数组第一个元素
     *
     * <p>安全地获取数组的第一个元素，如果数组为空或null则返回null。
     *
     * @param <E> 数组元素类型
     * @param a 要操作的数组，可以为null
     * @return 数组的第一个元素，如果数组为null或空则返回null
     * @throws ArrayIndexOutOfBoundsException 如果数组不为空但长度为0
     */
    public static <E> E getFirst(E[] a) {
        return a != null ? a[0] : null;
    }

    /**
     * #brief: 获取数组最后一个元素
     *
     * <p>安全地获取数组的最后一个元素，如果数组为空或null则返回null。
     *
     * @param <E> 数组元素类型
     * @param a 要操作的数组，可以为null
     * @return 数组的最后一个元素，如果数组为null或空则返回null
     * @throws ArrayIndexOutOfBoundsException 如果数组不为空但长度为0
     */
    public static <E> E getLast(E[] a) {
        return a != null ? a[a.length - 1] : null;
    }

    /**
     * #brief: 截取字节数组的子数组
     *
     * <p>从原始字节数组中截取指定位置和长度的子数组。
     *
     * @param original 原始字节数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的字节数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off或len超出范围
     */
    public static byte[] slice(byte[] original, int off, int len) {
        byte[] ret = new byte[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取字符数组的子数组
     *
     * <p>从原始字符数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始字符数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的字符数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static char[] slice(char[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        char[] ret = new char[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取短整型数组的子数组
     *
     * <p>从原始短整型数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始短整型数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的短整型数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static short[] slice(short[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        short[] ret = new short[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取整型数组的子数组
     *
     * <p>从原始整型数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始整型数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的整型数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static int[] slice(int[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        int[] ret = new int[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取长整型数组的子数组
     *
     * <p>从原始长整型数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始长整型数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的长整型数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static long[] slice(long[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        long[] ret = new long[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取浮点数组的子数组
     *
     * <p>从原始浮点数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始浮点数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的浮点数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static float[] slice(float[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        float[] ret = new float[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取双精度数组的子数组
     *
     * <p>从原始双精度数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始双精度数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的双精度数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static double[] slice(double[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        double[] ret = new double[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取布尔数组的子数组
     *
     * <p>从原始布尔数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始布尔数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的布尔数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static boolean[] slice(boolean[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        boolean[] ret = new boolean[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取字符串数组的子数组
     *
     * <p>从原始字符串数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始字符串数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的字符串数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static String[] slice(String[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        String[] ret = new String[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

    /**
     * #brief: 截取对象数组的子数组
     *
     * <p>从原始对象数组中截取指定位置和长度的子数组，自动调整长度以避免越界。
     *
     * @param original 原始对象数组，不能为null
     * @param off 起始位置（包含）
     * @param len 要截取的长度
     * @return 新的对象数组包含截取的元素
     * @throws NullPointerException 如果original为null
     * @throws ArrayIndexOutOfBoundsException 如果off超出范围
     */
    public static Object[] slice(Object[] original, int off, int len) {
        off = sliceOffset(original.length, off);
        len = sliceLength(original.length, off, len);
        Object[] ret = new Object[len];
        System.arraycopy(original, off, ret, 0, len);
        return ret;
    }

}
