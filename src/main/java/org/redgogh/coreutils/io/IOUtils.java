package org.redgogh.coreutils.io;

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

/* Creates on 2020/4/29. */

import org.redgogh.coreutils.Assert;
import org.redgogh.coreutils.Rethrow;
import org.redgogh.coreutils.exception.IOReadException;
import org.redgogh.coreutils.exception.IOWriteException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * IO操作工具包，整合大部分IO操作，使得在Java中更多的IO操作
 * 变得简单、简洁、易用。
 *
 * @author Red Gogh   
 */
public class IOUtils {

    /**
     * 文件、流、缓冲区结尾符号
     */
    public static final int EOF = -1;
    /**
     * 数据大小单位定义，1KB
     */
    public static final int  KB = Byte.BYTES * 1024;
    /**
     * 数据大小单位定义，1MB
     */
    public static final int MB = KB * 1024;
    /**
     * 数据大小单位定义，1GB
     */
    public static final int GB = MB * 1024;
    /**
     * 推荐缓冲区默认大小
     */
    public static final int DEFAULT_BYTE_BUFFER_SIZE = 64 * KB;

    /**
     * 只读模式，等同于 "r"
     */
    public static final OpenOption[] OPEN_MODE_R = new StandardOpenOption[]{
            StandardOpenOption.READ
    };

    /**
     * 写入模式，覆盖写，等同于 "w"
     */
    public static final OpenOption[] OPEN_MODE_W = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE
    };

    /**
     * 读写模式，等同于 "rw"
     */
    public static final OpenOption[] OPEN_MODE_RW = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
    };

    /**
     * 读写模式，覆盖写，等同于 "w+"
     */
    public static final OpenOption[] OPEN_MODE_RW_TRUNC = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
    };

    /**
     * 读写追加模式，等同于 "a+"
     */
    public static final OpenOption[] OPEN_MODE_RW_APPEND = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
    };

    /**
     * 追加写入模式，等同于 "a"
     */
    public static final OpenOption[] OPEN_MODE_A = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
    };

    /**
     * 写入并在关闭时删除文件，等同于 "d"
     */
    public static final OpenOption[] OPEN_MODE_D = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.DELETE_ON_CLOSE
    };

    /**
     * 写入并同步模式，等同于 "s"
     */
    public static final OpenOption[] OPEN_MODE_S = new StandardOpenOption[]{
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.SYNC
    };

    /**
     * 创建新文件写入，若文件已存在则抛异常，等同于 "x"
     */
    public static final OpenOption[] OPEN_MODE_X = new StandardOpenOption[]{
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.WRITE
    };

    /**
     * 关闭所有实现了 {@link Closeable} 的对象实例，并且不需要捕获
     * {@link IOException} 异常。
     * <p>
     * 非常安静的关闭文件、流等对象。
     *
     * @param closeable 实现了 {@link Closeable} 的对象实例。
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null)
            Rethrow.allow(closeable::close);
    }

    /**
     * 这个方法会读取整个 {@code file} 文件中的数据到新创建的字节数组中，数据读完以后这个函数会自动
     * 关闭输入流，外部无需手动关闭流。
     * <p>
     * 文件对象不能为空或是一个目录，必须是可读取的文件对象。
     *
     * @param file
     *        文件对象
     *
     * @return 返回所有文件中的字节数据
     */
    public static byte[] read(File file) {
        Assert.checkTrue(file != null && file.isFile(), "文件不能为空且不能是目录！");
        return Rethrow.allow(() -> read(new FileInputStream(file)));
    }

    /**
     * 这个方法会读取整个 {@code stream} 中的数据到新创建的字节数组中，数据读完以后这个函数会自动
     * 关闭输入流，外部无需手动关闭流。
     * <p>
     * 如果当某次 {@code read()} 操作时出现异常，就会直接停止读取内容并且抛出异常。但是字节流内部的指针数据位置
     * 并不会重置。所以当出现一个异常到时候，这个输入流 {@link InputStream} 就应该停止使用。
     * <p>
     * 当数据读取完毕后，返回值 {@code int} 类型是读取的字节总数，如果已经读取到末尾了，那么则会返回
     * 代表 EOF 的值 {@link #EOF}。
     *
     * @param stream
     *        输入流 {@link InputStream} 对象实例
     *
     * @return 读取写入到 {@code b} 字节缓冲区的总字节数。如果读到末尾则返回 {@link #EOF}
     */
    public static byte[] read(InputStream stream) {
        try {
            ByteArrayOutputStream byteArrayWriter = new ByteArrayOutputStream();
            byte[] tmp = new byte[DEFAULT_BYTE_BUFFER_SIZE];
            int len;
            while ((len = read(tmp, stream)) != EOF)
                byteArrayWriter.write(tmp, 0, len);
            return byteArrayWriter.toByteArray();
        } catch (Exception e) {
            throw new IOReadException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * 这个方法会从 {@code stream} 中读数据存入到 {@code b} 字节数组缓冲区中。读取的数据长度
     * 是 {@code b.length} 缓冲区大小。缓冲区多大，那么就读取多少数据。预估好缓冲区的大小能够
     * 很方便的读取配置文件、XML、等一切以文本作为标记的文件。
     * <p>
     * 如果当某次 {@code read()} 操作时出现异常，就会直接停止读取内容并且抛出异常。但是字节流内部的指针数据位置
     * 并不会重置。所以当出现一个异常到时候，这个输入流 {@link InputStream} 就应该停止使用。
     * <p>
     * 当数据读取完毕后，返回值 {@code int} 类型是读取的字节总数，如果已经读取到末尾了，那么则会返回
     * 代表 EOF 的值 {@link #EOF}。<p>
     *
     * 请注意：该方法不会自动关闭流，需要调用者手动关闭 {@code stream}。
     *
     * @param b
     *        字节缓冲区
     *
     * @param stream
     *        输入流 {@link InputStream} 对象实例
     *
     * @return 读取写入到 {@code b} 字节缓冲区的总字节数。如果读到末尾则返回 {@link #EOF}
     */
    public static int read(byte[] b, InputStream stream) {
        return read(b, 0, b.length, stream);
    }

    /**
     * 这个方法会从 {@code stream} 中读数据存入到 {@code b} 字节数组缓冲区中。读取到的第一个字节
     * 从 {@code b[off]} 开始存放，一直读到字节流的末尾。如果字节流中数据长度大于 {@code len} 的话，
     * 那么数据最多从 {@code b[off]} 读取到 {@code b[len]}。
     * <p>
     * 如果当某次 {@code read()} 操作时出现异常，就会直接停止读取内容并且抛出异常。但是字节流内部的指针数据位置
     * 并不会重置。所以当出现一个异常到时候，这个输入流 {@link InputStream} 就应该停止使用。
     * <p>
     * 当数据读取完毕后，返回值 {@code int} 类型是读取的字节总数，如果已经读取到末尾了，那么则会返回
     * 代表 EOF 的值 {@link #EOF}。<p>
     *
     * 请注意：该方法不会自动关闭流，需要调用者手动关闭 {@code stream}。
     *
     * @param b
     *        字节缓冲区
     *
     * @param off
     *        缓冲区开始位置的偏移量
     *
     * @param len
     *        读取内容总长度，最大不能超过 {@code b.length}
     *
     * @param stream
     *        输入流 {@link InputStream} 对象实例
     *
     * @return 读取写入到 {@code b} 字节缓冲区的总字节数。如果读到末尾则返回 {@link #EOF}
     */
    public static int read(byte[] b, int off, int len,
                           InputStream stream) {
        return Rethrow.allow(() -> stream.read(b, off, len));
    }

    /**
     * 传入一个文件路径会自动从该路径的文件中读取数据流到内存，然后转换为
     * 字符串返回。
     *
     * @param path
     *        文件路径
     *
     * @return 从文件描述符中读取到的字符串文本
     */
    public static String strread(String path) {
        return strread(new File(path));
    }

    /**
     * 将文件中的数据作为字符串文本读取，当前操作会读取整个文件中的数据并将它转
     * 为字符串类型返回。过后会自动关闭文件描述符，外部无需手动关闭。
     *
     * @param file
     *        文件对象引用
     *
     * @return 从文件描述符中读取到的字符串文本
     */
    public static String strread(java.io.File file) {
        return new String(read(file));
    }

    /**
     * 将输入流中的数据作为字符串文本读取，当前操作会读取整个输入流中的数据并将它转
     * 为字符串类型返回。过后会自动关闭输入流，外部无需手动关闭。
     *
     * @param stream
     *        输入流
     *
     * @return 从输入流中读取到的字符串文本
     */
    public static String strread(InputStream stream) {
        return new String(read(stream));
    }

    /**
     * 读取整个 {@code input} 输入流的数据，然后写入到指定的文件对象输出流中。
     * 如果文件不存在，则会创建。并且每次写入完成后都会关闭输出流。同时这个函数也
     * 支持大于2GB的数据拷贝，可用于两个数据量较大的对象相互拷贝使用。<p>
     *
     * 如果需要频繁写入不同的数据，建议在外部维护 {@code file} 的输出流
     * 对象。<p>
     *
     * 当正在执行对象拷贝的时候，一次性最大会读取 16kb 的数据到 JVM 内存
     * 缓冲区中。<p>
     *
     * 这个函数会自动关闭 {@code input} 输入流，无需调用者手动关闭输入流。
     *
     * @param file
     *        {@link File} 文件对象实例（如果文件不存在，则会创建）
     *
     * @param stream
     *        输入流
     *
     */
    public static void write(File file, InputStream stream) {
        write(file, stream, "w");
    }

    /**
     * 读取整个 {@code input} 输入流的数据，然后写入到指定的文件对象输出流中。
     * 如果文件不存在，则会创建。并且每次写入完成后都会关闭输出流。同时这个函数也
     * 支持大于2GB的数据拷贝，可用于两个数据量较大的对象相互拷贝使用。<p>
     *
     * 如果需要频繁写入不同的数据，建议在外部维护 {@code file} 的输出流
     * 对象。<p>
     *
     * 当正在执行对象拷贝的时候，一次性最大会读取 16kb 的数据到 JVM 内存
     * 缓冲区中。<p>
     *
     * 这个函数会自动关闭 {@code input} 输入流，无需调用者手动关闭输入流。
     *
     * @param file
     *        {@link File} 文件对象实例（如果文件不存在，则会创建）
     *
     * @param stream
     *        输入流
     *
     * @param mode
     *        打开模式
     *
     */
    public static void write(File file, InputStream stream, String mode) {
        try (OutputStream outputStream = Files.newOutputStream(file.toPath(), openMode(mode));) {
            write(outputStream, stream);
        } catch (Exception e) {
            throw new IOWriteException(e.getMessage());
        }
    }

    /**
     * 写入字符串 {@code input} 到指定的文件输出流中，字符串以字节流的形式写入。
     * 如果比较关注字符串编码建议使用 {@link #write(OutputStream, byte[])}
     * 函数来代替当前函数做写入操作。<p>
     *
     * 数据写入完成后会自动关闭文件的输出流。
     *
     * @param file
     *        {@link File} 文件对象实例（如果文件不存在，则会创建）
     *
     * @param input
     *        字符串
     *
     */
    public static void write(File file, String input) {
        write(file, input, "w");
    }

    /**
     * 写入字符串 {@code input} 到指定的文件输出流中，字符串以字节流的形式写入。
     * 如果比较关注字符串编码建议使用 {@link #write(OutputStream, byte[])}
     * 函数来代替当前函数做写入操作。<p>
     *
     * 数据写入完成后会自动关闭文件的输出流。
     *
     * @param file
     *        {@link File} 文件对象实例（如果文件不存在，则会创建）
     *
     * @param input
     *        字符串
     *
     * @param mode
     *        打开模式
     *
     */
    public static void write(File file, String input, String mode) {
        write(file, input.getBytes(StandardCharsets.UTF_8), mode);
    }

    /**
     * 将整个 {@code b} 字节数组缓冲区的内容写入到指定的文件输出流 {@code file}。
     * 这个函数不需要捕获任何异常，它能 “安静” 的写入数据。<p>
     *
     * 数据写入完成后会自动关闭文件的输出流。
     *
     * @param file
     *        指定输出流
     *
     * @param b
     *        字节数组缓冲区
     *
     */
    public static void write(File file, byte[] b) {
        write(file, b, "w+");
    }

    /**
     * 将整个 {@code b} 字节数组缓冲区的内容写入到指定的文件输出流 {@code file}。
     * 这个函数不需要捕获任何异常，它能 “安静” 的写入数据。<p>
     *
     * 数据写入完成后会自动关闭文件的输出流。
     *
     * @param file
     *        指定输出流
     *
     * @param b
     *        字节数组缓冲区
     *
     * @param mode
     *        读写模式
     *
     */
    public static void write(File file, byte[] b, String mode) {
        try {
            Files.write(file.toPath(), b, openMode(mode));
        } catch (IOException e) {
            throw new IOWriteException(e);
        }
    }

    /**
     * 根据模式字符串返回对应的文件打开选项数组
     * 支持模式：r/w/rw/w+/a+/a/d/s/x
     *
     * @param mode 打开模式字符串
     * @return 对应的 OpenOption 数组
     * @throws IllegalArgumentException 非法模式时抛出
     */
    private static OpenOption[] openMode(String mode) {
        return switch (mode) {
            case "r" -> OPEN_MODE_R;
            case "w" -> OPEN_MODE_W;
            case "rw" -> OPEN_MODE_RW;
            case "w+" -> OPEN_MODE_RW_TRUNC;
            case "a+" -> OPEN_MODE_RW_APPEND;
            case "a" -> OPEN_MODE_A;
            case "d" -> OPEN_MODE_D;
            case "s" -> OPEN_MODE_S;
            case "x" -> OPEN_MODE_X;
            default -> throw new IllegalArgumentException("支持的模式: r/w/rw/w+/a+/a/d/s/x，当前模式: " + mode);
        };
    }


    /**
     * 读取整个 {@code input} 输入流的数据，然后写入到指定的 {@code stream}
     * 输出流中。同时这个函数也支持大于2GB的数据拷贝，可用于两个数据量较大的
     * 对象相互拷贝使用。
     * <p>
     * 当正在执行对象拷贝的时候，一次性最大会读取 16kb 的数据到 JVM 内存
     * 缓冲区中。
     * <p>
     * 这个函数会自动关闭 {@code input} 输入流，无需调用者手动关闭输入流。
     *
     * @param stream
     *        指定输出流
     *
     * @param input
     *        输入流
     *
     */
    public static void write(OutputStream stream, InputStream input) {
        try {
            byte[] buf = new byte[DEFAULT_BYTE_BUFFER_SIZE];
            int len;
            while ((len = read(buf, input)) != EOF)
                write(stream, buf, 0, len);
        } catch (Exception e) {
            throw new IOWriteException(e);
        } finally {
            /* 如果出现异常关闭输入流，因为输入流中的数据已经被读取，所以
             * 这个函数可以替开发者将输入流关闭。 */
            closeQuietly(input);
        }
    }

    /**
     * 写入字符串 {@code input} 到指定的输出流中，字符串以字节流的形式写入。如果比较
     * 关注字符串编码建议使用 {@link #write(OutputStream,byte[])} 函数来代替当前
     * 函数做写入操作。
     *
     * @param stream
     *        指定输出流
     *
     * @param input
     *        字符串
     *
     */
    public static void write(OutputStream stream, String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        write(stream, bytes, 0, bytes.length);
    }

    /**
     * 将整个 {@code b} 字节数组缓冲区的内容写入到指定的输出流 {@code stream}。
     * 这个函数不需要捕获任何异常，它能 “安静” 的写入数据。
     *
     * @param stream
     *        指定输出流
     *
     * @param b
     *        字节数组缓冲区
     *
     */
    public static void write(OutputStream stream, byte[] b) {
        write(stream, b, 0, b.length);
    }

    /**
     * #brief: 写入一个字节数组到输出输出流中<p>
     *
     * 写入一个字节数组到输出输出流中。内部只是调用了 {@link OutputStream#write(byte[], int, int)}
     * 方法执行写入操作。它的作用是能安静的调用任何 {@code write} 函数做写入操作，不需要强制
     * 去捕获异常信息。
     * <p>
     * 该函数可以写入任何继承了 {@link OutputStream} 的子类。
     *
     * @param stream
     *        继承了 {@link OutputStream} 的所有子类
     *
     * @param b
     *        要写入的字节数组
     *
     * @param off
     *        字节数组起始位置偏移量，写入的时候从 {@code b[off]} 开始读取
     *        数据。
     *
     * @param len
     *        写入数据的总长度，这个长度不能大于 {@code b.length} 同时它也
     *        不能小于 {@code off}
     *
     */
    public static void write(OutputStream stream, byte[] b, int off, int len) {
        Rethrow.allow(() -> stream.write(b, off, len));
    }

}
