package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
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
    var playAnimation:Boolean = true
    var lists : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()


    fun loadNextList(generateNewList:Boolean){
        list = firestoreService.fetchList(generateNewList)
    }

    fun loadLists(): MutableLiveData<ArrayList<String>> {
        lists = firestoreService.listOfLists
        return lists
    }

    fun fetchFirestoreListItem(){
        list = firestoreService.fetchDocument()
    }

    fun fetchListServiceList(listName: String) {
        list = listService.fetchList(listName)
    }

    fun fetchStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        val service = StrawpollService()
        return service.getStrawpoll(id)
    }

    fun setIncrementTime(countdownTime: Long) {
        firestoreService.setIncrementTime(countdownTime)
    }

    fun getItemList(): MutableLiveData<ArrayList<ListItem>> {
        return  firestoreService.list
    }

    fun getCurrentListId(): String{
        return  firestoreService.currentList
    }

    fun fetchListNames(){
        return  firestoreService.fetchListNames()
    }

    fun getListOfLists(): MutableLiveData<ArrayList<String>> {
        return  firestoreService.listOfLists
    }

}