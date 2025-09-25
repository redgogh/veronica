package org.redgogh.coreutils.http;

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

import org.redgogh.coreutils.collection.Lists;
import org.redgogh.coreutils.collection.Maps;

import java.util.List;
import java.util.Map;

/**
 * HttpHeaders
 *
 * @author Red Gogh
 * @since 1.0
 */
public class HttpHeaders {

    private final Map<String, List<String>> headers = Maps.newHashMap();

    public void addHeader(String name, String value) {
        if (headers.containsKey(name)) {
            headers.get(name).add(value);
        } else {
            List<String> values = Lists.newArrayList();
            values.add(value);
            headers.put(name, values);
        }
    }

    public String getHeader(String name) {
        return headers.containsKey(name) ? headers.get(name).getFirst() : null;
    }

    public int size() {
        return headers.size();
    }

    public boolean isEmpty() {
        return headers.isEmpty();
    }

    public interface Iter {
        void action(String name, String value);
    }

    public void forEach(Iter iter) {
        headers.forEach((name, values) -> values.forEach(value -> iter.action(name, value)));
    }

}
