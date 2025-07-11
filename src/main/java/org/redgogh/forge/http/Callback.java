package org.redgogh.forge.http;

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
 * `Callback` 接口用于处理 HTTP 请求的响应和错误回调。
 *
 * <p>该接口定义了两个方法：`onFailure` 和 `onResponse`，分别用于处理请求失败时的异常
 * 和请求成功时的响应。通常用于异步 HTTP 请求的回调机制中，提供对请求结果的处理逻辑。
 *
 * <p>使用场景包括异步 HTTP 请求、网络请求回调等。<p>
 *
 * <h2>主要功能</h2>
 * <ul>
 *     <li>`onFailure(Throwable e)`：当 HTTP 请求失败时调用，传入引发失败的异常。</li>
 *     <li>`onResponse(Response response)`：当 HTTP 请求成功时调用，传入请求的响应结果。</li>
 * </ul>
 *
 * @author Red Gogh
 * @see Response
 * @since 1.0
 */
public interface Callback {

    /**
     * #brief: 处理请求失败的回调
     *
     * <p>该方法在 HTTP 请求失败时被调用，提供引发失败的异常信息。可用于记录错误日志、
     * 提示用户或者进行错误处理等。
     *
     * @param e 请求失败的异常
     */
    void onFailure(Throwable e);

    /**
     * #brief: 处理请求成功的回调
     *
     * <p>该方法在 HTTP 请求成功时被调用，提供请求的响应结果。可用于处理响应数据、
     * 更新 UI 或者进行进一步的业务逻辑处理。
     *
     * @param response 请求成功的响应结果
     */
    void onResponse(Response response);
}
