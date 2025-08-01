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

import org.redgogh.coreutils.bean.BeanUtils;
import org.redgogh.coreutils.exception.AssertException;
import org.redgogh.coreutils.exception.UncheckedException;
import org.redgogh.coreutils.iface.Action;
import org.redgogh.coreutils.iface.Callable;
import org.redgogh.coreutils.reflect.UClass;

/**
 * @author Red Gogh
 */
public class Rethrow {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private static Object objLogger = null;
    private static UClass loggerClass = null;

    static {
        String loggerFactoryClassName = "org.slf4j.LoggerFactory";
        if (UClass.hasClass(loggerFactoryClassName)) {
            UClass uClass = UClass.forName(loggerFactoryClassName);
            objLogger = uClass.staticInvoke("getLogger", Rethrow.class);
            loggerClass = UClass.forName("org.slf4j.Logger");;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void logWarn(String message, Object... args) {
        if (objLogger != null) {
            loggerClass.invoke(objLogger, "warn", message, args);
        }
    }

    /**
     * 异常包装类
     */
    public static class ExpectedThrow {
        /** 期望抛出的异常类型 */
        private final Class<? extends RuntimeException> expected;

        public ExpectedThrow(Class<? extends RuntimeException> expected) {
            this.expected = expected;
        }

        /**
         * 执行指定的无返回值函数，如果发生异常则抛出断言异常。
         *
         * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息。
         *
         * @param function 要执行的函数
         * @throws AssertException 如果函数执行时发生异常
         */
        public void allow(Action function) {
            try {
                function.call();
            } catch (Exception e) {
                if (expected.isInstance(e))
                    throw (RuntimeException) e;
                throw (RuntimeException) BeanUtils.newInstance(expected, e.getMessage(), EMPTY_ARGS);
            }
        }

        /**
         * 执行指定的无返回值函数，如果发生异常则抛出断言异常，并使用自定义的异常信息。
         *
         * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息和格式化支持。
         *
         * @param function 要执行的函数
         * @param fmt 自定义异常信息的格式
         * @param args 格式化参数
         * @throws AssertException 如果函数执行时发生异常
         */
        public void allow(Action function, String fmt, Object... args) {
            try {
                function.call();
            } catch (Exception e) {
                if (expected.isInstance(e))
                    throw (RuntimeException) e;
                throw (RuntimeException) BeanUtils.newInstance(expected, fmt, args);
            }
        }

        /**
         * 执行指定的有返回值函数，如果发生异常则抛出断言异常。
         *
         * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息。
         *
         * @param function 要执行的函数
         * @param <T> 返回值的类型
         * @return 函数返回的值；如果发生异常，则抛出 {@link AssertException}
         * @throws AssertException 如果函数执行时发生异常
         */
        public <T> T allow(Callable<T> function) {
            try {
                return function.call();
            } catch (Exception e) {
                if (expected.isInstance(e))
                    throw (RuntimeException) e;
                throw (RuntimeException) BeanUtils.newInstance(expected, e.getMessage(), EMPTY_ARGS);
            }
        }

        /**
         * 执行指定的有返回值函数，如果发生异常则抛出断言异常，并使用自定义的异常信息。
         *
         * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息和格式化支持。
         *
         * @param function 要执行的函数
         * @param fmt 自定义异常信息的格式
         * @param args 格式化参数
         * @param <T> 返回值的类型
         * @return 函数返回的值；如果发生异常，则抛出 {@link AssertException}
         * @throws AssertException 如果函数执行时发生异常
         */
        public <T> T allow(Callable<T> function, String fmt, Object... args) {
            try {
                return function.call();
            } catch (Exception e) {
                if (expected.isInstance(e))
                    throw (RuntimeException) e;
                throw (RuntimeException) BeanUtils.newInstance(expected, fmt, args);
            }
        }
    }

    /**
     * 重新封装异常类型并抛出
     *
     * <p>指定一个异常类型，若发生异常后则将异常类型封装成指定的异常并
     * 抛出
     *
     * @param expected 期望抛出的异常类型
     * @return 异常包装类
     */
    public static ExpectedThrow expect(Class<? extends RuntimeException> expected) {
        return new ExpectedThrow(expected);
    }

    /**
     * 执行指定的无返回值函数，如果发生任何异常则忽略并不处理。
     *
     * <p>该方法可用于执行可能会抛出异常的操作，但不需要关心异常的情况。
     *
     * @param function 要执行的函数
     */
    public static void swallow(Action function) {
        try {
            function.call();
        } catch (Exception e) {
            logWarn("Rethrow#swallow: {}", e.getMessage());
        }
    }

    /**
     * 执行带返回值的回调函数，并在出现异常时返回 `null`
     *
     * <p>该方法执行传入的 `RetFunction` 接口的回调函数，并捕获任何异常。如果函数调用成功，
     * 则返回其结果。如果在调用过程中抛出异常，则捕获异常并返回 `null`。
     *
     * <p>方法主要用于在执行可能抛出异常的操作时，简化异常处理流程，使调用者无需手动处理异常。
     *
     * @param function 要执行的回调函数，该函数返回类型为 `T`
     * @param <T> 返回值的类型
     * @return 回调函数的返回值，若发生异常则返回 `null`
     */
    public static <T> T swallow(Callable<T> function) {
        try {
            return function.call();
        } catch (Exception e) {
            logWarn("Rethrow#swallow: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 执行指定的无返回值函数，如果发生异常则抛出断言异常。
     *
     * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息。
     *
     * @param function 要执行的函数
     * @throws AssertException 如果函数执行时发生异常
     */
    public static void allow(Action function) {
        try {
            function.call();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * 执行指定的无返回值函数，如果发生异常则抛出断言异常，并使用自定义的异常信息。
     *
     * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息和格式化支持。
     *
     * @param function 要执行的函数
     * @param fmt 自定义异常信息的格式
     * @param args 格式化参数
     * @throws AssertException 如果函数执行时发生异常
     */
    public static void allow(Action function, String fmt, Object... args) {
        try {
            function.call();
        } catch (Exception e) {
            throw new UncheckedException(fmt, args);
        }
    }

    /**
     * 执行指定的有返回值函数，如果发生异常则抛出断言异常。
     *
     * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息。
     *
     * @param function 要执行的函数
     * @param <T> 返回值的类型
     * @return 函数返回的值；如果发生异常，则抛出 {@link AssertException}
     * @throws AssertException 如果函数执行时发生异常
     */
    public static <T> T allow(Callable<T> function) {
        try {
            return function.call();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * 执行指定的有返回值函数，如果发生异常则抛出断言异常，并使用自定义的异常信息。
     *
     * <p>此方法用于执行可能抛出异常的操作，并在发生异常时提供详细的异常信息和格式化支持。
     *
     * @param function 要执行的函数
     * @param fmt 自定义异常信息的格式
     * @param args 格式化参数
     * @param <T> 返回值的类型
     * @return 函数返回的值；如果发生异常，则抛出 {@link AssertException}
     * @throws AssertException 如果函数执行时发生异常
     */
    public static <T> T allow(Callable<T> function, String fmt, Object... args) {
        try {
            return function.call();
        } catch (Exception e) {
            throw new UncheckedException(fmt, args);
        }
    }

}
