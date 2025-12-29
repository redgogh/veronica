package org.viakko.tools.test.system;

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

import org.junit.Test;
import org.viakko.tools.system.SystemUtils;

@SuppressWarnings("ALL")
public class SystemUtilsTest {

    @Test
    public void resolvePathTest() {
        System.out.println(SystemUtils.resolvePath("~/"));
        System.out.println(SystemUtils.resolvePath("$JAVA_HOME/bin"));
        System.out.println(SystemUtils.resolvePath("$MAVEN_HOME/bin"));
    }

    @Test
    public void osPropertiesTest() {
        System.out.println(SystemUtils.osName());
        System.out.println(SystemUtils.osArch());
        System.out.println(SystemUtils.osVersion());
        System.out.println(SystemUtils.workDir());
        System.out.println(SystemUtils.tempDir());
        System.out.println(SystemUtils.availableProcessors());
    }

}
