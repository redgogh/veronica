package org.viakko.tools.exception;

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

import static org.viakko.tools.string.StringUtils.strfmt;

/**
 * @author Ekko
 */
public class UncheckedThreadException extends RuntimeException {

    /**
     * 默认构造函数。
     */
    public UncheckedThreadException() {
    }

    /**
     * 通过已有的异常创建一个新的运行时异常。
     *
     * @param e 原始异常，不能为 null。
     */
    public UncheckedThreadException(Throwable e) {
        super(e);
    }

    /**
     * 根据格式化字符串和参数创建异常信息。
     *
     * @param fmt 格式化字符串，不能为 null。
     * @param args 格式化参数。
     */
    public UncheckedThreadException(String fmt, Object... args) {
        super(strfmt(fmt, args));
    }

    /**
     * 根据格式化字符串、已有异常和参数创建异常信息。
     *
     * @param fmt 格式化字符串，不能为 null。
     * @param e 原始异常，不能为 null。
     * @param args 格式化参数。
     */
    public UncheckedThreadException(String fmt, Throwable e, Object... args) {
        super(strfmt(fmt, args), e);
    }

}

