package org.redgogh.coreutils.http;

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

import com.alibaba.fastjson.JSON;
import org.redgogh.coreutils.collection.Maps;
import org.redgogh.coreutils.exception.HttpRequestException;
import org.redgogh.coreutils.Assert;
import org.redgogh.coreutils.Rethrow;
import org.redgogh.coreutils.TryUtils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.redgogh.coreutils.string.StringUtils.strhas;
import static org.redgogh.coreutils.TypeCvt.atos;
import static org.redgogh.coreutils.string.StringUtils.uppercase;

/**
 * `HttpClient` 是一个用于发送 HTTP 请求的客户端工具类。该类支持多种 HTTP 请求方法，
 * 并提供了设置请求头、查询参数构建器、请求体等功能。使用该类可以方便地构建和发送 HTTP 请求，
 * 并接收响应。
 *
 * <p>该类包括以下功能：
 * <ul>
 *     <li>通过指定的请求方法和 URL 初始化 HTTP 客户端。</li>
 *     <li>设置请求头、查询参数构建器、请求体等。</li>
 *     <li>发送 HTTP 请求并返回响应对象。</li>
 * </ul>
 *
 * <h2>枚举类</h2>
 * <ul>
 *     <li>{@link HttpMethod}: 定义了支持的 HTTP 请求方法，如 GET、POST 等。</li>
 * </ul>
 *
 * @author Red Gogh
 * @since 1.0
 */
@SuppressWarnings("all")
public class HttpClient {

    /** HTTP 请求方法，如 GET、POST 等。 */
    private final HttpMethod method;

    /** 请求的 URL 地址。 */
    private String url;

    /** 查询参数构建器，用于构建请求的查询参数。 */
    private QueryArgumentsBuilder queryArgumentsBuilder;

    /** 请求体，可以是任意对象类型。 */
    private Object object;

    /** Http 请求配置 */
    private RequestConfigure configure = new RequestConfigure();

    /**
     * `HttpMethod` 枚举定义了支持的 HTTP 请求方法。
     */
    enum HttpMethod {
        GET, POST, PUT, PATCH, DELETE, HEAD
    }

    private static final OkHttpClient CLIENT = new OkHttpClient();

    /**
     * 构造一个 `HttpClient` 实例
     *
     * <p>根据请求方法和 URL 初始化 `HttpClient` 对象。请求方法会被转换为大写，
     * 并与 `HttpMethod` 枚举匹配，不支持的请求方法会抛出异常。
     *
     * @param method HTTP 请求方法（如 GET、POST 等）
     * @param url 请求的 URL
     * @throws NullPointerException 如果请求方法不受支持
     */
    private HttpClient(String method, String url) {
        String upperMethod = uppercase(method);
        this.method = TryUtils.ifError(() -> HttpMethod.valueOf(upperMethod), null);
        Assert.notNull(this.method, "不支持的请求方式 - %s", this.method);
        this.url = url;
    }

    /**
     * 创建一个新的 `HttpClient` 实例
     *
     * <p>该方法是一个静态工厂方法，通过指定的请求方法和 URL 创建一个新的 `HttpClient` 实例。
     *
     * @param method HTTP 请求方法
     * @param url 请求的 URL
     * @return 新的 `HttpClient` 实例
     */
    public static HttpClient open(String method, String url) {
        return new HttpClient(method, url);
    }

    /**
     * 直接发起一个 GET 请求
     *
     * <p>使用通用请求配置直接发起一个 GET 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @return 响应
     */
    public static Response get(String url) {
        return get(url, null, null);
    }

    /**
     * 直接发起一个 GET 请求
     *
     * <p>使用通用请求配置直接发起一个 GET 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param builder 请求参数，如果没有可以为空
     * @return 响应
     */
    public static Response get(String url, QueryArgumentsBuilder builder) {
        return get(url, null, builder);
    }

    /**
     * 直接发起一个 GET 请求
     *
     * <p>使用通用请求配置直接发起一个 GET 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param configure 请求配置信息
     * @return 响应
     */
    public static Response get(String url, RequestConfigure configure) {
        return get(url, configure, null);
    }

    /**
     * 直接发起一个 GET 请求
     *
     * <p>使用通用请求配置直接发起一个 GET 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param configure 请求配置信息
     * @param builder 请求参数，如果没有可以为空
     * @return 响应
     */
    public static Response get(String url, RequestConfigure configure, QueryArgumentsBuilder builder) {
        return open("GET", url)
                .configure(configure)
                .setQueryArgumentsBuilder(builder)
                .newCall();
    }

    /**
     * 直接发起一个 POST 请求
     *
     * <p>使用通用请求配置直接发起一个 POST 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param configure 请求配置信息
     * @return 响应
     */
    public static Response post(String url) {
        return post(url, null, null);
    }


    /**
     * 直接发起一个 POST 请求
     *
     * <p>使用通用请求配置直接发起一个 POST 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param body 请求体，如果没有可以为空
     * @return 响应
     */
    public static Response post(String url, Object body) {
        return post(url, null, body);
    }

    /**
     * 直接发起一个 POST 请求
     *
     * <p>使用通用请求配置直接发起一个 POST 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param configure 请求配置信息
     * @return 响应
     */
    public static Response post(String url, RequestConfigure configure) {
        return post(url, configure, null);
    }

    /**
     * 直接发起一个 POST 请求
     *
     * <p>使用通用请求配置直接发起一个 POST 请求，避免过多的配置让
     * 代码看着很乱。
     *
     * @param url 请求地址
     * @param configure 请求配置信息
     * @param body 请求体，如果没有可以为空
     * @return 响应
     */
    public static Response post(String url, RequestConfigure configure, Object body) {
        return open("POST", url)
                .configure(configure)
                .addRequestBody(body)
                .newCall();
    }

    public HttpClient configure(RequestConfigure configure) {
        if (configure != null)
            this.configure = configure;
        return this;
    }

    /**
     * 添加请求头
     *
     * <p>该方法用于向请求中添加一个新的请求头。
     *
     * @param name 请求头的名称
     * @param value 请求头的值
     * @return 当前 `HttpClient` 实例，以支持链式调用
     */
    public HttpClient addHeader(String name, String value) {
        configure.addHeader(name, value);
        return this;
    }

    /**
     * 设置查询构建器
     *
     * <p>该方法用于设置 HTTP 请求的查询构建器。它接收一个或多个参数字符串（每个参数应为“key=value”的格式），
     * 并使用这些参数初始化一个 `QueryBuilder` 实例，然后设置到 `HttpClient` 中。
     *
     * <p>适用于需要动态构建和设置查询参数的场景。
     *
     * @param parameters 参数数组，每个参数应为“key=value”格式的字符串
     * @see QueryArgumentsBuilder
     * @return 当前的 `HttpClient` 实例
     */
    public HttpClient addQueryArguments(String ...parameters) {
        return setQueryArgumentsBuilder(new QueryArgumentsBuilder(parameters));
    }

    /**
     * 设置查询参数构建器
     *
     * <p>该方法用于设置查询参数构建器，用于构建带有查询参数的请求 URL。
     *
     * @param queryArgumentsBuilder 用于构建查询参数的 `QueryBuilder` 对象
     * @return 当前 `HttpClient` 实例，以支持链式调用
     */
    public HttpClient setQueryArgumentsBuilder(QueryArgumentsBuilder queryArgumentsBuilder) {
        if (this.queryArgumentsBuilder == null)
            this.queryArgumentsBuilder = new QueryArgumentsBuilder();

        if (queryArgumentsBuilder != null)
            this.queryArgumentsBuilder.putAll(queryArgumentsBuilder);

        return this;
    }

    /**
     * 设置请求体
     *
     * <p>该方法用于设置请求体，可以为任意对象，实际请求时会将其转换为 JSON 格式。
     *
     * @param object 请求体对象
     * @return 当前 `HttpClient` 实例，以支持链式调用
     */
    public HttpClient addRequestBody(Object object) {
        if (strhas(method, HttpMethod.GET, HttpMethod.HEAD))
            throw new HttpRequestException("GET 或 HEAD 方法不支持请求主体。");

        this.object = object;
        return this;
    }

    /**
     * 禁用 SSL 验证
     *
     * <p>该方法用于禁用 SSL 证书验证。调用此方法后，`HttpClient` 将在发送 HTTP 请求时
     * 不对 SSL 证书进行验证。这通常用于开发或测试环境中，以避免证书验证错误影响测试进度。
     *
     * <p>注意：在生产环境中禁用 SSL 验证可能会导致安全风险，建议谨慎使用。
     *
     * @return 当前的 `HttpClient` 实例，允许链式调用
     */
    public HttpClient sslVerifierDisable() {
        configure.setSslVerificationDisable(true);
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * <p>该方法用于设置 HTTP 请求的读取超时时间。读取超时时间指定了在等待服务器响应时，
     * 客户端的最大等待时间（以毫秒为单位）。适用于需要自定义读取超时设置的场景。
     *
     * @param readTimeout 读取超时时间（毫秒）
     * @return 当前 `HttpClient` 实例
     */
    public HttpClient setReadTimeout(int readTimeout) {
        configure.setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * <p>该方法用于设置 HTTP 请求的连接超时时间。连接超时时间指定了客户端在与服务器建立连接时，
     * 等待的最大时间（以毫秒为单位）。适用于需要自定义连接超时设置的场景。
     *
     * @param connectTimeout 连接超时时间（毫秒）
     * @return 当前 `HttpClient` 实例
     */
    public HttpClient setConnectTimeout(int connectTimeout) {
        configure.setConnectTimeout(connectTimeout);
        return this;
    }

    /**
     * 创建一个新的 {@link StreamResponse} 实例。
     *
     * <p>此方法可以选择性地接收一个 {@link StreamCallback}
     * 参数，以便在处理八位字节流时提供自定义的回调功能。
     *
     * @return 一个新的 {@link StreamResponse} 实例。
     */
    public StreamResponse newStreamCall() {
        return newStreamCall(null);
    }

    /**
     * 创建一个新的 {@link StreamResponse} 实例，并指定回调。
     *
     * <p>如果提供的回调不为 {@code null}，将在处理八位字节流时
     * 调用该回调。
     *
     * @param callback 可选的回调，用于处理八位字节流。
     *
     * @return 一个新的 {@link StreamResponse} 实例。
     */
    public StreamResponse newStreamCall(StreamCallback callback) {
        return new StreamResponse(Rethrow.allow(() -> newCall0(callback)));
    }

    /**
     * 发送 HTTP 请求
     *
     * <p>该方法使用默认的读超时和连接超时发送 HTTP 请求，并返回响应对象。
     * 默认读取和链接超时时间 16 秒。
     *
     * @return 响应对象 `Response`
     */
    public Response newCall() {
        return newCall(null);
    }

    /**
     * 发送 HTTP 请求
     *
     * <p>该方法使用指定的读超时和连接超时发送 HTTP 请求，并返回响应对象。支持设置请求体、
     * 查询参数和请求头。请求体默认为 `application/json` 格式，支持多部分请求体（MultipartBody）。
     *
     * @param callback 回调接口，如果改对象不为 `null` 则是异步调用。
     * @return 响应对象 `Response`
     * @throws HttpRequestException 如果请求发送失败
     */
    public Response newCall(Callback callback) {
        try (okhttp3.Response response = newCall0(callback)) {
            return newCallResponse(response);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }


    /**
     * 发送 HTTP 请求
     *
     * <p>该方法使用指定的读超时和连接超时发送 HTTP 请求，并返回响应对象。支持设置请求体、
     * 查询参数和请求头。请求体默认为 `application/json` 格式，支持多部分请求体（MultipartBody）。
     *
     * @param callback 回调接口，如果改对象不为 `null` 则是异步调用。
     * @return 响应对象 `Response`
     * @throws HttpRequestException 如果请求发送失败
     */
    private okhttp3.Response newCall0(Object callback) throws IOException {
        /* init url. */
        if (queryArgumentsBuilder != null)
            url = queryArgumentsBuilder.argConcatBuild(url);

        OkHttpClient.Builder clientBuilder = CLIENT.newBuilder();
        clientBuilder.connectTimeout(configure.getConnectTimeout(), TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(configure.getReadTimeout(), TimeUnit.MILLISECONDS);

        if (configure.isSslVerificationDisable()) {
            clientBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager());
            clientBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        }

        OkHttpClient client = clientBuilder.build();

        /* call */
        Call call = request(client);

        /* async */
        if (callback != null) {
            if (callback instanceof Callback)
                async(call, (StreamCallback) callback);

            if (callback instanceof StreamCallback)
                async(call, (StreamCallback) callback);
            return null;
        }

        /* sync */
        return call.execute();
    }

    // /////////////////////////////////////////////////////////// //
    //                         private                             //
    // /////////////////////////////////////////////////////////// //

    /**
     * @return 根据请求体类型构建正确的请求主体。
     */
    private RequestBody getRequestBody() {
        /* request body */
        RequestBody requestBody = null;

        /* is MultipartBody. */
        if (object instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) object;

            /* okhttp MultipartBody */
            okhttp3.MultipartBody.Builder builder = new okhttp3.MultipartBody.Builder()
                    .setType(okhttp3.MultipartBody.FORM);

            for (Map.Entry<String, Object> entry : multipartBody.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(file, MediaType.parse("text/plain")));
                } else {
                    builder.addFormDataPart(entry.getKey(), atos(value));
                }
            }

            requestBody = builder.build();
        }

        /* default application/json raw body. */
        if (requestBody == null) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    object instanceof String ? (String) object : JSON.toJSONString(object));
        }

        return requestBody;
    }

    /**
     * @return 构建请求对象
     */
    private Call request(OkHttpClient client) {
        /* create request builder. */
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        /* pick method */
        switch (method) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                requestBuilder.post(getRequestBody());
                break;
            case PUT:
                requestBuilder.put(getRequestBody());
                break;
            case PATCH:
                requestBuilder.patch(getRequestBody());
                break;
            case DELETE:
                requestBuilder.delete(getRequestBody());
                break;
            case HEAD:
                requestBuilder.head();
                break;
        }

        /* add headers. */
        if (!Maps.isEmpty(configure.getHeaders()))
            configure.getHeaders().forEach(requestBuilder::addHeader);

        /* final execute request. */
        return client.newCall(requestBuilder.build());
    }

    /**
     * 处理响应
     */
    private Response newCallResponse(okhttp3.Response okResponse) throws IOException {
        /* response */
        Response retval = new Response(okResponse.code(), okResponse.headers(), okResponse.body());

        Assert.checkTrue(okResponse.isSuccessful(), "HTTP请求出错（%s）\n    - URL：%s \n    - Request Body：%s \n    - Message: %s",
                okResponse.code(), url, JSON.toJSONString(object), retval);

        return retval;
    }

    private void async(Call call, Callback callback) {
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                callback.onResponse(newCallResponse(response));
            }
        });
    }


    private void async(Call call, StreamCallback callback) {
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                callback.onResponse(new StreamResponse(response));
            }
        });
    }

}
