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
package org.mybatis.dynamic.sql.select.function;

import java.util.Arrays;
import java.util.List;

import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.BindableColumn;

public class Multiply<T> extends OperatorFunction<T> {

    private Multiply(BasicColumn firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        super("*", firstColumn, secondColumn, subsequentColumns); //$NON-NLS-1$
    }

    @Override
    protected Multiply<T> copy() {
        return new Multiply<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Multiply<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return of(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> Multiply<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        return new Multiply<>(firstColumn, secondColumn, subsequentColumns);
    }
}
