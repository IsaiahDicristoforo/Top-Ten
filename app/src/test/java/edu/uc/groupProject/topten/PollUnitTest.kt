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
    fun CreateAndFetchPoll() {
        var poll = pvm.createPoll()
        var url = poll?.value?.url

        var questionId = url?.substring(url?.lastIndexOf('/')+1)?.toInt()

        var retrievedPoll = pvm.getPoll(questionId)
    }
}