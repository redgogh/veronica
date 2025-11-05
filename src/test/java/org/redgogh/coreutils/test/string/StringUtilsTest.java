package org.redgogh.coreutils.test.string;

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

import org.junit.Test;
import org.redgogh.coreutils.collection.Lists;
import org.redgogh.coreutils.generator.Generator;
import org.redgogh.coreutils.string.StringOption;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.redgogh.coreutils.TypeCvt.atos;
import static org.redgogh.coreutils.string.StringUtils.*;

@SuppressWarnings("ALL")
public class StringUtilsTest {

    @Test
    public void operatorsTest() {
        String redGogh = " red_gogh ";
        System.out.printf("STRING_OPERATOR_TRIM: <%s>\n", strcut(redGogh, 0, 0, StringOption.TRIM));
        System.out.printf("STRING_OPERATOR_UPPER_CASE: <%s>\n", strcut(redGogh, 0, 0, StringOption.UPPERCASE));
        System.out.printf("STRING_OPERATOR_CAPITALIZE: <%s>\n", strcut(redGogh, 0, 0, StringOption.TRIM, StringOption.CAPITALIZE));
        System.out.printf("STRING_OPERATOR_REVERSE: <%s>\n", strcut(redGogh, 0, 0, StringOption.REVERSE));
        System.out.printf("STRING_OPERATOR_REMOVE_SPACES: <%s>\n", strcut(redGogh, 0, 0, StringOption.REMOVE_SPACE));
        System.out.printf("STRING_OPERATOR_LINE_HMP: <%s>\n", strcut(redGogh, 0, 0, StringOption.REMOVE_SPACE, StringOption.SNAKE2CAMEL));
    }

    @Test
    public void atosTest() {
        String vanGogh = "Vincent Van Gogh";
        System.out.printf("atos: byte array to string: %s\n", atos(vanGogh.getBytes()));
        System.out.printf("atos: byte array [7 - len] to string: %s\n", atos(vanGogh.getBytes(), 7, 0));
    }

    @Test
    public void strcutExmaple() {
        String author = "Ekko";
        System.out.println(strcut(author, 0, -1));
    }

    @Test
    public void globMatcherTest() {
        List<String> paths = Lists.newArrayList();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.print("\rsize: " + paths.size()), 0, 10, TimeUnit.MILLISECONDS);

        boolean isContinue = true;
        while (isContinue) {
            paths.add("/" + Generator.randomLetterCode(3, 8) + "/" + Generator.randomLetterCode(0, 8) + "." + Generator.randomLetterCode(3, 6));
            if (paths.size() > 10000000)
                isContinue = false;
        }
        scheduledExecutorService.shutdown();
        System.out.println();
        paths.add("/api/dwada.xdwadpojawopdjf");
        paths.forEach(V -> {
            if (strant(V, "/api/*.x*f"))
                System.out.println(V);
        });
    }

    @Test
    public void findTest() {
        String[] strfind = strfind("$JAVA_HOME/$MAVEN_HOME/$HOME/bin", "\\$[A-Za-z_][A-Za-z0-9_]*");
        for (String s : strfind) {
            System.out.println(s);
        }
    }

    @Test
    public void matchTest() {
        String str = "Hello World";
        for (int i = 0; i < 1000; i++) {
            strmatch(str, Generator.randomLetterCode(5));
        }
    }

}
