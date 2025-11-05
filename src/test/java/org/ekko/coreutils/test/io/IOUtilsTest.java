package org.ekko.coreutils.test.io;

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
import org.ekko.coreutils.io.IOUtils;
import org.ekko.coreutils.system.SystemUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ALL")
public class IOUtilsTest {

    @Test
    public void writeInputStreamTest() {
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream("Hello World2\n" .getBytes(StandardCharsets.UTF_8));
        IOUtils.write(new File(SystemUtils.resolvePath("~/ioutils_tmp2.txt")), byteArrayInputStream, "x");
    }

}
