/*
 * Copyright 2017 huxizhijian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.huxizhijian.hhcomic.comic;

import org.huxizhijian.hhcomic.comic.type.RequestFieldType;
import org.huxizhijian.hhcomic.usecase.base.UseCase;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;

/**
 * 传入参数
 *
 * @author huxizhijian 2017/10/2
 */
public class RequestValues implements UseCase.RequestValues {

    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE =
            new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    public RequestValues(LinkedHashMap<Object, Object> fields) {
        FIELDS_REFERENCE.get().putAll(fields);
    }

    @Override
    public int getSourceType() {
        return (int) FIELDS_REFERENCE.get().get(RequestFieldType.SOURCE_TYPE);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key) {
        return (T) FIELDS_REFERENCE.get().get(key);
    }

    public final LinkedHashMap<?, ?> getFields() {
        return FIELDS_REFERENCE.get();
    }

    public final RequestValues setField(Object key, Object value) {
        FIELDS_REFERENCE.get().put(key, value);
        return this;
    }

    public static RequestValuesBuilder builder() {
        return new RequestValuesBuilder();
    }

    public static class RequestValuesBuilder {

        private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

        public RequestValuesBuilder() {
            //必须先清除之前的数据
            FIELDS.clear();
        }

        public final RequestValuesBuilder setSourceType(int sourceType) {
            FIELDS.put(RequestFieldType.SOURCE_TYPE, sourceType);
            return this;
        }

        public final RequestValuesBuilder setField(Object key, Object value) {
            FIELDS.put(key, value);
            return this;
        }

        public final RequestValuesBuilder setField(WeakHashMap<Object, Object> map) {
            FIELDS.putAll(map);
            return this;
        }

        public final RequestValues build() {
            return new RequestValues(FIELDS);
        }
    }
}
