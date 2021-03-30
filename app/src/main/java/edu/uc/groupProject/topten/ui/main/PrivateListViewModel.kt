package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.Strawpoll
import edu.uc.groupProject.topten.service.StrawpollService

class PrivateListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    // Example fetch strawpoll by id method
    fun fetchStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        val service = StrawpollService()
        return service.getStrawpoll(id)
    }

    // Example create strawpoll method
    fun createStrawpoll() : MutableLiveData<Strawpoll>? {
        var testOptions = ArrayList<String>()
        testOptions.add("Option 1")
        testOptions.add("Option 2")

        val service = StrawpollService()
        return  service.createStrawpoll("3141592653", testOptions)
    }
}