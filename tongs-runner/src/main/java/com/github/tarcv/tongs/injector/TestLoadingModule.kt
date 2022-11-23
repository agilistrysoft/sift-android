/*
 * Copyright 2021 TarCV
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.tarcv.tongs.injector

import com.github.tarcv.tongs.api.devices.Pool
import com.github.tarcv.tongs.api.testcases.TestCaseProvider
import com.github.tarcv.tongs.api.testcases.TestCaseProviderContext
import com.github.tarcv.tongs.api.testcases.TestCaseProviderFactory
import com.github.tarcv.tongs.suite.JUnitTestCaseProviderFactory
import org.koin.dsl.module

val testLoadingModule = module(createdAtStart = modulesCreatedAtStart) {
    factory {
        TestSuiteLoaderSupplier(get(), listOf(JUnitTestCaseProviderFactory()))
    }
}

class TestSuiteLoaderSupplier(
    ruleManagerFactory: RuleManagerFactory,
    predefinedProviderFactories: List<TestCaseProviderFactory<TestCaseProvider>>
) {
    private val ruleManager = ruleManagerFactory.create(
        TestCaseProviderFactory::class.java,
        predefinedProviderFactories
    ) { factory, context: TestCaseProviderContext -> factory.suiteLoaders(context) }

    fun supply(pool: Pool): List<TestCaseProvider> {
        return ruleManager.createRulesFrom {
            TestCaseProviderContext(
                it,
                pool
            )
        }
    }
}