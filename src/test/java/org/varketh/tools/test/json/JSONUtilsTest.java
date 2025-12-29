package org.varketh.tools.test.json;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 Varketh Nockrath  All rights reserved.                *|
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

import com.alibaba.fastjson.JSONArray;
import org.varketh.tools.collection.Lists;
import org.junit.Test;
import org.varketh.tools.json.JSONUtils;
import org.varketh.tools.student.HighStudent;

import java.util.List;

import static org.apache.commons.math3.stat.inference.TestUtils.t;

@SuppressWarnings("ALL")
public class JSONUtilsTest {

    static final String content = """
            {
                "users": [
                    {
                        "name": "judy",
                        "age": 18
                    },
                    {
                        "name": "john",
                        "age": 25
                    }                
                ]
            }
            """;

    @Test
    public void testEval() {
        HighStudent highStudent = JSONUtils.get(content, "$.users[0]", HighStudent.class);
        System.out.println(JSONUtils.stringify(highStudent));

        System.out.println("=======================");

        List<HighStudent> highStudents = JSONUtils.get(content, "$.users", HighStudent.class);
        System.out.println(JSONUtils.stringify(highStudents));
    }

    @Test
    public void testParse() {
        List<String> users = Lists.of("a", "b", "c");
        JSONArray array = JSONUtils.parse(users);
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }

}
