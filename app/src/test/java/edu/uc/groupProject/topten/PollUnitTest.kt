package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.groupProject.topten.ui.main.PrivateListViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PollUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var pvm: PrivateListViewModel

    @Before
    fun populateStrawpoll() {
        pvm = PrivateListViewModel()
    }

    @Test
    fun createPoll() {
        pvm.createPoll()
    }
}