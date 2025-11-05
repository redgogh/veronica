package org.ekko.coreutils.io;

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

/* Creates on 2020/4/29. */

import java.io.File;

/**
 * 对象序列化与反序列化操作工具
 *
 * @author Ekko
 */
public class ObjectSerializer {

    /**
     * 将给定的 {@code object} 序列化并保存到指定的 {@code file} 文件中。
     * <p>
     * 序列化过程将会把对象转换为字节流并写入到文件中，文件路径由 {@code file} 提供。
     *
     * @param object  要序列化的对象
     * @param file    目标文件
     */
    public static void serialize(Object object, File file) {
        org.ekko.coreutils.reflect.ObjectSerializer.serialize(object, file);
    }

    /**
     * 从指定的 {@code file} 文件中反序列化对象。
     * <p>
     * 该方法会读取文件中的字节流并将其转换为原始的对象。
     *
     * @param file  源文件
     * @return      反序列化后的对象
     */
    public static Object deserialize(File file) {
        return org.ekko.coreutils.reflect.ObjectSerializer.deserialize(file);
    }

}
