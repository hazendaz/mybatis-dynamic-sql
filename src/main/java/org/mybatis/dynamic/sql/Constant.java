/*
 *    Copyright 2016-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.dynamic.sql.render.RenderingContext;
import org.mybatis.dynamic.sql.util.FragmentAndParameters;

public class Constant<T> implements BindableColumn<T> {

    private final @Nullable String alias;
    private final String value;

    private Constant(String value) {
        this(value, null);
    }

    private Constant(String value, @Nullable String alias) {
        this.value = Objects.requireNonNull(value);
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public FragmentAndParameters render(RenderingContext renderingContext) {
        return FragmentAndParameters.fromFragment(value);
    }

    @Override
    public Constant<T> as(String alias) {
        return new Constant<>(value, alias);
    }

    public static <T> Constant<T> of(String value) {
        return new Constant<>(value);
    }
}
