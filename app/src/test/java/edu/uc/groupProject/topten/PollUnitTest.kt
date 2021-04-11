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
        // Poll Create POST Options
        var testOptions = ArrayList<String>()
        testOptions.add("Option 1")
        testOptions.add("Option 2")

        // Create Poll and save its URL
        var poll = pvm.createPoll("test question", testOptions)
        var url = poll?.value?.url

        // Get the question_id from created poll
        var questionId = url?.substring(url?.lastIndexOf('/')+1)?.toInt()

        // Get the choice_id from the first choice in the created poll
        var choice = poll?.value?.choices?.get(0)
        var choiceId = choice?.url?.substring(choice?.url?.lastIndexOf('/')+1)?.toInt()

        // Vote on the poll
        var votedPoll = pvm.castVote(questionId, choiceId)

        // Retrieve the poll
        var retrievedPoll = pvm.getPoll(questionId)
    }
}