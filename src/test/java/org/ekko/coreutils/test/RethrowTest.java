package org.ekko.coreutils.test;

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

import org.junit.Test;
import org.ekko.coreutils.Rethrow;
import org.ekko.coreutils.exception.IOWriteException;

import java.util.List;

/**
 * @author Ekko
 */
@SuppressWarnings("ALL")
public class RethrowTest {

    @Test
    public void rethrowTest() {
        Rethrow.expect(IOWriteException.class, "OH～").allow(() -> {
            throw new RuntimeException("Rethrow to IOWriteException test");
        });
    }

    @Test
    public void swallowTest() {
        List<Object> o = null;
        Rethrow.swallow(() -> o.size());
    }

}
