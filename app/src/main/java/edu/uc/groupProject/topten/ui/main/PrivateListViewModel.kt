package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.PollResponse
import edu.uc.groupProject.topten.service.PollService

class PrivateListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var service = PollService()

    // Example create poll method
    fun createPoll() : MutableLiveData<PollResponse>? {
        var testOptions = ArrayList<String>()
        testOptions.add("Option 1")
        testOptions.add("Option 2")

        return  service.createPoll("3141592653", testOptions)
    }

    fun getPoll(id: Int? = 1) : MutableLiveData<PollResponse>? {
        return service.getPoll(id)
    }
}