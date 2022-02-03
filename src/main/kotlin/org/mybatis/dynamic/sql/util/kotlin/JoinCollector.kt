/*
 *    Copyright 2016-2021 the original author or authors.
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
package org.mybatis.dynamic.sql.util.kotlin

import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.select.join.JoinCondition
import org.mybatis.dynamic.sql.select.join.JoinCriterion

typealias JoinReceiver = JoinCollector.() -> Unit

@MyBatisDslMarker
class JoinCollector {
    val onJoinCriterion: JoinCriterion by lazy { internalOnCriterion }
    val andJoinCriteria = mutableListOf<JoinCriterion>()
    private lateinit var internalOnCriterion: JoinCriterion

    fun on(leftColumn: BasicColumn): RightColumnCollector = RightColumnCollector {
        internalOnCriterion = JoinCriterion.Builder()
            .withConnector("on")
            .withJoinColumn(leftColumn)
            .withJoinCondition(it)
            .build()
    }

    fun and(leftColumn: BasicColumn): RightColumnCollector = RightColumnCollector {
        andJoinCriteria.add(
            JoinCriterion.Builder()
                .withConnector("and")
                .withJoinColumn(leftColumn)
                .withJoinCondition(it)
                .build()
        )
    }

    @Deprecated("Please use: on(leftColumn) equalTo rightColumn")
    fun on(column: BasicColumn, condition: JoinCondition): JoinCollector =
        apply {
            internalOnCriterion = JoinCriterion.Builder()
                .withConnector("on")
                .withJoinColumn(column)
                .withJoinCondition(condition)
                .build()
        }

    @Deprecated("Please use: and(leftColumn) equalTo rightColumn")
    fun and(column: BasicColumn, condition: JoinCondition): JoinCollector =
        apply {
            andJoinCriteria.add(
                JoinCriterion.Builder()
                    .withConnector("and")
                    .withJoinColumn(column)
                    .withJoinCondition(condition)
                    .build()
            )
        }
}

class RightColumnCollector(private val joinConditionConsumer: (JoinCondition) -> Unit) {
    infix fun equalTo(rightColumn: BasicColumn) = joinConditionConsumer.invoke(SqlBuilder.equalTo(rightColumn))
}
