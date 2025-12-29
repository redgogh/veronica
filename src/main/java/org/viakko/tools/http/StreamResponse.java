package org.viakko.tools.http;

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

import okhttp3.ResponseBody;
import org.viakko.tools.Assert;
import org.viakko.tools.io.IOUtils;

import java.io.Closeable;
import java.io.File;

/**
 * 类 {@link StreamResponse} 表示一个字节流响应。
 *
 * <p>该类实现了 {@link Closeable} 接口，意味着在使用完该响应后，
 * 应该调用 {@link #close()} 方法来释放资源。
 *
 * <p>此类通常用于处理来自服务器的字节流数据，提供了方法来
 * 读取响应内容和处理响应状态。
 *
 * @author Ekko
 */
public class StreamResponse implements Closeable {

    private final okhttp3.Response response;

    StreamResponse(okhttp3.Response response) {
        this.response = response;
    }

    /**
     * 将响应体内容传输到指定路径的文件。
     *
     * <p>此方法通过调用 {@link #transferTo(File)} 实现，
     * 将给定路径 {@code path} 转换为 {@link File} 对象，
     * 并将响应体内容写入该文件。
     *
     * @param path 要传输内容的目标文件路径
     * @return 传输完成的 {@link File} 对象
     */
    public File transferTo(String path) {
        return transferTo(new File(path));
    }
    
    /**
     * 将响应体内容传输到指定的 {@link File} 对象。
     *
     * <p>从响应体中获取输入流，并将其内容写入提供的
     * {@link File} 实例，然后返回该文件。
     *
     * @param file 要传输内容的目标 {@link File} 对象
     * @return 传输完成的 {@link File} 对象
     */
    public File transferTo(File file) {
        ResponseBody body = response.body();
        Assert.notNull(body, "没有数据响应。");
        IOUtils.write(file, body.byteStream());
        close();
        return file;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(response);
    }

}
