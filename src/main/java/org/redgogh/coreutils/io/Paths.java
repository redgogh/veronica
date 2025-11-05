package org.redgogh.coreutils.io;

import org.jetbrains.annotations.NotNull;
import org.redgogh.coreutils.Rethrow;
import org.redgogh.coreutils.system.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 路径相关的工具方法
 *
 * @author Ekko
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

    /**
     * 校验路径是否存在
     *
     * @param path 路径字符串
     * @return 如果路径存在则返回 {@code true} 反之返回 {@code false}。
     */
    public static boolean exists(String path) {
        return Files.exists(Path.of(path));
    }

    /**
     * 复制 {@code src} 文件或目录到 {@code dst} 目标路径，支持单个文件复制
     * 以及整个目录的递归复制操作。如果目标路径已存在，将会覆盖已存在的文件。
     * <p>
     * 该方法会自动解析 {@code src} 和 {@code dst} 的路径，并进行格式化，
     * 保证其在物理存储上的合法性，适用于不同的文件系统环境。
     *
     * @param src  源文件路径
     * @param dst  目标文件路径
     */
    public static void copy(String src, String dst) {
        copy(new File(src), new File(dst));
    }

    /**
     * 复制 {@code src} 文件或目录到 {@code dst} 目标路径，支持文件到目录的转换，
     * 当 {@code dst} 为目录时，文件会复制到目标目录下，保持源文件名不变。
     * <p>
     * 如果 {@code dst} 已存在且为文件，则会覆盖原文件；如果 {@code dst}
     * 为目录，且源是目录，则会递归复制整个目录结构。
     *
     * @param src  源文件对象
     * @param dst  目标文件路径
     */
    public static void copy(File src, String dst) {
        copy(src, new File(dst));
    }

    /**
     * 将 {@code src} 文件或目录复制到 {@code dst} 目标路径。该方法会递归地
     * 复制目录及其内容，并在目标路径中创建相应的目录结构。
     * <p>
     * 如果目标文件已存在，则会被替换；如果源文件是目录，文件树会被递归地
     * 复制到目标路径中。
     *
     * @param src  源文件或目录
     * @param dst  目标文件或目录
     *
     * @throws IllegalArgumentException 如果 {@code src} 或 {@code dst} 为空
     */
    public static void copy(File src, File dst) {
        if (src == null || dst == null)
            throw new IllegalArgumentException("源文件和目标文件不能为空");

        Path srcPath = Paths.get(src.getPath());
        Path dstPath = Paths.get(dst.getPath());

        // 如果是文件，直接复制
        if (src.isFile()) {
            Rethrow.allow(() -> Files.copy(srcPath, dstPath, StandardCopyOption.REPLACE_EXISTING));
            return;
        }

        // 如果是目录，递归复制文件树
        Rethrow.allow(() -> {
            Files.walkFileTree(srcPath, new SimpleFileVisitor<>() {
                @NotNull
                @Override
                public FileVisitResult preVisitDirectory(@NotNull Path dir, @NotNull BasicFileAttributes attrs)
                        throws IOException {
                    Path target = dstPath.resolve(srcPath.relativize(dir));
                    Files.createDirectories(target);
                    return FileVisitResult.CONTINUE;
                }

                @NotNull
                @Override
                public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs)
                        throws IOException {
                    Path target = dstPath.resolve(srcPath.relativize(file));
                    Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        });
    }

    /**
     * 移动 {@code src} 文件或目录到 {@code dst} 目标路径。该方法会解析
     * 传入的字符串路径，并转换为物理文件对象进行操作。
     * <p>
     * 如果目标路径已存在，将会覆盖已存在的文件；如果目标路径为目录，
     * 则会保持原文件名不变移动文件。
     *
     * @param src  源文件路径
     * @param dst  目标文件路径
     */
    public static void move(String src, String dst) {
        move(new File(src), new File(dst));
    }

    /**
     * 移动 {@code src} 文件或目录到 {@code dst} 目标路径。该方法允许
     * 直接传入 {@link File} 类型的文件对象，内部会转换为物理文件对象。
     * <p>
     * 该方法支持跨目录移动，并保证文件数据完整性，不会因中途异常导致数据丢失。
     *
     * @param src  源文件对象
     * @param dst  目标文件路径
     */
    public static void move(File src, String dst) {
        move(src, new File(dst));
    }

    /**
     * 移动 {@code src} 文件或目录到 {@code dst} 目标路径，支持文件到文件、
     * 目录到目录的移动操作。若目标文件已存在，将覆盖原文件。
     * <p>
     * 该方法调用 {@link Files#move} 进行底层移动操作，并采用
     * {@link StandardCopyOption#REPLACE_EXISTING} 选项确保目标文件被替换。
     *
     * @param src  源文件对象
     * @param dst  目标文件对象
     *
     * @throws IllegalArgumentException 如果 {@code src} 或 {@code dst} 为空
     */
    public static void move(File src, File dst) {
        if (src == null || dst == null)
            throw new IllegalArgumentException("源文件和目标文件不能为空");
        Rethrow.allow(() -> Files.move(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING));
    }

}
