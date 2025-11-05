package org.ekko.coreutils.test.reflect;

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

import org.ekko.coreutils.reflect.OnMissing;
import org.ekko.coreutils.reflect.UClass;
import org.ekko.coreutils.reflect.UField;
import org.junit.Test;
import org.ekko.coreutils.time.Chrono;

import java.awt.*;
import java.util.List;

@SuppressWarnings("ALL")
public class UClassTest {

    @Test
    public void newUClassTest() {
        UClass uClass = new UClass(Button.class);

        System.out.printf("uClass(%s)\n", uClass.getName());

        List<UField> properties = uClass.getDeclaredFields();
        for (UField property : properties) {
            // System.out.printf("  - uField path: %s\n", property.getPath());
        }
    }

    @Test
    public void performance_newUClassTest() {
        for (int i = 0; i < 2; i++) {
            Chrono start = Chrono.now();
            newUClassTest();
            Chrono end = Chrono.now();
            System.out.println(end.getTime() -  start.getTime() + "ms");
        }
    }

    public static class Node {
        private Node next;
    }

    @Test
    public void recursionOverflowTest() {
        // 递归调用，构造 UClass(Node) -> UField(next).getType() -> new UClass(Node) ...
        UClass uClass = new UClass(Node.class);

        // 这里会不断递归调用 UField.getType()，导致栈溢出
        uClass.getDeclaredField("next").getType();
    }

    static class User {
        /* test field */
        private String name;

        public User(String name) {
            this.name = name;
        }

        /* static method */
        public void sayIntroduce() {
            System.out.printf("介绍 - 永乐大帝\n");
        }

        /* static method */
        public static void say(String value) {
            System.out.printf("朱棣 - %s\n", value);
        }

    }

    @Test
    public void readFieldValueTest() {
        User judy = new User("Judy");
        System.out.println((String) new UClass(judy).read("name", judy, OnMissing.THROW_EXCEPTION));
    }

    @Test
    public void invokeMethodTest() {
        User judy = new User("Judy");
        UClass uClass = new UClass(judy);

        uClass.invoke(judy,"sayIntroduce");
        uClass.staticInvoke("say", "如此江山，岂不让人留恋 ~");

        Chrono start = Chrono.now();
        uClass.invoke(judy,"sayIntroduce");
        uClass.staticInvoke("say", "如此江山，岂不让人留恋 ~");
        Chrono end = Chrono.now();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

}
