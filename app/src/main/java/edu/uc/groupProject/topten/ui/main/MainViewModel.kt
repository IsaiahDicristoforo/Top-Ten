package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.Strawpoll
import edu.uc.groupProject.topten.service.FirestoreService
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.service.StrawpollService

/**
 * MainViewModel class. Does most of the heavy lifting for the database work.
 *
 * Responsible for connecting the Firebase database to the recyclerview, and actively updating the
 * list when a vote is made.
 */
class MainViewModel : ViewModel() {
    var list : MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService : ListService = ListService()
    var firestoreService : FirestoreService =  FirestoreService()

    fun fetchFirestoreList(){
        list = firestoreService.fetchList()
    }

    fun fetchFirestoreListItem(){
        list = firestoreService.fetchDocument()
    }

    fun fetchListServiceList(listName: String) {
        list = listService.fetchList(listName)
    }

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