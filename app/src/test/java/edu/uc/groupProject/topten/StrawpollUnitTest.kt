package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.groupProject.topten.ui.main.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class StrawpollUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @Before
    fun populateStrawpoll() {
        mvm = MainViewModel()
    }

    @Test
    fun strawpollDTO_isPopulated() {
        whenJSONDataAreReadAndParsed()
    }

    fun whenJSONDataAreReadAndParsed() {
        mvm.fetchStrawpoll(1)
    }
}