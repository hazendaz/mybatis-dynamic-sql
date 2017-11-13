/**
 *    Copyright 2016-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.util.ConstantMapping;
import org.mybatis.dynamic.sql.util.InsertMapping;
import org.mybatis.dynamic.sql.util.NullMapping;
import org.mybatis.dynamic.sql.util.PropertyMapping;
import org.mybatis.dynamic.sql.util.StringConstantMapping;

public class InsertBatchModelBuilder<T> {

    private List<T> records;
    private SqlTable table;
    private List<InsertMapping> columnMappings = new ArrayList<>();
    
    private InsertBatchModelBuilder(List<T> records, SqlTable table) {
        this.records = records;
        this.table = table;
    }
    
    public <F> BatchColumnMappingFinisher<F> map(SqlColumn<F> column) {
        return new BatchColumnMappingFinisher<>(column);
    }
    
    public InsertBatchModel<T> build() {
        return new InsertBatchModel.Builder<T>()
                .withTable(table)
                .withRecords(records)
                .withColumnMappings(columnMappings)
                .build();
    }

    @SafeVarargs
    public static <T> IntoGatherer<T> insert(T...records) {
        return new IntoGatherer<>(Arrays.asList(records));
    }
    
    public static <T> IntoGatherer<T> insert(List<T> records) {
        return new IntoGatherer<>(records);
    }
    
    public static class IntoGatherer<T> {
        private List<T> records;
        
        private IntoGatherer(List<T> records) {
            this.records = records;
        }

        public InsertBatchModelBuilder<T> into(SqlTable table) {
            return new InsertBatchModelBuilder<>(records, table);
        }
    }
    
    public class BatchColumnMappingFinisher<F> {
        private SqlColumn<F> column;
            
        public BatchColumnMappingFinisher(SqlColumn<F> column) {
            this.column = column;
        }
            
        public InsertBatchModelBuilder<T> toProperty(String property) {
            columnMappings.add(PropertyMapping.of(column, property));
            return InsertBatchModelBuilder.this;
        }
            
        public InsertBatchModelBuilder<T> toNull() {
            columnMappings.add(NullMapping.of(column));
            return InsertBatchModelBuilder.this;
        }
            
        public InsertBatchModelBuilder<T> toConstant(String constant) {
            columnMappings.add(ConstantMapping.of(column, constant));
            return InsertBatchModelBuilder.this;
        }
            
        public InsertBatchModelBuilder<T> toStringConstant(String constant) {
            columnMappings.add(StringConstantMapping.of(column, constant));
            return InsertBatchModelBuilder.this;
        }
    }
}
