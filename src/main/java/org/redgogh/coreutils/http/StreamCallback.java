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

/**
 * 接口 {@link StreamCallback} 定义了处理八位字节流响应的回调方法。
 *
 * <p>该接口包含两个方法：
 * <ul>
 *   <li>{@link #onFailure(Throwable)}: 当处理响应时发生错误时调用。</li>
 *   <li>{@link #onResponse(StreamResponse)}: 当成功接收到响应时调用。</li>
 * </ul>
 *
 * <p>实现此接口的类可以提供自定义的处理逻辑，以便在接收到八位字节流
 * 或发生错误时采取相应的措施。
 *
 * @author Red Gogh
 */
public interface StreamCallback {

    /**
     * 当处理字节流响应时发生错误时调用。
     *
     * @param e 发生的异常，包含错误信息。
     */
    void onFailure(Throwable e);

    /**
     * 当成功接收到字节流响应时调用。
     *
     * @param response 成功的 {@link StreamResponse} 实例。
     */
    void onResponse(StreamResponse response);
}
