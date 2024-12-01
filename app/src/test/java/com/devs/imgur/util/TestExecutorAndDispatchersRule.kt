package com.devs.imgur.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.Description


// This is a custom Rule that extend one predefined Rule InstantTaskExecutorRule,
// This is to just reduce the boilerplate code
// Reference: https://itnext.io/unit-test-for-android-viewmodel-livedata-coroutines-and-mock-b039a94a003f
@OptIn(ExperimentalCoroutinesApi::class)
class TestExecutorAndDispatchersRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : InstantTaskExecutorRule() {

    override fun starting(description: Description) {
        super.starting(description)

        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)

        Dispatchers.resetMain()
    }

}