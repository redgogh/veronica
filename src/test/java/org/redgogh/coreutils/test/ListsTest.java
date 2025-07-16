package org.redgogh.coreutils.test;

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
import org.redgogh.coreutils.collection.ChunkList;
import org.redgogh.coreutils.collection.Lists;

import java.util.List;

@SuppressWarnings("ALL")
public class ListsTest {

    @Test
    public void splitByChunkSizeTest() {
        List<String> list = Lists.of("1", "2", "3", "4", "5");
        ChunkList<String> lists = Lists.splitByChunkSize(list, 3);
        System.out.println();
    }

    @Test
    public void splitIntoNChunkTest() {
        List<String> list = Lists.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        ChunkList<String> lists = Lists.splitIntoNChunk(list, 3);
        System.out.println();
    }

}
