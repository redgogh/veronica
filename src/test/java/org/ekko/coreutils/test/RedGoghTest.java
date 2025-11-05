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

import static org.ekko.coreutils.TypeCvt.atobool;

@SuppressWarnings("ALL")
public class EkkoTest {

    /**
     * atobool
     */
    @Test
    public void atoboolTest() {
        System.out.printf("----------------------------\n");
        System.out.printf("atobool 'y' example: %s\n", atobool("y"));
        System.out.printf("atobool 'n' example: %s\n", atobool("n"));
        System.out.printf("----------------------------\n");
        System.out.printf("atobool '1' example: %s\n", atobool(1));
        System.out.printf("atobool '0' example: %s\n", atobool(0));
        System.out.printf("----------------------------\n");
        System.out.printf("atobool 'true' example: %s\n", atobool("true"));
        System.out.printf("atobool 'false' example: %s\n", atobool("false"));
        System.out.printf("----------------------------\n");
    }

}
