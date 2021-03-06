package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.service.FirestoreService
import edu.uc.groupProject.topten.service.ListService

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
    var playAnimation:Boolean = true

    fun loadNextList(generateNewList:Boolean){
        list = firestoreService.fetchList(generateNewList)
    }

    fun fetchListServiceList(listName: String) {
        list = listService.fetchList(listName)
    }

}