package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.PollChoice
import edu.uc.groupProject.topten.dto.PollResponse
import edu.uc.groupProject.topten.service.PollService

class PrivateListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var service = PollService()

    // Example create poll method
    fun createPoll(question: String, choices: ArrayList<String>) : MutableLiveData<PollResponse>? {
        return service.createPoll(question, choices)
    }

    fun getPoll(id: Int? = 1) : MutableLiveData<PollResponse>? {
        return service.getPoll(id)
    }

    fun castVote(question_id: Int?, choice_id: Int?) : MutableLiveData<PollChoice>? {
        return service.castVote(question_id, choice_id)
    }
}