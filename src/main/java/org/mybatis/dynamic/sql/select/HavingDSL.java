/*
 *    Copyright 2016-2023 the original author or authors.
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
package org.mybatis.dynamic.sql.select;

import org.jetbrains.annotations.NotNull;
import org.mybatis.dynamic.sql.CriteriaGroup;
import org.mybatis.dynamic.sql.util.Buildable;

public class HavingDSL extends AbstractHavingStarter <HavingDSL.StandaloneHavingFinisher>
        implements Buildable<HavingModel> {
    private StandaloneHavingFinisher havingFinisher;

    @Override
    protected StandaloneHavingFinisher having() {
        if (havingFinisher == null) {
            havingFinisher = new StandaloneHavingFinisher();
        }

        return havingFinisher;
    }

    @NotNull
    @Override
    public HavingModel build() {
        return havingFinisher.buildModel();
    }

    public class StandaloneHavingFinisher extends AbstractHavingFinisher<StandaloneHavingFinisher>
            implements Buildable<HavingModel> {

        @Override
        protected StandaloneHavingFinisher getThis() {
            return this;
        }

        @NotNull
        @Override
        public HavingModel build() {
            return HavingDSL.this.build();
        }

        public HavingApplier toHavingApplier() {
            CriteriaGroup ic = new CriteriaGroup.Builder()
                    .withInitialCriterion(getInitialCriterion())
                    .withSubCriteria(subCriteria)
                    .build();

            return d -> d.initialize(ic);
        }
    }
}
