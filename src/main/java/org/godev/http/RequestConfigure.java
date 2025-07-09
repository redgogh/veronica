package org.godev.http;

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

import org.godev.collection.Maps;
import org.godev.bean.BeanUtils;

import java.util.Map;

/**
 * @since 1.0
 * @author Red Gogh
 */
@SuppressWarnings({"UnusedReturnValue"})
public class RequestConfigure {

    /** 禁用 SSL 证书验证 */
    private boolean sslVerificationDisable = false;

    /** HTTP 请求头的集合。 */
    private final Map<String, String> headers = Maps.newHashMap();

    /** 读取响应的超时时间 */
    private int readTimeout = 60000;

    /** 连接请求的超时时间 */
    private int connectTimeout = 60000;

    /**
     * 拷贝另一个 Config 对象中的属性到当前对象
     */
    public static RequestConfigure from(RequestConfigure config) {
        RequestConfigure requestConfigure = new RequestConfigure();
        BeanUtils.copyProperties(config, requestConfigure, "headers");
        requestConfigure.headers.putAll(config.headers);
        return requestConfigure;
    }

    //////////////////////////////////////////////////////////////
    //                          SET                             //
    //////////////////////////////////////////////////////////////

    public RequestConfigure setSslVerificationDisable(boolean sslVerificationDisable) {
        this.sslVerificationDisable = sslVerificationDisable;
        return this;
    }

    public RequestConfigure addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestConfigure setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RequestConfigure setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    //////////////////////////////////////////////////////////////
    //                          GET                             //
    //////////////////////////////////////////////////////////////

    public boolean isSslVerificationDisable() {
        return sslVerificationDisable;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

}
